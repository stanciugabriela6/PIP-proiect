package org.example.proiectpip2;

import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Clasa principală a aplicației SmartDocs.
 *
 * Această clasă inițializează interfața grafică,
 * încarcă scenele JavaFX și aplică
 * animațiile vizuale ale aplicației.
 */
public class HelloApplication extends Application {

    /**
     * Pornește aplicația JavaFX.
     *
     * @param stage fereastra principală a aplicației
     * @throws IOException dacă fișierul FXML
     * nu poate fi încărcat
     */
    @Override
    public void start(Stage stage) throws IOException {

        Scene scene = buildScene("hello-view.fxml");

        stage.setTitle("SmartDocs â€” Sign In");

        stage.setScene(scene);

        stage.setMaximized(true);

        stage.show();
    }

    /**
     * Construiește o scenă JavaFX
     * pe baza unui fișier FXML.
     *
     * @param fxmlFile numele fișierului FXML
     * @return scena generată
     * @throws IOException dacă fișierul
     * nu poate fi încărcat
     */
    public static Scene buildScene(String fxmlFile) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(fxmlFile)
        );

        Parent content = loader.load();

        StackPane root = buildAnimatedRoot(content);

        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                HelloApplication.class
                        .getResource("style.css")
                        .toExternalForm()
        );

        return scene;
    }

    /**
     * Creează containerul principal
     * cu animații și efecte vizuale.
     *
     * @param content conținutul principal
     * al aplicației
     * @return containerul animat
     */
    public static StackPane buildAnimatedRoot(Parent content) {

        StackPane root = new StackPane();

        root.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #1e3a8a, #020617);"
        );

        /**
         * Primul cerc decorativ animat.
         */
        Circle c1 = new Circle(100, Color.web("#60a5fa"));

        c1.setTranslateX(-400);

        c1.setTranslateY(200);

        c1.setEffect(new GaussianBlur(70));

        TranslateTransition c1y =
                new TranslateTransition(Duration.seconds(6), c1);

        c1y.setByY(-80);

        c1y.setAutoReverse(true);

        c1y.setCycleCount(Animation.INDEFINITE);

        TranslateTransition c1x =
                new TranslateTransition(Duration.seconds(8), c1);

        c1x.setByX(50);

        c1x.setAutoReverse(true);

        c1x.setCycleCount(Animation.INDEFINITE);

        c1y.play();

        c1x.play();

        /**
         * Al doilea cerc decorativ animat.
         */
        Circle c2 = new Circle(130, Color.web("#3b82f6"));

        c2.setTranslateX(420);

        c2.setTranslateY(-120);

        c2.setEffect(new GaussianBlur(65));

        TranslateTransition c2y =
                new TranslateTransition(Duration.seconds(4), c2);

        c2y.setByY(-60);

        c2y.setAutoReverse(true);

        c2y.setCycleCount(Animation.INDEFINITE);

        ScaleTransition c2scale =
                new ScaleTransition(Duration.seconds(3), c2);

        c2scale.setToX(1.3);

        c2scale.setToY(1.3);

        c2scale.setAutoReverse(true);

        c2scale.setCycleCount(Animation.INDEFINITE);

        c2y.play();

        c2scale.play();

        /**
         * Al treilea cerc decorativ animat.
         */
        Circle c3 = new Circle(70, Color.web("#818cf8"));

        c3.setTranslateX(150);

        c3.setTranslateY(280);

        c3.setEffect(new GaussianBlur(50));

        TranslateTransition c3move =
                new TranslateTransition(Duration.seconds(5), c3);

        c3move.setByX(-90);

        c3move.setByY(-50);

        c3move.setAutoReverse(true);

        c3move.setCycleCount(Animation.INDEFINITE);

        c3move.play();

        /**
         * Configurează animația
         * de apariție a conținutului.
         */
        content.setOpacity(0);

        content.setTranslateY(40);

        FadeTransition fade =
                new FadeTransition(Duration.millis(700), content);

        fade.setToValue(1);

        TranslateTransition slide =
                new TranslateTransition(Duration.millis(600), content);

        slide.setToY(0);

        fade.play();

        slide.play();

        root.getChildren().addAll(c1, c2, c3, content);

        return root;
    }

    /**
     * Punctul principal de pornire
     * al aplicației.
     *
     * @param args argumentele liniei de comandă
     */
    public static void main(String[] args) {

        launch();
    }
}