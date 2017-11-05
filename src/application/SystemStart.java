package application;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
* SystemStart luokan tarkoitus on käynnistää graafinenkäyttöliittymä
*
* @author Ville Naukkarinen, Hannu Havila, Riku Pelkonen
* @version 2.0
*/

public class SystemStart extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		/**
		* Määtellään rootiksi FXML tiedosto
		*/
		Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
		/**
		* Asetaan käyttöliittymään otsikko
		*/
		stage.setTitle("LeJOS EV3 - Remote controller");
		/**
		* Haetaan käyttäjän koneen näytön resoluution ja asetaan koekeus ja leveys sen mukaisiksi
		*/
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();

		/**
		* Asetetaan käyttöliimälle FXML tiedosto ja edellä määritelllyt korkus ja leveys
		*/
		Scene scene = new Scene(root, width, height);
		stage.setScene(scene);
		/**
		* Asetetaan minimi käyttöliittymän koko
		*/
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		/**
		* Käynnistetään käyttöliittymä kokonäyttö tilassa
		*/
		stage.setFullScreen(true);
		stage.show();
	}

	public static void main(String[] args) {
		SystemStart.launch(SystemStart.class, args);
	}
}
