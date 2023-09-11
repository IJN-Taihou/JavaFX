package javaFX;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JavaClassFX extends Application {

	private volatile boolean isPlaying = false;
	private SourceDataLine line;
	private Thread audioThread;

	@Override
	public void start(Stage primaryStage) {
		String homeScreenMusic = "C:\\Users\\Sandro Santos\\Downloads\\Images\\Game Images\\Audio\\moreboulets.wav";
		// Create a VBox to hold all UI components
		VBox titlepos = new VBox(); //VBOX meaning Vertical Box
		Pane buttonpos = new Pane();

		//Sets the positions of the title and buttons
		titlepos.setAlignment(Pos.BASELINE_CENTER);

		// Create an ImageView for the background image
		ImageView backgroundImage = new ImageView(new Image("C:\\Users\\Sandro Santos\\Downloads\\Images\\Game Images\\moreboulets.jpg"));
		backgroundImage.setFitWidth(1366);
		backgroundImage.setFitHeight(768);

		//Layers the title on top of the background image
		StackPane stackPane = new StackPane(backgroundImage, titlepos, buttonpos);

		// Create UI components
		Text title = new Text("I need more Boolets");
		title.setFont(Font.font("Times New Roman", 72));
		title.setFill(Color.CRIMSON);
		title.setStroke(Color.DARKRED);
		title.setStrokeWidth(2);

		//Gives the buttons their own unique names and creates the buttons
		Button newGameButton = new Button("New Game");
		Button loadGameButton = new Button("Load Game");
		Button exitButton = new Button("Exit Game");
		Button musicButton = new Button("Toggle Music");

		//newgamebutton sizing, custom skin (Animations), and positioning
		newGameButton.setMinHeight(120);
		newGameButton.setMaxHeight(120);
		newGameButton.setMinWidth(700);
		newGameButton.setMaxWidth(700);
		newGameButton.setLayoutX(-500);
		newGameButton.setLayoutY(184);
		HoverTranslateButton newGameAni = new HoverTranslateButton(newGameButton);

		//loadgamebutton sizing, custom skin (Animations), and positioning
		loadGameButton.setMinHeight(120);
		loadGameButton.setMaxHeight(120);
		loadGameButton.setMinWidth(700);
		loadGameButton.setMaxWidth(700);
		loadGameButton.setLayoutX(-500);
		loadGameButton.setLayoutY(320);
		HoverTranslateButton loadGameAni = new HoverTranslateButton(loadGameButton);

		//musicbutton sizing, custom skin (Animations),  and positioning
		musicButton.setMinHeight(70);
		musicButton.setMaxHeight(70);
		musicButton.setMinWidth(700);
		musicButton.setMaxWidth(700);
		musicButton.setLayoutX(-500);
		musicButton.setLayoutY(456);

		//exitbutton sizing, custom skin (Animations),  and positioning
		exitButton.setMinHeight(70);
		exitButton.setMaxHeight(70);
		exitButton.setMinWidth(700);
		exitButton.setMaxWidth(700);
		exitButton.setLayoutX(-500);
		exitButton.setLayoutY(545);
		HoverTranslateButton exitGameAni = new HoverTranslateButton(exitButton);


		HoverTranslateButton musicButtonAni = new HoverTranslateButton(musicButton);

		//gets the "children" for the parent (Groups them together)
		titlepos.getChildren().addAll(title);
		buttonpos.getChildren().addAll(newGameButton, loadGameButton, musicButton, exitButton);

		primaryStage.setTitle("Game");
		primaryStage.setScene(new Scene(stackPane, 800, 600));
		primaryStage.show();

		//When clicked chooses an action based on which button has been pressed
		newGameButton.setOnAction(e -> {
			// Start a new game, navigate to the first scene
		});

		loadGameButton.setOnAction(e -> {
			// Load a saved game, navigate to the last saved scene
		});

		musicButton.setOnAction(e -> {
			toggleAudioPlayback(homeScreenMusic);
		});

		exitButton.setOnAction(e -> {
			primaryStage.close();
			System.exit(0);
		});

		//Changes the colour of the buttons on the start page
		newGameButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; "
				+ "-fx-font-size: 18px; -fx-font-weight: bold; "
				+ "-fx-border-color: black; -fx-border-radius: 4;"
				+ "-fx-border-width: 1.25; -fx-border-insets: -1; -fx-opacity: 0.5;");
		newGameButton.setAlignment(Pos.BASELINE_RIGHT);

		loadGameButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; "
				+ "-fx-font-size: 18px; -fx-font-weight: bold; "
				+ "-fx-border-color: black; -fx-border-radius: 4;"
				+ "-fx-border-width: 1.25; -fx-border-insets: -1; -fx-opacity: 0.5;");
		loadGameButton.setAlignment(Pos.BASELINE_RIGHT);

		musicButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; "
				+ "-fx-font-size: 18px; -fx-font-weight: bold; "
				+ "-fx-border-color: black; -fx-border-radius: 4;"
				+ "-fx-border-width: 1.25; -fx-border-insets: -1; -fx-opacity: 0.5;");
		musicButton.setAlignment(Pos.BASELINE_RIGHT);

		exitButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; "
				+ "-fx-font-size: 18px; -fx-font-weight: bold; "
				+ "-fx-border-color: black; -fx-border-radius: 4; "
				+ "-fx-border-width: 1.25; -fx-border-insets: -1; -fx-opacity: 0.5;");
		exitButton.setAlignment(Pos.BASELINE_RIGHT);

	}

	public void toggleAudioPlayback(String music) {
        if (!isPlaying) {
            isPlaying = true;
            startAudio(music);
        } else {
            isPlaying = false;
            stopAudio();
        }
    }

    public void startAudio(String music) {
        if (audioThread != null && audioThread.isAlive()) {
            return; // Audio playback is already running
        }

        audioThread = new Thread(() -> {
            playAudio(music);
        });

        audioThread.start();
    }

    public void stopAudio() {
        if (line != null) {
            line.drain();
            line.close();
        }
    }

    public void playAudio(String audioFilePath) {
        File audioFile = new File(audioFilePath);

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);

            line.start();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while (isPlaying && (bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            line.drain();
            line.close();
            audioStream.close();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
