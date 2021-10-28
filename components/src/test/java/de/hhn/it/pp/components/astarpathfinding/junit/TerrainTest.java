package de.hhn.it.pp.components.astarpathfinding.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import de.hhn.it.pp.components.astarpathfinding.exceptions.PositionOutOfBounds;
import de.hhn.it.pp.components.astarpathfinding.provider.MapManager;
import de.hhn.it.pp.components.astarpathfinding.provider.Terrain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TerrainTest {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(TerrainTest.class);

  private MapManager testMapManager;

  @BeforeEach
  void setup() {
    testMapManager = new MapManager();
  }

  @Test
  @DisplayName("Successfully create new terrain")
  public void constructor_newTerrain() throws PositionOutOfBounds {
    Terrain[][] map = testMapManager.getMap();
    Position location = new Position(5, 5);
    testMapManager.createTerrain(TerrainType.LAVA, location);
    assertEquals(
        TerrainType.LAVA, testMapManager.getTerrainAt(location).getType(), "Expected to find Lava");
  }

  @Test
  @DisplayName("Create new terrain expecting PositionOutOfBounds exception")
  public void constructor_newTerrain_Exception() throws PositionOutOfBounds {
    Terrain[][] map = testMapManager.getMap();
    Position location = new Position(-1, -1);
    assertThrows(
        PositionOutOfBounds.class,
        () -> testMapManager.createTerrain(TerrainType.LAVA, location),
        "Expected PositionOutOfBounds exception to be thrown");
  }

  @Test
  @DisplayName("Clone terrain and check for ")
  public void clone_successful() throws CloneNotSupportedException, PositionOutOfBounds {
    Position pos = new Position(5, 5);
    Terrain terrain = testMapManager.getTerrainAt(pos);
    Terrain clonedTerrain = new Terrain(terrain);

    assertNotSame(terrain, clonedTerrain, "The two terrains should not be the same object");
    assertEquals(terrain, clonedTerrain, "The two terrains should be equal");
  }
}
