import java.util.ArrayList;
import java.util.List;

public class Literal extends Expression
{
	public final String VARIABLE_NAME;
	
	public Literal(String variableName)
	{
		VARIABLE_NAME = variableName;
	}
	
	@Override
	public List<Literal> getVariables()
	{
		List<Literal> temp = new ArrayList<Literal>();
		temp.add(this);
		return temp;
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
}