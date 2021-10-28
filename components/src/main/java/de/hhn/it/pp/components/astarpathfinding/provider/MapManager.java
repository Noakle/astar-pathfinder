package de.hhn.it.pp.components.astarpathfinding.provider;

import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import de.hhn.it.pp.components.astarpathfinding.exceptions.OccupiedPositionException;
import de.hhn.it.pp.components.astarpathfinding.exceptions.PositionOutOfBounds;
import de.hhn.it.pp.components.astarpathfinding.exceptions.PositionOutOfBounds.PositionType;

public class MapManager {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(MapManager.class);

  public static final int MAX_WIDTH = 29;
  public static final int MAX_HEIGHT = 18;
  public static final int MIN_WIDTH = 2;
  public static final int MIN_HEIGHT = 2;

  public static final int DEFAULT_WIDTH = 10;
  public static final int DEFAULT_HEIGHT = 10;

  public static final Position DEFAULT_START_POSITION =
      new Position((int) Math.ceil(DEFAULT_HEIGHT / 2f) - 1, 0);
  public static final Position DEFAULT_DESTINATION_POSITION =
      new Position((int) Math.ceil(DEFAULT_HEIGHT / 2f) - 1, DEFAULT_WIDTH - 1);

  private Position startCoordinates;
  private Position destinationCoordinates;

  private Terrain[][] map;

  /** Constructor of the MapManager class. */
  public MapManager() {
    logger.info("constructor: no params");
    try {
      createMap(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    } catch (PositionOutOfBounds e) {
      // Do nothing, because this case should never happen
      e.printStackTrace();
    }
  }

  /**
   * Creates the map with the given width and height.
   *
   * @param width the width of the map, must higher then 1
   * @param height the height of the map, must higher then 1
   * @throws PositionOutOfBounds if either the width or the height is invalid
   */
  public void createMap(int width, int height) throws PositionOutOfBounds {
    logger.info("createMap: width = {}, height = {}", width, height);
    // Check minimum boundaries of the map
    if (width < MIN_WIDTH || height < MIN_HEIGHT) {
      throw new PositionOutOfBounds("Width or height cannot be lower than 2!");
    }

    // Check maximum boundaries of the map
    if (width > MAX_WIDTH || height > MAX_HEIGHT) {
      throw new PositionOutOfBounds(
          String.format(
              "Width or height exceeded max value! Maximum width is %d. Maximum height is %d.",
              MAX_WIDTH, MAX_HEIGHT));
    }

    // Set new start and destination coordinates
    startCoordinates = new Position((int) Math.ceil(height / 2f) - 1, 0);
    destinationCoordinates = new Position((int) Math.ceil(height / 2f) - 1, width - 1);

    // Create new map with grass terrain
    map = new Terrain[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        map[row][col] = new Terrain(new Position(row, col), TerrainType.DIRT);
      }
    }

    logger.debug("createMap: map successfully created");
  }

  /**
   * Creates and places a terrain from the given type on the given position.
   *
   * @param type the terrain type
   * @param position the position on the map
   * @return the created terrain
   */
  public Terrain createTerrain(TerrainType type, Position position) throws PositionOutOfBounds {
    logger.debug("createTerrain: type = {}, position = {}", type, position.toString());

    // Check map boundaries
    checkPositionInBounds(position);
    Terrain terrain = new Terrain(position, type);
    map[position.getRow()][position.getCol()] = terrain;
    return terrain;
  }

  /**
   * Evaluates if the position is in the current map's boundaries.
   *
   * @param position the x and y coordinates
   * @throws PositionOutOfBounds thrown when the position is not on the map
   */
  private void checkPositionInBounds(Position position) throws PositionOutOfBounds {
    if (position.getRow() < 0) {
      throw new PositionOutOfBounds("X cannot be lower than 0!", PositionType.DEFAULT);
    } else if (position.getRow() > getHeight() - 1) {
      throw new PositionOutOfBounds(
          String.format("X cannot be greater than %d!", getHeight() - 1), PositionType.DEFAULT);
    } else if (position.getCol() < 0) {
      throw new PositionOutOfBounds("Y cannot be lower than 0!", PositionType.DEFAULT);
    } else if (position.getCol() > getWidth() - 1) {
      throw new PositionOutOfBounds(
          String.format("Y cannot be greater than %d!", getWidth() - 1), PositionType.DEFAULT);
    }
  }

  /** Overwrites the map with a new empty map of the same size. */
  public void reset() {
    logger.debug("reset: no params");
    try {
      createMap(map[0].length, map.length);
    } catch (PositionOutOfBounds e) {
      // Do nothing, because this case should never happen
      e.printStackTrace();
    }
  }

  /**
   * Returns the terrain at a given position.
   *
   * @param position the position to look at
   * @return the terrain at this position
   * @throws PositionOutOfBounds thrown if the given position is out of bounds
   */
  public Terrain getTerrainAt(Position position) throws PositionOutOfBounds {
    logger.debug("getTerrainAt: position = {}", position.toString());
    checkPositionInBounds(position);
    return map[position.getRow()][position.getCol()];
  }

  public Terrain[][] getMap() {
    return map;
  }

  public int getWidth() {
    return map[0].length;
  }

  public int getHeight() {
    return map.length;
  }

  public Position getStartCoordinates() {
    return startCoordinates;
  }

  /**
   * Places the start coordinate on the given position.
   *
   * @param startCoordinates the desired start position
   * @throws OccupiedPositionException thrown if the position is already blocked by the destination
   *     position
   * @throws PositionOutOfBounds thrown if the given position is out of bounds
   */
  public void setStartCoordinates(Position startCoordinates)
      throws OccupiedPositionException, PositionOutOfBounds {
    logger.debug("setStartCoordinates: startCoordinates = {}", startCoordinates.toString());

    // Check map boundaries
    checkPositionInBounds(startCoordinates);

    // Check occupied position
    if (getDestinationCoordinates().equals(startCoordinates)) {
      throw new OccupiedPositionException(
          "The start point cannot be placed on the destination point");
    }
    this.startCoordinates = startCoordinates;
  }

  public Position getDestinationCoordinates() {
    return destinationCoordinates;
  }

  /**
   * Places the destination coordinate on the given position.
   *
   * @param destinationCoordinates the desired destination position
   * @throws OccupiedPositionException thrown if the position is already blocked by the start
   *     position
   * @throws PositionOutOfBounds thrown if the given position is out of bounds
   */
  public void setDestinationCoordinates(Position destinationCoordinates)
      throws OccupiedPositionException, PositionOutOfBounds {
    logger.debug(
        "setDestinationCoordinates: destinationCoordinates = {}",
        destinationCoordinates.toString());

    // Check map boundaries
    checkPositionInBounds(destinationCoordinates);

    // Check occupied position
    if (getStartCoordinates().equals(destinationCoordinates)) {
      throw new OccupiedPositionException(
          "The destination point cannot be placed on the start point");
    }
    this.destinationCoordinates = destinationCoordinates;
  }


}
