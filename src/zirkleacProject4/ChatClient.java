package zirkleacProject4;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * The ChatClientCon class is a thread that allows the checking of the console for inputs
 * @author Andrew Zirkle
 *
 */
class ChatClientCon extends Thread {
	// Scanner s = new Scanner(System.in);
	BufferedReader br;
	Socket Base;
	private String name;
	public boolean breaker=false;
	ArrayList<ChatThread> clientConnections;

	/**
	 * Constructor for ChatClientCon which checks the console for input, user names to ask the server for the addresses
	 * @param name The username
	 */
	public ChatClientCon(String name, ArrayList<ChatThread> clients) {
		this.clientConnections=clients;
		this.name = name;
		br = new BufferedReader(new InputStreamReader(System.in));
		start();
	}
/**
 * The run method checks for user input and then checks with the server if it is a vaild username
 * @param clientConnections 
 */
	public void run() {
		// System.out.println("Got to this point");
		for (; true;) {

			String connector = "";
			try {
				connector = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (!connector.equalsIgnoreCase("")) {
				if (connector.equalsIgnoreCase("quit")){
					breaker= true;
					break;
				}
				if(connector.equalsIgnoreCase("remove")){
					try {
					Socket home = new Socket("127.0.0.1", ChatServer.SERVERPORT);

					DataInputStream dis = new DataInputStream(
							home.getInputStream());
					DataOutputStream dos = new DataOutputStream(
							home.getOutputStream());

					dos.writeInt(2);
					dos.writeUTF(this.name);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
				try {
					Socket home = new Socket("127.0.0.1", ChatServer.SERVERPORT);

					DataInputStream dis = new DataInputStream(
							home.getInputStream());
					DataOutputStream dos = new DataOutputStream(
							home.getOutputStream());

					dos.writeInt(1);
					dos.writeUTF(connector);
					String response = dis.readUTF();
					System.out.println(response);
					if (!response.equalsIgnoreCase("Sorry we can't find it"))
						clientConnections.add(new ChatThread(name, new Socket(
								InetAddress.getByName(response), dis.readInt())));
					connector = "";
					home.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
				}
				}

			}

		
		}
}
/**
 * ChatClient
 * @author Andrew Zirkle
 * The ChatClient first registers itself with the server and then runs a two part check, one checking the console input and another checking for incoming connections.
 */
public class ChatClient {
	Socket homeServer;
	ArrayList<ChatThread> clientConnections = new ArrayList<ChatThread>();

	/**
	 * ChatClient Constructor to start the client
	 */
	public ChatClient() {
		try {
			homeServer = new Socket("127.0.0.1", ChatServer.SERVERPORT);
			// new ChatClientCon(homeServer);
			System.out.println("Enter your username");
			Scanner s = new Scanner(System.in);
			String name = s.next();
			// s.close();
			System.out
					.println("Enter a username to be connected to it or type quit to exit.");
			DataOutputStream dos = new DataOutputStream(
					homeServer.getOutputStream());
			ServerSocket clientCon = new ServerSocket(0);
			dos.writeInt(0);
			dos.writeUTF(name);
			int port = clientCon.getLocalPort();
			System.out.println();
			dos.writeInt(port);
			homeServer.close();
			ChatClientCon coner=new ChatClientCon(name, clientConnections);
			
			for (; true;) {
				clientConnections.add(new ChatThread(name, clientCon.accept()));
				if (coner.breaker)
					break;

			}
			for (int i = 0; i < clientConnections.size(); i++) {
				clientConnections.get(i).closeTheConnection();
			}
			clientCon.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		new ChatClient();
	}
}

