package xyz.destr.rpg.client;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

import xyz.destr.io.serealizer.Serializer;
import xyz.destr.io.serealizer.SerializerManager;
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

	public BasicClient(EntityBehavior behavior) {
		this.behavior = behavior;
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
		final Serializer userInputSerializer = SerializerManager.getInstance().get(UserOutput.class);
		final Serializer userOutputSerializer = SerializerManager.getInstance().get(UserInput.class);

		final UserOutput userOutput = new UserOutput();
		final UserInput userInput = new UserInput();
		
		try {
			if(address == null) {
				address = InetAddress.getByName("localhost");
			}
			System.out.println("Connection to: " + address);
			System.out.println("Port: " + port);
			socket = new Socket(address, port);
			final DataInput in = new DataInputStream(socket.getInputStream());
			final DataOutput out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(Constants.HANDSHAKE);
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
			final UUID clientUUID = new UUID(in.readLong(), in.readLong());
			if(!clientUUID.equals(clientUuidFromFile)) {
				System.out.println("User created: " + clientUUID);
				writeClientUUID(clientUUID);
			} else {
				System.out.println("Sucsess login as: " + clientUUID);
			}
			
			userInputSerializer.writeProtocol(out);
			userOutputSerializer.writeProtocol(out);
			
			connected(clientUUID);
			
			while(socket.isConnected()) {
				userInputSerializer.read(userOutput, in);
				process(userInput, userOutput);
				userOutputSerializer.write(out, userInput);
			}
		} catch (IOException e) {
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