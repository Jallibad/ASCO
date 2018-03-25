package logic;

import java.util.List;

public interface Transform
{
	public Expression transform(Expression orig);
	public List<TransformStep> transformWithSteps(Expression orig);
}