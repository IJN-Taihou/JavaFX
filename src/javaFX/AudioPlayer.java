package javaFX;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AudioPlayer extends Application {

    private volatile boolean isPlaying = false;
    private SourceDataLine line;
    private Thread audioThread;
    private Button musicButton;
    private Button musicButton2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox titlepos = new VBox();
        Pane buttonpos = new Pane();
        titlepos.setAlignment(Pos.BASELINE_CENTER);

        ImageView backgroundImage = new ImageView(new Image("C:\\Users\\Sandro Santos\\Downloads\\Images\\Game Images\\moreboulets.jpg"));
        backgroundImage.setFitWidth(1366);
        backgroundImage.setFitHeight(768);

        StackPane stackPane = new StackPane(backgroundImage, titlepos, buttonpos);

        Text title = new Text("I need more Boolets");
        title.setFont(Font.font("Times New Roman", 72));
        title.setFill(Color.CRIMSON);
        title.setStroke(Color.DARKRED);
        title.setStrokeWidth(2);

        musicButton = new Button("Toggle Music");
        musicButton2 = new Button("Toggle Music2");
        musicButton.setMinHeight(70);
        musicButton.setMaxHeight(70);
        musicButton.setMinWidth(700);
        musicButton.setMaxWidth(700);
        musicButton.setLayoutX(-500);
        musicButton.setLayoutY(456);
        HoverTranslateButton musicButtonAni = new HoverTranslateButton(musicButton);
        musicButton2.setMinHeight(70);
        musicButton2.setMaxHeight(70);
        musicButton2.setMinWidth(700);
        musicButton2.setMaxWidth(700);
        musicButton2.setLayoutX(-500);
        musicButton2.setLayoutY(256);
        HoverTranslateButton musicButtonAni2 = new HoverTranslateButton(musicButton2);

        titlepos.getChildren().addAll(title);
        buttonpos.getChildren().addAll(musicButton, musicButton2);

        primaryStage.setTitle("Visual Novel Start Page");
        primaryStage.setScene(new Scene(stackPane, 800, 600));
        primaryStage.show();
        String bobies = "C:\\Users\\Sandro Santos\\Downloads\\Images\\Game Images\\Audio\\Beautiful.wav";  
        String bobies2 = "C:\\Users\\Sandro Santos\\Downloads\\Images\\Game Images\\Audio\\EU4.wav";
        musicButton2.setOnAction(e -> toggleAudioPlayback(bobies));
        musicButton.setOnAction(e -> toggleAudioPlayback(bobies2));
    }

    public void toggleAudioPlayback(String audioFilePath) {
        if (!isPlaying) {
            isPlaying = true;
            startAudio(audioFilePath);
        } else {
            isPlaying = false;
            stopAudio();
        }
    }

    public void startAudio(String audioFilePath) {
        if (audioThread != null && audioThread.isAlive()) {
            return; // Audio playback is already running
        }

        audioThread = new Thread(() -> {
            playAudio(audioFilePath);
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
}