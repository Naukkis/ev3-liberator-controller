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
* SystemStart luokan tarkoitus on k�ynnist�� graafinenk�ytt�liittym�
*
* @author Ville Naukkarinen, Hannu Havila, Riku Pelkonen
* @version 2.0
*/

public class SystemStart extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		/**
		* M��tell��n rootiksi FXML tiedosto
		*/
		Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
		/**
		* Asetaan k�ytt�liittym��n otsikko
		*/
		stage.setTitle("LeJOS EV3 - Remote controller");
		/**
		* Haetaan k�ytt�j�n koneen n�yt�n resoluution ja asetaan koekeus ja leveys sen mukaisiksi
		*/
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();

		/**
		* Asetetaan k�ytt�liim�lle FXML tiedosto ja edell� m��ritelllyt korkus ja leveys
		*/
		Scene scene = new Scene(root, width, height);
		stage.setScene(scene);
		/**
		* Asetetaan minimi k�ytt�liittym�n koko
		*/
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		/**
		* K�ynnistet��n k�ytt�liittym� kokon�ytt� tilassa
		*/
		stage.setFullScreen(true);
		stage.show();
	}

	public static void main(String[] args) {
		SystemStart.launch(SystemStart.class, args);
	}
}
