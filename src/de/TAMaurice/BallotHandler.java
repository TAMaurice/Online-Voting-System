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
	
	public BallotHandler(String ballot, String userName, String symmetricKey, PrivateKey privateKey) {
		
	}
}
