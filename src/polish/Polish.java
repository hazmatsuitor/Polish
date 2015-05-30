package polish;

import java.util.Scanner;

import polish.tree.ParseTree;

/**
 * An implementation of a simple calculator which parses prefix or postfix (not infix yet) using a binary parse tree.
 * 
 * Using the rules detailed in ParseTree.java, a parse tree can easily be constructed from prefix or postfix notation.
 * For prefix, tokens are added as nodes to the left of the tree starting from the left of the expression.
 * For postfix, the opposite is done; nodes are added to the right of the tree, starting from the right of the expression.
 * 
 * Once the tree is constructed, it can be reformatted as prefix, postfix, or infix notation
 * simply by walking the tree depth-first in the right order for each.
 * It can also be evaluated easily by recursively applying the operators from the bottom up, starting at the leaf nodes.
 * 
 * This method is not as simple or efficient as existing algorithms for evaluating and converting between notations,
 * but it benefits from the simplicity and versatility that an intermediate format (the tree) affords.
 */

public final class Polish {

	public static void main(String[] args) {
		//I'm not used to reading from STDIN with Java, sorry if this is a bad way
		System.out.print("Give a valid expression in prefix or postfix notation: ");
		Scanner in = new Scanner(System.in);
		String line = in.nextLine();
		in.close();
		
		String[] tokens = line.trim().split(" ");
		ParseTree tree;
		
		//check if input is prefix or postfix
		if (ParseTree.isOperation(tokens[0]) && !ParseTree.isOperation(tokens[tokens.length - 1])) {
			tree = ParseTree.fromPrefix(tokens);
		} else if (!ParseTree.isOperation(tokens[0]) && ParseTree.isOperation(tokens[tokens.length - 1])) {
			tree = ParseTree.fromPostfix(tokens);
		} else {
			System.out.println("That notation is not supported.");
			return;
		}
		
		System.out.print("\nAs prefix:   " + tree.toPrefix());
		System.out.print("\nAs postfix:  " + tree.toPostfix());
		System.out.print("\nAs infix:    " + tree.toInfix());
		System.out.print("\n\nResult: " + tree.evaluate());
	}

}
