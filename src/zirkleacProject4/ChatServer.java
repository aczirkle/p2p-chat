package zirkleacProject4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 * ChatServerListener
 * @author Andrew Zirkle
 * A thread which, based on the command received adds someone to the list, returns a ip and port number of a requested username
 * or returns a username not found message, or removes from the list
 */
class ChatServerListener extends Thread{
		
private Socket socks;
private ConcurrentHashMap<String, InetSocketAddress> clientMap;
/**
 * This thread that takes a command and returns an appropriate response.
 * @param socks The current socket connecting
 * @param clientMap The hashmap of all the users in the system
 */
	public ChatServerListener(Socket socks, ConcurrentHashMap<String, InetSocketAddress> clientMap)  {
		this.socks=socks;
		this.clientMap=clientMap;
				
		start();
	}
	public void run() {
		DataInputStream dis;
		try {
			dis = new DataInputStream(socks.getInputStream());

		DataOutputStream dos = new DataOutputStream(socks.getOutputStream());
		int cse= dis.readInt();
		//String n= dis.readUTF();
		System.out.println("Got command: "+cse);
		if(cse==0){
			String n= dis.readUTF();
			int port= dis.readInt();
			clientMap.putIfAbsent(n, new InetSocketAddress(socks.getInetAddress(),
					port));
			System.out.println(n+" was added to the group at "+socks.getInetAddress()+":"+port);
			}
		else if(cse==1){
			String n= dis.readUTF();
			if(clientMap.containsKey(n)){
				dos.writeUTF(clientMap.get(n).getHostString());
				dos.writeInt(clientMap.get(n).getPort());
			}
			else
				dos.writeUTF("Sorry we can't find it");
		}
		else if(cse==2){
			String n=dis.readUTF();
			clientMap.remove(n);
			System.out.println(n+" was removed");
		}
		
		dos.close();
		dis.close();
		socks.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
	/**
	 * ChatServer which holds all the usernames and addresses
	 * @author Andrew Zirkle
	 *
	 */
public class ChatServer {

	public static final int SERVERPORT = 31200;
	ConcurrentHashMap<String, InetSocketAddress> clientMap = new ConcurrentHashMap<String, InetSocketAddress>();

	/**
	 * ChatServer Constructor which creates a ServerSocket and then accepts connections
	 */
	public ChatServer() {

			try {
				ServerSocket serverSocket = new ServerSocket(SERVERPORT);
				for(;true;){
				new ChatServerListener(serverSocket.accept(), clientMap);
				}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}
}
