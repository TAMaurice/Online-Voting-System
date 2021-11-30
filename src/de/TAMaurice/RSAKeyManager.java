package de.TAMaurice;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAKeyManager {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public RSAKeyManager() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom();
 
        keyPairGenerator.initialize(2048,secureRandom);
 
        KeyPair pair = keyPairGenerator.generateKeyPair();
 
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();

        this.publicKey = publicKey;
        this.privateKey = privateKey;
        
	}
	
	public static String encrypt(String toEncrypt, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher encryptionCipher = Cipher.getInstance("RSA");
		encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedString = encryptionCipher.doFinal(toEncrypt.getBytes());
		return Base64.getEncoder().encodeToString(encryptedString);
	}
	
	public static String decrypt(String toDecrypt, PrivateKey pk) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		Cipher decryptionCipher = Cipher.getInstance("RSA");
		decryptionCipher.init(Cipher.DECRYPT_MODE, pk);
		byte[] decryptedString = decryptionCipher.doFinal(toDecrypt.getBytes());
		System.out.println(decryptedString.toString());
		return decryptedString.toString();
	}
	
	public String getPublicKey() {
		String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
		return publicKeyString;	
	}
	
	public String getPrivateKey() {
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
		return privateKeyString;
	}
	
	public PrivateKey getAsPrivateKey() {
		return privateKey;
	}
	
	public PublicKey getAsPublicKey() {
		return publicKey;
	}
}
