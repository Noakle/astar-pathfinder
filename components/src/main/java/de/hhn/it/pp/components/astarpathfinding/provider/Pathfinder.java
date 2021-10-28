package de.hhn.it.pp.components.astarpathfinding.provider;

import de.hhn.it.pp.components.astarpathfinding.PathfindingInformation;
import de.hhn.it.pp.components.astarpathfinding.PathfindingService;
import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import de.hhn.it.pp.components.astarpathfinding.exceptions.OccupiedPositionException;
import de.hhn.it.pp.components.astarpathfinding.exceptions.PositionOutOfBounds;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;
import java.util.List;

public final class Pathfinder implements PathfindingService {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(Pathfinder.class);

  private final MapManager mapManager = new MapManager();
  private boolean diagonalPathing;

  @Override
  public void createMap(int width, int height)
      throws PositionOutOfBounds {
    logger.info("createMap: width = {} height = {}", width, height);
    mapManager.createMap(width, height);
  }

  @Override
  public void setStartPoint(Position position)
      throws PositionOutOfBounds, OccupiedPositionException {
    logger.info("setStartPoint: position = {}", position.toString());
    mapManager.setStartCoordinates(position);
  }

  @Override
  public void setEndPoint(Position position) throws PositionOutOfBounds, OccupiedPositionException {
    logger.info("setEndPoint: position = {}", position.toString());
    mapManager.setDestinationCoordinates(position);
  }

  @Override
  public void placeTerrain(TerrainType type, Position position) throws PositionOutOfBounds {
    logger.info("placeTerrain: type = {}, position = {} ", type, position.toString());
    mapManager.createTerrain(type, position);
  }

  @Override
  public List<PathfindingInformation> doPathfinding() throws IllegalParameterException {
    logger.info("doPathfinding: no params");
    return new AStarPathfindingAlgorithm(mapManager, diagonalPathing).findPath();
  }

  @Override
  public void reset() {
    logger.info("reset: no params");
    // Reset the TerrainType modifiers
    TerrainType.resetModifers();
    // Reset the mapManager
    mapManager.reset();
  }

  @Override
  public void changeTerrainTypeModifier(TerrainType type, double modifier)
      throws IllegalParameterException {
    logger.info("changeTerrainTypeFactor: type = {}, modifier = {} ", type, modifier);
    // Check the range of the modifier
    if (modifier < TerrainType.MIN_VALUE) {
      throw new IllegalParameterException(
          String.format(
              "Invalid modifier value! Modifier must not be lower than %f!",
              TerrainType.MIN_VALUE));
    } else if (modifier > TerrainType.MAX_VALUE) {
      throw new IllegalParameterException(
          String.format(
              "Invalid modifier value! Modifier must not be greater than %f!",
              TerrainType.MAX_VALUE));
    }
    type.setModifier(modifier);
  }

  @Override
  public void setDiagonalPathing(boolean enabled) {
    this.diagonalPathing = enabled;
  }

  @Override
  public String toString() {
    return "Pathfinder";
  }
}
