package logic.transform;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import logic.Expression;

public class Simplify implements Transform
{
	private static final long serialVersionUID = -5938246885231590898L;
	private static final List<Transform> strategies = new ArrayList<>();
	
	private Simplify()
	{
		
	}

	@Override
	public Expression transform(Expression e)
	{
		// TODO implement
		return strategies.stream()
				.map(strat -> strat.transform(e))
				.min(Comparator.comparing(simplified -> e.complexity()-simplified.complexity()))
				.orElse(e);
	}

	@Override
	public TransformSteps transformWithSteps(Expression orig)
	{
		// TODO Auto-generated method stub
		return null;
	}
}