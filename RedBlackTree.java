import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

  /**
   * Checks if a new red node in the RedBlackTree causes a red property violation by having a red
   * parent. If this is not the case, the method terminates without making any changes to the tree.
   * If a red property violation is detected, then the method repairs this violation and any
   * additional red property violations that are generated as a result of the applied repair
   * operation.
   *
   * @param newRedNode a newly inserted red node, or a node turned red by previous repair
   */
  protected void ensureRedProperty(RBTNode<T> newRedNode) {
    // Get the parent of the newly inserted red node
    RBTNode<T> parent = (RBTNode<T>) newRedNode.getUp();

    // If there is no parent, we're at the root, so no further action is needed
    if (parent == null || !parent.isRed()) {
      return;
    }


    // Get the grandparent of the new red node (the parent of the parent)
    RBTNode<T> grandparent = (RBTNode<T>) parent.getUp();

    // If there is no grandparent, we're at the root level or second level,
    // and no violations are possible at this stage
    if (grandparent == null)
      return;

    // Determine the aunt node, which is the sibling of the parent node
    RBTNode<T> aunt;
    if (parent == grandparent.getLeft()) {
      aunt = grandparent.getRight(); // Parent is a left child, aunt is the right child
    } else {
      aunt = grandparent.getLeft();  // Parent is a right child, aunt is the left child
    }

    // Case 1: The aunt is red, which causes a violation in red-black properties
    // In this case, we perform a recoloring of the parent, aunt, and grandparent
    if (parent.isRed && (aunt != null && aunt.isRed)) {
      parent.flipColor();      // Flip parent color from red to black
      aunt.flipColor();        // Flip aunt color from red to black
      grandparent.flipColor(); // Flip grandparent color from black to red
      // We need to recursively ensure the red property further up the tree
      // since flipping the grandparent to red might cause a violation
      ensureRedProperty(grandparent);
    }



    // Case 2: Aunt is either null or black, but the parent and new node are on opposite sides
    // This is a not in line so requiring two rotations
    else if ((aunt == null || !aunt.isRed()) &&
        ((parent == grandparent.getRight() && newRedNode == parent.getLeft()) ||
            (parent == grandparent.getLeft() && newRedNode == parent.getRight()))) {

      rotate(newRedNode, parent);
      rotate(newRedNode, grandparent);

      // Flip the new red node color to black (was red before rotation)
      newRedNode.flipColor();
      // Flip the grandparent color to red (was black before rotation)
      grandparent.flipColor();
    }



    // Case 3: Aunt is either null or black and both the parent and new node are one line
    else if ((aunt == null || !aunt.isRed()) &&
        ((parent == grandparent.getRight() && newRedNode == parent.getRight()) ||
            (parent == grandparent.getLeft() && newRedNode == parent.getLeft()))) {

      // Perform a single rotation between the parent and grandparent to restore balance
      rotate(parent, grandparent);

      // After rotation, flip the parent color to black (was red before rotation)
      parent.flipColor();  // Equivalent to setting parent.isRed = false;
      // Flip the grandparent color to red (was black before rotation)
      grandparent.flipColor();  // Equivalent to setting grandparent.isRed = true;
    }


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
      root = new RBTNode<>(data);
      ((RBTNode<T>) root).isRed = false;
    } else {
      RBTNode<T> newNode = new RBTNode<>(data);
      insertHelper(newNode, root);
      // Call ensureRedProperty for the new red node
      ensureRedProperty(newNode);
      // Ensure the root is black
      ((RBTNode<T>) root).isRed = false;

    }
  }

  //  public static void main(String[] args) {
  //    RedBlackTree<Integer> rbTree = new RedBlackTree<>();
  //    rbTree.insert(10);
  //    rbTree.insert(20);
  //    rbTree.insert(30);
  //    rbTree.insert(15);
  //    rbTree.insert(25);
  //    rbTree.insert(5);
  //    rbTree.insert(1);
  //    if (rbTree.root != null) {
  //      System.out.println("Level Order Traversal: " + rbTree.root.toLevelOrderString());
  //    } else {
  //      System.out.println("The tree is empty.");
  //    }
  //  }

  /**
   * Test inserting multiple nodes into the Red-Black Tree and verify the correctness of the
   * tree structure
   * using two methods: assertFalse to check red-black properties, and level-order traversal
   * to validate the tree structure.
   */
  @Test
  public void testNodes() {
    RedBlackTree<Integer> rbt = new RedBlackTree<>();

    // Insert multiple nodes
    rbt.insert(40);
    rbt.insert(35);
    rbt.insert(45);
    rbt.insert(30);
    rbt.insert(25);
    rbt.insert(50);
    rbt.insert(55);

    // Verify level-order traversal structure
    String expectedLevelOrder = "[ 40(b), 30(b), 50(b), 25(r), 35(r), 45(r), 55(r) ]";
    String actualLevelOrder = rbt.root.toLevelOrderString();
    System.out.println("Level-order traversal: " + actualLevelOrder);
    assertEquals(expectedLevelOrder, actualLevelOrder);

    // Check that no two consecutive red nodes exist
    assertFalse(((RBTNode<Integer>) rbt.root).isRed);
    assertFalse(
        ((RBTNode<Integer>) rbt.root.getLeft()).isRed && ((RBTNode<Integer>) rbt.root
            .getLeft().getLeft()).isRed);
    assertFalse(
        ((RBTNode<Integer>) rbt.root.getLeft()).isRed && ((RBTNode<Integer>) rbt.root
            .getLeft().getRight()).isRed);
    assertFalse(
        ((RBTNode<Integer>) rbt.root.getRight()).isRed && ((RBTNode<Integer>) rbt.root
            .getRight().getLeft()).isRed);
    assertFalse(
        ((RBTNode<Integer>) rbt.root.getRight()).isRed && ((RBTNode<Integer>) rbt.root
            .getRight().getRight()).isRed);
  }

  /**
   * Test inserting a single node into the Red-Black Tree. The node should be black since it's
   * the root.
   */
  @Test
  public void testOneNode() {
    RedBlackTree<Integer> rbt = new RedBlackTree<>();
    rbt.insert(5);

    // Verify the root is black and correctly inserted
    RBTNode<Integer> root = (RBTNode<Integer>) rbt.root;
    assertFalse(root.isRed);  // Root must be black
    assertEquals(5, root.getData());  // Data must match the inserted value
    assertNotNull(root, "Root can't be null.");
  }

  /**
   * Test case where the aunt node is red. This test should make recoloring of the parent, aunt,
   * and grandparent.
   */
  @Test
  public void testWithRedAunt() {
    RedBlackTree<Integer> rbt = new RedBlackTree<>();

    // Insert nodes that will cause a violation due to a red aunt
    rbt.insert(20);
    rbt.insert(15);
    rbt.insert(25);
    rbt.insert(1);
    rbt.insert(16);

    // Check red-black properties after recoloring
    assertFalse(((RBTNode<Integer>) rbt.root).isRed);  // Root must be black
    assertFalse(((RBTNode<Integer>) rbt.root.getLeft()).isRed);  // Left child must be black
    assertFalse(((RBTNode<Integer>) rbt.root.getRight()).isRed);  // Right child must be black
    assertTrue(((RBTNode<Integer>) rbt.root.getLeft().getLeft()).isRed);  // Left-left grandchild
    // should be red
    assertTrue(((RBTNode<Integer>) rbt.root.getLeft().getRight()).isRed);  // Left-right grandchild
    // should be red

    // Verify level-order traversal structure
    String expectedLevelOrder = "[ 20(b), 15(b), 25(b), 1(r), 16(r) ]";
    String actualLevelOrder = rbt.root.toLevelOrderString();
    System.out.println("Level-order traversal: " + actualLevelOrder);
    assertEquals(expectedLevelOrder, actualLevelOrder);
  }

  /**
   * Test case for quiz where inserting nodes will make rotations and recoloring. Verifies the
   * level-order traversal output. Question 3
   */
  @Test
  public void testQuiz() {
    RedBlackTree<String> rbt = new RedBlackTree<>();
    rbt.insert("L");
    rbt.insert("H");
    rbt.insert("V");
    rbt.insert("D");
    rbt.insert("J");
    rbt.insert("X");

    // Additional quiz insertion that may cause rotations
    rbt.insert("W");

    // Verify level-order traversal structure
    String expectedLevelOrder = "[ L(b), H(b), W(b), D(r), J(r), V(r), X(r) ]";
    String actualLevelOrder = rbt.root.toLevelOrderString();
    System.out.println("Level-order traversal: " + actualLevelOrder);
    assertEquals(expectedLevelOrder, actualLevelOrder);
  }

  /**
   * Test case for quiz where inserting nodes will trigger 3 rotations. Verifies the
   * level-order traversal output. Question 4
   *
   */
  @Test
  public void testQuiz4() {
    RedBlackTree<String> rbt = new RedBlackTree<>();
    rbt.insert("L");
    rbt.insert("G");
    rbt.insert("Q");
    rbt.insert("C");
    rbt.insert("I");
    rbt.insert("O");
    rbt.insert("U");
    rbt.insert("B");
    rbt.insert("D");

    // Additional quiz insertion that may cause rotations
    rbt.insert("A");

    String expectedLevelOrder = "[ G(b), C(r), L(r), B(b), D(b), I(b), Q(b), A(r), O(r), U(r) ]";
    String actualLevelOrder = rbt.root.toLevelOrderString();
    System.out.println("Level-order traversal: " + actualLevelOrder);
    assertEquals(expectedLevelOrder, actualLevelOrder);
  }
  /**
   * Test case where the aunt node is black or null, triggering a single rotation to restore balance
   * in the Red-Black Tree.
   */
  @Test
  public void testWithBlackAunt() {
    RedBlackTree<Integer> rbt = new RedBlackTree<>();

    // Insert nodes to cause a violation where the aunt is black or null, leading to rotations
    rbt.insert(20);
    rbt.insert(15);
    rbt.insert(25);
    rbt.insert(11);
    rbt.insert(10);

    // Check red-black properties after the rotation
    assertFalse(((RBTNode<Integer>) rbt.root).isRed);  // Root must be black
    assertFalse(((RBTNode<Integer>) rbt.root.getLeft()).isRed);  // Left child must be black
    assertFalse(((RBTNode<Integer>) rbt.root.getRight()).isRed);  // Right child must be black
    assertTrue(((RBTNode<Integer>) rbt.root.getLeft().getLeft()).isRed);  // Left-left grandchild
    // should be red

    // Verify level-order traversal structure
    String expectedLevelOrder = "[ 20(b), 11(b), 25(b), 10(r), 15(r) ]";
    String actualLevelOrder = rbt.root.toLevelOrderString();
    System.out.println("Level-order traversal: " + actualLevelOrder);
    assertEquals(expectedLevelOrder, actualLevelOrder);
  }

  /**
   * Test case where the aunt is black or null, but requires rotations to restore balance.
   * This test handles a non-line configuration and checks rotation and recoloring.
   */
  @Test
  public void testNotInLine() {
    RedBlackTree<Integer> rbt = new RedBlackTree<>();

    // Insert nodes that will cause a non-line configuration, requiring two rotations and recoloring
    rbt.insert(100);
    rbt.insert(90);
    rbt.insert(110);
    rbt.insert(80);
    rbt.insert(95);
    rbt.insert(81);
    rbt.insert(82);

    // Verify level-order traversal structure after two rotations and recoloring
    String expectedLevelOrder = "[ 100(b), 90(r), 110(b), 81(b), 95(b), 80(r), 82(r) ]";
    String actualLevelOrder = rbt.root.toLevelOrderString();
    System.out.println("Level-order traversal: " + actualLevelOrder);
    assertEquals(expectedLevelOrder, actualLevelOrder);
  }


}



