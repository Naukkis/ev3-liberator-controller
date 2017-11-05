package application;

import java.io.IOException;
import java.net.Socket;

/**
* RoboConnector luokan tarkoitus on luoda yhteys robotin ja kkäyttäjän tietokoneen välille
*
* @author Ville Naukkarinen, Hannu Havila, Riku Pelkonen
* @version 2.0
*/

public class RoboConnector {
	/**
	* Muuttujan DataSingulator tarkoitus on tuoda singulator komponentti käyttöön
	*/
	private DataSingulator singulator;

	/**
	* Metodin RoboConnector tarkoitus on määritellä yhteinen portti jotta yhteys toimisi
	*/
	public RoboConnector() throws IOException {
		Socket socket = new Socket("10.0.1.1", 1111);
		this.singulator = new DataSingulator(socket);
		this.singulator.start();
	}

	/**
	* getDataSingulator idea on palauttaa singulator
	*/
	public DataSingulator getDataSingulator() {
		return singulator;
	}

}
