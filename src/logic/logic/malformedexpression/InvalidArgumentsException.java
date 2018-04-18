package logic.malformedexpression;

public class InvalidArgumentsException extends MalformedExpressionException
{
	private static final long serialVersionUID = -208040771676620152L;
	
	public InvalidArgumentsException(String s)
	{
		super(s);
	}
}