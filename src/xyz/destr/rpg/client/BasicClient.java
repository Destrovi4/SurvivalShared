package xyz.destr.rpg.client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.UUID;

import xyz.destr.rpg.Constants;
import xyz.destr.rpg.ServerMessage;
import xyz.destr.rpg.UserInput;
import xyz.destr.rpg.UserOutput;
import xyz.destr.rpg.entity.EntityBehavior;
import xyz.destr.rpg.entity.EntityInput;
import xyz.destr.rpg.entity.EntityOutput;

public class BasicClient implements Runnable {
	
	public static final String CONFIG_FILE_NAME = "uuid.txt";
	protected InetAddress address;
	protected int port = Constants.PORT;
	protected EntityBehavior behavior;
	
	protected ByteBuffer buffer = ByteBuffer.allocate(2048);

	public BasicClient(EntityBehavior behavior) {
		this.behavior = behavior;
	}
	
	protected void writeObject(OutputStream out, Object object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(object);
		out.write(baos.toByteArray());
		out.flush();
	}
	
	protected Object readObject(InputStream in) throws IOException {
		try {
			return new ObjectInputStream(in).readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		}
	}
	
	public void setArguments(String... args) {
		for(int i = 0, count = args.length; i < count;) {
			switch(args[i++]) {
				case "-a":{
					String[] addressData = args[i++].split(":");
					if(addressData.length > 0) {
						try {
							address = InetAddress.getByName(addressData[0]);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					}
					if(addressData.length > 1) {
						port = Integer.parseInt(addressData[1]);
					}
				}
				break;
				default:
					throw new RuntimeException("Invalid argument " + args[i-1]);
			}
		}
	}
		
	@Override
	public void run() {
		Socket socket;
		
		//final UserOutput userOutput = new UserOutput();
		final UserInput userInput = new UserInput();
		
		try {
			if(address == null) {
				address = InetAddress.getByName("localhost");
			}
			System.out.println("Connection to: " + address);
			System.out.println("Port: " + port);
			socket = new Socket(address, port);
			final DataInputStream in = new DataInputStream(socket.getInputStream());
			final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			writeObject(out, Constants.HANDSHAKE);
	
			if(!Constants.HANDSHAKE.equals(readObject(in))) throw new IOException();
			
			final UUID clientUuidFromFile = readClientUUID();
			if(clientUuidFromFile == null) {
				writeObject(out, new UUID(0,0));
			} else {
				System.out.println("Loaded uuid " + clientUuidFromFile);
				writeObject(out, clientUuidFromFile);
			}
			
			final UUID clientUUID = (UUID)readObject(in);
			connected(clientUUID);
			while(socket.isConnected()) {
				UserOutput userOutput = (UserOutput)readObject(in);
				process(userInput, userOutput);
				writeObject(out, userInput);
				userInput.clear();
			}
			
			
			/*
			out.writeUTF(Constants.HANDSHAKE);
			out.flush();
			if(!Constants.HANDSHAKE.equals(in.readUTF())) throw new IOException();
				
			final UUID clientUuidFromFile = readClientUUID();
			if(clientUuidFromFile == null) {
				out.writeLong(0);
				out.writeLong(0);
			} else {
				System.out.println("Loaded uuid " + clientUuidFromFile);
				out.writeLong(clientUuidFromFile.getMostSignificantBits());
				out.writeLong(clientUuidFromFile.getLeastSignificantBits());
			}
			out.flush();
			
			final UUID clientUUID = new UUID(in.readLong(), in.readLong());
			if(!clientUUID.equals(clientUuidFromFile)) {
				System.out.println("User created: " + clientUUID);
				writeClientUUID(clientUUID);
			} else {
				System.out.println("Sucsess login as: " + clientUUID);
			}
						
			//userInputSerializer.writeProtocol(out);
			//userOutputSerializer.writeProtocol(out);
			
			connected(clientUUID);
			while(socket.isConnected()) {
				userInputSerializer.read(userOutput, in);
				process(userInput, userOutput);
				userOutputSerializer.write(out, userInput);
				out.flush();
				userInput.clear();
			}
			*/
		} catch (IOException e) {//| ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void connected(UUID clientUUID) {
		System.out.println("Connected " + clientUUID);
	}
	
	protected void onServerMessage(ServerMessage message) {
		switch(message.type) {
		case INFO:
			System.out.print("INFO: ");
			System.out.println(message.text);
		break;
		case ERROR:
			System.err.print("ERROR: ");
			System.err.println(message.text);
		break;
		default:
			System.err.print("Unknown message type: ");
			System.err.println(message.text);
		}
	}
	
	protected void process(UserInput userInput, UserOutput userOutput) {
		for(ServerMessage message: userOutput.messageList) {
			onServerMessage(message);
		}
		
		EntityInput input = new EntityInput();
		for(EntityOutput output: userOutput.entityOutputList) {
			final UUID uuid = output.uuid;
			behavior.onOutput(output);
			behavior.onInput(input);
			if(!input.isEmpty()) {
				input.uuid = uuid;
				userInput.addEntityInput(input);
				input = new EntityInput();
			}
		}
	}
	
	protected static UUID readClientUUID() {
		final File file = new File(CONFIG_FILE_NAME);
		if(!file.exists()) return null;
		try {
			final InputStream is = new FileInputStream(file);
			try {
				final BufferedReader br = new BufferedReader(new InputStreamReader(is));
				final String line = br.readLine();
				if(line != null) {
					return UUID.fromString(line);
				} else {
					return null;
				}
			} finally {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected void writeClientUUID(UUID uuid) {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(CONFIG_FILE_NAME));
			writer.println(uuid.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}