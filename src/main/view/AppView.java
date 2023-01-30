package main.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import main.model.ProjectTesterImp;
import main.utils.SearchResult;

public class AppView extends GridPane {
    private final ProjectTesterImp model;
    private final GridPane grid;
    private final ListView<SearchResult> searchResultList;
    private final Button crawlBtn;
    private final RadioButton radioBtn;
    private final TextField userTextField;

    public GridPane getGrid() {
        return grid;
    }
    public RadioButton getRadioBtn() {
        return radioBtn;
    }
    public Button getCrawlBtn() {
        return crawlBtn;
    }
    public TextField getUserTextField() {
        return userTextField;
    }

    public AppView() {
        this.model = new ProjectTesterImp();
        // Set screen grid
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Totally Google");
        sceneTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 32));
        grid.add(sceneTitle, 0, 0, 3, 1);

        Label queryLabel = new Label("Search Query:");
        queryLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 12));
        grid.add(queryLabel, 0, 1);

        userTextField = new TextField();
        userTextField.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 12));
        grid.add(userTextField, 0, 2);

        radioBtn = new RadioButton("Boost with PageRank?");
        radioBtn.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 12));
        grid.add(radioBtn, 0, 3);

        Label resultLabel = new Label("Search Results:");
        resultLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 12));
        grid.add(resultLabel, 0, 5);

        searchResultList = new ListView<>();
        grid.add(searchResultList, 0, 6);

        crawlBtn = new Button("New Dataset");
        crawlBtn.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 12));
        grid.add(crawlBtn, 0, 7);
    }

    public void update(String query, boolean boost, int X) {
        this.searchResultList.getItems().clear();
        for (SearchResult searchResult : this.model.search(query, boost, X)) {
            this.searchResultList.getItems().add(searchResult);
        }
    }
}