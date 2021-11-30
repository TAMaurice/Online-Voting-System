package de.TAMaurice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	public static List<String> getCandidates() {
		List<String> candidates = new ArrayList<String>();
		
		try {
			BufferedReader candidateReader = new BufferedReader(new FileReader("src/Candidates.txt"));
			String currentLine;
		
			while((currentLine=candidateReader.readLine()) != null) {
				candidates.add(currentLine);
			}

			candidateReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return candidates;
	}
	
	public static PublicKey getPublicKeyFromFile() {
		byte[] keyBytes;
		X509EncodedKeySpec spec;
		KeyFactory kf;
		
		PublicKey publicKey = null;
		
		try {
			keyBytes = Files.readAllBytes(Paths.get("src/PublicKey"));
			spec = new X509EncodedKeySpec(keyBytes);
		    kf = KeyFactory.getInstance("RSA");
		    publicKey = kf.generatePublic(spec);
		} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.err.println("ERROR | Could not read Public Key from File");
			e.printStackTrace();
		}

		return publicKey;
	}
	
	public static PrivateKey getPrivateKeyFromFile() {
		byte[] keyBytes;
		PKCS8EncodedKeySpec spec;
		KeyFactory kf;
		
		PrivateKey privateKey = null;
		
		try {
			keyBytes = Files.readAllBytes(Paths.get("src/PublicKey"));
			spec = new PKCS8EncodedKeySpec(keyBytes);
		    kf = KeyFactory.getInstance("RSA");
		    privateKey = kf.generatePrivate(spec);
		} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.err.println("ERROR | Could not read Public Key from File");
			e.printStackTrace();
		}

		return privateKey;
	}
	
	public static void createKeyFiles() {
		try {
			FileWriter pubWriter = new FileWriter("src/publicKey");
			FileWriter privWriter = new FileWriter("src/privateKey");
			RSAKeyManager keyManager = new RSAKeyManager();
			
			pubWriter.write(keyManager.getPublicKey());
			privWriter.write(keyManager.getPrivateKey());
			
			pubWriter.close();
			privWriter.close();
			
		} catch (NoSuchAlgorithmException | IOException e) {
			System.err.println("ERROR | Failed generating Keypair: " +e.getMessage());
			e.printStackTrace();
		}
	}
}
