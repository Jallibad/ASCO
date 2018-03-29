package logic;

public interface BiDirectionalTransform extends Transform
{
	public Expression left();
	public Expression right();
	default public Expression transformLeft(Expression orig)
	{
		return left().matches(orig) ? Transform.transform(left().fillMatches(orig), right()) : orig;
	}
	
	default public TransformSteps transformLeftWithSteps(Expression orig)
	{
		TransformSteps ans = new TransformSteps(orig);
		ans.addStep(this); // TODO this might not be technically correct
		return ans;
	}
	
	default public void transformLeftWithSteps(TransformSteps steps)
	{
		if (left().matches(steps.result()))
			steps.addStep(this);
	}
	
	default public Expression transformRight(Expression orig)
	{
		return right().matches(orig) ? Transform.transform(right().fillMatches(orig), left()) : orig;
	}
	
	default public TransformSteps transformRightWithSteps(Expression orig)
	{
		TransformSteps ans = new TransformSteps(orig);
		ans.addStep(this); // TODO this might not be technically correct
		return ans;
	}
	
	default public void transformRightWithSteps(TransformSteps steps)
	{
		if (right().matches(steps.result()))
			steps.addStep(this);
	}
	
	default public boolean inLeft(Expression orig)
	{
		return left().matches(orig);
	}
	
	default public boolean inRight(Expression orig)
	{
		return right().matches(orig);
	}
}