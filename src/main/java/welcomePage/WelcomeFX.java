package welcomePage;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeFX extends Application {

    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3a8a, #020617);");

        Circle c1 = new Circle(80, Color.web("#60a5fa"));
        c1.setTranslateX(-250);
        c1.setTranslateY(150);
        c1.setEffect(new GaussianBlur(60));

        TranslateTransition c1y = new TranslateTransition(Duration.seconds(6), c1);
        c1y.setByY(-60);
        c1y.setAutoReverse(true);
        c1y.setCycleCount(Animation.INDEFINITE);

        TranslateTransition c1x = new TranslateTransition(Duration.seconds(8), c1);
        c1x.setByX(40);
        c1x.setAutoReverse(true);
        c1x.setCycleCount(Animation.INDEFINITE);

        c1y.play();
        c1x.play();

        Circle c2 = new Circle(100, Color.web("#3b82f6"));
        c2.setTranslateX(250);
        c2.setTranslateY(150);
        c2.setEffect(new GaussianBlur(50));

        TranslateTransition c2y = new TranslateTransition(Duration.seconds(4), c2);
        c2y.setByY(-50);
        c2y.setAutoReverse(true);
        c2y.setCycleCount(Animation.INDEFINITE);

        ScaleTransition c2scale = new ScaleTransition(Duration.seconds(3), c2);
        c2scale.setToX(1.2);
        c2scale.setToY(1.2);
        c2scale.setAutoReverse(true);
        c2scale.setCycleCount(Animation.INDEFINITE);

        c2y.play();
        c2scale.play();

        Rectangle glass = new Rectangle(420, 230);
        glass.setArcWidth(30);
        glass.setArcHeight(30);
        glass.setFill(Color.rgb(255, 255, 255, 0.08));
        glass.setStroke(Color.rgb(255, 255, 255, 0.15));

        Label title = new Label("Welcome to SmartDocs");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label desc = new Label("Your intelligent RAG document assistant");
        desc.setStyle("-fx-text-fill: #94a3b8;");

        Button btn = new Button("Get Started");
        btn.setStyle("""
                -fx-background-color: linear-gradient(to right, #2563eb, #3b82f6);
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-background-radius: 20;
                -fx-padding: 8 20 8 20;
        """);

        btn.setOnAction(e -> {
            try {
                Scene scenaNoua = org.example.proiectpip2.HelloApplication.buildScene("hello-view.fxml");

                Stage stageCurent = (Stage) btn.getScene().getWindow();
                stageCurent.setScene(scenaNoua);
                stageCurent.setTitle("SmartDocs — Sign In");
                Platform.runLater(() -> {
                    stageCurent.setMaximized(false);
                    stageCurent.setMaximized(true);
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox box = new VBox(15, title, desc, btn);
        box.setAlignment(Pos.CENTER);

        StackPane glassPane = new StackPane(glass, box);

        glassPane.setOpacity(0);
        glassPane.setTranslateY(50);

        FadeTransition fade = new FadeTransition(Duration.seconds(2), glassPane);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.seconds(1.5), glassPane);
        slide.setToY(0);

        fade.play();
        slide.play();

        root.getChildren().addAll(c1, c2, glassPane);

        Scene scene = new Scene(root);

        stage.setTitle("SmartDocs");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}