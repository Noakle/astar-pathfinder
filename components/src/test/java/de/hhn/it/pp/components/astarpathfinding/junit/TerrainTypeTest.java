package de.hhn.it.pp.components.astarpathfinding.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TerrainTypeTest {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(TerrainTypeTest.class);

  @Test
  @DisplayName("Successfully reset TerrainType modifiers to the default values")
  public void resetModifiers_toDefaultValues() {
    TerrainType type1 = TerrainType.SWAMP;
    type1.setModifier(20);
    TerrainType type2 = TerrainType.DIRT;
    type2.setModifier(1);
    TerrainType.resetModifers();
    assertEquals(
        type1.getDefaultModifier(),
        type1.getModifier(),
        "The modifier should be equal to the default value");
    assertEquals(
        type2.getDefaultModifier(),
        type2.getModifier(),
        "The modifier should be equal to the default value");
  }
}
