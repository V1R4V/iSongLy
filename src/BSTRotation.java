
/**
 * The BSTRotation class implements rotation operations (both left and right rotations)
 * for a Binary Search Tree (BST) structure. These operations allow us to maintain
 * the balance of the tree. Rotations are performed on existing nodes and no new node
 * objects are created. This class extends BinarySearchTree and uses a bounded generic
 * type to store the data in the tree.
 *
 * @param <T> the type of data stored in the nodes of this tree, which must be
 *            comparable to ensure proper ordering of nodes in the BST.
 */
public class BSTRotation <T extends Comparable<T>> extends BinarySearchTree<T> {

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a left child of the provided parent, this method will perform a right rotation. When the
   * provided child is a right child of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will either
   * throw a NullPointerException: when either reference is null, or otherwise will throw an
   * IllegalArgumentException.
   *
   * @param child  is the node being rotated from child to parent position
   * @param parent is the node being rotated from parent to child position
   * @throws NullPointerException     when either passed argument is null
   * @throws IllegalArgumentException when the provided child and parent nodes are not initially
   *                                  (pre-rotation) related that way
   */
  protected void rotate(BSTNode<T> child, BSTNode<T> parent)
      throws NullPointerException, IllegalArgumentException {
    if (child == null || parent == null) {
      throw new NullPointerException("Data passed cannot be null for rotation");
    }
    if (parent.getLeft() == child) {
      rotateRight(child, parent);
    } else if (parent.getRight() == child) {
      rotateLeft(child, parent);
    } else {
      throw new IllegalArgumentException("Child must be either left or right child of parent for " +
          "rotation.");
    }
  }

  /**
   * Performs a right rotation on the provided parent and child nodes. The child node must
   * be the left child of the parent. This method rearranges the tree structure such that
   * the child becomes the new parent.
   *
   * @param child  the left child node that will be promoted to the parent position
   * @param parent the parent node that will be demoted to the right child position
   * @throws IllegalArgumentException if the child is not the left child of the parent or
   *                                  if the input is invalid
   */
  protected void rotateRight(BSTNode<T> child, BSTNode<T> parent) {
    if (child == null || parent == null || parent.getLeft() != child) {
      throw new IllegalArgumentException("Invalid rotation parameters");
    }

    // Store grandparent reference
    BSTNode<T> grandparent = parent.getUp();
    boolean parentWasLeftChild = (grandparent != null && grandparent.getLeft() == parent);

    // Update parent's left child
    parent.setLeft(child.getRight());
    if (child.getRight() != null) {
      child.getRight().setUp(parent);
    }

    // Perform right rotation
    child.setRight(parent);
    parent.setUp(child);

    // Update grandparent or root
    if (grandparent == null) {
      root = child;
      child.setUp(null);
    } else {
      if (parentWasLeftChild) {
        grandparent.setLeft(child);
      } else {
        grandparent.setRight(child);
      }
      child.setUp(grandparent);
    }
  }

  /**
   * Performs a left rotation on the provided parent and child nodes. The child node must
   * be the right child of the parent. This method rearranges the tree structure such that
   * the child becomes the new parent.
   *
   * @param child  the right child node that will be promoted to the parent position
   * @param parent the parent node that will be demoted to the left child position
   * @throws IllegalArgumentException if the child is not the right child of the parent or
   *                                  if the input is invalid
   */
  protected void rotateLeft(BSTNode<T> child, BSTNode<T> parent) {
    if (child == null || parent == null || parent.getRight() != child) {
      throw new IllegalArgumentException("Invalid rotation parameters");
    }

    BSTNode<T> grandparent = parent.getUp();
    boolean parentWasLeftChild = (grandparent != null && grandparent.getLeft() == parent);

    // Update parent's right child
    parent.setRight(child.getLeft());
    if (child.getLeft() != null) {
      child.getLeft().setUp(parent);
    }

    // Perform rotation
    child.setLeft(parent);
    parent.setUp(child);

    // Update grandparent or root
    if (grandparent == null) {
      root = child;
      child.setUp(null);
    } else {
      if (parentWasLeftChild) {
        grandparent.setLeft(child);
      } else {
        grandparent.setRight(child);
      }
      child.setUp(grandparent);
    }
  }

  /**
   * Test case 1: Performs left and right rotations, checks for proper behavior,
   * and ensures the correct updates of tree structure.
   *
   * @return true if the rotation works correctly, false otherwise
   */
  public boolean test1() {
    BSTRotation<Integer> testBst = new BSTRotation<>();
    testBst.insert(3);
    testBst.insert(2);
    testBst.insert(5);
    testBst.insert(4);
    testBst.insert(6);
    if (!testBst.root.toLevelOrderString().equals("[ 3, 2, 5, 4, 6 ]")) {
      return false;
    }
    testBst.rotate(testBst.root.getRight(), testBst.root);
    return testBst.root.toLevelOrderString().equals("[ 5, 3, 6, 2, 4 ]");
  }

  /**
   * Test case 2: Performs rotations that include the root node and rotations on subtrees
   * that do not involve the root node. Ensures proper behavior for both cases.
   *
   * @return true if all rotations work correctly, false otherwise
   */
  public boolean test2() {
    // Perform rotation that includes the root node
    {
      BSTRotation<Integer> testBST = new BSTRotation<>();
      testBST.insert(7);
      testBST.insert(5);
      testBST.insert(9);
      testBST.insert(3);
      testBST.insert(6);

      if (!testBST.root.toLevelOrderString().equals("[ 7, 5, 9, 3, 6 ]")) {
        return false;
      }

      // Rotate the left child (root included in rotation)
      testBST.rotate(testBST.root.getLeft(), testBST.root);
      if (!testBST.root.toLevelOrderString().equals("[ 5, 3, 7, 6, 9 ]")) {
        return false;
      }
    }

    // Perform rotation that does not involve the root node
    {
      BSTRotation<Integer> testBST = new BSTRotation<>();
      testBST.insert(10);
      testBST.insert(6);
      testBST.insert(12);
      testBST.insert(4);
      testBST.insert(8);
      testBST.insert(7);
      testBST.insert(9);

      if (!testBST.root.toLevelOrderString().equals("[ 10, 6, 12, 4, 8, 7, 9 ]")) {
        return false;
      }

      testBST.rotate(testBST.root.getLeft().getRight(), testBST.root.getLeft());
      if (!testBST.root.toLevelOrderString().equals("[ 10, 8, 12, 6, 9, 4, 7 ]")) {
        return false;
      }
    }

    return true;
  }


  /**
   * Test case 3: Checks multiple rotations with varying numbers of shared children
   * and ensures proper tree structure updates.
   *
   * @return true if all rotations work correctly, false otherwise
   */
  public boolean test3() {

    // 0 shared nodes
    {
      BSTRotation<Integer> testBst = new BSTRotation<>();
      testBst.insert(3);
      testBst.insert(2);
      testBst.rotate(testBst.root.getLeft(), testBst.root);
      if (!testBst.root.toLevelOrderString().equals("[ 2, 3 ]")) {
        return false;
      }
    }

    // 1 shared node
    {
      BSTRotation<Integer> testBst = new BSTRotation<>();
      testBst.insert(4);
      testBst.insert(2);
      testBst.insert(3);
      testBst.rotate(testBst.root.getLeft(), testBst.root);
      if (!testBst.root.toLevelOrderString().equals("[ 2, 4, 3 ]")) {
        return false;
      }
    }

    // 2 shared nodes
    {
      BSTRotation<Integer> testBst = new BSTRotation<>();
      testBst.insert(6);
      testBst.insert(4);
      testBst.insert(2);
      testBst.insert(5);
      testBst.rotate(testBst.root.getLeft(), testBst.root);
      if (!testBst.root.toLevelOrderString().equals("[ 4, 2, 6, 5 ]")) {
        return false;
      }
    }

    // 3 shared nodes
    {
      BSTRotation<Integer> testBst = new BSTRotation<>();
      testBst.insert(8);
      testBst.insert(4);
      testBst.insert(2);
      testBst.insert(6);
      testBst.insert(5);
      testBst.insert(7);
      testBst.rotate(testBst.root.getLeft(), testBst.root);
      if (!testBst.root.toLevelOrderString().equals("[ 4, 2, 8, 6, 5, 7 ]")) {
        return false;
      }
    }

    // Left rotations
    // 0 shared nodes
    {
      BSTRotation<Integer> testBst = new BSTRotation<>();
      testBst.insert(2);
      testBst.insert(3);
      testBst.rotate(testBst.root.getRight(), testBst.root);
      if (!testBst.root.toLevelOrderString().equals("[ 3, 2 ]")) {
        return false;
      }
    }

    // 1 shared node
    {
      BSTRotation<Integer> testBst = new BSTRotation<>();
      testBst.insert(2);
      testBst.insert(4);
      testBst.insert(3);
      testBst.rotate(testBst.root.getRight(), testBst.root);
      if (!testBst.root.toLevelOrderString().equals("[ 4, 2, 3 ]")) {
        return false;
      }
    }

    // 2 shared nodes
    {
      BSTRotation<Integer> testBst = new BSTRotation<>();
      testBst.insert(4);
      testBst.insert(6);
      testBst.insert(5);
      testBst.insert(7);
      testBst.rotate(testBst.root.getRight(), testBst.root);
      if (!testBst.root.toLevelOrderString().equals("[ 6, 4, 7, 5 ]")) {
        return false;
      }
    }

    // 3 shared nodes
    {
      BSTRotation<Integer> testBst = new BSTRotation<>();
      testBst.insert(2);
      testBst.insert(6);
      testBst.insert(4);
      testBst.insert(8);
      testBst.insert(3);
      testBst.insert(5);
      testBst.rotate(testBst.root.getRight(), testBst.root);
      if (!testBst.root.toLevelOrderString().equals("[ 6, 2, 8, 4, 3, 5 ]")) {
        return false;
      }
    }
    return true;
  }

  /**
   * The main method runs a series of test cases for the Binary Search Tree (BST)
   * rotation implementation
   * and prints the result for each test.
   *
   * @param args
   */
  public static void main(String[] args) {
    BSTRotation<Integer> bstRotation = new BSTRotation<>();
    System.out.println("Test 1: " + bstRotation.test1());
    System.out.println("Test 2: " + bstRotation.test2());
    System.out.println("Test 3: " + bstRotation.test3());

  }
}


//protected void rotateRight(BSTNode<T> child, BSTNode<T> parent) {
//  // case 1- child has no right child hence the left child of parent is null
//  if (child.getRight() == null) {
//    parent.setLeft(null);
//    child.setRight(parent);
//
//    if (parent.getUp() == null) {
//      root = child;
//    }
//
//    if (parent.getUp() != null) {
//      if (parent.getUp().getData().compareTo(child.getData()) > 0) {
//        parent.getUp().setLeft(child);
//      }
//      if (parent.getUp().getData().compareTo(child.getData()) < 0) {
//        parent.getUp().setRight(child);
//      }
//      child.setUp(parent.getUp());
//    }
//
//    parent.setUp(child);
//  }
//
//  // case 2 general implementation
//  if (child.getRight() != null) {
//    parent.setLeft(child.getRight());
//    child.getRight().setUp(parent);
//    child.setRight(parent);
//
//    if (parent.getUp() == null) {
//      root = child;
//    }
//
//    if (parent.getUp() != null) {
//      if (parent.getUp().getData().compareTo(child.getData()) > 0) {
//        parent.getUp().setLeft(child);
//      }
//      if (parent.getUp().getData().compareTo(child.getData()) < 0) {
//        parent.getUp().setRight(child);
//      }
//      child.setUp(parent.getUp());
//    }
//
//    parent.setUp(child);
//  }
//}
//
//protected void rotateLeft(BSTNode<T> child, BSTNode<T> parent) {
//  // Case 1 - Child has no left child
//  if (child.getLeft() == null) {
//    parent.setRight(null);        // Parent's right child becomes null
//    child.setLeft(parent);        // Child's left becomes the parent (rotation)
//
//    if (parent.getUp() == null) { // If parent is the root
//      root = child;             // Update root to the child
//    }
//
//    if (parent.getUp() != null) { // If parent has an upper node (grandparent)
//      if (parent.getUp().getData().compareTo(child.getData()) > 0) {
//        parent.getUp().setLeft(child);  // Set child as the left child of grandparent
//      } else {
//        parent.getUp().setRight(child); // Set child as the right child of grandparent
//      }
//      child.setUp(parent.getUp());        // Set child's parent to grandparent
//    }
//
//    parent.setUp(child);  // Set parent's new parent as child
//  }
//
//  // Case 2 - Child has a left child
//  if (child.getLeft() != null) {
//    parent.setRight(child.getLeft());       // Parent's right child becomes child's left child
//    child.getLeft().setUp(parent);          // Child's left child gets a new parent (parent)
//    child.setLeft(parent);                  // Rotate child to be the parent
//
//    if (parent.getUp() == null) {           // If parent is the root
//      root = child;                       // Update root to the child
//    }
//
//    if (parent.getUp() != null) {           // If parent has an upper node (grandparent)
//      if (parent.getUp().getData().compareTo(child.getData()) > 0) {
//        parent.getUp().setLeft(child);  // Set child as the left child of grandparent
//      } else {
//        parent.getUp().setRight(child); // Set child as the right child of grandparent
//      }
//      child.setUp(parent.getUp());        // Set child's parent to grandparent
//    }
//
//    parent.setUp(child);  // Set parent's new parent as child
//  }
//}
