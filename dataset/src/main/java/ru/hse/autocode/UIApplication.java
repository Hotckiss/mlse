package ru.hse.autocode;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.hse.autocode.csv.SparseCSVBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * UI version of app for collecting dataset
 */
public class UIApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Refactorings Loader");

        StackPane root = new StackPane();

        GridPane gridpane = UIUtils.makeGridPane();

        Label datasetLabel = new Label("Repos path:");
        final TextField datasetInput = new TextField();
        Button datasetLoadButton = new Button(" RUN ");
        datasetLoadButton.setStyle
                (
                        "-fx-font-size: 11px;"
                                + "-fx-background-color: #c2d22b;"
                                + "-fx-border-style: solid inside;"
                                + "-fx-border-width: 0pt;"
                );
        UIUtils.addDatasetInput(gridpane, datasetLabel, datasetInput, datasetLoadButton);

        datasetLoadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String path = datasetInput.getText();
                runWithPath(path);
            }
        });

        Label allLabel = new Label("All repositories progress:");
        GridPane.setHalignment(allLabel, HPos.LEFT);
        gridpane.add(allLabel,  2, 1);
        ProgressBar allBar = new ProgressBar();
        GridPane.setHalignment(allBar, HPos.LEFT);
        gridpane.add(allBar, 3, 1);
        Label allLabelProg = new Label("0/1");
        GridPane.setHalignment(allLabelProg, HPos.CENTER);
        gridpane.add(allLabelProg,  4, 1);

        Label binLabel = new Label("Current repository progress:");
        GridPane.setHalignment(binLabel, HPos.LEFT);
        gridpane.add(binLabel,  2, 2);
        ProgressBar algorithmBar = new ProgressBar();
        GridPane.setHalignment(algorithmBar, HPos.LEFT);
        gridpane.add(algorithmBar, 3, 2);
        Label binLabelProg = new Label("0/1");
        GridPane.setHalignment(binLabelProg, HPos.CENTER);
        gridpane.add(binLabelProg,  4, 2);

        CheckBox useDirectExtraction = new CheckBox("Directly extract from file");
        useDirectExtraction.setSelected(true);
        GridPane.setHalignment(useDirectExtraction, HPos.LEFT);
        gridpane.add(useDirectExtraction,  2, 4);
        useDirectExtraction.setOnAction(event -> GlobalConfig.extractDirectly = useDirectExtraction.isSelected());

        CheckBox onlyExtractedOperation = new CheckBox("Print only extractions");
        onlyExtractedOperation.setSelected(true);
        GridPane.setHalignment(onlyExtractedOperation, HPos.LEFT);
        gridpane.add(onlyExtractedOperation,  2, 5);
        onlyExtractedOperation.setOnAction(event -> GlobalConfig.onlyExtractedOperation = onlyExtractedOperation.isSelected());

        CheckBox noSubfolders = new CheckBox("Results without subfolders");
        GridPane.setHalignment(noSubfolders, HPos.LEFT);
        gridpane.add(noSubfolders,  2, 6);
        noSubfolders.setOnAction(event -> GlobalConfig.noSubfolders = noSubfolders.isSelected());

        Label totalRefactorings = new Label("Total method extractions:");
        GridPane.setHalignment(totalRefactorings, HPos.LEFT);
        gridpane.add(totalRefactorings,  2, 7);

        Label totalRefactoringsCount = new Label("0");
        GridPane.setHalignment(totalRefactoringsCount, HPos.LEFT);
        gridpane.add(totalRefactoringsCount,  3, 7);
        root.getChildren().add(gridpane);
        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.show();
    }

    /**
     * dataset collection runner
     * @param path path to repos file
     */
    private void runWithPath(String path) {
        System.out.println(path);
        File file = new File(path);

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String> repos = new ArrayList<>();
            br.lines().forEach(s -> repos.add(s));
            SparseCSVBuilder.sharedInstance = new SparseCSVBuilder("true_commons.csv", GlobalConfig.nFeatures);


        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("File " + path + " not found!");
            alert.setContentText("Please, correct file name");

            alert.showAndWait();
        }
    }
}