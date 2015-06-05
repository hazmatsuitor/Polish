package polish.tree;

/**
 * An implementation of a binary tree with the following rules:
 * - new nodes may only be inserted to the left or the right
 * - only nodes whose keys match a given set of operations may have children
 */

public class ParseTree {
	private static final String[] operations = {"+", "-", "*", "/"};
	private Node root;

	public ParseTree() {
		//currently no-op
	}

	/** factory methods */
	public static ParseTree fromPrefix(String[] keys) {
		ParseTree tree = new ParseTree();
		for (String key : keys)
			if (!key.equals(""))
				tree.addLeft(key);
		return tree;
	}

	public static ParseTree fromPostfix(String[] keys) {
		ParseTree tree = new ParseTree();
		for (int i = keys.length - 1; i >= 0; --i) //http://stackoverflow.com/questions/1642028/what-is-the-name-of-the-operator
			if (!keys[i].equals(""))
				tree.addRight(keys[i]);
		return tree;
	}

	/** returns true iff key represents is a valid operation */
	public static boolean isOperation(String key) {
		for (String operation : operations)
			if (operation.equals(key))
				return true;
		return false;
	}

	/** evaluates operands by the operations specified in a given string */
	private static int evaluate(int lhs, String operation, int rhs) {
		//in an interpreted language, we could use fancy reflection here
		//instead, you're stuck with a boring switch statement. sorry!
		switch(operation) {
			case "+": return lhs + rhs;
			case "-": return lhs - rhs;
			case "*": return lhs * rhs;
			case "/": return lhs / rhs;
		}

		return 0; //this should never happen
	}

	private static int precedence(String operation) {
		//implements MDAS
		switch(operation) {
			case "+": case "-": return 1;
			case "*": case "/": return 2;
		}

		return -1; //with this, if you pass a non-operation as an arg, things might still work
	}

	/** print entire tree as prefix */
	public String toPrefix() {
		return toPrefix(root);
	}

	private String toPrefix(Node node) {
		if (node.left == null)
			return node.key;
		return node.key + " " + toPrefix(node.left) + " " + toPrefix(node.right);
	}

	/** print entire tree as postfix */
	public String toPostfix() {
		return toPostfix(root);
	}

	private String toPostfix(Node node) {
		if (node.left == null)
			return node.key;
		return toPostfix(node.left) + " " + toPostfix(node.right) + " " + node.key;
	}

	/** print entire tree as postfix */
	public String toInfix() {
		return toInfix(root, -1);
	}

	private String toInfix(Node node, int precedence) {
		if (node.left == null)
			return node.key;
		if (precedence(node.key) < precedence)
			return "(" + toInfix(node.left, precedence(node.key)) + " " + node.key + " " + toInfix(node.right, precedence(node.key)) + ")";
		return toInfix(node.left, precedence(node.key)) + " " + node.key + " " + toInfix(node.right, precedence(node.key));
	}

	/** evaluate and return the value of the entire tree */
	public int evaluate() {
		return evaluate(root);
	}

	private int evaluate(Node node) {
		if (isOperation(node.key))
			return evaluate(evaluate(node.left), node.key, evaluate(node.right));
		return Integer.valueOf(node.key);
	}

	/** adds a new node to the leftmost possible position without violating the tree's properties */
	public void addLeft(String key) {
		if (root == null)
			root = new Node(key);
		else
			addLeftRecursive(root, key);
	}

	private boolean addLeftRecursive(Node node, String key) {
		if (!isOperation(node.key))
			return false;

		if (node.left == null) {
			node.left = new Node(key);
			return true;
		}

		if (addLeftRecursive(node.left, key))
			return true;

		if (node.right == null) {
			node.right = new Node(key);
			return true;
		}

		return addLeftRecursive(node.right, key);
	}

	/** adds a new node to the rightmost possible position without violating the tree's properties */
	public void addRight(String key) {
		if (root == null)
			root = new Node(key);
		else
			addRightRecursive(root, key);
	}

	private boolean addRightRecursive(Node node, String key) {
		if (!isOperation(node.key))
			return false;

		if (node.right == null) {
			node.right = new Node(key);
			return true;
		}

		if (addRightRecursive(node.right, key))
			return true;

		if (node.left == null) {
			node.left = new Node(key);
			return true;
		}

		return addRightRecursive(node.left, key);
	}
}
