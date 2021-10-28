package de.hhn.it.pp.javafx.controllers.astarpathfinder;

import de.hhn.it.pp.components.astarpathfinding.exceptions.PositionOutOfBounds;
import de.hhn.it.pp.components.astarpathfinding.provider.MapManager;
import de.hhn.it.pp.components.astarpathfinding.provider.Pathfinder;
import java.util.regex.Pattern;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class CreateMapDialog extends Dialog<Pair<String, String>> {

  private TextField widthField;
  private TextField heightField;

  /**
   * Creates a new map creation dialog.
   *
   * @param pathfinder the pathfinder interface implementation
   * @param mapPane the current map
   */
  public CreateMapDialog(Pathfinder pathfinder, MapPane mapPane) {
    setTitle("Create Map Dialog");
    setHeaderText("Choose the map size");

    // Create the ui elements
    ButtonType createButtonType = new ButtonType("Create", ButtonData.OK_DONE);
    getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    widthField = new TextField();
    widthField.setPromptText("max " + MapManager.MAX_WIDTH);
    heightField = new TextField();
    heightField.setPromptText("max " + MapManager.MAX_HEIGHT);

    Label widthErrorLabel =
        new Label(
            String.format(
                "The width must be between %d and %d", MapManager.MIN_WIDTH, MapManager.MAX_WIDTH));
    widthErrorLabel.setTextFill(Color.RED);
    widthErrorLabel.setVisible(false);
    Label heightErrorLabel =
        new Label(
            String.format(
                "The height must be between %d and %d",
                MapManager.MIN_HEIGHT, MapManager.MAX_HEIGHT));
    heightErrorLabel.setTextFill(Color.RED);
    heightErrorLabel.setVisible(false);

    grid.add(new Label("Width:"), 0, 0);
    grid.add(widthField, 1, 0);
    grid.add(new Label("Height:"), 0, 1);
    grid.add(heightField, 1, 1);
    grid.add(widthErrorLabel, 2, 0);
    grid.add(heightErrorLabel, 2, 1);

    Node createButton = getDialogPane().lookupButton(createButtonType);
    createButton.setDisable(true);

    // Do some validation for width textField
    widthField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                onValueChange(heightField, widthErrorLabel, createButton, newValue));

    // Do some validation for height textField
    heightField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                onValueChange(widthField, heightErrorLabel, createButton, newValue));

    getDialogPane().setContent(grid);

    // Try to create the map when the create button is clicked.
    setResultConverter(
        dialogButton -> {
          if (dialogButton == createButtonType) {
            try {
              pathfinder.createMap(
                  Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
              mapPane.createMap(
                  Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
            } catch (PositionOutOfBounds e) {
              e.printStackTrace();
            }
          }

          return null;
        });
  }

  private void onValueChange(
      TextField textField, Label errorLabel, Node createButton, String newValue) {
    boolean isValidNumber = false;
    try {
      int value = Integer.parseInt(newValue);
      int min;
      int max;

      if (textField.equals(heightField)) {
        min = MapManager.MIN_WIDTH;
        max = MapManager.MAX_WIDTH;
      } else {
        min = MapManager.MIN_HEIGHT;
        max = MapManager.MAX_HEIGHT;
      }

      if (value < min || value > max) {
        errorLabel.setVisible(true);
      } else {
        isValidNumber = true;
        errorLabel.setVisible(false);
      }
    } catch (NumberFormatException e) {
      errorLabel.setVisible(true);
    }

    createButton.setDisable(
        !checkTextInput(newValue) || !checkTextInput(textField.getText()) || !isValidNumber);
  }

  private boolean checkTextInput(String text) {
    return Pattern.matches("^\\d+$", text);
  }
}
