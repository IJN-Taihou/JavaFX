package javaFX;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class HoverTranslateButton {
    private Button button;
    private TranslateTransition transition;
    private FadeTransition fadeTransitionIn;
    private FadeTransition fadeTransitionOut;

    public HoverTranslateButton(Button button) {
        this.button = button;

        // Create TranslateTransition
        transition = new TranslateTransition(Duration.millis(100), button);
        transition.setToX(50);

        // Create FadeTransition for fading in
        fadeTransitionIn = new FadeTransition(Duration.millis(100), button);
        fadeTransitionIn.setFromValue(0.5);
        fadeTransitionIn.setToValue(1.0);

        // Create FadeTransition for fading out
        fadeTransitionOut = new FadeTransition(Duration.millis(100), button);
        fadeTransitionOut.setFromValue(0.5);
        fadeTransitionOut.setToValue(0.5);
        fadeTransitionOut.setOnFinished(event -> {
            transition.setToX(0);
            transition.play();
        });

        // Set up event handlers
        button.setOnMouseEntered(event -> {
            fadeTransitionOut.stop();
            transition.stop();
            transition.setToX(250);
            fadeTransitionIn.play();
            transition.play();
        });

        button.setOnMouseExited(event -> {
            fadeTransitionIn.stop();
            fadeTransitionOut.play();
        });
    }
}
