package logic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * An abstract class that represents a FOL statement.
 * Subclassed by Literal and Function 
 * @author Jallibad
 *
 */
public abstract class Expression implements Serializable
{
	private static final long serialVersionUID = -1298428615072603639L;

	public static void main(String[] args)
	{
		Expression e1 = create("(AND A (OR B (AND D E)))");
		Expression e2 = create("(AND A (AND (OR B D) (OR B E)))");
		System.out.println(e1.proveEquivalence(e2));
		try
		{
			System.out.println(parse("A"));
		}
		catch (MalformedExpressionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * return a new string from the specified string with the desired character added
	 * @param s the original string
	 * @param loc the location of the character before which to add
	 * @param c the character to write to string position loc
	 * @return a new string equal to s with character number loc preceded by c
	 */
	private static String addChar(String s, int loc, char c)
	{
		return s.substring(0, loc) + c + s.substring(loc);
	}
	
	/**
	 * return a new string from the specified string with the desired character removed
	 * @param s the original string
	 * @param loc the location of the character to remove
	 * @return a new string equal to s with character number loc removed
	 */
	private static String removeChar(String s, int loc)
	{
		return s.substring(0,loc) + s.substring(loc+1);
	}
	
	/**
	 * convert all operator characters to the operator name
	 * @param exp the expression in which to convert all operators
	 * @return the specified expression string with all operator characters replaced with the operator name
	 */
	private static String operatorsToEnglish(String exp)
	{
		for (Operator op : Operator.values())
			exp = exp.replaceAll(op.DISPLAY_TEXT, op.name());
		return exp;
	}
	
	/**
	 * determine whether or not the specified character is an operator
	 * @param c the character to check
	 * @return whether c is an operator (true) or not (false)
	 */
	private static boolean charIsOperator(char c)
	{
		for (Operator o : Operator.values())
			if (o.DISPLAY_TEXT.charAt(0) == c) // TODO maybe match multiple characters to an operator?
				return true;
		return false;
	}
	
	/**
	 * sanitize the input expression in order to prepare it for parsing
	 * @param exp the string to sanitize
	 * @return the specified string sanitized for expression parsing
	 */
	private static String sanitizeInput(String exp)
	{
		//add parentheses around the expression if not already present
		boolean isWrapped = true;
		if (exp.charAt(0) != '(')
			isWrapped = false;
		else
		{
			int bracketNum = 1;
			for (int i = 1; i < exp.length(); ++i)
			{
				if (exp.charAt(i) == '(')
					++bracketNum;
				else if (exp.charAt(i) == ')' && --bracketNum == 0)
				{
					isWrapped &= i == exp.length()-1;
					break;
				}
			}
		}
		if (!isWrapped)
			exp = "("+exp+")";
		
//		System.out.println("before paren neg: " + exp);
		//add parentheses around all negations
		for (int i = 0; i < exp.length(); ++i)
		{
			if (exp.charAt(i) == '¬')
			{
				exp = addChar(exp,i,'(');
				i+=2;
				exp = addChar(exp,i,' ');
				i+=1;
				while (exp.charAt(i) != ' ' && exp.charAt(i) != ')')
					++i;
				exp = addChar(exp,i,')');
			}
		}
//		System.out.println("after paren neg: " + exp);
		
		//parenthesize all position 1 operators
		for (int i = 0; i < exp.length(); ++i)
		{
			if (charIsOperator(exp.charAt(i)))
			{
				//ignore non-position 1 operators
				boolean isPos1 = true;
				for (Operator o : Operator.values())
					if (o.DISPLAY_TEXT.charAt(0) == exp.charAt(i))
					{
						isPos1 &= o.SYMBOL_POSITION == 1;
						break;
					}
				if (!isPos1)
					continue;
				//move back until we find the first operator or same-level open paren
				int r = i;
				int bracketCount = 0;
				while (--r >= 0)
				{
					if (exp.charAt(r) == ')')
						++bracketCount;
					else if (exp.charAt(r) == '(')
					{
						if (--bracketCount == 0)
						{
							exp = addChar(exp, r, '(');
							break;
						}
					}
					else if (Character.isAlphabetic(exp.charAt(r)))
					{
						exp = addChar(exp,r, '(');
						break;
					}
				}
				i+=1; //move forward one space to make room for the new open parenthesis
				//move forward until we find the second operator or same-level closed paren
				r = i;
				bracketCount = 0;
				while (++r < exp.length())
				{
					if (exp.charAt(r) == '(')
						++bracketCount;
					else if (exp.charAt(r) == ')')
					{
						if (--bracketCount == 0)
						{
							exp = addChar(exp, ++r, ')');
							break;
						}
					}
					else if (Character.isAlphabetic(exp.charAt(r)))
					{
						exp = addChar(exp,++r, ')');
						break;
					}
				}
				i+=1; //move forward one space to make room for the new closed parenthesis
			}
		}		
		
		//System.out.println("before wrap: " + exp);
		//remove parentheses from expression while double-wrapped
		boolean doubleWrapped = true;
		while (doubleWrapped) {
			if (!(exp.charAt(0) == '(' && exp.charAt(1) == '('))
				doubleWrapped = false;
			else
			{
				int bracketNum = 2;
				for (int i = 2; i < exp.length(); ++i)
				{
					if (exp.charAt(i) == '(')
						++bracketNum;
					else if (exp.charAt(i) == ')' && --bracketNum == 1)
					{
						doubleWrapped &= i == exp.length()-2;
						break;
					}
				}
			}
			if (doubleWrapped)
			{
				exp = removeChar(exp,0);
				exp = removeChar(exp,exp.length()-1);
			}	
		}
		
		//System.out.println("after wrap: " + exp);
		
		return exp;
	}
	
	/**
	 * convert the input expression from prefix into infix
	 * @param exp the expression to convert
	 * @return the specified expression in infix form
	 */
	private static String infixToPrefix2(String exp)
	{
		//search for operators
		for (int i = 0; i < exp.length(); ++i)
		{
			//System.out.println(i);
			if (charIsOperator(exp.charAt(i)))
			{
				//found an operator; move it back to produce infix notation
				int bracketCount = 0;
				int r = i;
				while (--r >= 0)
				{
					if (exp.charAt(r) == ')')
						++bracketCount;
					else if (exp.charAt(r) == '(' && --bracketCount < 0)
					{
						//move operator to just ahead of same-level opening paren
						exp = addChar(exp,++r, exp.charAt(i));
						//remove operator from old location
						exp = removeChar(exp, ++i);
						//add a space before the second operator, if not already present
						if (exp.charAt(i) != ' ')
							exp = addChar(exp,i,' ');
						//decrement here so we don't skip over the next character
						i-=1;
						//add a space before the first operator, if not already present
						if (exp.charAt(++r) != ' ')
							exp = addChar(exp,r,' ');
						break;
					}
				}
			}
		}
		return exp;
	}
	
	/**
	 * Parses text for an expression
	 * @param exp the String to convert to an Expression
	 * @return an Expression object that is equivalent to the specified expression
	 */
	public static Expression parse(String exp) throws MalformedExpressionException
	{
		try
		{
			//System.out.println("sanitized: " + sanitizeInput(exp));
			String ans = infixToPrefix2(sanitizeInput(exp));
			//System.out.println("sanitized prefix: " + ans);
			//System.out.println("\""+ans+"\"");
			String ans2 = operatorsToEnglish(ans);
			//System.out.println("\""+ans2+"\"");
			System.out.println("englsh prefix sanitized: " + ans);
			return create(ans2);
		}
		catch (Exception e)
		{
			System.out.println(e);
			throw new MalformedExpressionException();
		}
	}
	
	// TODO add error checking/handling
	/**
	 * Takes a String representing FOL using prefix notation (ie. "(NEG (AND A B))")
	 * Currently does not do any error checking or handling
	 * @param exp the String to convert to an Expression
	 * @return An Expression object that is equivalent to the specified expression
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
	
	/**
	 * A version of the expression with proper infix notation and symbols
	 * @return a formatted String
	 */
	public abstract String prettyPrint();
	
	/**
	 * Helper function that returns null if expression is a Literal, the operator if it is a Function
	 * @return the operator
	 */
	abstract Operator getOperator();
	
	public abstract int complexity();
	
	/**
	 * Tests full equality, whether the two Expression have the exact same form.
	 * In other words, (AND A B) != (AND B A)
	 */
	@Override
	public abstract boolean equals(Object other);
	
	/**
	 * Checks whether two Expressions are simply logically equivalent, including commutativity and associativity
	 * @param other
	 * @return
	 */
	public abstract boolean simplyEquivalent(Expression other);
	
	public abstract Optional<TransformSteps> proveEquivalence(Expression other);
}