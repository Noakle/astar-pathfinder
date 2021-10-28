package de.hhn.it.pp.components.astarpathfinding;

import de.hhn.it.pp.components.astarpathfinding.exceptions.OccupiedPositionException;
import de.hhn.it.pp.components.astarpathfinding.exceptions.PositionOutOfBounds;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;
import java.util.List;

/**
 * This Service is an interface for a pathfinding simulator that provides all the functionality to
 * interact with the simulator.
 */
public interface PathfindingService {

  /**
   * Creates the map with the given length and height.
   *
   * @param width the width of the map, must higher then 0
   * @param height the height of the map, must higher then 0
   * @throws IllegalParameterException if either the width or the height is invalid
   * @throws PositionOutOfBounds if the start or destination position would be out of bounds after
   *     creating the new map
   */
  void createMap(int width, int height) throws PositionOutOfBounds;

  /**
   * Sets the start point for the pathfinding algorithm.
   *
   * @param position position on the grid
   * @throws PositionOutOfBounds if the position is out of bounds
   * @throws OccupiedPositionException thrown when start point is equal to the end point
   */
  void setStartPoint(Position position) throws PositionOutOfBounds, OccupiedPositionException;

  /**
   * Sets the end point for the pathfinding algorithm.
   *
   * @param position position on the grid
   * @throws PositionOutOfBounds if the position is out of bounds
   * @throws OccupiedPositionException thrown when end point is equal to the start point
   */
  void setEndPoint(Position position) throws PositionOutOfBounds, OccupiedPositionException;

  /**
   * Places a terrain of the given type on the given position.
   *
   * @param type the type defining the terrain
   * @param position the position on the map
   * @throws PositionOutOfBounds if the position is out of bounds
   */
  void placeTerrain(TerrainType type, Position position) throws PositionOutOfBounds;

  /**
   * Starts the visualization of the pathfinding algorithm.
   *
   * @return the list of all pathfinding algorithm calculations
   * @throws IllegalParameterException thrown if the mapsize or the total number of terrain on the
   *     map is less then 1. This should never be the case due to the minimum restriction in
   *     the @{@link de.hhn.it.pp.components.astarpathfinding.provider.MapManager} class.
   */
  List<PathfindingInformation> doPathfinding() throws IllegalParameterException;

  /**
   * Stops the visualization if it is running and resets the start and end point and removes the
   * obstacles on the grid.
   */
  void reset();

  /**
   * Changes the terrain modifier for a specific terrain type.
   *
   * @param type the terrain type which will be changed
   * @param modifier the new value for the terrain type modifier. The value must be between 0 and 1
   * @throws IllegalParameterException thrown if the value is not between 0 and 1
   */
  void changeTerrainTypeModifier(TerrainType type, double modifier)
      throws IllegalParameterException;

  /**
   * Enables or disables the diagonal pathing option for the algorithm.
   *
   * @param enabled whether the option is enabled
   */
  void setDiagonalPathing(boolean enabled);
}
