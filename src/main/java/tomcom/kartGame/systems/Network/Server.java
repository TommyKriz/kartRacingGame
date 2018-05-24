package tomcom.kartGame.systems.Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageDecoder;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Network;
import com.quantumreboot.ganet.Peer;

public class Server {

	private static final int DEFAULT_PORT = 54321;
	
	private static AtomicBoolean running = new AtomicBoolean();
	private static AtomicBoolean shuttingDown = new AtomicBoolean();
	private Network network;
		
	public Server() {
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
		network.addListener(new ServerLogListener(LogLevel.INFO));
		network.startup(DEFAULT_PORT);
		
		Gdx.app.log("Server","\nServer started");
	}
	public void update(float deltaTime) {
		if(running.get()) {
			network.update(deltaTime);
		}
		else {
			//shutdown or smthng
		}
	}
	public void cleanup() throws IOException {
		network.shutdown();
		Gdx.app.log("Server","\nServer terminated");
		shuttingDown.set(false);
	}
	
	private static void printHeader() {
		Gdx.app.log("Server","Echo Server using Ganet for Java");
		Gdx.app.log("Server","Copyright (c) 2015 Quantum Reboot.");
		Gdx.app.log("Server","All rights reserved.");
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
	
}
