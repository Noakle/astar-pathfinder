package de.hhn.it.pp.components.astarpathfinding;

/**
 * A class to represent a position with two values.
 */
public class Position {
  private int row;
  private int col;

  /**
   * A representation of a coordinate.
   *
   * @param row the x coordinate
   * @param col the y coordinate
   */
  public Position(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int col) {
    this.col = col;
  }

  @Override
  public String toString() {
    return "row=" + row + ", col=" + col;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Position pos = (Position) obj;
    return getRow() == pos.getRow() && getCol() == pos.getCol();
  }
}
