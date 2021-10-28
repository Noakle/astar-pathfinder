package de.hhn.it.pp.components.astarpathfinding;

import de.hhn.it.pp.components.exceptions.IllegalParameterException;

public class Heap<T extends IHeapItem<? super T>> {
  private T[] items;
  private int currentItemCount;

  /**
   * Class constructor. Declares a new array of items with the given max heap size.
   *
   * @throws IllegalParameterException thrown when the max heap size is lower then 1
   */
  @SuppressWarnings(value = "unchecked")
  public Heap(int maxHeapSize) throws IllegalParameterException {
    if (maxHeapSize <= 0) {
      throw new IllegalParameterException("Heap size cannot be lower than 1!");
    }
    items = (T[]) new IHeapItem[maxHeapSize];
  }

  /**
   * Copy constructor for heap objects. This constructor takes an heap object and creates a copy of
   * this object.
   *
   * @param heapToBeCopied heap to be copied
   * @throws IllegalParameterException thrown when the max heap size is lower then 1
   */
  public Heap(Heap<T> heapToBeCopied) throws IllegalParameterException {
    this(heapToBeCopied.items.length);
    this.items = heapToBeCopied.items.clone();
    this.currentItemCount = heapToBeCopied.getItemCount();
  }

  /**
   * Add an item to the last index of the heap and sort the heap.
   *
   * @param item that should be added to the heap
   */
  public void add(T item) {
    item.setHeapIndex(currentItemCount);
    items[currentItemCount] = item;
    sortItemUp(item);
    currentItemCount++;
  }

  /**
   * Removes and returns the first item of the heap, which is the item with the lowest priority or
   * in this case the item with the lowest F cost. After removing the item the heap will be sorted.
   *
   * @return the item in the first position of the heap
   */
  public T removeFirst() {
    final T firstItem = items[0];
    currentItemCount--;
    // Place last item in heap in first position and sort
    items[0] = items[currentItemCount];
    items[0].setHeapIndex(0);
    sortItemDown(items[0]);
    return firstItem;
  }

  /**
   * Check if the heap contains a specific item.
   *
   * @param item which should be checked if it is in the heap
   * @return true if the item is in the heap false if not
   */
  public boolean contains(T item) {
    T itemInHeap = items[item.getHeapIndex()];
    if (itemInHeap != null) {
      return itemInHeap.equals(item);
    } else {
      return false;
    }
  }

  /**
   * Update an item's position in the heap.
   *
   * @param item which needs to be updated
   */
  public void updateItem(T item) {
    sortItemUp(item);
    // We don't need to call sortItemDown because we never what the decrease the priority of a item
  }

  public int getItemCount() {
    return currentItemCount;
  }

  private void sortItemUp(T item) {
    int parentIdx = (item.getHeapIndex() - 1) / 2;
    while (true) {
      T parentItem = items[parentIdx];
      if (item.compareTo(parentItem) > 0) {
        swapItems(item, parentItem);
      } else {
        break;
      }
      parentIdx = (item.getHeapIndex() - 1) / 2;
    }
  }

  private void sortItemDown(T item) {
    while (true) {
      int childIdxLeft = item.getHeapIndex() * 2 + 1;
      int childIdxRight = item.getHeapIndex() * 2 + 2;
      int swapIdx;

      if (childIdxLeft < currentItemCount) {
        swapIdx = childIdxLeft;

        if (childIdxRight < currentItemCount) {
          // If the right child has a lower priority (F Cost) then the left child then set the right
          // child as the swap index
          if (items[childIdxLeft].compareTo(items[childIdxRight]) < 0) {
            swapIdx = childIdxRight;
          }
        }

        if (item.compareTo(items[swapIdx]) < 0) {
          // The children has a lower priority then its parent so swap them
          swapItems(item, items[swapIdx]);
        } else {
          // If the parent has a lower priority then its children we don't need to swap
          return;
        }
      } else {
        // Parent doesn't have any children
        return;
      }
    }
  }

  private void swapItems(T item1, T item2) {
    // Swap items in item array
    items[item1.getHeapIndex()] = item2;
    items[item2.getHeapIndex()] = item1;
    // Swap index field in the item object
    int item1Index = item1.getHeapIndex();
    item1.setHeapIndex(item2.getHeapIndex());
    item2.setHeapIndex(item1Index);
  }

  public T[] getItems() {
    return items;
  }
}
