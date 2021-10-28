package de.hhn.it.pp.components.astarpathfinding;

import de.hhn.it.pp.components.astarpathfinding.provider.Terrain;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an object which holds information about pathfinding algorithms.
 */
public class PathfindingInformation implements Cloneable {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(PathfindingInformation.class);

  /**
   * List of positions specific to algorithms.
   */
  private Heap<Terrain> specificPositions;

  /**
   * List of positions a pathfinding algorithm did visit.
   */
  private List<Terrain> visitedPositions = new ArrayList<>();

  /**
   * List of positions of the final and shortest path.
   */
  private List<Terrain> finalPathPositions = new ArrayList<>();

  public PathfindingInformation(int maxHeapSize) throws IllegalParameterException {
    this.specificPositions = new Heap<Terrain>(maxHeapSize);
  }

  public Heap<Terrain> getSpecificPositions() {
    return specificPositions;
  }

  public void setSpecificPositions(Heap<Terrain> specificPositions) {
    this.specificPositions = specificPositions;
  }

  public List<Terrain> getVisitedPositions() {
    return visitedPositions;
  }

  public void setVisitedPositions(List<Terrain> visitedPositions) {
    this.visitedPositions = visitedPositions;
  }

  public List<Terrain> getFinalPathPositions() {
    return finalPathPositions;
  }

  public void setFinalPathPositions(List<Terrain> finalPathPositions) {
    this.finalPathPositions = finalPathPositions;
  }

  public void addVisitedPosition(Terrain visitedPosition) {
    this.visitedPositions.add(visitedPosition);
  }

  public void addSpecificPosition(Terrain specificPosition) {
    this.specificPositions.add(specificPosition);
  }

  /**
   * Resets the heap and lists for this information object.
   */
  public void reset() {
    logger.debug("reset: no params");
    try {
      int maxSize = specificPositions.getItems().length;
      specificPositions = new Heap<Terrain>(maxSize);
    } catch (IllegalParameterException e) {
      // This case should never happen due to map size minimum
      e.printStackTrace();
    }
    visitedPositions.clear();
    finalPathPositions.clear();
  }

  @Override
  public PathfindingInformation clone() throws CloneNotSupportedException {
    PathfindingInformation cloned = (PathfindingInformation) super.clone();
    try {
      cloned.setSpecificPositions(new Heap<>(this.specificPositions));
    } catch (IllegalParameterException e) {
      // Should never happen
      e.printStackTrace();
    }
    cloned.setVisitedPositions(this.cloneList(this.visitedPositions));
    cloned.setFinalPathPositions(this.cloneList(this.finalPathPositions));
    return cloned;
  }

  private List<Terrain> cloneList(List<Terrain> list) {
    List<Terrain> newList = new ArrayList<>();
    for (Terrain t : list) {
      newList.add(new Terrain(t));
    }
    return newList;
  }
}
