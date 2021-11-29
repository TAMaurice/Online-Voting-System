package de.TAMaurice;

import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class BallotHandler {
	private List<String> results;
	private List<String> users;
	
	public BallotHandler(String input, PrivateKey pk) {
		try {
			String[] parts = input.split(" ");
			String ballot = KeyManager.decrypt(parts[1], pk);
						
			if(FileManager.getPublicKeyFromFile() == null) {
				FileManager.createKeyFiles();
			}
			
			PublicKey publicKey = FileManager.getPublicKeyFromFile();
			String output = KeyManager.encrypt(ballot, publicKey);
			
			results.add(output);
			users.add(parts[2]);
			
			FileWriter resultWriter = new FileWriter("src/results");
			FileWriter userWriter = new FileWriter("src/voters");
			
			for(String str: results) {
				resultWriter.write(str + System.lineSeparator());
			}

			for(String str: users) {
				userWriter.write(str + System.lineSeparator());
			}
			
			userWriter.close();
			resultWriter.close();
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | IOException | InvalidKeySpecException e) {
			System.err.println("ERROR | Something went wrong while decrypting the ballot: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
