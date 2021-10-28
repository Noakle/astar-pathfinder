package de.hhn.it.pp.javafx.controllers.astarpathfinder;

import de.hhn.it.pp.components.astarpathfinding.PathfindingInformation;
import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import de.hhn.it.pp.components.astarpathfinding.provider.Terrain;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Cursor;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;

public class MapPane extends FlowPane {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MapPane.class);

  private CellLabel[][] map;
  private List<CellLabel> markedCells = new ArrayList<>();

  private AStarPathfinderController controller;

  private boolean obstaclePlaceMode;

  public MapPane(int width, int height, AStarPathfinderController controller) {
    this.controller = controller;
    createMap(width, height);
  }

  /**
   * Creates a new map of cells with a given width and height. Adds event handlers to the cells on
   * creation.
   *
   * @param width the width of the map
   * @param height the height of the map
   */
  public void createMap(int width, int height) {
    // Clear cells and set new size
    getChildren().clear();
    setPrefHeight(height * (CellLabel.CELL_SIZE) + 2);
    setPrefWidth(width * (CellLabel.CELL_SIZE) + 2);

    map = new CellLabel[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Position pos = new Position(row, col);
        CellLabel cell = new CellLabel(pos, TerrainType.DIRT);
        map[row][col] = cell;
        if (pos.equals(new Position((int) Math.ceil(height / 2f) - 1, 0))) {
          cell.setStartPoint(true);
        } else if (pos.equals(new Position((int) Math.ceil(height / 2f) - 1, width - 1))) {
          cell.setEndPoint(true);
        }

        addEventHandler(cell);
        getChildren().add(cell);
      }
    }

    logger.debug("createMap: map successfully created");
  }

  /** Resets the current map. */
  public void reset() {
    createMap(map[0].length, map.length);
  }

  /**
   * Displays the shortest path on the map if a path could be found by the algorithm.
   *
   * @param result the path information
   */
  public void showPath(List<PathfindingInformation> result) {
    // Clear path if shown
    removePathMarking();

    // Mark new path
    if (result.size() >= 1) {
      List<Terrain> finalPath = result.get(result.size() - 1).getFinalPathPositions();
      for (Terrain t : finalPath) {
        Position position = t.getPosition();
        CellLabel cell = map[position.getRow()][position.getCol()];
        if (cell != null) {
          if (!cell.isStartPoint() && !cell.isEndPoint()) {
            cell.markAsPath();
            markedCells.add(cell);
          }
        }
      }
    }
  }

  private void addEventHandler(CellLabel cell) {
    cell.setOnDragDetected(
        event -> {
          // drag was detected, start a drag-and-drop gesture allow any transfer mode
          if (cell.isStartPoint() || cell.isEndPoint()) {
            Dragboard db = cell.startDragAndDrop(TransferMode.ANY);
            // Put a string on a dragboard
            ClipboardContent content = new ClipboardContent();
            content.putString(cell.getText());
            db.setContent(content);
            // Clear path
            removePathMarking();
          }
          event.consume();
        });

    cell.setOnDragOver(
        event -> {
          CellLabel source = (CellLabel) event.getGestureSource();
          // data is dragged over the target
          if (source != cell && event.getDragboard().hasString()) {
            if ((source.isStartPoint() && !cell.isEndPoint())
                || (source.isEndPoint() && !cell.isStartPoint())) {
              //  allow for moving
              event.acceptTransferModes(TransferMode.ANY);
            }
          }
          event.consume();
        });

    cell.setOnDragDropped(
        event -> {
          CellLabel source = (CellLabel) event.getGestureSource();
          // data dropped
          Dragboard db = event.getDragboard();
          boolean success = false;
          if (db.hasString()) {
            if (source != null && !cell.equals(source)) {
              // Start and destination
              if (source.isStartPoint()) {
                controller.setStartPoint(source, cell);
              } else if (source.isEndPoint()) {
                controller.setEndPoint(source, cell);
              }
            }
            success = true;
          }
          event.setDropCompleted(success);
          event.consume();
        });

    cell.setOnMouseEntered(
        mouseEvent -> {
          if (obstaclePlaceMode) {
            setTerrain((CellLabel) mouseEvent.getSource());
          }
        });

    cell.setOnMouseClicked(
        mouseEvent -> {
          if (!cell.isStartPoint() && !cell.isEndPoint()) {
            obstaclePlaceMode = !obstaclePlaceMode;
            setCursor(obstaclePlaceMode ? Cursor.CROSSHAIR : Cursor.DEFAULT);
            controller.setTerrain((CellLabel) mouseEvent.getSource());
          }
        });
  }

  private void removePathMarking() {
    if (!markedCells.isEmpty()) {
      for (CellLabel cell : markedCells) {
        cell.setGraphic(null);
      }
      markedCells.clear();
    }
  }

  /**
   * Calls the setTerrain method in the controller.
   *
   * @param source the cell which should be changed
   */
  public void setTerrain(CellLabel source) {
    controller.setTerrain(source);
  }
}
