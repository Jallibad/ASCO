package logic.malformedexpression;

public class NotAnOperatorException extends MalformedExpressionException
{
	public final String operator;
	
	private static final long serialVersionUID = -8575123240896718843L;
	public NotAnOperatorException(String operator)
	{
		this.operator = operator;
	}
}