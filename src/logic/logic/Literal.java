package logic;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
}