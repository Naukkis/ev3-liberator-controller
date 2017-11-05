package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
/**
* DataSngulator perii Thread-luokan ja saa parametrina socketin ja luo input/output virrat. Lukee String-muotoisia viestejä robotilta. Vie int-muotoisia käskyjä robootille.
*
* @author Ville Naukkarinen, Hannu Havila, Riku Pelkonen
* @version 2.0
*/
public class DataSingulator extends Thread {
	private Socket socket;
	private DataOutputStream dataOut;
	private DataInputStream dataIn;
	/**
	* Pysäytysehto.
	*/
	private boolean connected;
	/**
	* Robotilta luettu viesti.
	*/
	private String message;

	public DataSingulator(Socket socket) throws IOException {
		this.socket = socket;
		this.dataOut = new DataOutputStream(socket.getOutputStream());
		this.dataIn = new DataInputStream(socket.getInputStream());
		connected = true;
	}

	@Override
	public void run() {
		while(connected) {
			try {
				message = dataIn.readUTF();
			} catch (IOException e) {
				System.out.println("datastream in not working");
				connected = false;
			}
		}
	}
	/**
	* Lähettää robotille datavirrassa käyttäjältä saadut komennot
	* @param Ohjauskomento
	*/
	public void sendCommand(int command){
		try {
			dataOut.writeInt(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	* Vastaanottaa viestejä robotilta.
	* @return Palauttaa viestin stringinä.
	*/
	public String getMessageFromRobot() {
		System.out.println(message);
		return message;
	}


}
