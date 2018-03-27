package logic;

public class TransformStep
{
	public final Expression before;
	public final Transform step;
	public final Expression after;
	
	public TransformStep(Expression before, Transform step, Expression after)
	{
		this.step = step;
		this.before = before;
		this.after = after;
	}
	
	@Override
	public String toString()
	{
		return "";
	}
}