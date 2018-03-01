package logic;

// TODO add truth table stuff to this
/**
 * An Enum containing each of the valid operators in FOL.
 * @author Jallibad
 *
 */
public enum Operator
{
	/**
	 * Represents the unary not/negation
	 */
	NEG("¬", 1),
	AND("∧", 2),
	OR("∨", 2);
	
	Operator(String displayText, int numArguments)
	{
		DISPLAY_TEXT = displayText;
		NUM_ARGUMENTS = numArguments;
	}
	/**
	 * A Unicode representation of the symbol or text to be displayed when pretty-printing
	 * the operator
	 */
	public final String DISPLAY_TEXT;
	public final int NUM_ARGUMENTS;
}