package de.hhn.it.pp.components.astarpathfinding.junit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.hhn.it.pp.components.astarpathfinding.Heap;
import de.hhn.it.pp.components.astarpathfinding.PathfindingInformation;
import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import de.hhn.it.pp.components.astarpathfinding.provider.Terrain;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathfindingInformationTest {
  private static final org.slf4j.Logger logger =
    org.slf4j.LoggerFactory.getLogger(PathfindingInformationTest.class);

  @Test
  @DisplayName("Creates a deep copy of the PathfindingInformation object.")
  public void clone_newClonedInformation() throws CloneNotSupportedException, IllegalParameterException {
    PathfindingInformation information = new PathfindingInformation(100);
    // Create list objects
    Heap<Terrain> specificPositions = new Heap<>(100);
    List<Terrain> visitedPositions = new ArrayList<>();
    List<Terrain> finalPathPositions = new ArrayList<>();

    // Fill specificPositions list
    Terrain position1 = new Terrain(new Position(1, 1), TerrainType.WATER);
    Terrain position2 = new Terrain(new Position(2, 1), TerrainType.WATER);
    Terrain position3 = new Terrain(new Position(3, 1), TerrainType.WATER);
    specificPositions.add(position1);
    specificPositions.add(position2);
    specificPositions.add(position3);

    // Fill visitedPositions list
    Terrain position4 = new Terrain(new Position(2, 5), TerrainType.GRASS);
    Terrain position5 = new Terrain(new Position(2, 6), TerrainType.GRASS);
    Terrain position6 = new Terrain(new Position(3, 6), TerrainType.DIRT);
    Terrain position7 = new Terrain(new Position(4, 6), TerrainType.DIRT);
    visitedPositions.add(position4);
    visitedPositions.add(position5);
    visitedPositions.add(position6);
    visitedPositions.add(position7);

    // Fill finalPathPositions list
    finalPathPositions.add(position5);
    finalPathPositions.add(position6);
    finalPathPositions.add(position7);

    // Add lists to the PathfindingInformation
    information.setSpecificPositions(specificPositions);
    information.setVisitedPositions(visitedPositions);
    information.setFinalPathPositions(finalPathPositions);

    PathfindingInformation clone = information.clone();

    assertAll(
      "Checking the cloned object's list size",
      () ->
        assertEquals(
          3,
          clone.getSpecificPositions().getItemCount(),
          "The cloned information should have 3 objects stored in its specificPositions list"),
      () ->
        assertEquals(
          4,
          clone.getVisitedPositions().size(),
          "The cloned information should have 3 objects stored in its visitedPositions list"),
      () ->
        assertEquals(
          3,
          clone.getFinalPathPositions().size(),
          "The cloned information should have 3 objects stored in its finalPathPositions list"));

    System.out.println();


    assertAll(
      "Checking specificPositions objects",
      () ->
        assertArrayEquals(clone.getSpecificPositions().getItems(), specificPositions.getItems()));
    assertAll(

      "Checking visitedPositions objects",
      () ->
        assertEquals(
          clone.getVisitedPositions().get(2).getPosition().getRow(),
          position6.getPosition().getRow(),
          "The row of the visited position at index 2 should be the same but was not"),
      () ->
        assertEquals(
          clone.getVisitedPositions().get(2).getPosition().getCol(),
          position6.getPosition().getCol(),
          "The col of the visited position at index 2 should be the same but was not"),
      () ->
        assertSame(
          clone.getVisitedPositions().get(2).getType(),
          position6.getType(),
          "The type of the visited position at index 2 should be the same but was not"));

    assertAll(
      "Checking finalPathPositions objects",
      () ->
        assertEquals(
          clone.getFinalPathPositions().get(2).getPosition().getRow(),
          position7.getPosition().getRow(),
          "The row of the final position at index 2 should be the same but was not"),
      () ->
        assertEquals(
          clone.getFinalPathPositions().get(2).getPosition().getCol(),
          position7.getPosition().getCol(),
          "The col of the final position at index 2 should be the same but was not"),
      () ->
        assertSame(
          clone.getFinalPathPositions().get(2).getType(),
          position7.getType(),
          "The type of the final position at index 2 should be the same but was not"));
  }
}
