package zirkleacProject4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

	public static void main(String[] args) {
		System.out.println("Enter your username");
		Scanner s=new Scanner(System.in);
		String name=s.next();
		ChatGUI con=new ChatGUI(name);
		s.close();
		DataInputStream dis;
		DataOutputStream dos;
		try {
			
			new ChatThread( name, new Socket("127.0.0.1", 31200 ) );
			
			
			
			
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
