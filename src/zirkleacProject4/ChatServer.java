package zirkleacProject4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

	public static void main(String[] args) {
		{		
			DataOutputStream dos;
			ConcurrentHashMap<String, InetSocketAddress> clientMap 
			 = new ConcurrentHashMap<String, InetSocketAddress>(); 
			DataInputStream dis;
				try {
			ServerSocket serverSocket = new ServerSocket(31200);
			for(;true;){
			Socket socks=serverSocket.accept();
			dis= new DataInputStream(socks.getInputStream());
			dos= new DataOutputStream(socks.getOutputStream());
			String n=dis.readUTF();

			clientMap.put(n, new InetSocketAddress(socks.getInetAddress(), 0));
			byte [] ip = socks.getInetAddress().getAddress(); 
			dos.write(ip); 
			
			
				new ChatThread( "ListenForConnection",  socks );
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
			
		
	}
}
