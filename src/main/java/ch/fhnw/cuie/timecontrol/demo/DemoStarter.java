package ch.fhnw.cuie.timecontrol.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class DemoStarter extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PresentationModel pm = new PresentationModel();
        Region rootPanel = new DemoPane(pm);
        rootPanel.setStyle("-fx-background-color: linear-gradient(black,grey);");


        Scene scene = new Scene(rootPanel);


        primaryStage.setTitle("Business Control Demo");
        primaryStage.setScene(scene);

        primaryStage.setMinHeight(520);
        primaryStage.setMinWidth(420);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
