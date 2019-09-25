package main;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import protocols.DHKE;

public class Experiment {
	
	
	
	public static void main(String[] args) {
		
		final String ADDRESS = "127.0.0.2";
		final int PORT = 8000;
		
		int id = 1;
		
		ServerSocket host = null;
		Socket s;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		
		try {
			SocketAddress dest = new InetSocketAddress(ADDRESS, PORT);
			s = new Socket();
			s.connect(dest);
			id = 2;
			System.out.println("Connection to Server Successful");
			in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
		} catch(Exception e) {
			System.out.println("Connection not open, running server");
			try {
				host = new ServerSocket(PORT);
				s = host.accept();
				System.out.println("Connection established");
				out =  new ObjectOutputStream(s.getOutputStream());
				in = new ObjectInputStream(s.getInputStream());
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		
		DHKE test = new DHKE(id);
		test.calculateAgreement(in, out);
		System.out.println("Shared Secret for P"+ id + ": " + test.getSharedSecret());
		
	}
}
