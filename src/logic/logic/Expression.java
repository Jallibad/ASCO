package logic;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An abstract class that represents a FOL statement.
 * Subclassed by Literal and Function 
 * @author Jallibad
 *
 */
public abstract class Expression
{
	public static void main(String[] args)
	{
		Expression e1 = create("(NEG (OR A (NEG C)))");
		System.out.println(NormalForm.NEGATION.transform(e1));
	}
	
	// TODO add error checking/handling
	/**
	 * Takes a String representing FOL using prefix notation (ie. "(NEG (AND A B))").
	 * Currently does not do any error checking or handling.
	 * @param exp the String to convert to an Expression
	 * @return An Expression object that is equivalent to the parameter
	 */
	public static Expression create(String exp)
	{
		if (exp.charAt(0) != '(') // Every Function is wrapped in ()
			return new Literal(exp);
		int endIndex = exp.indexOf(' ');
		Operator op = Operator.valueOf(exp.substring(1, endIndex));
		List<Expression> terms = new ArrayList<Expression>();
		int numParentheses = 0;
		for (int i=++endIndex; i<exp.length(); ++i)
		{
			if (numParentheses == 0)
			{
				if (exp.charAt(i) == ' ')
				{
					terms.add(create(exp.substring(endIndex,i)));
					endIndex = i+1;
				}
				else if (exp.charAt(i) == '(')
					numParentheses++;
			}
			else if (exp.charAt(i) == '(')
				numParentheses++;
			else if (exp.charAt(i) == ')')
				numParentheses--;			
		}
		terms.add(create(exp.substring(endIndex,exp.length()-1)));
		return new Function(op, terms);
	}
	
	//This only covers basic shit
	public Expression transformCNF(Expression e){
		if(e instanceof Literal)//is a class that inherits from Literal
		{
			return e;
		}
		//Check if the expression is an instance of Function
		else if(e instanceof Function)
		{
			//Cast the expression to a function so that we can access it's variables
			Function curr = (Function)e; 
			
			if(curr.operator == Operator.NEG) 
			{
				
				if(curr.getTerm(0) instanceof Literal)
				{
					return curr;
				}
				else if(curr.getTerm(0) instanceof Function)
				{
					Function innerCurr = (Function)curr.getTerm(0);
					if(innerCurr.operator == Operator.NEG)
					{
						InferenceRule.DOUBLE_NEGATION.transform(innerCurr);
						return innerCurr;
					}
				
					else if(innerCurr.operator == Operator.OR)
					{
						
					}
				}
			}
		}
		return e;
	}
	
	/**
	 * Creates a Set containing each variable that occurs in the expression
	 * @return the specified Set
	 */
	public abstract Set<Literal> getVariables();
	public abstract List<TruthAssignment> getTruthAssignments();
	
	// TODO rewrite this JavaDoc to be more correct
	/**
	 * Checks if the Expressions have the same syntactic form, ignoring Literal names.
	 * The argument specifies the minimum complexity
	 * "A".matches("B"), and "(AND A B)".matches("(AND P Q)"), but !"(AND A B)".matches("(OR A B)")
	 * @param pattern the Expression to match against
	 * @return True if the Expressions match, false otherwise
	 */
	public abstract boolean matches(Expression pattern);
	public boolean matches(String pattern) // TODO write JavaDoc for this I'm lazy
	{
		return matches(Expression.create(pattern));
	}
	public abstract Map<Literal,Expression> fillMatches(Expression e);
}