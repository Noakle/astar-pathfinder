package de.hhn.it.pp.javafx.controllers.astarpathfinder;

import de.hhn.it.pp.components.astarpathfinding.PathfindingInformation;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import de.hhn.it.pp.components.astarpathfinding.exceptions.OccupiedPositionException;
import de.hhn.it.pp.components.astarpathfinding.exceptions.PositionOutOfBounds;
import de.hhn.it.pp.components.astarpathfinding.provider.MapManager;
import de.hhn.it.pp.components.astarpathfinding.provider.Pathfinder;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;
import de.hhn.it.pp.javafx.controllers.Controller;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class AStarPathfinderController extends Controller implements Initializable {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(AStarPathfinderController.class);

  @FXML public Button createNewMapButton;
  @FXML public ComboBox<TerrainType> obstacleComboBox;
  @FXML public Slider costSlider;
  @FXML public Label costLabel;
  @FXML public FlowPane mapContainer;
  @FXML public SplitPane splitPane;
  @FXML public Label obstacleColorLabel;
  @FXML public CheckBox diagonalPathingCheckbox;

  private Pathfinder pathfinder;
  private final MapPane mapPane;

  /**
   * Map table for TerrainType and their Color.
   */
  public static final Map<TerrainType, Color> TERRAIN_COLOR;

  static {
    TERRAIN_COLOR =
        Map.of(
            TerrainType.DIRT, CellLabel.DIRT_COLOR,
            TerrainType.GRASS, CellLabel.GRASS_COLOR,
            TerrainType.SWAMP, CellLabel.SWAMP_COLOR,
            TerrainType.WATER, CellLabel.WATER_COLOR,
            TerrainType.LAVA, CellLabel.LAVA_COLOR);
  }

  /**
   * Constructor for the Javafx AStarPathfinderController.
   */
  public AStarPathfinderController() {
    pathfinder = new Pathfinder();
    mapPane = new MapPane(MapManager.DEFAULT_WIDTH, MapManager.DEFAULT_HEIGHT, this);
    logger.debug("AStarPathfinderController has been created");
  }

  /**
   * Called to initialize a controller after its root element has been completely processed.
   *
   * @param url The location used to resolve relative paths for the root object, or <tt>null</tt> if
   *     the location is not known.
   * @param resourceBundle The resources used to localize the root object, or <tt>null</tt> if
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    // Add Background color to mapContainer
    mapContainer.setBackground(
        new Background(
            new BackgroundFill(Color.rgb(150, 150, 150), CornerRadii.EMPTY, Insets.EMPTY)));

    // Assign mapPane to mapContainer
    mapContainer.getChildren().add(mapPane);

    // Assign terrain types to obstacleComboBox
    obstacleComboBox.getItems().setAll(TerrainType.values());
    obstacleComboBox.setValue(TerrainType.DIRT);

    // Add Dirt Color to coloLabel
    obstacleColorLabel.setBackground(
        new Background(new BackgroundFill(CellLabel.DIRT_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

    // Initialize cost label
    costLabel.setText(String.valueOf((int) TerrainType.DIRT.getModifier()));

    // Add listener to the cost slider
    costSlider.setValue(TerrainType.DIRT.getModifier());
    costSlider
        .valueProperty()
        .addListener(
          (ov, oldValue, newValue) -> {
            try {
              pathfinder.changeTerrainTypeModifier(
                  obstacleComboBox.getSelectionModel().getSelectedItem(),
                  newValue.doubleValue());
              costLabel.setText(String.valueOf(newValue.intValue()));
            } catch (IllegalParameterException e) {
              logger.debug("The value for the terrainTypeModifier was too low or too high");
              e.printStackTrace();
            }
          });
  }

  /**
   * Called when the create map button was clicked. A dialog will be opened.
   *
   * @param actionEvent the action event
   */
  public void onCreateNewMap(ActionEvent actionEvent) {
    CreateMapDialog dialog = new CreateMapDialog(pathfinder, mapPane);
    dialog.showAndWait();
  }

  /**
   * Called when the reset button was clicked. A call the the backends reset method will be done.
   * The ui will be set to the default values.
   *
   * @param actionEvent the action event
   */
  public void onResetMap(ActionEvent actionEvent) {
    pathfinder.reset();
    mapPane.reset();
    // Reset cost label and slider
    costLabel.setText(
        String.valueOf((int) obstacleComboBox.getSelectionModel().getSelectedItem().getModifier()));
    costSlider.setValue(obstacleComboBox.getSelectionModel().getSelectedItem().getModifier());
  }

  /**
   * Called when the start visualization button was clicked. Triggers the pathfinding algorithm and
   * fetches the result. The result will be displayed on the current map.
   *
   * @param actionEvent the action event
   */
  public void onStartVisualization(ActionEvent actionEvent) {
    try {
      List<PathfindingInformation> result = pathfinder.doPathfinding();
      mapPane.showPath(result);
    } catch (IllegalParameterException e) {
      e.printStackTrace();
    }
  }

  /**
   * Called when a new terrain type is selected in the comboBox. The model and the ui elements which
   * show the types cost will be updated.
   *
   * @param actionEvent the action event
   */
  public void onSelectObstacle(ActionEvent actionEvent) {
    if (actionEvent.getSource() instanceof ComboBox) {
      ComboBox<?> comboBox = (ComboBox<?>) actionEvent.getSource();
      if (comboBox.getSelectionModel().getSelectedItem() instanceof TerrainType) {
        TerrainType type = (TerrainType) comboBox.getSelectionModel().getSelectedItem();
        costSlider.setValue(type.getModifier());
        costLabel.setText(String.valueOf((int) type.getModifier()));
        Color color = AStarPathfinderController.TERRAIN_COLOR.get(type);
        if (color != null) {
          obstacleColorLabel.setBackground(
              new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        }
      }
    }
  }

  /**
   * Sets the new start point.
   *
   * @param oldPoint the old start point
   * @param newPoint the new start point
   */
  public void setStartPoint(CellLabel oldPoint, CellLabel newPoint) {
    try {
      pathfinder.setStartPoint(newPoint.getPosition());
      newPoint.setStartPoint(true);
      oldPoint.setStartPoint(false);
    } catch (OccupiedPositionException e) {
      logger.info("The start point cannot be placed on the end point");
    } catch (PositionOutOfBounds positionOutOfBounds) {
      positionOutOfBounds.printStackTrace();
    }
  }

  /**
   * Sets the new end point.
   *
   * @param oldPoint the old end point
   * @param newPoint the new end point
   */
  public void setEndPoint(CellLabel oldPoint, CellLabel newPoint) {
    try {
      pathfinder.setEndPoint(newPoint.getPosition());
      newPoint.setEndPoint(true);
      oldPoint.setEndPoint(false);
    } catch (OccupiedPositionException e) {
      logger.info("The end point cannot be placed on the start point");
    } catch (PositionOutOfBounds positionOutOfBounds) {
      positionOutOfBounds.printStackTrace();
    }
  }

  /**
   * Places a new terrain on the map. Only if the terrain type is different from the previous one.
   *
   * @param cell the cell which should be changed
   */
  public void setTerrain(CellLabel cell) {
    TerrainType selectedTerrainType = obstacleComboBox.getSelectionModel().getSelectedItem();
    if (!cell.getType().equals(selectedTerrainType)) {
      try {
        pathfinder.placeTerrain(selectedTerrainType, cell.getPosition());
        cell.setType(selectedTerrainType);
      } catch (PositionOutOfBounds positionOutOfBounds) {
        positionOutOfBounds.printStackTrace();
      }
    }
  }

  /**
   * Enables or disabled diagonal pathing.
   *
   * @param actionEvent event
   */
  public void onCheck(ActionEvent actionEvent) {
    pathfinder.setDiagonalPathing(diagonalPathingCheckbox.isSelected());
  }
}
