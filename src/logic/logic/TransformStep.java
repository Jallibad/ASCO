package logic;

public class TransformStep
{
	Transform step;
	Expression before;
	Expression after;
	
	public TransformStep(Transform step, Expression before, Expression after)
	{
		this.step = step;
		this.before = before;
		this.after = after;
	}
}