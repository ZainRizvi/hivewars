package hivewars;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


//abstracts some of the leg work needed for UDP sockets
public class UDPSocket{
	private DatagramSocket socket;
	protected SocketData remoteSocketData;
	int msgSize; //message size in bytes
	
	UDPSocket(int messageSize) throws SocketException, UnknownHostException{
		//initialize 
		msgSize = messageSize;		
		socket = new DatagramSocket(); 
	}
	
	UDPSocket(int messageSize, int port) throws SocketException, UnknownHostException{
		//initialize 
		msgSize = messageSize;		
		socket = new DatagramSocket(port); 
	}
	
	public void sendMessage(Object message, InetAddress srvIP, int srvPort) throws IOException {
		ByteArrayOutputStream b_out = new ByteArrayOutputStream();
		ObjectOutputStream o_out = new ObjectOutputStream(b_out);
		
		o_out.writeObject(message);
		byte[] sStream = b_out.toByteArray();
		DatagramPacket sPacket = new DatagramPacket(sStream, sStream.length, srvIP, srvPort);
		socket.send(sPacket);
	}
	
	public Object getMessage(){
		Object message = null;
		
		byte[] r_data = new byte[msgSize]; // Allocate space for the packet conservatively estimate the size
		ByteArrayInputStream b_in = new ByteArrayInputStream(r_data);// As we want to unflatten an object from a byte array we 
		 															 // wrap the byte array in a ByteArrayInputStream from
																	 // which we can read an unflattened object .
		DatagramPacket r_packet = new DatagramPacket(r_data, r_data.length);

		try {
			socket.receive(r_packet);
			ObjectInputStream o_in = new ObjectInputStream(b_in); // b_in contains the flattened message object
																  // pass it to an ObjectInputStream so we can read
			message = (Object) o_in.readObject();				  // the message in an unflattened form
		} catch (Exception e){e.printStackTrace();}
		
		//store location where message was received from
		try {
			remoteSocketData = new SocketData(r_packet.getAddress(), r_packet.getPort());
		} catch (UnknownHostException e) {e.printStackTrace();}
		return message;
	}
	
	public int getLocalPort(){
		return socket.getLocalPort();
	}
	
	public void close(){
		socket.close();
	}

}
