package de.TAMaurice;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerMain {
	private int port;
	
	
	public ServerMain(int port) {
		this.port = port;
	}
	
	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("INFO | Server listening on port " + port);
			Socket socket;

			while(true) {
				socket = serverSocket.accept();
				System.out.println("INFO | User connected");
				
				UserThread user = new UserThread(socket, this);
				user.start();
			} 
			
		} catch (IOException e) {
			System.err.println("ERROR | Server failed - " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Random random = new Random();
		int port = random.nextInt(65535);
		ServerMain server = new ServerMain(port);
		server.start();
	}
}
