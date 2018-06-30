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
import tomcom.kartGame.systems.Network.Commands.SendCarDataCommand;
import tomcom.kartGame.systems.Network.DataContainer.InputData;
import tomcom.kartGame.systems.Network.DataContainer.NetworkTransferData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;
import tomcom.kartGame.systems.Network.LogListener.ServerLogListener;
import tomcom.kartGame.systems.Network.DataContainer.CarData;

public class Server {

	private static final int DEFAULT_PORT = 54321;
	

	
	private AtomicBoolean running = new AtomicBoolean();
	private AtomicBoolean shuttingDown = new AtomicBoolean();
	
	public double time;	

	Queue<Command> commands = new PriorityQueue<>(11, new CommandComparator());

	private ServerSystem serverSystem;
	private Network network;
	
		
	public Server(ServerSystem serverSystem) {
		this.serverSystem = serverSystem;
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
		network.addListener(new ServerLogListener(LogLevel.INFO, serverSystem));
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
	

	
	
	

	
}



