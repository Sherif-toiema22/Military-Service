package com.sherif.gettingthedifferenceelement;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class FileDiff extends Application {

    private TextArea textArea1;
    private TextArea textArea2;
    private TextArea diffArea;

    private List<String> file1Lines = new ArrayList<>();
    private List<String> file2Lines = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CSV Difference Tool (Arabic Supported)");

        Button loadFile1Btn = new Button("Load CSV File 1");
        Button loadFile2Btn = new Button("Load CSV File 2");
        Button compareBtn = new Button("Compare");

        textArea1 = new TextArea();
        textArea2 = new TextArea();
        diffArea = new TextArea();

        textArea1.setPromptText("CSV File 1 content...");
        textArea2.setPromptText("CSV File 2 content...");
        diffArea.setPromptText("Differences will appear here...");

        textArea1.setWrapText(true);
        textArea2.setWrapText(true);
        diffArea.setWrapText(true);

        HBox buttons = new HBox(10, loadFile1Btn, loadFile2Btn, compareBtn);
        VBox root = new VBox(10, buttons,
                new Label("File 1:"), textArea1,
                new Label("File 2:"), textArea2,
                new Label("Differences:"), diffArea);

        root.setPadding(new Insets(10));

        // Button actions
        loadFile1Btn.setOnAction(e -> {
            File file = chooseFile(primaryStage);
            if (file != null) {
                file1Lines = readFile(file);
                textArea1.setText(String.join("\n", file1Lines));
            }
        });

        loadFile2Btn.setOnAction(e -> {
            File file = chooseFile(primaryStage);
            if (file != null) {
                file2Lines = readFile(file);
                textArea2.setText(String.join("\n", file2Lines));
            }
        });

        compareBtn.setOnAction(e -> compareFiles());

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private File chooseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        return fileChooser.showOpenDialog(stage);
    }

    private List<String> readFile(File file) {
        try {
            // Reads CSV as plain lines (UTF-8 ensures Arabic text works)
            return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            showError("Error reading file: " + file.getName());
            return new ArrayList<>();
        }
    }

    private void compareFiles() {
        // Extract names with line numbers
        Map<String, Integer> names1 = extractNamesWithLineNumbers(file1Lines);
        Map<String, Integer> names2 = extractNamesWithLineNumbers(file2Lines);

        // Just sets for difference calculation
        Set<String> set1 = new HashSet<>(names1.keySet());
        Set<String> set2 = new HashSet<>(names2.keySet());

        Set<String> diff1 = new HashSet<>(set1);
        diff1.removeAll(set2);

        Set<String> diff2 = new HashSet<>(set2);
        diff2.removeAll(set1);

        // Show result
        StringBuilder sb = new StringBuilder();
        sb.append("Names in File1 but not in File2:\n");
        diff1.forEach(name -> sb.append("Line ")
                .append(names1.get(name))
                .append(": ")
                .append(name)
                .append("\n"));

        sb.append("\nNames in File2 but not in File1:\n");
        diff2.forEach(name -> sb.append("Line ")
                .append(names2.get(name))
                .append(": ")
                .append(name)
                .append("\n"));

        diffArea.setText(sb.toString());
    }

    private Map<String, Integer> extractNamesWithLineNumbers(List<String> lines) {
        Map<String, Integer> names = new HashMap<>();
        boolean skipHeader = true;
        int lineNum = 0;
        for (String line : lines) {
            lineNum++;
            if (skipHeader) {
                skipHeader = false;
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length > 1) {
                names.put(parts[1].trim(), lineNum); // index 1 = name column
            }
        }
        return names;
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

}
