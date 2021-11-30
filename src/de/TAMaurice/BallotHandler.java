package de.TAMaurice;

import java.io.File;
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
	private List<String> ballots;
	private List<String> users;
	
	public BallotHandler(String ballotEncrypted, String userNameEncrypted, String symmetricEncryptedKey, PrivateKey privateKey) {
		try {
			String symmetricalKey = RSAKeyManager.decrypt(symmetricEncryptedKey, privateKey);
			String ballot = AESKeyManager.decrypt(ballotEncrypted, symmetricalKey);
			String userName = AESKeyManager.decrypt(userNameEncrypted, symmetricalKey);
			
			if(!users.contains(userName)) {
				users.add(userName);
				ballots.add(ballot);
				
				checkKeys();
				toFile(users.toString(), ballots.toString());
				
			}
			
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
	
	private void toFile(String users, String ballots) {
		PublicKey publicKey = FileManager.getPublicKeyFromFile();
		try {
			String usersEncrypted = RSAKeyManager.encrypt(users, publicKey);
			String ballotsEncrypted = RSAKeyManager.encrypt(ballots, publicKey);
			
			FileWriter userWriter = new FileWriter("src/users");
			FileWriter ballotWriter = new FileWriter("src/ballots");
			
			userWriter.write(usersEncrypted);
			ballotWriter.write(ballotsEncrypted);
			
			userWriter.close();
			ballotWriter.close();
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			e.printStackTrace();
		}
	}

	private void checkKeys() {
		if(!(new File("src/PrivateKey").exists() && new File("src/PublicKey").exists())) {
			FileManager.createKeyFiles();
		} 
	}
}
