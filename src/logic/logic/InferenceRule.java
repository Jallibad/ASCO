package logic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum InferenceRule
{
	// TODO change to using clearer Function construction syntax
	DE_MORGANS("(NEG (OR P Q))", "(AND (NEG P) (NEG Q))");
	
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
		Map<Literal,Expression> mapping = new HashMap<Literal,Expression>();
		fillMapping(mapping, left, orig);
		return transform(mapping, right);
	}
	
	private void fillMapping(Map<Literal,Expression> m, Expression orig, Expression from)
	{
		if (orig instanceof Literal)
		{
			m.put((Literal) orig, from);
		}
		else
		{
			// TODO check that operators are the same
			List<Expression> origTerms = ((Function) orig).getTerms();
			List<Expression> fromTerms = ((Function) from).getTerms();
			for (int i=0; i<origTerms.size(); ++i)
				fillMapping(m, origTerms.get(i), fromTerms.get(i));
		}
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