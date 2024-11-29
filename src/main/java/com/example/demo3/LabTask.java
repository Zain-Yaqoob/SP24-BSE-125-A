package com.example.demo3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class LabTask extends Application {

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(60, 60, 45, 45));
        gridPane.setStyle("-fx-background-color: linear-gradient(to bottom, #1e2a47, #4a4e69);");

        Scene mainScene = new Scene(gridPane, 500, 500);

        // Applying global text styling
        String textStyle = "-fx-text-fill: white; -fx-font-size: 13px;";

        Label userNameLabel = new Label("User Name");
        userNameLabel.setStyle(textStyle);
        gridPane.add(userNameLabel, 0, 1);

        TextField userNameTextField = new TextField();
        gridPane.add(userNameTextField, 1, 1);

        Label IDLabel = new Label("ID");
        IDLabel.setStyle(textStyle);
        gridPane.add(IDLabel, 0, 2);

        PasswordField passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);

        Label homeProvinceLabel = new Label("Home Province");
        homeProvinceLabel.setStyle(textStyle);
        gridPane.add(homeProvinceLabel, 0, 3);

        TextField homeField = new TextField();
        gridPane.add(homeField, 1, 3);

        Label DOBLabel = new Label("DOB");
        DOBLabel.setStyle(textStyle);
        gridPane.add(DOBLabel, 0, 4);

        DatePicker datePicker = new DatePicker();
        HBox datePickerBox = new HBox();
        datePickerBox.getChildren().add(datePicker);
        gridPane.add(datePickerBox, 1, 4);

        Label genderLabel = new Label("Gender");
        genderLabel.setStyle(textStyle);
        gridPane.add(genderLabel, 0, 5);

        RadioButton maleButton = new RadioButton("Male");
        maleButton.setStyle(textStyle);
        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setStyle(textStyle);

        ToggleGroup genderGroup = new ToggleGroup();
        maleButton.setToggleGroup(genderGroup);
        femaleButton.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(10, maleButton, femaleButton);
        genderBox.setAlignment(Pos.BASELINE_LEFT);
        gridPane.add(genderBox, 1, 5);

        Button newButton = new Button("NEW");
        gridPane.add(newButton, 3, 1);
        newButton.setOnAction(e -> {
            String gender = "";
            if (maleButton.isSelected()) {
                gender = "Male";
            } else if (femaleButton.isSelected()) {
                gender = "Female";
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
                String data = String.format("%s,%s,%s,%s,%s%n", userNameTextField.getText(),
                        passwordField.getText(), homeField.getText(), datePicker.getValue(), gender);
                writer.write(data);

                primaryStage.close();
                start(new Stage());
            } catch (IOException io) {
                System.out.println(io.getMessage());
            }
        });

        Button deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 3, 2);

        Button restoreButton = new Button("Restore");
        gridPane.add(restoreButton, 3, 3);

        Button findNButton = new Button("Find Next");
        gridPane.add(findNButton, 3, 5);

        Button criteriaButton = new Button("Criteria");
        gridPane.add(criteriaButton, 3, 6);

        Button findPreButton = new Button("Find Previous");
        gridPane.add(findPreButton, 3, 4);

        findPreButton.setOnAction(e -> openFindPreviousWindow());

        // Close Button
        Button closeButton = new Button("Close");
        gridPane.add(closeButton, 3, 7);

        closeButton.setOnAction(e -> System.exit(0));

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("My Application");
        primaryStage.show();
    }

    private void openFindPreviousWindow() {
        Stage findStage = new Stage();
        GridPane findGrid = new GridPane();
        findGrid.setHgap(15);
        findGrid.setVgap(10);
        findGrid.setPadding(new Insets(15, 15, 15, 15));
        findGrid.setStyle("-fx-background-color: linear-gradient(to bottom, #1e2a47, #4a4e69);");

        Scene findScene = new Scene(findGrid, 400, 200);

        Label nameLabel = new Label("Enter Name:");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        findGrid.add(nameLabel, 0, 0);

        TextField nameField = new TextField();
        findGrid.add(nameField, 1, 0);

        Button findButton = new Button("Find");
        findGrid.add(findButton, 1, 1);

        findButton.setOnAction(e -> {
            String searchName = nameField.getText();
            boolean userFound = false;
            String userInfo = "";

            try (Scanner scanner = new Scanner(new File("users.txt"))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts[0].equalsIgnoreCase(searchName)) {
                        userInfo = String.format("User Name: %s%nID: %s%nHome Province: %s%nDOB: %s%nGender: %s",
                                parts[0], parts[1], parts[2], parts[3], parts[4]);
                        userFound = true;
                        break;
                    }
                }
            } catch (IOException io) {
                System.out.println(io.getMessage());
            }

            if (userFound) {
                Stage resultStage = new Stage();
                TextArea resultArea = new TextArea(userInfo);
                resultArea.setEditable(false);

                Scene resultScene = new Scene(resultArea, 400, 200);
                resultStage.setScene(resultScene);
                resultStage.setTitle("User Information");
                resultStage.show();
                findStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User Not Found");
                alert.setHeaderText(null);
                alert.setContentText("No user found with the name: " + searchName);
                alert.showAndWait();
            }
        });

        findStage.setScene(findScene);
        findStage.setTitle("Find Previous User");
        findStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}