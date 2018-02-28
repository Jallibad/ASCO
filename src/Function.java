import java.util.ArrayList;
import java.util.List;

public class Function extends Expression
{
	public Operator operator;
	private List<Expression> terms;
	
	public Function(Operator operator, List<Expression> terms)
	{
		if (terms.size() != operator.NUM_ARGUMENTS)
		{
			// TODO throw error and shit
		}
		this.operator = operator;
		this.terms = new ArrayList<Expression>(terms);
	}
	
	@Override
	public String toString()
	{
		String ans = operator.toString();
		return ans;
	}

	@Override
	public List<Literal> getVariables()
	{
		List<Literal> ans = new ArrayList<Literal>();
		for (Expression e : terms)
			ans.addAll(e.getVariables());
		return ans;
	}

	@Override
	public List<TruthAssignment> getTruthAssignments()
	{
		return null;
	}
}