package de.hhn.it.pp.javafx.controllers.astarpathfinder;

import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class CellLabel extends Label {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CellLabel.class);

  public static final int CELL_SIZE = 30;

  public static final Color DIRT_COLOR = Color.rgb(150, 80, 40);
  public static final Color GRASS_COLOR = Color.rgb(90, 150, 40);
  public static final Color SWAMP_COLOR = Color.rgb(40, 150, 110);
  public static final Color WATER_COLOR = Color.rgb(40, 120, 150);
  public static final Color LAVA_COLOR = Color.rgb(235, 95, 30);

  public static final Insets INSETS = new Insets(2);

  private Position position;
  private TerrainType type;
  private boolean isStartPoint;
  private boolean isEndPoint;

  private static ImageView startIcon;
  private static ImageView endIcon;

  {
    // Image Source
    InputStream input = getClass().getResourceAsStream("/AStarPathfinder/image/start.png");
    Image image = new Image(input);
    startIcon = new ImageView(image);
    input = getClass().getResourceAsStream("/AStarPathfinder/image/ziel.png");
    image = new Image(input);
    endIcon = new ImageView(image);
  }

  /**
   * Constructor of a CellLabel.
   *
   * @param position the position on the map
   * @param type the terrain types
   */
  public CellLabel(Position position, TerrainType type) {
    super();
    this.position = position;
    this.setPrefSize(CELL_SIZE, CELL_SIZE);
    setType(type);
  }

  public TerrainType getType() {
    return type;
  }

  /**
   * Sets the terrain type of the cell. Depending on the this type the cell will have a colored
   * background.
   *
   * @param type the terrain type
   */
  public void setType(TerrainType type) {
    this.type = type;
    Color color = AStarPathfinderController.TERRAIN_COLOR.get(type);
    if (color != null) {
      setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, INSETS)));
    }
  }

  public boolean isStartPoint() {
    return isStartPoint;
  }

  public boolean isEndPoint() {
    return isEndPoint;
  }

  /**
   * Marks the current cell as the start point. An image (icon) will be displayed. If the cell was
   * the start point but was changed to a normal cell then the icon will be removed.
   *
   * @param startPoint boolean that indicates if the cell should be the start point
   */
  public void setStartPoint(boolean startPoint) {
    isStartPoint = startPoint;
    if (startPoint) {
      this.setGraphic(startIcon);
    } else {
      this.setGraphic(null);
    }
  }

  /**
   * Marks the current cell as the end point. An image (icon) will be displayed. If the cell was the
   * end point but was changed to a normal cell then the icon will be removed.
   *
   * @param endPoint boolean that indicates if the cell should be the end point
   */
  public void setEndPoint(boolean endPoint) {
    isEndPoint = endPoint;
    if (endPoint) {
      this.setGraphic(endIcon);
    } else {
      this.setGraphic(null);
    }
  }

  public Position getPosition() {
    return position;
  }

  /** Marks the current cell as a part of the shortest path. An image (icon) will be displayed. */
  public void markAsPath() {
    InputStream input = getClass().getResourceAsStream("/AStarPathfinder/image/path.png");
    Image image = new Image(input);
    setGraphic(new ImageView(image));
  }
}
