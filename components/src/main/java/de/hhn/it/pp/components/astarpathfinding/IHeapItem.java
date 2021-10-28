package de.hhn.it.pp.components.astarpathfinding;

public interface IHeapItem<T> extends Comparable<T> {

  /**
   * Returns the position of the item in the heap.
   *
   * @return the items position in the heap
   */
  int getHeapIndex();

  /**
   * Set the item's position in the heap.
   *
   * @param heapIndex the new position
   */
  void setHeapIndex(int heapIndex);
}
