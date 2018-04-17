package logic;

import java.util.Arrays;
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
	NEG("¬", 1, 0),
	/**
	 * Represents the logical and "∧"
	 */
	AND("∧", 2, 1, OperatorTrait.COMMUTATIVE, OperatorTrait.ASSOCIATIVE),
	/**
	 * Represents the logical or "∨"
	 */
	OR("∨", 2, 1, OperatorTrait.COMMUTATIVE, OperatorTrait.ASSOCIATIVE);
	
	Operator(String displayText, int numArguments, int symbolPosition, OperatorTrait... traits)
	{
		DISPLAY_TEXT = displayText;
		NUM_ARGUMENTS = numArguments;
		SYMBOL_POSITION = symbolPosition;
		TRAITS = Collections.unmodifiableSet(new HashSet<OperatorTrait>(Arrays.asList(traits)));
	}
	
	public boolean hasTrait(OperatorTrait t)
	{
		return TRAITS.contains(t);
	}
	
	/**
	 * A Unicode representation of the symbol or text to be displayed when pretty-printing
	 * the operator.  Currently only single characters are supported for parsing.
	 */
	public final String DISPLAY_TEXT;
	public final int NUM_ARGUMENTS;
	/**
	 * The ordinal position of the operator in the pretty-printed version.  In other '∧' in "A∧B" has a position of 1.
	 */
	public final int SYMBOL_POSITION;
	private final Set<OperatorTrait> TRAITS;
}