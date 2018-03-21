package logic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	
	/**
	 * Getter method for the the terms or arguments to this function.
	 * Terms are in sorted order with getTerms().get(0) being the first argument.
	 * @return The List of terms, a copy is made to avoid rep exposure
	 */
	public List<Expression> getTerms()
	{
		return new ArrayList<Expression>(terms); // Copy new list to avoid representation exposure
	}
	
	/**
	 * Gets the term[i] element of the terms List.
	 * It's slightly more efficient to use this function if the
	 * full list of terms isn't needed.
	 * @param i the index of the term, 0 is the first
	 * @return The i'th term of the terms List
	 */
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
		if (!(o instanceof Function))
			return false;
		Function other = (Function) o;
		if (operator != other.operator)
			return false;
		for (int i=0; i<terms.size(); ++i)
			if (!terms.get(i).equals(other.getTerm(i)))
				return false;
		return true;
	}

	@Override
	public boolean matches(Expression e)
	{
		if (!(e instanceof Function))
			return false;
		Function other = (Function) e;
		if (operator != other.operator)
			return false;
		for (int i=0; i<terms.size(); ++i)
			if (!terms.get(i).matches(other.getTerm(i)))
				return false;
		return true;
	}

	@Override
	public Map<Literal, Expression> fillMatches(Expression e)
	{
		if (!(e instanceof Function))
			return null;
		Function other = (Function) e;
		if (operator != other.operator)
			return null;
		Map<Literal, Expression> ans = new HashMap<Literal, Expression>();
		for (int i=0; i<terms.size(); ++i)
			ans.putAll(terms.get(i).fillMatches(other.getTerm(i)));
		return ans;
	}

	@Override
	public String prettyPrint()
	{
		if (operator == Operator.NEG)
		{
			if (terms.get(0) instanceof Function)
				return operator.DISPLAY_TEXT+"("+terms.get(0).prettyPrint()+")";
			else
				return operator.DISPLAY_TEXT+terms.get(0).prettyPrint();
		}
		
		String ans = "";
		for (int i=0; i<terms.size()+1; ++i)
		{
			if (i == operator.SYMBOL_POSITION)
			{
				ans += " "+operator.DISPLAY_TEXT;
				continue;
			}
			Expression currTerm = terms.get(i<operator.SYMBOL_POSITION ? i : i-1); // Account for inserting the operator
			if (currTerm instanceof Literal || currTerm.getOperator() == Operator.NEG)
				ans += " "+currTerm.prettyPrint();
			else
				ans += " ("+currTerm.prettyPrint()+")";
		}
		return ans.substring(1);
	}

	@Override
	Operator getOperator()
	{
		return operator;
	}

	@Override
	public int complexity()
	{
		// Sum of complexity of all terms + 1 for the operator
		return terms.stream().collect(Collectors.summingInt(Expression::complexity))+1;
	}
}