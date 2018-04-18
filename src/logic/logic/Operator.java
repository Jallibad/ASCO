package logic;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// TODO add truth table stuff to this
/**
 * An Enum containing each of the valid operators in FOL.
 * @author Jallibad
 *
 */
public enum Operator
{
	/**
	 * Represents the unary not/negation "¬"
	 */
	NEG("¬", 1, 0, new boolean[][] {
		{false, true},
		{true, false}
	}),
	/**
	 * Represents the logical and "∧"
	 */
	AND("∧", 2, 1, new boolean[][] {
		{false, false, false},
		{false, true, false},
		{true, false, false},
		{true, true, true}
	}, OperatorTrait.COMMUTATIVE, OperatorTrait.ASSOCIATIVE),
	/**
	 * Represents the logical or "∨"
	 */
	OR("∨", 2, 1, new boolean[][] {
		{false, false, false},
		{false, true, true},
		{true, false, true},
		{true, true, true}
	}, OperatorTrait.COMMUTATIVE, OperatorTrait.ASSOCIATIVE);
	
	Operator(String displayText, int numArguments, int symbolPosition, boolean[][] truthTable, OperatorTrait... traits)
	{
		this.displayText = displayText;
		this.numArguments = numArguments;
		this.symbolPosition = symbolPosition;
		this.truthTable = new ArrayList<ArrayList<Boolean>>();
		for (int i = 0; i < truthTable.length; ++i) {
			this.truthTable.add(new ArrayList<Boolean>());
			for (int r = 0; r < truthTable[i].length; ++r) {
				this.truthTable.get(i).add(truthTable[i][r]);
			}
		}
		this.traits = Collections.unmodifiableSet(new HashSet<OperatorTrait>(Arrays.asList(traits)));
	}
	
	public boolean hasTrait(OperatorTrait t)
	{
		return traits.contains(t);
	}
	
	/**
	 * A Unicode representation of the symbol or text to be displayed when pretty-printing
	 * the operator.  Currently only single characters are supported for parsing.
	 */
	public final String displayText;
	public final int numArguments;
	/**
	 * The ordinal position of the operator in the pretty-printed version.  In other '∧' in "A∧B" has a position of 1.
	 */
	public final int symbolPosition;
	public final ArrayList<ArrayList<Boolean>> truthTable;
	private final Set<OperatorTrait> traits;
}