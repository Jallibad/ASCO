package logic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Function class represent functions such as "(NEG A)" and "(AND A B)".
 * Functions are represented as an Operator and an ordered List of terms, with terms[0] being the first argument.
 * The Expression::create function is a simpler method of constructing Function instances, especially nested
 * ones rather than using the constructors here.
 * @author Jallibad
 *
 */
public class Function extends Expression
{
	public final Operator operator;
	/**
	 * An ordered List of the arguments to the Function, with term[0] being the first argument.
	 * For instance with "(AND A B)" the list would contain [A, B].
	 */
	private List<Expression> terms;
	
	/**
	 * The basic constructor for the Function class.  Checks that the number
	 * terms matches up with the expected number of arguments.
	 * @param operator the operator for the new Function
	 * @param terms a List of the terms, a copy is made to avoid rep exposure
	 */
	public Function(Operator operator, List<Expression> terms)
	{
		if (terms.size() != operator.NUM_ARGUMENTS)
		{
			throw new Error(String.format
			(
				"Operator \"%s\" expects %d arguments, %d were provided",
				operator,
				operator.NUM_ARGUMENTS,
				terms.size()
			));
		}
		this.operator = operator;
		this.terms = new ArrayList<Expression>(terms);
	}

	/**
	 * A wrapper constructor to make constructing Functions inline somewhat simpler.
	 * @param operator the Operator to be used in the Function
	 * @param terms an Expression[] consisting of the terms in order.  Uses variadic arguments.
	 */
	public Function(Operator operator, Expression... terms)
	{
		this(operator, Arrays.asList(terms));
	}
	
	/**
	 * A wrapper constructor to make constructing Functions inline somewhat simpler.
	 * Each String in the second argument is parsed using Expression::create.
	 * @param operator the Operator to be used in the Function
	 * @param terms a String[] consisting of the terms in order.  Uses variadic arguments.
	 */
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