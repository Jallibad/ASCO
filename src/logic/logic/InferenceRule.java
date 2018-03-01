package logic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum InferenceRule
{
	DE_MORGANS_OR("(NEG (OR P Q))", "(AND (NEG P) (NEG Q))"),
	DE_MORGANS_AND("(NEG (AND P Q))", "(OR (NEG P) (NEG Q))"),
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
		Map<Literal,Expression> mapping = new HashMap<Literal,Expression>();
		try
		{
			fillMapping(mapping, left, orig);
			return transform(mapping, right);
		}
		catch (InferenceMismatchException e)
		{
			return null; // TODO this could be an awful idea
		}
	}
	
	public void fillMapping(Map<Literal,Expression> m, Expression orig, Expression from) throws InferenceMismatchException
	{
		if (orig instanceof Literal)
		{
			m.put((Literal) orig, from);
		}
		else
		{
			if (((Function) orig).operator != ((Function) from).operator)
			{
				throw new InferenceMismatchException("TEST");
				// TODO handle case where operators are not the same
			}
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