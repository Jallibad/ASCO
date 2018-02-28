package logic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
	
	public Function(Operator operator, Expression... terms)
	{
		this(operator, Arrays.asList(terms));
	}
	
	public Function(Operator operator, String... terms)
	{
		this(operator, Arrays.stream(terms).map(Literal::new).collect(Collectors.toList()));
	}
	
	public List<Expression> getTerms()
	{
		return new ArrayList<Expression>(terms); // Copy new list to avoid representation exposure
	}
	
	public Expression getTerm(int i)
	{
		return terms.get(i);
	}
	
	@Override
	public String toString()
	{
		String ans = operator.toString();
		for (Expression e : terms)
			ans += " "+e;
		return "("+ans+")";
	}

	@Override
	public Set<Literal> getVariables()
	{
		Set<Literal> ans = new HashSet<Literal>();
		for (Expression e : terms)
			ans.addAll(e.getVariables());
		return ans;
	}

	@Override
	public List<TruthAssignment> getTruthAssignments()
	{
		return null;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Function)
		{
			Function f2 = (Function) o;
			if (!operator.equals(f2.operator))
				return false;
			for (int i=0; i<terms.size(); ++i)
				if (!terms.get(i).equals(f2.getTerm(i)))
					return false;
			return true;
		}
		return false;
	}
}