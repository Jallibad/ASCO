public enum Operator
{
	NEG("¬", 1),
	AND("∧", 2),
	OR("∨", 2);
	
	Operator(String displayText, int numArguments)
	{
		DISPLAY_TEXT = displayText;
		NUM_ARGUMENTS = numArguments;
	}
	public final String DISPLAY_TEXT;
	public final int NUM_ARGUMENTS;
}