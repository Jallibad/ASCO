package logic;

public interface Transform
{
	public Expression transform(Expression orig);
	public TransformSteps transformWithSteps(Expression orig);
}