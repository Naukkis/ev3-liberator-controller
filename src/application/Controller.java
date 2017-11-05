package application;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;

/**
* Controller luokan tarkoitus on hallita kï¿½yttï¿½jï¿½n kï¿½skyjï¿½ ja siirtï¿½ï¿½ niitï¿½ robotille ja sama toisinpï¿½in
*
* @author Ville Naukkarinen, Hannu Havila, Riku Pelkonen
* @version 2.0
*/
public class Controller {

	RoboConnector connector;
	/**
	* Muuttujan MediaPlayer tarkoitus on tuoda kï¿½yttï¿½liittymï¿½lle mahdollisuus toistaa videota
	*/
	private MediaPlayer mediaPlayer;
	/**
	* Muuttujan gasReleased tarkoitus on estï¿½ï¿½ useamman kuin yhden komennon lï¿½hettï¿½minen kerralla
	*/
	boolean gasReleased = true;
	/**
	* Muuttujan steeringReleased tarkoitus on estï¿½ï¿½ useamman kuin yhden komennon lï¿½hettï¿½minen kerralla
	*/
	boolean steeringReleased = true;
	/**
	* Muuttujan towerReleased tarkoitus on estï¿½ï¿½ useamman kuin yhden komennon lï¿½hettï¿½minen kerralla
	*/
	boolean towerReleased = true;
	/**
	* Muuttujan cannonReleased tarkoitus on estï¿½ï¿½ useamman kuin yhden komennon lï¿½hettï¿½minen kerralla
	*/
	boolean cannonReleased = true;
	/**
	* Muuttujien tarkoitus on luoda koodissa kuvaavat nimet komennoille jotka muuten lï¿½hetettï¿½isiin numerona
	*/
	private final int FORWARD = 0, RIGHT = 1, BACKWARD = 2, LEFT = 3, GAS_STOP = 4, STEERING_STOP = 5, TOWER_RIGHT = 6,
			TOWER_LEFT = 7, TOWER_STOP = 8, FIRE_CANNON_UP = 9, CANNON_STOP = 11;

	/**
	* Muuttujan MediaView tarkoitus on mahdollistaa videon nï¿½yttï¿½minen kï¿½yttï¿½liittymï¿½ssï¿½
	*/
	@FXML
	private MediaView mediaView;
	/**
	* Muuttujan filePath mï¿½ï¿½rittï¿½ï¿½ videon sijainnin
	*/
	@FXML
	private String filePath = "http://10.5.5.9:8080/live/amba.m3u8";
	/**
	* Muuttujan speedslider takoitus on mï¿½ï¿½ritellï¿½ FXML kï¿½ytettï¿½vï¿½ slider
	*/
	@FXML
	private Slider speedSlider = new Slider(0, 1, 1);
	/**
	* Muuttujan buttonFWD tarkoitus on mï¿½ï¿½ritellï¿½ FXML kï¿½ytettï¿½vï¿½ ohjainnappi
	*/
	@FXML
	private Button buttonMoveFWD;
	/**
	* Muuttujan buttonFWD tarkoitus on mï¿½ï¿½ritellï¿½ FXML kï¿½ytettï¿½vï¿½ ohjainnappi
	*/
	@FXML
	private Button buttonMoveLeft;
	/**
	* Muuttujan buttonFWD tarkoitus on mï¿½ï¿½ritellï¿½ FXML kï¿½ytettï¿½vï¿½ ohjainnappi
	*/
	@FXML
	private Button buttonMoveRight;
	/**
	* Muuttujan buttonFWD tarkoitus on mï¿½ï¿½ritellï¿½ FXML kï¿½ytettï¿½vï¿½ ohjainnappi
	*/
	@FXML
	private Button buttonMoveBCK;
	/**
	* Muuttujan buttonFWD tarkoitus on mï¿½ï¿½ritellï¿½ FXML kï¿½ytettï¿½vï¿½ ohjainnappi
	*/
	@FXML
	private Button rotateLeft;
	/**
	* Muuttujan buttonFWD tarkoitus on mï¿½ï¿½ritellï¿½ FXML kï¿½ytettï¿½vï¿½ ohjainnappi
	*/
	@FXML
	private Button rotateRight;
	/**
	* Muuttujan buttonFWD tarkoitus on mï¿½ï¿½ritellï¿½ FXML kï¿½ytettï¿½vï¿½ ohjainnappi
	*/
	@FXML
	private Button shoot;
	/**
	* Muuttujan sliderValue tarkoitus on pï¿½ivittï¿½ï¿½ kï¿½yttï¿½liittymï¿½ssï¿½ oleva teksti nopeudelle
	*/
	@FXML
	private Text sliderValue;
	/**
	* Muuttujan batteryStatus tarkoitus on pï¿½ivittï¿½ï¿½ kï¿½yttï¿½liittymï¿½ssï¿½ oleva teksti patterin varaustasolle
	*/
	@FXML
	private Text batteryStatus;

	/**
	* Metodin initialize tarkoitus on lisï¿½tï¿½ ohjelman ja kï¿½yttï¿½liittymï¿½n vï¿½lille "kuuntelijoita" sliderille ja nï¿½ppï¿½imille
	*/
	@FXML
	public void initialize() throws IOException {
	connector = new RoboConnector();
		speedSlider.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                 	int speedSetting = new_val.intValue();
                 	sliderValue.setText(Double.toString(speedSetting));
                 	connector.getDataSingulator().sendCommand(speedSetting);

            }
        });

		buttonMoveFWD.armedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println( "armed: " + newValue);
                if(newValue) {
                	connector.getDataSingulator().sendCommand(FORWARD);
                } else {
                	connector.getDataSingulator().sendCommand(GAS_STOP);
                }
            }
        });

		buttonMoveLeft.armedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println( "armed: " + newValue);
                if(newValue) {
                	connector.getDataSingulator().sendCommand(LEFT);
                } else {
                	connector.getDataSingulator().sendCommand(STEERING_STOP);
                }
            }
        });
		buttonMoveRight.armedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println( "armed: " + newValue);
                if(newValue) {
                	connector.getDataSingulator().sendCommand(RIGHT);
                } else {
                	connector.getDataSingulator().sendCommand(STEERING_STOP);
                }
            }
        });
		buttonMoveBCK.armedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println( "armed: " + newValue);
                if(newValue) {
                	connector.getDataSingulator().sendCommand(BACKWARD);
                } else {
                	connector.getDataSingulator().sendCommand(GAS_STOP);
                }
            }
        });
		rotateLeft.armedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println( "armed: " + newValue);
                if(newValue) {
                	connector.getDataSingulator().sendCommand(TOWER_RIGHT);
                } else {
                	connector.getDataSingulator().sendCommand(TOWER_STOP);
                }
            }
        });
		rotateRight.armedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println( "armed: " + newValue);
                if(newValue) {
                	connector.getDataSingulator().sendCommand(TOWER_LEFT);
                } else {
                	connector.getDataSingulator().sendCommand(TOWER_STOP);
                }
            }
        });
		shoot.armedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println( "armed: " + newValue);
                if(newValue) {
                	connector.getDataSingulator().sendCommand(FIRE_CANNON_UP);
                } else {
                	connector.getDataSingulator().sendCommand(CANNON_STOP);
                }
            }
        });
	}

	/**
	* Metodin handleKeyPressed tarkoitus on hallita taphtumaa jossa kï¿½yttï¿½jï¿½ on painanut nï¿½ppï¿½imistï¿½stï¿½ nappia
	* @param tapahtuma joka indikoi mitï¿½ nï¿½ppï¿½intï¿½ kï¿½yttï¿½jï¿½ on painanut
	* @param event otetaan haltuun kyseinen tapahtuma
	*/
	@FXML
	private void handleKeyPressed(KeyEvent event) {
		String code = event.getCode().toString();
		if (steeringReleased) {
			if (code.equals("A")) {
				connector.getDataSingulator().sendCommand(LEFT);
				steeringReleased = false;
			} else if (code.equals("D")) {
				connector.getDataSingulator().sendCommand(RIGHT);
				steeringReleased = false;
			}
		}
		if (gasReleased) {
			if (code.equals("W")) {
				connector.getDataSingulator().sendCommand(FORWARD);
				gasReleased = false;
			} else if (code.equals("S")) {
				connector.getDataSingulator().sendCommand(BACKWARD);
				gasReleased = false;
			}
		}
		if (towerReleased) {
			if (code.equals("J")) {
				connector.getDataSingulator().sendCommand(TOWER_RIGHT);
				towerReleased = false;
			} else if (code.equals("L")) {
				connector.getDataSingulator().sendCommand(TOWER_LEFT);
				towerReleased = false;
			}
		}
		if (cannonReleased && code.equals("K")) {
			connector.getDataSingulator().sendCommand(FIRE_CANNON_UP);
			cannonReleased = false;
		}
		if (code.equals("U")) {
			batteryStatus.setText((connector.getDataSingulator().getMessageFromRobot()+" V"));
			connector.getDataSingulator().getMessageFromRobot();
		}
	}

	/**
	* Metodin handleKeyReleased tarkoitus on hallita tapahtumia jos kï¿½yttï¿½jï¿½n painama nï¿½ppï¿½in on vapautettu
	* @param tapahtuma joka indikoi mitï¿½ nï¿½ppï¿½intï¿½ kï¿½yttï¿½jï¿½ on painanut
	* @param event otetaan haltuun kyseinen tapahtuma
	*/
	@FXML
	private void handleKeyReleased(KeyEvent event) {
		String code = event.getCode().toString();

		if (code.equals("A") || code.equals("D")) {
			connector.getDataSingulator().sendCommand(STEERING_STOP);
			steeringReleased = true;
		}
		if (code.equals("W") || code.equals("S")) {
			connector.getDataSingulator().sendCommand(GAS_STOP);
			gasReleased = true;
		}
		if (code.equals("J") || code.equals("L")) {
			connector.getDataSingulator().sendCommand(TOWER_STOP);
			towerReleased = true;
		}
		if (code.equals("K")) {
			connector.getDataSingulator().sendCommand(CANNON_STOP);
			cannonReleased = true;
		}
		connector.getDataSingulator().sendCommand(-1);
	}

	/**
	* Metodin playVideo tarkoitus on aloittaa videon toistaminen kï¿½yttï¿½jï¿½n painettua videon kï¿½ynnistysnappia
	* @param ActionEvent tapahtuma joka ilmoittaa komponentti-määritelty tapahtuma on tapahtunut.
	* @param event otetaan haltuun kyseinen tapahtuma
	*/
	@FXML
	public void playVideo(ActionEvent event) {

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
		@Override
			public void run() {
		    	Platform.runLater(new Runnable() {
		    		@Override
		            public void run() {
		    			Media media = new Media(filePath);
		    			mediaPlayer = new MediaPlayer(media);
		    			mediaView.setMediaPlayer(mediaPlayer);
		    			mediaPlayer.play();
		            }
		        });
		    }
		}, 0, 2000);


	}

	/**
	* Metodin manualControl tarkoitus on siirtää robotti manuaaliseen ajotilaan
	* @param ActionEvent tapahtuma joka ilmoittaa komponentti-määritelty tapahtuma on tapahtunut.
	* @param event otetaan haltuun kyseinen tapahtuma
	*/
	@FXML
	private void manualControl(ActionEvent event) {
		//TODO
	}

	/**
	* Metodin handleKeyPressed tarkoitus on lisï¿½tï¿½ ohjelman ja nï¿½ppï¿½imistï¿½n vï¿½lille "kuuntelija"
	* @param ActionEvent tapahtuma joka ilmoittaa komponentti-määritelty tapahtuma on tapahtunut.
	* @param event otetaan haltuun kyseinen tapahtuma
	*/
	@FXML
	private void demoMode(ActionEvent event) {
		connector.getDataSingulator().sendCommand(1001);
	}
}
