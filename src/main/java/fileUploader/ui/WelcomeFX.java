package fileUploader.ui;

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
import org.example.proiectpip2.infra.ServiceBootstrap;

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

        Rectangle glass = new Rectangle(420, 260);
        glass.setArcWidth(30);
        glass.setArcHeight(30);
        glass.setFill(Color.rgb(255, 255, 255, 0.08));
        glass.setStroke(Color.rgb(255, 255, 255, 0.15));

        Label title = new Label("SmartDocs");
        title.setStyle("""
                -fx-font-size: 34px;
                -fx-font-weight: bold;
                -fx-text-fill: #e2e8f0;
                -fx-font-family: 'Segoe UI', system-ui, sans-serif;
                -fx-effect: dropshadow(gaussian, rgba(99,135,255,0.6), 16, 0.3, 0, 0);
                """);

        Label desc = new Label("Intelligent RAG Document Assistant");
        desc.setStyle("""
                -fx-text-fill: #6387ff;
                -fx-font-size: 13px;
                -fx-font-weight: 600;
                -fx-font-family: 'Segoe UI', system-ui, sans-serif;
                """);

        Label serviceStatus = new Label("Starting services: Face API, Ollama/Chroma, RAG...");
        serviceStatus.setStyle("""
                -fx-text-fill: #4b5563;
                -fx-font-size: 12px;
                -fx-font-family: 'Segoe UI', system-ui, sans-serif;
                """);
        serviceStatus.setWrapText(true);
        serviceStatus.setMaxWidth(340);

        Button btn = new Button("Get Started  →");
        btn.setDisable(true);
        btn.setStyle("""
                -fx-background-color: #6387ff;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-font-family: 'Segoe UI', system-ui, sans-serif;
                -fx-background-radius: 10;
                -fx-padding: 10 28;
                -fx-effect: dropshadow(gaussian, rgba(99,135,255,0.45), 12, 0.15, 0, 4);
        """);

        btn.setOnAction(e -> {
            try {
                Scene scenaNoua = org.example.proiectpip2.HelloApplication.buildScene("hello-view.fxml");

                Stage stageCurent = (Stage) btn.getScene().getWindow();
                stageCurent.setScene(scenaNoua);
                stageCurent.setTitle("SmartDocs - Sign In");
                Platform.runLater(() -> {
                    stageCurent.setMaximized(false);
                    stageCurent.setMaximized(true);
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox box = new VBox(15, title, desc, serviceStatus, btn);
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

        ServiceBootstrap.warmUpAsync().whenComplete((report, err) -> Platform.runLater(() -> {
            if (err != null) {
                serviceStatus.setText("Startup error: " + err.getMessage());
                serviceStatus.setStyle("-fx-text-fill: #f87171; -fx-font-size: 12px; -fx-font-weight: 600;");
                btn.setDisable(false);
                return;
            }

            if (report.success()) {
                serviceStatus.setText("All services ready.");
                serviceStatus.setStyle("-fx-text-fill: #34d399; -fx-font-size: 12px; -fx-font-weight: 600;");
                btn.setStyle(btn.getStyle() + "-fx-opacity: 1;");
            } else {
                serviceStatus.setText("Partial startup: " + report.details());
                serviceStatus.setStyle("-fx-text-fill: #fbbf24; -fx-font-size: 12px; -fx-font-weight: 600;");
            }
            btn.setDisable(false);
        }));
    }

    public static void main(String[] args) {
        launch();
    }
}