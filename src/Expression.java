import java.util.List;

public abstract class Expression
{
	public abstract List<Literal> getVariables();
	public abstract List<TruthAssignment> getTruthAssignments();
}