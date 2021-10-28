package de.hhn.it.pp.components.astarpathfinding.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.hhn.it.pp.components.astarpathfinding.PathfindingInformation;
import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import de.hhn.it.pp.components.astarpathfinding.exceptions.OccupiedPositionException;
import de.hhn.it.pp.components.astarpathfinding.exceptions.PositionOutOfBounds;
import de.hhn.it.pp.components.astarpathfinding.provider.AStarPathfindingAlgorithm;
import de.hhn.it.pp.components.astarpathfinding.provider.MapManager;
import de.hhn.it.pp.components.astarpathfinding.provider.Pathfinder;
import de.hhn.it.pp.components.astarpathfinding.provider.Terrain;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AStarPathfindingAlgorithmTest {
  private static final org.slf4j.Logger logger =
    org.slf4j.LoggerFactory.getLogger(AStarPathfindingAlgorithmTest.class);

  private MapManager testMapManager;
  private AStarPathfindingAlgorithm algorithm;
  private AStarPathfindingAlgorithm algorithmDiagonal;

  @BeforeEach
  void setup() {
    testMapManager = new MapManager();
    algorithm = new AStarPathfindingAlgorithm(testMapManager, false);
    algorithmDiagonal = new AStarPathfindingAlgorithm(testMapManager, true);
  }

  @Nested
  @DisplayName("Good cases for doPathfinding")
  class AStarPathfindingAlgorithmGoodCases {

    @Test
    @DisplayName("Find the final and fastest path")
    public void doPathfinding_goodResult()
      throws PositionOutOfBounds, OccupiedPositionException, IllegalParameterException {
      Position start = new Position(2, 2);
      testMapManager.setStartCoordinates(start);
      Position end = new Position(2, 6);
      testMapManager.setDestinationCoordinates(end);
      testMapManager.createMap(7, 7);
      testMapManager.createTerrain(TerrainType.SWAMP, new Position(1, 4));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(2, 4));
      testMapManager.createTerrain(TerrainType.SWAMP, new Position(3, 4));
      ArrayList<PathfindingInformation> result = algorithm.findPath();
      List<Terrain> finalPath = result.get(result.size() - 1).getFinalPathPositions();
      printMapWithFinalPath(finalPath);

      List<Terrain> expectedPath = new ArrayList<>();

      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 0)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 1)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 2)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 3)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 3)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 4)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 5)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 5)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 6)));

      assertFalse(finalPath.isEmpty());
      assertEquals(expectedPath, finalPath, "The 1. expected path is not the same as the actual path");

      testMapManager.createTerrain(TerrainType.WATER, new Position(0, 3));
      testMapManager.createTerrain(TerrainType.WATER, new Position(1, 3));
      testMapManager.createTerrain(TerrainType.WATER, new Position(2, 3));
      testMapManager.createTerrain(TerrainType.WATER, new Position(3, 3));
      testMapManager.createTerrain(TerrainType.WATER, new Position(4, 3));

      result = algorithm.findPath();
      finalPath = result.get(result.size() - 1).getFinalPathPositions();
      printMapWithFinalPath(finalPath);

      expectedPath.clear();
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 0)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 1)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 2)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 2)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(5, 2)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(5, 3)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(5, 4)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 4)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 5)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 5)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 6)));
      assertEquals(expectedPath, finalPath, "The 2. expected path is not the same as the actual path");
    }

    @Test
    @DisplayName("Influence pathfinding with changed modifiers")
    public void doPathfinding_changedTerrainTypeModifier_GoodResult()
      throws OccupiedPositionException, PositionOutOfBounds, IllegalParameterException {
      testMapManager.createMap(7, 7);
      testMapManager.createTerrain(TerrainType.SWAMP, new Position(1, 4));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(2, 4));
      testMapManager.createTerrain(TerrainType.SWAMP, new Position(3, 4));

      ArrayList<PathfindingInformation> result = algorithm.findPath();
      List<Terrain> finalPath = result.get(result.size() - 1).getFinalPathPositions();

      List<Terrain> expectedPath = new ArrayList<>();
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 0)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 1)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 2)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 3)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 3)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 4)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 5)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 5)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 6)));

      printMapWithFinalPath(finalPath);
      assertFalse(finalPath.isEmpty());
      assertEquals(expectedPath, finalPath, "The 1. expected path is not the same as the actual path");

      TerrainType.SWAMP.setModifier(20.0);
      result = algorithm.findPath();
      finalPath = result.get(result.size() - 1).getFinalPathPositions();
      expectedPath.clear();
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 0)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 1)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 2)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 3)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 4)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 5)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 6)));
      printMapWithFinalPath(finalPath);

      assertFalse(finalPath.isEmpty());
      assertEquals(expectedPath, finalPath, "The 2. expected path is not the same as the actual path");
    }

    @Test
    @DisplayName("Check pathfinding with diagonal pathing enabled")
    public void doPathfinding_diagonalPathingEnabled_GoodResult() throws PositionOutOfBounds, OccupiedPositionException, IllegalParameterException {
      testMapManager.createMap(5, 7);
      testMapManager.setStartCoordinates(new Position(0, 0));
      testMapManager.setDestinationCoordinates(new Position(6, 4));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(0,1));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(1,1));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(2,1));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(3,0));

      ArrayList<PathfindingInformation> result = algorithmDiagonal.findPath();
      List<Terrain> finalPath = result.get(result.size() - 1).getFinalPathPositions();
      printMapWithFinalPath(finalPath);

      List<Terrain> expectedPath = new ArrayList<>();
      expectedPath.add(testMapManager.getTerrainAt(new Position(0, 0)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(1, 0)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(2, 0)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(3, 1)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(4, 2)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(5, 3)));
      expectedPath.add(testMapManager.getTerrainAt(new Position(6, 4)));

      assertFalse(finalPath.isEmpty());
      assertEquals(expectedPath, finalPath, "The expected path is not the same as the actual path");
    }
  }

  @Nested
  @DisplayName("Bad cases for doPathfinding")
  class AStarPathfindingAlgorithmBadCases {
    @Test
    @DisplayName("Try doPathfinding with no result")
    public void doPathfinding_startAndEndPosition_surroundedWithObstacle()
      throws OccupiedPositionException, PositionOutOfBounds, IllegalParameterException {
      // Set start point
      Position start = new Position(2, 2);
      testMapManager.setStartCoordinates(start);

      // Start point surrounded with lava
      testMapManager.createTerrain(TerrainType.LAVA, new Position(1, 2));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(2, 1));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(2, 3));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(3, 2));

      ArrayList<PathfindingInformation> result = algorithm.findPath();
      assertTrue(result.isEmpty(), "There should not be a path from the start position");

      // Reset map
      testMapManager.reset();

      // Set end point
      Position end = new Position(2, 6);
      testMapManager.setDestinationCoordinates(end);

      // End point surrounded with lava
      testMapManager.createTerrain(TerrainType.LAVA, new Position(1, 6));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(3, 6));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(2, 7));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(2, 5));

      result = algorithm.findPath();
      assertTrue(result.isEmpty(), "There should not be a path to the end position");
    }

    @Test
    @DisplayName("Check pathfinding with diagonal pathing enabled and start sourrounded")
    public void doPathfinding_diagonalPathingEnabled_StartPointSourrounded_BadResult()
      throws PositionOutOfBounds, OccupiedPositionException, IllegalParameterException {
      testMapManager.createMap(5, 7);
      testMapManager.setStartCoordinates(new Position(0, 0));
      testMapManager.setDestinationCoordinates(new Position(6, 4));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(0,1));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(1,0));
      testMapManager.createTerrain(TerrainType.LAVA, new Position(1,1));

      ArrayList<PathfindingInformation> result = algorithmDiagonal.findPath();
      assertTrue(result.isEmpty(), "There should not be a path to the end position");
    }

  }

  private void printMapWithFinalPath(List<Terrain> finalPath) {
    String red = "\033[38;2;255;178;4m";
    for (Terrain[] row : testMapManager.getMap()) {
      for (Terrain col : row) {
        boolean found = false;
        for (Terrain pathTer : finalPath) {
          if (col.equals(pathTer)) {
            System.out.print(String.format("\u001b[38;2;125;125;4m%s\u001b[0m", col + " | "));
            found = true;
            break;
          }
        }
        if (!found) System.out.print(col + " | ");
      }
      System.out.println();
    }
    System.out.println();
  }
}
