package de.hhn.it.pp.components.astarpathfinding.provider;

import de.hhn.it.pp.components.astarpathfinding.IHeapItem;
import de.hhn.it.pp.components.astarpathfinding.Position;
import de.hhn.it.pp.components.astarpathfinding.TerrainType;
import java.util.Objects;

public class Terrain implements IHeapItem<Terrain> {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Terrain.class);

  /**
   * The movement cost to move from the starting point to a given square on the grid, following the
   * path generated to get there. (Distance from starting cell)
   */
  private int gCost;

  /**
   * The estimated movement cost to move from that given square on the grid to the final
   * destination. (Distance from the end cell)
   */
  private int hCost;

  /** Position on the map. */
  private Position position;

  /** The terrain type. */
  private TerrainType type;

  /** The neighbour with the lowest f cost. */
  private Terrain parent;

  private int heapIndex;

  private Terrain() {}

  /**
   * Terrain constructor. Needs a position and a type.
   *
   * @param position the position of the new terrain
   * @param type the terrain type
   */
  public Terrain(Position position, TerrainType type) {
    super();
    this.position = position;
    this.type = type;
  }

  /**
   * Copy constructor for terrains. This constructor takes an terrain object and creates a copy of
   * this object.
   *
   * @param terrainToBeCopied the terrain which should be copied
   */
  public Terrain(Terrain terrainToBeCopied) {
    this(terrainToBeCopied.getPosition(), terrainToBeCopied.getType());
    this.gCost = terrainToBeCopied.getGCost();
    this.hCost = terrainToBeCopied.getHCost();
    if (terrainToBeCopied.getParent() != null) {
      Terrain parent = terrainToBeCopied.getParent();
      Terrain newParent = new Terrain(parent.getPosition(), parent.getType());
      newParent.setGCost(parent.getGCost());
      newParent.setHCost(parent.getHCost());
      newParent.setHeapIndex(parent.getHeapIndex());
      this.parent = newParent;
    }
    this.heapIndex = terrainToBeCopied.getHeapIndex();
  }

  /**
   * Calculates the sum of g and h.
   *
   * @return the sum of g and h.
   */
  public int calculateFCost() {
    return gCost + hCost;
  }

  public int getGCost() {
    return gCost;
  }

  public void setGCost(int g) {
    this.gCost = g;
  }

  public int getHCost() {
    return gCost;
  }

  public void setHCost(int h) {
    this.hCost = h;
  }

  public Terrain getParent() {
    return parent;
  }

  public void setParent(Terrain parent) {
    this.parent = parent;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public TerrainType getType() {
    return type;
  }

  public void setType(TerrainType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return type + " " + position;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Terrain ter = (Terrain) obj;
    return position.equals(ter.position)
        && hCost == ter.hCost
        && gCost == ter.gCost
        && type == ter.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(gCost, hCost, position, type);
  }

  @Override
  public int getHeapIndex() {
    return heapIndex;
  }

  @Override
  public void setHeapIndex(int heapIndex) {
    this.heapIndex = heapIndex;
  }

  @Override
  public int compareTo(Terrain terrainToCompareTo) {
    // If the objects are the same then return 0 and don't do anything else
    if (this == terrainToCompareTo) {
      return 0;
    }

    // Check if the objects can be compared
    if (terrainToCompareTo.getClass() != getClass()) {
      throw new ClassCastException();
    }

    int comparison = calculateFCost() - terrainToCompareTo.calculateFCost();
    // If the f costs are equal then compare the h costs
    if (comparison == 0) {
      comparison = hCost - terrainToCompareTo.hCost;
    }
    // Set the comparison value to the common values -1, 0 or 1
    comparison = Integer.compare(comparison, 0);

    // In this case we want to return 1 if the f cost is lower so reverse the comparison
    return -comparison;
  }
}
