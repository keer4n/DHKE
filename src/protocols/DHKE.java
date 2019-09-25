package protocols;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class DHKE {
	
	final int SIZE = 256;

	final Boolean VERBOSE = false;
	
	
	int id;
	SecureRandom rnd;
	BigInteger p,g,xy;
	
	public DHKE(int id) {
		this.id = id;
		rnd = new SecureRandom();
	}
	
	public void calculateAgreement(ObjectInputStream in, ObjectOutputStream out) {
		
		try {
			if (id == 1) {
				p = BigInteger.probablePrime(SIZE+1, rnd);
				g = new BigInteger(SIZE,rnd);
				out.writeObject(p);
				out.flush();
				out.writeObject(g);
				out.flush();
				
				BigInteger x = new BigInteger(SIZE,rnd);
				BigInteger X = g.modPow(x, p);
				
				out.writeObject(X);
				out.flush();
				
				BigInteger Y = (BigInteger) in.readObject();
				xy = Y.modPow(x, p);
				
				if(VERBOSE) {
					System.out.println("I'm party 1");
					System.out.println("my secret x: " + x);
					System.out.println("my public X: " + X);
					System.out.println("his public Y: " + Y);
				}
			} else if(id == 2) {
				p = (BigInteger) in.readObject();
				g = (BigInteger) in.readObject();
				BigInteger X = (BigInteger) in.readObject();
				
				BigInteger y = new BigInteger(SIZE,rnd);
				BigInteger Y = g.modPow(y, p);
				
				out.writeObject(Y);
				out.flush();
				xy = X.modPow(y, p);
				
				if(VERBOSE) {
					System.out.println("I'm party 2");
					System.out.println("my secret y: " + y);
					System.out.println("my public Y: " + Y);
					System.out.println("his public X: " + X);
				}
			}
		} catch (IOException|ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BigInteger getSharedSecret() {
		return xy;
	}
}
