package hivewars;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

//Data structure used to store UDP socket information
public class SocketData implements Serializable{
	
	protected InetAddress IPAddr;
	protected int port;
	
	SocketData(InetAddress IP, int port) throws UnknownHostException{
		this.IPAddr = IP;
		this.port = port;
	}
	
	public String toString(){
		return ("IP Addr = " + this.IPAddr + " : port " + this.port);
	}
	
	public boolean equals(SocketData op2){
		if(this.IPAddr.equals(op2.IPAddr)){
			if(this.port == op2.port){
				return true;
			}
		}
		return false;
	}
}
