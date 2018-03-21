package logic;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a single variable such as "A" or "B".
 * @author Jallibad
 *
 */
public class Literal extends Expression
{
	public final String VARIABLE_NAME;
	
	public Literal(String variableName)
	{
		VARIABLE_NAME = variableName;
	}
	
	@Override
	public Set<Literal> getVariables()
	{
		return Collections.singleton(this);
	}

	@Override
	public List<TruthAssignment> getTruthAssignments()
	{
		return null; // TODO Implement
	}
	
	@Override
	public String toString()
	{
		return VARIABLE_NAME;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Literal)
			return VARIABLE_NAME.equals(((Literal) o).VARIABLE_NAME);
		return false;
	}
	@Override
	public int hashCode()
	{
		return VARIABLE_NAME.hashCode();
	}

	@Override
	public boolean matches(Expression e)
	{
		return true; // A Literal matches any Expression
	}

	@Override
	public Map<Literal, Expression> fillMatches(Expression e)
	{
		Map<Literal, Expression> ans = new HashMap<Literal, Expression>();
		ans.put(this, e);
		return ans;
	}

	@Override
	public String prettyPrint()
	{
		return VARIABLE_NAME;
	}

	@Override
	Operator getOperator()
	{
		return null;
	}
}