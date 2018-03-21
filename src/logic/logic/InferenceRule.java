package logic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum InferenceRule implements Transform
{
	DE_MORGANS_OR("(NEG (OR P Q))", "(AND (NEG P) (NEG Q))"),
	DE_MORGANS_AND("(NEG (AND P Q))", "(OR (NEG P) (NEG Q))"),
	OR_DISTRIBUTION("(OR P (AND Q R))", "(AND (OR P Q) (OR P R))"),
	AND_DISTRIBUTION("(AND P (OR Q R))", "(OR (AND P Q) (AND P R))"),
	DOUBLE_NEGATION("(NEG (NEG P))", "P");
	
	InferenceRule(String left, String right)
	{
		this.left = Expression.create(left);
		this.right = Expression.create(right);
	}
	
	/**
	 * 
	 * @param orig
	 * @return
	 */
	public Expression transform(Expression orig)
	{
		if (left.matches(orig))
			return transform(left.fillMatches(orig), right);
		else if (right.matches(orig))
			return transform(right.fillMatches(orig), left);
		System.out.println("An inference rule couldn't be successfully applied");
		return null; // TODO this could be a terrible idea
	}
	
	private Expression transform(Map<Literal,Expression> mapping, Expression e)
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
	
	final Expression left;
	final Expression right;
}