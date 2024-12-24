
/**
 * BinarySearchTree class implements the SortedCollection interface.
 * It represents a generic binary search tree (BST) data structure
 * that stores comparable values in their natural ordering. It supports
 * operations such as insert, contains, size, clear, and checking if the
 * tree is empty.
 *
 * @param <T> the type of data stored in the BST, which must be comparable
 */

public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {
  protected BSTNode<T> root;

  /**
   * Checks if the Binary Search Tree is empty (i.e., contains no nodes).
   * @return true if the BST is empty, false otherwise
   */
  @Override
  public boolean isEmpty() {
    return root == null;
  }

  /**
   * Checks if the specified data value is present in the tree.
   *
   * @param data the value to check for in the collection
   * @return true if the collection contains the data value, false otherwise
   */

  @Override
  public boolean contains(Comparable<T> data) {
    return containsHelper(root, data);
  }

  /**
   * Helper method to recursively search for the specified data value in the tree.
   *
   * @param node the current node being checked
   * @param data the value to search for in the collection
   * @return true if the value is found in the tree, false otherwise
   */

  protected boolean containsHelper(BSTNode<T> node, Comparable<T> data) {
    if (node == null) {
      return false;  // Base case - node is not found
    }
    if (data.compareTo(node.getData()) == 0) {
      return true;  // Data found
    } else if (data.compareTo(node.getData()) < 0) {
      return containsHelper(node.getLeft(), data);  // Search left subtree
    } else {
      return containsHelper(node.getRight(), data);  // Search right subtree
    }
  }

  /**
   * Returns the number of values stored in the tree, including duplicates.
   *
   * @return the total number of nodes in the tree
   */
  @Override
  public int size() {
    return sizeHelper(root);
  }

  /**
   * Helper method to recursively count the number of nodes in the tree.
   *
   * @param node the current node being counted
   * @return the number of nodes in the tree
   */
  protected int sizeHelper(BSTNode<T> node) {
    if (node == null) {
      return 0;
    }
    return 1 + sizeHelper(node.getLeft()) + sizeHelper(node.getRight());
  }

  /**
   * Inserts a new data value into the Binary Search Tree.
   *
   * @param data the new value being inserted
   * @throws NullPointerException if the data argument is null
   */
  @Override
  public void insert(T data) throws NullPointerException {
    if (data == null) {
      throw new NullPointerException("data inserted cannot be null");
    }
    if (root == null) {
      root = new BSTNode<>(data);
    } else {
      insertHelper(new BSTNode<>(data), root);
    }
  }

  /**
   * Performs the naive binary search tree insert algorithm to recursively insert the provided
   * newNode (which has already been initialized with a data value) into the provided tree/subtree.
   * When the provided subtree is null, this method does nothing.
   */
  protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {

    if (newNode.getData().compareTo(subtree.getData()) <= 0) {
      if (subtree.getLeft() == null) {
        subtree.setLeft(newNode);
        newNode.setUp(subtree);
      } else {
        insertHelper(newNode, subtree.getLeft());
      }
    } else {
      if (subtree.getRight() == null) {
        subtree.setRight(newNode);
        newNode.setUp(subtree);
      } else {
        insertHelper(newNode, subtree.getRight());
      }
    }
  }

  /**
   * Removes all values and duplicates from the tree by setting the root to null.
   */
  @Override
  public void clear() {
    root = null;
  }

  /**
   * Returns a string representation of the tree's values in in-order traversal format.
   *
   * @return the in-order traversal of the tree as a string
   */
  protected String toInOrderString() {
    if (root == null) {
      return "[ ]";
    }
    return root.toInOrderString();
  }

  /**
   * Returns a string representation of the tree's values in level-order traversal format.
   *
   * @return the level-order traversal of the tree as a string
   */
  protected String toLevelOrderString() {
    if (root == null) {
      return "[ ]";
    }
    return root.toLevelOrderString();
  }


  /**
   * This test case verifies the following: - Inserting multiple values as both left and right
   * children to create differently shaped trees. - Handling duplicates correctly by inserting them
   * to the left and counting them in the size. - Checking size correctness and ensuring values are
   * stored in the correct order. - Ensuring the `contains()` method works for all inserted values.
   *
   * @return boolean - true if all sub-tests pass, false otherwise.
   */
  public boolean test1() {
    // Integer Test Cases
    boolean sizeTest1 = true;
    boolean sizeTest2 = true;
    boolean sizeTest3 = true;

    boolean stringSizeTest1 = true;
    boolean stringSizeTest2 = true;
    boolean stringSizeTest3 = true;


    // Test 1: Integer - Ascending order insertion
    BinarySearchTree<Integer> intTree1 = new BinarySearchTree<>();
    intTree1.insert(1);
    intTree1.insert(2);
    intTree1.insert(3);
    intTree1.insert(4);

    // Check size and structure with in-order and level-order
    if (intTree1.size() != 4 || !intTree1.toInOrderString().equals("[ 1, 2, 3, 4 ]") ||
        !intTree1.toLevelOrderString().equals("[ 1, 2, 3, 4 ]")) {
      sizeTest1 = false;
    }
    if (!intTree1.contains(1) || !intTree1.contains(2) || !intTree1.contains(3) ||
        !intTree1.contains(4) || intTree1.contains(5)) {
      sizeTest1 = false;
    }

    // Test 2: Integer - Mixed order insertion
    BinarySearchTree<Integer> intTree2 = new BinarySearchTree<>();
    intTree2.insert(3);
    intTree2.insert(1);
    intTree2.insert(4);
    intTree2.insert(2);

    if (intTree2.size() != 4 || !intTree2.toInOrderString().equals("[ 1, 2, 3, 4 ]") ||
        !intTree2.toLevelOrderString().equals("[ 3, 1, 4, 2 ]")) {
      sizeTest2 = false;
    }
    if (!intTree2.contains(1) || !intTree2.contains(2) || !intTree2.contains(3) ||
        !intTree2.contains(4) || intTree2.contains(5)) {
      sizeTest2 = false;
    }

    // Test 3: Integer - Duplicate insertion
    BinarySearchTree<Integer> intTree3 = new BinarySearchTree<>();
    intTree3.insert(2);
    intTree3.insert(1);
    intTree3.insert(3);
    intTree3.insert(2);  // Duplicate

    if (intTree3.size() != 4 || !intTree3.toInOrderString().equals("[ 1, 2, 2, 3 ]") ||
        !intTree3.toLevelOrderString().equals("[ 2, 1, 3, 2 ]")) {
      sizeTest3 = false;
    }
    if (!intTree3.contains(1) || !intTree3.contains(2) || !intTree3.contains(3) ||
        intTree3.contains(4)) {
      sizeTest3 = false;
    }

    // Repeat the same for String type

    // Test 1: String - Ascending order insertion
    BinarySearchTree<String> stringTree1 = new BinarySearchTree<>();
    stringTree1.insert("Arun");
    stringTree1.insert("Bailey");
    stringTree1.insert("Chris");
    stringTree1.insert("Damian");

    if (stringTree1.size() != 4 || !stringTree1.toInOrderString()
        .equals("[ Arun, Bailey, Chris, Damian ]") || !stringTree1.toLevelOrderString().
        equals("[ Arun, Bailey, Chris, Damian ]")) {
      stringSizeTest1 = false;
    }

    if (!stringTree1.contains("Arun") || !stringTree1.contains("Bailey") ||
        !stringTree1.contains("Chris") || !stringTree1.contains("Damian") ||
        stringTree1.contains("Eve")) {
      stringSizeTest1 = false;
    }


    // Test 2: String - Mixed order insertion
    BinarySearchTree<String> stringTree2 = new BinarySearchTree<>();
    stringTree2.insert("Chris");
    stringTree2.insert("Arun");
    stringTree2.insert("Damian");
    stringTree2.insert("Bailey");

    if (stringTree2.size() != 4 || !stringTree2.toInOrderString()
        .equals("[ Arun, Bailey, Chris, Damian ]") || !stringTree2.toLevelOrderString().
        equals("[ Chris, Arun, Damian, Bailey ]")) {
      stringSizeTest2 = false;
    }

    if (!stringTree2.contains("Arun") || !stringTree2.contains("Bailey") ||
        !stringTree2.contains("Chris") || !stringTree2.contains("Damian") ||
        stringTree2.contains("Eve")) {
      stringSizeTest2 = false;
    }

    // Test 3: String - Duplicate insertion
    BinarySearchTree<String> stringTree3 = new BinarySearchTree<>();
    stringTree3.insert("Bailey");
    stringTree3.insert("Arun");
    stringTree3.insert("Chris");
    stringTree3.insert("Bailey");  // Duplicate

    if (stringTree3.size() != 4 || !stringTree3.toInOrderString()
        .equals("[ Arun, Bailey, Bailey, Chris ]") || !stringTree3.toLevelOrderString().
        equals("[ Bailey, Arun, Chris, Bailey ]")) {
      stringSizeTest3 = false;
    }

    // Return true if all tests pass
    return sizeTest1 && sizeTest2 && sizeTest3 && stringSizeTest1 && stringSizeTest2 &&
        stringSizeTest3;
  }



  /**
   * This test case verifies the following: - Ensuring the `clear()` method works by clearing the
   * tree and checking if the size becomes zero. - Checking size after multiple insertions and
   * comparing with expected size. - Ensuring the tree structure can be rebuilt after clearing for
   * both Integer and String data types.
   *
   * @return boolean - true if the test passes for both Integer and String types, false otherwise.
   */
  public boolean test2() {
    boolean testClear = true;

    // Integer Test Case
    BinarySearchTree<Integer> intTree = new BinarySearchTree<>();
    intTree.insert(10);
    intTree.insert(5);
    intTree.insert(20);
    intTree.insert(3);
    intTree.insert(7);

    if (intTree.size() != 5) {
      testClear = false;
    }

    // Clear and check size
    intTree.clear();
    if (!intTree.isEmpty() || intTree.size() != 0) {
      testClear = false;
    }

    // Rebuild and check size again
    intTree.insert(5);
    intTree.insert(100);
    intTree.insert(20);

    if (intTree.size() != 3 || !intTree.toInOrderString().equals("[ 5, 20, 100 ]")) {
      testClear = false;
    }

    // String Test Case
    BinarySearchTree<String> stringTree = new BinarySearchTree<>();
    stringTree.insert("Apple");
    stringTree.insert("Banana");
    stringTree.insert("Cherry");
    stringTree.insert("Date");

    if (stringTree.size() != 4) {
      testClear = false;
    }

    // Clear and check size
    stringTree.clear();
    if (!stringTree.isEmpty() || stringTree.size() != 0) {
      testClear = false;
    }

    // Rebuild and check size again
    stringTree.insert("A");
    stringTree.insert("B");
    stringTree.insert("C");

    if (stringTree.size() != 3 || !stringTree.toInOrderString().equals("[ A, B, C ]")) {
      testClear = false;
    }

    return testClear;
  }


  /**
   * This test case verifies the following: - Proper identification and storage of leaf and interior
   * nodes. - The tree structure is validated using in-order and level-order traversals. - Both
   * Integer and String data types are used.
   *
   * @return boolean - true if all sub-tests pass, false otherwise.
   */
  public boolean test3() {
    boolean test1 = true;
    boolean test2 = true;
    boolean test3 = true;
    boolean stringTest1 = true;
    boolean stringTest2 = true;
    boolean stringTest3 = true;

    BinarySearchTree<Integer> intTree1 = new BinarySearchTree<>();
    intTree1.insert(50);  // Root
    intTree1.insert(25);  // Left subtree
    intTree1.insert(75);  // Right subtree
    intTree1.insert(10);  // Left leaf
    intTree1.insert(40);  // Right leaf of left subtree
    intTree1.insert(60);  // Left leaf of right subtree
    intTree1.insert(90);  // Right leaf

    if (intTree1.size() != 7 || !intTree1.toInOrderString()
        .equals("[ 10, 25, 40, 50, 60, 75, 90 ]") || !intTree1.toLevelOrderString().
        equals("[ 50, 25, 75, 10, 40, 60, 90 ]")) {
      test1  = false;
    }
    if (!intTree1.contains(10) || !intTree1.contains(25) || !intTree1.contains(40)
        || !intTree1.contains(50) ||
        !intTree1.contains(60) || !intTree1.contains(75) || !intTree1.contains(90)
        || intTree1.contains(100)) {
      test1 = false;
    }
    BinarySearchTree<Integer> intTree2 = new BinarySearchTree<>();
    intTree2.insert(10);
    intTree2.insert(20);
    intTree2.insert(30);

    if (intTree2.size() != 3 || !intTree2.toInOrderString().equals("[ 10, 20, 30 ]") ||
        !intTree2.toLevelOrderString().equals("[ 10, 20, 30 ]")) {
      test2 = false;
    }
    if (!intTree2.contains(10) || !intTree2.contains(20) || !intTree2.contains(30) ||
        intTree2.contains(40)) {
      test2 = false;
    }
    BinarySearchTree<Integer> intTree3 = new BinarySearchTree<>();
    intTree3.insert(5);

    if (intTree3.size() != 1 || !intTree3.toInOrderString().equals("[ 5 ]") ||
        !intTree3.toLevelOrderString().equals("[ 5 ]")) {
      test3  = false;
    }
    if (!intTree3.contains(5) || intTree3.contains(10)) {
      test3 = false;
    }
    // String type testers
    BinarySearchTree<String> stringTree1 = new BinarySearchTree<>();
    stringTree1.insert("Chris");
    stringTree1.insert("Bailey");
    stringTree1.insert("Damian");
    stringTree1.insert("Arun");

    if (stringTree1.size() != 4 ||
        !stringTree1.toInOrderString().equals("[ Arun, Bailey, Chris, Damian ]") ||
        !stringTree1.toLevelOrderString().equals("[ Chris, Bailey, Damian, Arun ]")) {
      stringTest1 = false;
    }
    if (!stringTree1.contains("Arun") || !stringTree1.contains("Bailey") ||
        !stringTree1.contains("Chris") ||
        !stringTree1.contains("Damian") || stringTree1.contains("Eve")) {
      stringTest1 = false;
    }

    BinarySearchTree<String> stringTree2 = new BinarySearchTree<>();
    stringTree2.insert("Arun");
    stringTree2.insert("Bailey");
    stringTree2.insert("Chris");
    if (stringTree2.size() != 3 ||
        !stringTree2.toInOrderString().equals("[ Arun, Bailey, Chris ]") ||
        !stringTree2.toLevelOrderString().equals("[ Arun, Bailey, Chris ]")) {
      stringTest2 = false;
    }
    if (!stringTree2.contains("Arun") || !stringTree2.contains("Bailey") ||
        !stringTree2.contains("Chris") ||
        stringTree2.contains("Damian")) {
      stringTest2 = false;
    }

    BinarySearchTree<String> stringTree3 = new BinarySearchTree<>();
    stringTree3.insert("Arun");

    if (stringTree3.size() != 1 ||
        !stringTree3.toInOrderString().equals("[ Arun ]") ||
        !stringTree3.toLevelOrderString().equals("[ Arun ]")) {
      stringTest3 = false;
    }
    if (!stringTree3.contains("Arun") || stringTree3.contains("Bailey")) {
      stringTest3 = false;
    }
    return test1  && test2 && test3  && stringTest1 && stringTest2 &&
        stringTest3;
  }

  /**
   * Tests the correctness of the 'up' references in the BinarySearchTree.
   *This method creates a BST, inserts several elements, and then checks
   * if the 'up' references of various nodes are set correctly.
   *
   * @return true if all 'up' references are correct, false otherwise
 */
  public boolean test4() {
    boolean testUp = true;

    // Create a new BST and insert elements
    BinarySearchTree<Integer> bst = new BinarySearchTree<>();
    bst.insert(5);
    bst.insert(3);
    bst.insert(6);
    bst.insert(1);
    bst.insert(9);
    if (bst.root.getUp() != null) {
      testUp = false;
    }
//root should be 5
    if (bst.root.getLeft().getUp() != bst.root || bst.root.getRight().getUp() != bst.root) {
      testUp = false;
    }
    if (bst.root.getLeft().getLeft().getUp() != bst.root.getLeft() ||
        bst.root.getRight().getRight().getUp() != bst.root.getRight()) {
      testUp = false;
    }
    return testUp;
  }

  /**
   * The main method runs a series of test cases for the Binary Search Tree (BST) implementation
   * and prints the result for each test.
   *
   * @param args
   */

  public static void main(String[] args) {
    BinarySearchTree<Integer>bst=new BinarySearchTree<>();
    System.out.println("Test Results:");
    System.out.println("TEST 1. Size, content, and duplicates test: " + (bst.test1() ? "PASSED" :
        "FAILED"));

    System.out.println("TEST 2. Clear method and resizing test: " + (bst.test2() ? "PASSED" :
        "FAILED"));

    System.out.println("TEST 3. Leaf and interior nodes test: " + (bst.test3() ? "PASSED" :
        "FAILED"));

    System.out.println("TEST 4. Parent reference test:  " + (bst.test4() ? "PASSED" :
        "FAILED"));
  }


}


