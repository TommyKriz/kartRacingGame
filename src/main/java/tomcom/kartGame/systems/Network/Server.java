package tomcom.kartGame.systems.Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageDecoder;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Network;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.components.NetworkIdentityComponent;
import tomcom.kartGame.components.physics.Body2DComponent;
import tomcom.kartGame.systems.Network.Commands.Command;
import tomcom.kartGame.systems.Network.Commands.CommandComparator;
import tomcom.kartGame.systems.Network.Commands.SendSpawnCommand;
import tomcom.kartGame.systems.Network.Commands.SendVehicleDataCommand;
import tomcom.kartGame.systems.Network.DataContainer.ForceInputData;
import tomcom.kartGame.systems.Network.DataContainer.NetworkTransferData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;
import tomcom.kartGame.systems.Network.DataContainer.VehicleData;

public class Server {

	private static final int DEFAULT_PORT = 54321;
	
	public static final double DATA_UPDATE_DELAY = 0.05;
	
	private AtomicBoolean running = new AtomicBoolean();
	private AtomicBoolean shuttingDown = new AtomicBoolean();
	
	private double time;	
	private double lastUpdateTime;
	private Queue<Command> commands = new PriorityQueue<>(11, new CommandComparator());

	private NetworkingSystem networkingSystem;
	private Network network;
	private List<Peer> peers = new ArrayList<Peer>();
		
	public Server(NetworkingSystem ns) {
		networkingSystem = ns;
		printHeader();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdown();
			} });
	}
	
	public void Start() throws IOException {

		running.set(true);
		shuttingDown.set(false);
		network = new Network();
		network.addListener(new ServerLogListener(LogLevel.INFO, this));
		network.startup(DEFAULT_PORT);
		Gdx.app.log("Server","\nServer started");
	}
	
	private void processCommands() {
		while (!commands.isEmpty() && time >= commands.peek().time) {
			Command cmd = commands.poll();
			cmd.execute();
		}
	}
	
	public void update(float deltaTime) {
		time+=deltaTime;
		if(running.get()) {
			network.update(deltaTime);
			if(time >= DATA_UPDATE_DELAY + lastUpdateTime) {
				lastUpdateTime = time;
				List<NetworkTransferData> data = networkingSystem.getDataPackets();
				for(NetworkTransferData packet : data) {
					sendDataPackets(packet);
				}
			}
			processCommands();
		}
		else {
			//shutdown or something
		}
	}
	
	public void cleanup() throws IOException {
		network.shutdown();
		Gdx.app.log("Server","\nServer terminated");
		shuttingDown.set(false);
	}
	
	public void newConnection(Peer peer) {
		peers.add(peer);
		networkingSystem.newConnection(peer);
	}
	
	public void sendDataPackets(NetworkTransferData data) {
		for(Peer p : peers) {
			if(data instanceof VehicleData) {
				commands.add(new SendVehicleDataCommand(0, p, (VehicleData)data, MessageQuality.UNRELIABLE));
			}
		}
	}
	
	public void sendSpawnCommand(Peer owner, SpawnData spawnData) {
		
		for(Peer p : peers) {
			Gdx.app.log("Server","Sending SpawnInfo to Client: " + p);
			if(p == owner) {
				spawnData.localControl = true;
			}
			commands.add(new SendSpawnCommand(0, 
				p, spawnData, MessageQuality.RELIABLE_ORDERED));
		}
		
	}
	private void printHeader() {
		Gdx.app.log("Server","Echo Server using Ganet for Java");
		Gdx.app.log("Server","Copyright (c) 2015 Quantum Reboot.");
		Gdx.app.log("Server","All rights reserved.");
	}

	private void shutdown() {
		if (running.get()) {
			running.set(false);
			shuttingDown.set(true);
			while (shuttingDown.get()) {
				// busy wait
			}
		}
	}
	public void receivedApplyForceCommand(int entityId, float xAxis, float yAxis) {
		networkingSystem.receivedApplyForceCommand(new ForceInputData(entityId, xAxis, yAxis));
		
	}	
	
}



