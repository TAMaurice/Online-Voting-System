package de.TAMaurice;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.List;

public class UserThread extends Thread {
	private Socket socket;
	private PrintWriter writer;
	
	boolean running = true;
	
	private PrivateKey privateKey;
	
	private String symmetricalKey;
	private String userName;
	private String ballot;
	
	public UserThread(Socket socket, ServerMain server) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            
            String receivedString;
            
            do {
            	receivedString = reader.readLine();
            	System.out.println("INFO | User issued command: " + receivedString);
            	
            	if(receivedString == null) {
            		receivedString = "EXIT";
            	} else {
            		handleCommands(receivedString);
            	}
            } while(!receivedString.equals("EXIT"));
            
            socket.close();
            System.out.println("INFO | User disconnected");

		} catch (IOException e) {
			System.err.println("ERROR | UserThread: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void handleCommands(String input) {
		switch(input) {
			case "GETCANDIDATES":
				List<String> candidates = FileManager.getCandidates();
				writer.println(candidates);
				System.out.println("INFO | Sent the list of candidates");
				break;
				
			case "GETPUBKEY":
				try {
					RSAKeyManager rsaKeyManager = new RSAKeyManager();
					privateKey = rsaKeyManager.getAsPrivateKey();
					writer.println("PUB "+rsaKeyManager.getPublicKey());
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				break;
				
			case "EXIT":
				break;
		
			default:
				if(input.startsWith("BALLOT")) {
					String[] parts = input.split(" ");
					ballot = parts[1];
					System.out.println("INFO | Received Ballot");
					
				} else if(input.startsWith("USERNAME")) {
					String[] parts = input.split(" ");
					userName = parts[1];
					System.out.println("INFO | Received Username");
					
				} else if(input.startsWith("KEY")) {
					String[] parts = input.split(" ");
					symmetricalKey = parts[1];	
					System.out.println("INFO | Received Symmetrical Key");
					
				} else if(input.startsWith("FINAL")) {
					new BallotHandler(ballot, userName, symmetricalKey, privateKey);
				}
				break;
		}
	}
}