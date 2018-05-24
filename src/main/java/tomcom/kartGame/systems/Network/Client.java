package tomcom.kartGame.systems.Network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.quantumreboot.ganet.LogLevel;
import com.quantumreboot.ganet.MessageDecoder;
import com.quantumreboot.ganet.MessageEncoder;
import com.quantumreboot.ganet.MessageQuality;
import com.quantumreboot.ganet.Network;
import com.quantumreboot.ganet.Peer;

public class Client {

	private static final int DEFAULT_PORT = 54321;
	private static final String DEFAULT_HOST = "localhost";//192.168.0.118
	public static final double DELAY = 1.0;
	
	private static AtomicBoolean running = new AtomicBoolean();
	private static AtomicBoolean shuttingDown = new AtomicBoolean();
	public static double time;	
	private static Queue<Command> commands = new PriorityQueue<>(11, new CommandComparator());
	private static int msgCnt = 0;
	private Network network;
	
	public Client () {
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
		network.addListener(new ClientLogListener(LogLevel.INFO, running));
		network.startup();
		commands.add(new ConnectCommand(2.0, network, new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT)));
		
		Gdx.app.log("Client","\nClient started");
	}
	
	public static String getMessageText() {
		return "Message " + (++msgCnt);
	}
	
	public static void sendText(Peer peer, double delay, String text) {
		commands.add(new SendTextCommand(time + delay, 
				peer, text, MessageQuality.RELIABLE_ORDERED));
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
	
	public void update(float deltaTime) throws IOException {
		time+=deltaTime;
		if(running.get()) {
			network.update(deltaTime);
			processCommands();
		}
		else {
		}
		
	}
	public void cleanup() throws IOException {
		network.shutdown();
		Gdx.app.log("Client","\nClient terminated");
		shuttingDown.set(false);
	}
	
}

class ClientLogListener extends LogNetworkListener {

	private AtomicBoolean running;
	private MessageDecoder decoder = new MessageDecoder();
	
	public ClientLogListener(LogLevel level, AtomicBoolean running) {
		super(level);
		this.running = running;
	}

	private void terminate() {
		running.set(false);
	}
	
	@Override
	public void onConnectionAccepted(Peer peer) {
		super.onConnectionAccepted(peer);
		
		Client.sendText(peer, Client.DELAY, Client.getMessageText());
	}
	
	@Override
	public void onPeerLost(Peer peer) {
		super.onPeerLost(peer);
		terminate();
	}

	@Override
	public void onPeerDisconnected(Peer peer) {
		super.onPeerDisconnected(peer);
		terminate();
	}

	@Override
	public void onConnectionRefused(Peer peer) {
		super.onConnectionRefused(peer);
		terminate();
	}
	
	@Override
	public void onMessage(Peer peer, ByteBuffer msg) {
		decoder.reset(msg);
		String txt = decoder.readString();
		Gdx.app.log("Client","Got message from " + peer.getAddr() + " '" + txt + "'");

	}
	
}

abstract class Command {
	public double time;
	
	public Command(double time) {
		this.time = time;
	}
	
	public abstract void execute();
}

class ConnectCommand extends Command {
	
	private Network network;
	private InetSocketAddress addr;
	
	public ConnectCommand(double time, Network network, InetSocketAddress addr) {
		super(time);
		this.network = network;
		this.addr = addr;
	}

	@Override
	public void execute() {
		Gdx.app.log("Client","Echo Client attempts to connect to " + addr);
		network.connect(addr);
	}
}

class SendTextCommand extends Command {

	private String txt;
	private Peer peer;
	private MessageEncoder encoder = new MessageEncoder();
	private MessageQuality quality;

	public SendTextCommand(double time, Peer peer, String txt, MessageQuality quality) {
		super(time);
		this.txt = txt;
		this.peer = peer;
		this.quality = quality;
	}
	
	@Override
	public void execute() {
		Gdx.app.log("Client","Sending " + this.quality + " to " + peer.getAddr() + " '" + txt + "'");
		encoder.reset();
		encoder.writeString(txt);
		peer.send(encoder.getMessage(), this.quality);
		
		// schedule new message
		Client.sendText(peer, Client.DELAY, Client.getMessageText());
	}
	
}

class CommandComparator implements Comparator<Command> {

	@Override
	public int compare(Command c1, Command c2) {
		return Double.compare(c1.time, c2.time);
	}
	
}