package de.TAMaurice;

import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class UserThread extends Thread {
	private Socket socket;
	private PrintWriter writer;
	
	boolean running = true;
	boolean receivedList = false;
	boolean receivedPub = false;
	
	private String publicKeyString;
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
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
			System.out.println("ERROR | UserThread: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void handleCommands(String input) {
		switch(input) {
			case "START":
				List<String> candidates = FileManager.getCandidates();
				writer.println(candidates);
				System.out.println("INFO | Sent the list of candidates");
				break;
				
			case "LISTCONFIRM":
				receivedList = true;
				System.out.println("INFO | Candidate-List arrived successfully");
				break;
				
			case "PUB":
				try { 
					KeyManager keyManager = new KeyManager(); 
					
					publicKeyString = keyManager.getPublicKey(); 
					privateKey = keyManager.getAsPrivateKey();
					publicKey = keyManager.getAsPublicKey();
					
					writer.println(publicKeyString); 
					
				} catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
				break;
				
			case "PUBCONFIRM":
				receivedPub = true;
				System.out.println("INFO | Public-Key received successfully");
				break;
				
			case "EXIT":
				break;
		
			default:
				if(input.startsWith("BALLOT")) {
					new BallotHandler(input, privateKey);
					System.out.println("INFO | Received ballot successfully");
				} else if(input.startsWith("ENCRYPT")) {
					try {
						System.out.println(KeyManager.encrypt("1", publicKey));
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| IllegalBlockSizeException | BadPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
		}
	}
}