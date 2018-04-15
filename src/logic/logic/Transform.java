package logic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An interface representing arbitrary transformations that can be applied to
 * Expressions, along with the InferenceRule steps applied to get the desired result
 * @author Jallibad
 *
 */
public interface Transform extends Serializable
{
	/**
	 * A simple transform that returns only the final result
	 * @param orig the expression to be transformed
	 * @return the resultant Expression
	 */
	public Expression transform(Expression orig);
	
	/**
	 * Transforms the given Expression, saving the intermediate steps along the way
	 * @param orig the expression to be transformed
	 * @return a TransformSteps object containing each intermediate InferenceRule
	 */
	public TransformSteps transformWithSteps(Expression orig);
	
	public static Expression transform(Map<Literal,Expression> mapping, Expression e)
	{
		if (e instanceof Literal)
		{
			if (mapping.containsKey(e))
				return mapping.get(e);
			else
			{
				// TODO throw error and shit
				throw new Error();
			}
		}
		else if (e instanceof Function)
		{
			Function curr = (Function) e;
			List<Expression> terms = curr.getTerms().stream().map(x -> transform(mapping,x)).collect(Collectors.toList());
			return new Function(curr.operator, terms);
		}
		else
		{
			// TODO throw error and shit
			return null;
		}
	}
}