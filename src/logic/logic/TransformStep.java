package logic;

public class TransformStep
{
	public final Transform step;
	public final Expression before;
	public final Expression after;
	
	public TransformStep(Transform step, Expression before, Expression after)
	{
		this.step = step;
		this.before = before;
		this.after = after;
	}
}