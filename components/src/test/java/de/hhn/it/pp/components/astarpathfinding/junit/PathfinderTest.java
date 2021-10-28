package de.hhn.it.pp.components.astarpathfinding.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.hhn.it.pp.components.astarpathfinding.PathfindingInformation;
import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import de.hhn.it.pp.components.astarpathfinding.exceptions.OccupiedPositionException;
import de.hhn.it.pp.components.astarpathfinding.exceptions.PositionOutOfBounds;
import de.hhn.it.pp.components.astarpathfinding.provider.MapManager;
import de.hhn.it.pp.components.astarpathfinding.provider.Pathfinder;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PathfinderTest {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(PathfinderTest.class);

  Pathfinder pathfinder;

  @BeforeEach
  void setup() {
    pathfinder = new Pathfinder();
  }

  @Nested
  @DisplayName("Good test cases")
  class PathfinderTest_goodTestCases {
    @Test
    @DisplayName("Create map without exception")
    public void createMap_successful() throws PositionOutOfBounds {
      pathfinder.createMap(15, 15);
    }

    @Test
    @DisplayName("Set the start point without exception")
    public void setStartPoint_successful() throws PositionOutOfBounds, OccupiedPositionException {
      pathfinder.setStartPoint(new Position(8, 3));
    }

    @Test
    @DisplayName("Set the end point without exception")
    public void setEndPoint_successful() throws PositionOutOfBounds, OccupiedPositionException {
      pathfinder.setEndPoint(new Position(8, 3));
    }

    @Test
    @DisplayName("Place a terrain without exception")
    public void placeTerrain_successful() throws PositionOutOfBounds {
      pathfinder.placeTerrain(TerrainType.WATER, new Position(8, 3));
    }

    @Test
    @DisplayName("Place a terrain without exception")
    public void doPathfinding_successful() throws IllegalParameterException {
      List<PathfindingInformation> result = pathfinder.doPathfinding();
      assertNotNull(result);
      assertTrue(result.size() >= 1);
    }

    @Test
    @DisplayName("Place a terrain without exception")
    public void reset_successful() throws IllegalParameterException {
      // Change the cost modifier of the terrain type water
      pathfinder.changeTerrainTypeModifier(TerrainType.WATER, 75);
      assertEquals(75, TerrainType.WATER.getModifier());
      pathfinder.reset();
      assertEquals(TerrainType.WATER.getDefaultModifier(), TerrainType.WATER.getModifier());
    }
  }

  @Nested
  @DisplayName("Bad test cases")
  class PathfinderTest_badTestCases {
    @Test
    @DisplayName("Create map with exception")
    public void createMap_unsuccessful() {
      assertThrows(
          PositionOutOfBounds.class,
          () -> pathfinder.createMap(15, MapManager.MAX_HEIGHT + 1),
          "Expected exception was not thrown");
    }

    @Test
    @DisplayName("Set the start point without exception")
    public void setStartPoint_unsuccessful() {
      assertThrows(
          PositionOutOfBounds.class,
          () -> pathfinder.setStartPoint(new Position(MapManager.MAX_WIDTH + 1, 3)),
          "Expected PositionOutOfBounds exception was not thrown");
      assertThrows(
          OccupiedPositionException.class,
          () ->
              pathfinder.setStartPoint(
                  new Position(
                      (int) Math.ceil(MapManager.DEFAULT_HEIGHT / 2f) - 1,
                      MapManager.DEFAULT_WIDTH - 1)),
          "Expected OccupiedPositionException exception was not thrown");
    }

    @Test
    @DisplayName("Set the end point without exception")
    public void seEndPoint_unsuccessful() {
      assertThrows(
          PositionOutOfBounds.class,
          () -> pathfinder.setEndPoint(new Position(MapManager.MAX_WIDTH + 1, 3)),
          "Expected PositionOutOfBounds exception was not thrown");
      assertThrows(
          OccupiedPositionException.class,
          () ->
              pathfinder.setEndPoint(
                  new Position((int) Math.ceil(MapManager.DEFAULT_HEIGHT / 2f) - 1, 0)),
          "Expected OccupiedPositionException exception was not thrown");
    }

    @Test
    @DisplayName("Place terrain with exception")
    public void placeTerrain_unsuccessful() {
      assertThrows(
          PositionOutOfBounds.class,
          () ->
              pathfinder.placeTerrain(TerrainType.WATER, new Position(MapManager.MAX_WIDTH + 1, 3)),
          "Expected exception was not thrown");
    }
  }

  @Nested
  @DisplayName("Tests for changeTerrainTypeModifier")
  class PathfinderTest_changeTerrainTypeModifier {

    @Nested
    @DisplayName("Good Cases for changeTerrainTypeModifier")
    class PathfinderTest_changeTerrainTypeModifier_goodCases {
      @Test
      @DisplayName("Successful change TerrainType value")
      public void changeTerrainTypeModifier_successful() throws IllegalParameterException {
        pathfinder.changeTerrainTypeModifier(TerrainType.WATER, 0.7);
        assertEquals(0.7, TerrainType.WATER.getModifier());
      }
    }

    @Nested
    @DisplayName("Bad Cases for changeTerrainTypeModifier")
    class PathfinderTest_changeTerrainTypeModifier_badCases {
      @Test
      @DisplayName("Change TerrainType value below the minimum")
      public void changeTerrainTypeModifier_belowMinValue() throws IllegalParameterException {
        IllegalParameterException e =
            assertThrows(
                IllegalParameterException.class,
                () -> pathfinder.changeTerrainTypeModifier(TerrainType.WATER, -1));
        assertEquals(
            String.format(
                "Invalid modifier value! Modifier must not be lower than %f!",
                TerrainType.MIN_VALUE),
            e.getMessage());
      }

      @Test
      @DisplayName("Change TerrainType value above the maximum")
      public void changeTerrainTypeModifier_aboveMaxValue() throws IllegalParameterException {
        IllegalParameterException e =
            assertThrows(
                IllegalParameterException.class,
                () -> pathfinder.changeTerrainTypeModifier(TerrainType.WATER, 150));
        assertEquals(
            String.format(
                "Invalid modifier value! Modifier must not be greater than %f!",
                TerrainType.MAX_VALUE),
            e.getMessage());
      }
    }
  }
}
