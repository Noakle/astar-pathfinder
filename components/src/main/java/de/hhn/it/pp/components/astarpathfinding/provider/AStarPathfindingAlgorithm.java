package de.hhn.it.pp.components.astarpathfinding.provider;

import de.hhn.it.pp.components.astarpathfinding.PathfindingInformation;
import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AStarPathfindingAlgorithm {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(AStarPathfindingAlgorithm.class);

  private final ArrayList<PathfindingInformation> result = new ArrayList<>();
  private final MapManager mapManager;
  private final boolean diagonalPathing;

  /**
   * Constructor for this class.
   *
   * @param mapManager holds all the information from the map
   */
  public AStarPathfindingAlgorithm(MapManager mapManager, boolean diagonalPathing) {
    this.mapManager = mapManager;
    this.diagonalPathing = diagonalPathing;
  }

  /**
   * Determines the shortest path from the start position to the destination position on the given
   * map. The algorithm used for this search is an implementation of the A* algorithm. The
   * approximation heuristics are calculated using the Manhattan Distance.
   *
   * @return If there is a shortest path then all states the algorithm has been through else an
   *     empty list.
   */
  public ArrayList<PathfindingInformation> findPath() throws IllegalParameterException {
    logger.debug("findPath: no params");

    // Benchmark time
    final long start = System.nanoTime();

    Terrain[][] map = mapManager.getMap();
    PathfindingInformation information = new PathfindingInformation(map.length * map[0].length);
    Position startCoordinates = mapManager.getStartCoordinates();
    Position destinationCoordinates = mapManager.getDestinationCoordinates();

    // Add the start cell to the open list
    information
        .getSpecificPositions()
        .add(map[startCoordinates.getRow()][startCoordinates.getCol()]);

    while (information.getSpecificPositions().getItemCount() > 0) {
      Terrain currentTerrain = information.getSpecificPositions().removeFirst();
      information.getVisitedPositions().add(currentTerrain);

      // Check whether the algorithm reached the destination cell
      if (currentTerrain.equals(
          map[destinationCoordinates.getRow()][destinationCoordinates.getCol()])) {
        final long end = System.nanoTime();
        logger.debug("Pathfinding took " + (end - start) / 1000000 + " ms");
        logger.debug("findPath: shortest path found");
        information.setFinalPathPositions(
            tracePath(map[startCoordinates.getRow()][startCoordinates.getCol()], currentTerrain));
        result.add(information);
        return result;
      }

      // Add neighbours to possible path if they are accessible i.e. no obstacle.
      for (Terrain neighbour : getNeighbours(currentTerrain)) {
        if (neighbour != null) {
          if (neighbour.getType().getModifier() >= TerrainType.MAX_VALUE
              || information.getVisitedPositions().contains(neighbour)) {
            continue;
          }

          // Update costs of the neighbour if a shorter path was found
          int newCostToNeighbour =
              currentTerrain.getGCost()
                  + (int)
                      ((getMDistance(currentTerrain, neighbour))
                          * (1 + (currentTerrain.getType().getModifier())));
          if (newCostToNeighbour < neighbour.getGCost()
              || !information.getSpecificPositions().contains(neighbour)) {
            neighbour.setGCost(newCostToNeighbour);
            neighbour.setHCost(
                getMDistance(
                    neighbour,
                    map[destinationCoordinates.getRow()][destinationCoordinates.getCol()]));
            neighbour.setParent(currentTerrain);

            if (!information.getSpecificPositions().contains(neighbour)) {
              information.getSpecificPositions().add(neighbour);
            } else {
              // Update the neighbour
              information.getSpecificPositions().updateItem(neighbour);
            }
          }
        }
      }

      try {
        result.add(information.clone());
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    }

    logger.debug("findPath: unable to find path");
    return new ArrayList<>();
  }

  /**
   * Backtrack the shortest path from the destination cell to the first cell after the start cell.
   *
   * @param startTerrain start point in the grid
   * @param endTerrain destination in the grid
   * @return the final path.
   */
  private List<Terrain> tracePath(Terrain startTerrain, Terrain endTerrain) {
    List<Terrain> path = new ArrayList<>();
    Terrain currentTerrain = endTerrain;

    while (currentTerrain != startTerrain) {
      path.add(currentTerrain);
      currentTerrain = currentTerrain.getParent();
    }
    path.add(startTerrain);

    Collections.reverse(path);
    return path;
  }

  /**
   * Calculates the approximation heuristics (Manhattan Distance).
   *
   * @param terrainA from
   * @param terrainB to
   * @return approximation distance between two cells
   */
  private int getMDistance(Terrain terrainA, Terrain terrainB) {
    return Math.abs(terrainA.getPosition().getRow() - terrainB.getPosition().getRow())
        + Math.abs(terrainA.getPosition().getCol() - terrainB.getPosition().getCol());
  }

  /**
   * Looks for all available horizontal and vertical neighbours of the given cell in the grid and
   * returns them as an array of cells. If diagonal pathing is enabled, diagonal neighbours are
   * considered.
   *
   * @param terrain the cell from which the neighbours are to be determined
   * @return an array of cells which are neighbours of the given cell
   */
  private Terrain[] getNeighbours(Terrain terrain) {
    // We currently have a maximum of 4 neighbours, possibly even less.
    Terrain[] neighbours = new Terrain[4];
    Terrain[][] map = mapManager.getMap();

    // Left neighbour
    if (terrain.getPosition().getCol() - 1 >= 0) {
      neighbours[0] = map[terrain.getPosition().getRow()][terrain.getPosition().getCol() - 1];
    }

    // Right neighbour
    if (terrain.getPosition().getCol() + 1 < map[terrain.getPosition().getRow()].length) {
      neighbours[1] = map[terrain.getPosition().getRow()][terrain.getPosition().getCol() + 1];
    }

    // Top neighbour
    if (terrain.getPosition().getRow() - 1 >= 0) {
      neighbours[2] = map[terrain.getPosition().getRow() - 1][terrain.getPosition().getCol()];
    }

    // Bottom neighbour
    if (terrain.getPosition().getRow() + 1 < map.length) {
      neighbours[3] = map[terrain.getPosition().getRow() + 1][terrain.getPosition().getCol()];
    }

    if (diagonalPathing) {
      Terrain[] tmp = new Terrain[8];
      for (int i = 0; i < neighbours.length; i++) {
        if (neighbours[i] != null) {
          tmp[i] = neighbours[i];
        }
      }

      // Diagonal neighbours
      // Top left
      if (terrain.getPosition().getCol() - 1 >= 0 && terrain.getPosition().getRow() - 1 >= 0) {
        tmp[4] = map[terrain.getPosition().getRow() - 1][terrain.getPosition().getCol() - 1];
      }

      // Top right
      if (terrain.getPosition().getCol() + 1 < map[0].length
          && terrain.getPosition().getRow() - 1 >= 0) {
        tmp[5] = map[terrain.getPosition().getRow() - 1][terrain.getPosition().getCol() + 1];
      }

      // Bottom left
      if (terrain.getPosition().getCol() - 1 >= 0
          && terrain.getPosition().getRow() + 1 < map.length) {
        tmp[6] = map[terrain.getPosition().getRow() + 1][terrain.getPosition().getCol() - 1];
      }

      // Bottom right
      if (terrain.getPosition().getCol() + 1 < map[0].length
          && terrain.getPosition().getRow() + 1 < map.length) {
        tmp[7] = map[terrain.getPosition().getRow() + 1][terrain.getPosition().getCol() + 1];
      }
      neighbours = tmp;
    }
    return neighbours;
  }

  @Override
  public String toString() {
    return String.format("Diagonal pathing enabled: %b;", diagonalPathing);
  }
}
