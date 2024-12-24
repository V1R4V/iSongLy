import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it
 * stores in sorted, ascending order.
 */
public class IterableRedBlackTree<T extends Comparable<T>>
    extends RedBlackTree<T> implements IterableSortedCollection<T> {

  // Fields for storing the minimum and maximum iterator bounds
  protected Comparable<T> iteratorMin = null;
  protected Comparable<T> iteratorMax = null;

  /**
   * Allows setting the start (minimum) value of the iterator. When this method is called, every
   * iterator created after it will use the minimum set by this method until this method is called
   * again to set a new minimum value.
   *
   * @param min the minimum for iterators created for this tree, or null for no minimum
   */
  public void setIteratorMin(Comparable<T> min) {
    this.iteratorMin = min;
  }

  /**
   * Allows setting the stop (maximum) value of the iterator. When this method is called, every
   * iterator created after it will use the maximum set by this method until this method is called
   * again to set a new maximum value.
   *
   * @param max the maximum for iterators created for this tree, or null for no maximum
   */
  public void setIteratorMax(Comparable<T> max) {
    this.iteratorMax = max;
  }

  /**
   * Returns an iterator over the values stored in this tree. The iterator uses the start (minimum)
   * value set by a previous call to setIteratorMin, and the stop (maximum) value set by a previous
   * call to setIteratorMax. If setIteratorMin has not been called before, or if it was called with
   * a null argument, the iterator uses no minimum value and starts with the lowest value that
   * exists in the tree. If setIteratorMax has not been called before, or if it was called with a
   * null argument, the iterator uses no maximum value and finishes with the highest value that
   * exists in the tree.
   */
  public Iterator<T> iterator() {
    return new RBTIterator<>(root, iteratorMin, iteratorMax);  // Assuming root is the root node of RedBlackTree
  }

  /**
   * Nested class for Iterator objects created for this tree and returned by the iterator method.
   * This iterator follows an in-order traversal of the tree and returns the values in sorted,
   * ascending order.
   */
  protected static class RBTIterator<R> implements Iterator<R> {

    // stores the start point (minimum) for the iterator
    Comparable<R> min = null;
    // stores the stop point (maximum) for the iterator
    Comparable<R> max = null;
    // stores the stack that keeps track of the inorder traversal
    Stack<BSTNode<R>> stack = null;

    /**
     * Constructor for a new iterator if the tree with root as its root node, and min as the start
     * (minimum) value (or null if no start value) and max as the stop (maximum) value (or null if
     * no stop value) of the new iterator.
     *
     * @param root root node of the tree to traverse
     * @param min  the minimum value that the iterator will return
     * @param max  the maximum value that the iterator will return
     */
    public RBTIterator(BSTNode<R> root, Comparable<R> min, Comparable<R> max) {
      this.min = min;
      this.max = max;
      this.stack = new Stack<>();
      buildStackHelper(root);   // Initialize stack by pushing leftmost nodes
    }

    /**
     * Helper method for initializing and updating the stack. This method both - finds the next data
     * value stored in the tree (or subtree) that is bigger than or equal to the specified start
     * point (maximum), and - builds up the stack of ancestor nodes that contain values larger than
     * or equal to the start point so that those nodes can be visited in the future.
     *
     * @param node the root node of the subtree to process
     */
    private void buildStackHelper(BSTNode<R> node) {
      // Base case:
      if (node == null) {
        return;
      }


      // Traverse the tree, pushing nodes onto the stack if they are greater than or equal to min
      if (min != null && min.compareTo(node.data) > 0) {
        buildStackHelper(node.right);
      } else {
        // if ((min == null || min.compareTo(node.data) <= 0) && (min == null || min.compareTo(node.data) <= 0)) {
        stack.push(node);  // Push current node onto the stack
        buildStackHelper(node.left);
        //  }
      }
    }

    /**
     * Returns true if the iterator has another value to return, and false otherwise.
     */
    public boolean hasNext() {
      if (stack.isEmpty()) {
        return false;
      }

      BSTNode<R> nodeBST = stack.peek();  // Get the top element of the stack

      if (max != null) {
        if (max.compareTo(nodeBST.data) < 0) {
          return false;
        }
      }
      // Check if the next element is within the maximum bound
      if (min != null) {
        if (min.compareTo(nodeBST.data) > 0) {
          return false;
        }
      }
      return true;
    }

    /**
     * Returns the next value of the iterator.
     *
     * @throws NoSuchElementException if the iterator has no more values to return
     */
    public R next() {
      if (!hasNext()) {
        throw new NoSuchElementException("No more elements available");
      }

      BSTNode<R> nodeBST = stack.pop(); // Get the next node to return

      if (nodeBST.right != null) {
        buildStackHelper(nodeBST.right); // Push right subtree if it exists
      }

      return nodeBST.data;
    }
  }


  private IterableRedBlackTree<Integer> treeWithInt;
  private IterableRedBlackTree<String> treeWithStr;

  @BeforeEach
  public void initialize() {
    // Initialize the trees for testing
    treeWithInt = new IterableRedBlackTree<>();
    treeWithStr = new IterableRedBlackTree<>();

    // Adding integer values to the integer tree
    treeWithInt.insert(1);
    treeWithInt.insert(2);
    treeWithInt.insert(3);
    treeWithInt.insert(4);
    treeWithInt.insert(5);

    // Adding string values to the string tree
    treeWithStr.insert("a");
    treeWithStr.insert("b");
    treeWithStr.insert("c");
    treeWithStr.insert("d");
    treeWithStr.insert("e");
  }

  /**
   * Test iterating over the string tree without bounds (no min, no max).
   */
  @Test
  public void testString() {
    // Create the iterator for the string tree with no specified bounds
    Iterator<String> iterator = treeWithStr.iterator();

    // Expected output: all strings in sorted order from the tree
    String[] expected = {"a", "b", "c", "d", "e"};

    // Iterate through the elements of the tree and compare with the expected output
    for (String expectedValue : expected) {
      // Check if there are more elements to iterate
      assertTrue(iterator.hasNext());
      // Validate that the next value matches the expected value
      assertEquals(expectedValue, iterator.next());
    }

    // Ensure that the iterator has no more elements after iterating through all expected values
    assertFalse(iterator.hasNext());
  }

  /**
   * Test iterating over an integer tree with specified start and stop points.
   */
  @Test
  public void testStartStop() {
    // Insert an additional value into the integer tree
    treeWithInt.insert(9);

    // Set the minimum and maximum bounds for the iterator
    treeWithInt.setIteratorMin(2);
    treeWithInt.setIteratorMax(9);

    // Create the iterator with the specified bounds
    Iterator<Integer> iterator = treeWithInt.iterator();

    // Expected output: integers within the specified bounds in sorted order
    Integer[] expected = {2, 3, 4, 5, 9};

    // Iterate through the elements and compare with the expected output
    for (Integer expectedValue : expected) {
      // Check if there are more elements to iterate
      assertTrue(iterator.hasNext());
      // Validate that the next value matches the expected value
      Integer actualValue = iterator.next();
      assertEquals(expectedValue, actualValue);
    }

    // Ensure that the iterator has no more elements after iterating through all expected values
    assertFalse(iterator.hasNext());
  }

  /**
   * Test iterating over an integer tree with no specified stop point.
   */
  @Test
  public void testStartOnly() {
    // Set only the minimum bound for the iterator
    treeWithInt.setIteratorMin(2);

    // Create the iterator with the specified minimum bound
    Iterator<Integer> iterator = treeWithInt.iterator();

    // Expected output: integers from the minimum bound onward in sorted order
    Integer[] expected = {2, 3, 4, 5};

    // Iterate through the elements and compare with the expected output
    for (Integer expectedValue : expected) {
      // Check if there are more elements to iterate
      assertTrue(iterator.hasNext());
      // Validate that the next value matches the expected value
      assertEquals(expectedValue, iterator.next());
    }

    // Ensure that the iterator has no more elements after iterating through all expected values
    assertFalse(iterator.hasNext());
  }

  /**
   * Test iterating over an integer tree with only a specified max bound.
   */
  @Test
  public void testStopOnly() {
    // Set only the maximum bound for the iterator
    treeWithInt.setIteratorMax(5);

    // Create the iterator with the specified maximum bound
    Iterator<Integer> iterator = treeWithInt.iterator();

    // Expected output: integers up to the maximum bound in sorted order
    Integer[] expected = {1, 2, 3, 4, 5};

    // Iterate through the elements and compare with the expected output
    for (Integer expectedValue : expected) {
      // Check if there are more elements to iterate
      assertTrue(iterator.hasNext());
      // Validate that the next value matches the expected value
      Integer actualValue = iterator.next();
      assertEquals(expectedValue, actualValue);
    }

    // Ensure that the iterator has no more elements after iterating through all expected values
    assertFalse(iterator.hasNext());
  }

  /**
   * Test iterating over an integer tree with duplicate values and specified bounds.
   */
  @Test
  public void testDuplicates() {
    // Insert duplicate values into the integer tree
    treeWithInt.insert(2);
    treeWithInt.insert(4);
    treeWithInt.insert(4);

    // Set the minimum and maximum bounds for the iterator
    treeWithInt.setIteratorMin(2);
    treeWithInt.setIteratorMax(4);

    // Create the iterator with the specified bounds
    Iterator<Integer> iterator = treeWithInt.iterator();

    // Expected output: integers within the specified bounds, including duplicates, in sorted order
    Integer[] expected = {2, 2, 3, 4, 4, 4};

    // Iterate through the elements and compare with the expected output
    for (Integer expectedValue : expected) {
      // Check if there are more elements to iterate
      assertTrue(iterator.hasNext());
      // Validate that the next value matches the expected value
      Integer actualValue = iterator.next();
      assertEquals(expectedValue, actualValue);
    }

    // Ensure that the iterator has no more elements after iterating through all expected values
    assertFalse(iterator.hasNext());
  }

}
