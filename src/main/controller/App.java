package main.controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.model.ProjectTesterImp;
import main.view.AppView;

import java.util.Objects;

public class App extends Application {
    ProjectTesterImp model;

    public App() {
        this.model = new ProjectTesterImp();
    }

    private void updateResults(AppView view) {
        String userText = view.getUserTextField().getText();
        if (!Objects.equals(userText, "")) {
            view.update(view.getUserTextField().getText(), view.getRadioBtn().isSelected(), 10);
        }
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Totally Google");
        AppView view = new AppView();
        GridPane grid = view.getGrid();

        // Interactions

        view.getUserTextField().textProperty().addListener((observable, oldValue, newValue) -> updateResults(view));

        view.getRadioBtn().selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> updateResults(view));

        view.getCrawlBtn().setOnAction(e -> {
            model.crawl(javax.swing.JOptionPane.showInputDialog("Please enter the seed URL: \nPlease note that this app will buffer while loading new dataset"));
            updateResults(view);
        });

        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
