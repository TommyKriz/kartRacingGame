package tomcom.kartGame.systems.Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Network;
import com.quantumreboot.ganet.Peer;

import tomcom.kartGame.systems.Network.Commands.Command;
import tomcom.kartGame.systems.Network.Commands.CommandComparator;
import tomcom.kartGame.systems.Network.Commands.ConnectCommand;
import tomcom.kartGame.systems.Network.Commands.SendInputCommand;
import tomcom.kartGame.systems.Network.DataContainer.CarData;
import tomcom.kartGame.systems.Network.DataContainer.InputData;
import tomcom.kartGame.systems.Network.DataContainer.SpawnData;
import tomcom.kartGame.systems.Network.LogListener.*;

public class Client{

	public static double time;	

	private static AtomicBoolean running = new AtomicBoolean();
	private static AtomicBoolean shuttingDown = new AtomicBoolean();
	
	public static Queue<Command> commands = new PriorityQueue<>(11, new CommandComparator());
	
	private Network network;
	private ClientSystem clientSystem;
	private String ip;
	private int port;
	
	public Client (ClientSystem clientSystem, String ip, int port) {
		this.clientSystem = clientSystem;
		printHeader();
		this.ip = ip;
		this.port = port;
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
		network.addListener(new ClientLogListener(LogLevel.INFO, running, clientSystem));
		network.startup();
		commands.add(new ConnectCommand(2.0, network, new InetSocketAddress(ip, port)));
		
		Gdx.app.log("Client","\nClient started");
	}
	
	private static void printHeader() {
		Gdx.app.log("Client","Echo Client using Ganet for Java");
		Gdx.app.log("Client","Copyright (c) 2015 Quantum Reboot.");
		Gdx.app.log("Client"," All rights reserved.");
	}

	private static void shutdown() {
		if (running.get()) {
			running.set(false);
			shuttingDown.set(true);
			while (shuttingDown.get()) {
				// busy wait
			}
		}
	}	
	
	private static void processCommands() {
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
			try {
				cleanup();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public void cleanup() throws IOException {
		running.set(false);
		network.shutdown();
		Gdx.app.log("Client","\nClient terminated");
		shuttingDown.set(false);
	}
	
		
	

}






