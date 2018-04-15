package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.malformedexpression.MalformedExpressionException;
import logic.malformedexpression.NotAnOperatorException;
import logic.malformedexpression.UnmatchedParenthesesException;

public final class ExpParser
{
	private static final Logger LOGGER = Logger.getLogger(Expression.class.getName());
	
	/**
	 * Private constructor to prevent instantiation
	 */
	private ExpParser()
	{
		
	}
	
	/**
	 * convert all operator characters to the operator name
	 * @param exp the expression in which to convert all operators
	 * @return the specified expression string with all operator characters replaced with the operator name
	 */
	private static void operatorsToEnglish(StringBuilder exp)
	{
		String ans = exp.toString();
		for (Operator op : Operator.values())
			ans = ans.replaceAll(op.DISPLAY_TEXT, op.name());
		exp.setLength(0);
		exp.append(ans);
	}
	
	/**
	 * determine whether or not the specified character is an operator
	 * @param c the character to check
	 * @return whether c is an operator (true) or not (false)
	 */
	private static boolean charIsOperator(char c)
	{
		for (Operator o : Operator.values())
			if (o.DISPLAY_TEXT.charAt(0) == c)
				return true;
		return false;
	}
	
	/**
	 * sanitize the results of infix->prefix parse, removing wrapped literals
	 * @param exp the expression to sanitize
	 * @return the sanitized expression
	 */
	private static void postSanitize(StringBuilder exp)
	{
		//remove wrapped literals
		StringBuffer buffer = new StringBuffer();
		Pattern p = Pattern.compile("\\(([a-zA-Z]+)\\)");
		Matcher m = p.matcher(exp);
		while (m.find())
			m.appendReplacement(buffer, "$1");
		m.appendTail(buffer);
		
		//remove extra spaces
		Matcher trimMatcher = Pattern.compile("^\\s|\\s+(?=\\s)|\\s$").matcher(buffer.toString());
		buffer.setLength(0);
		while (trimMatcher.find())
			trimMatcher.appendReplacement(buffer, "");
		trimMatcher.appendTail(buffer);
		
		exp.setLength(0);
		exp.append(buffer);
	}
	
	/**
	 * remove sets of parentheses until the specified expression is no longer double-wrapped
	 * @param exp the expression from which to remove sets of parentheses
	 * @return the specified expression wrapped in at most one set of parentheses
	 */
	private static void removeDoubleWrapParens(StringBuilder ans)
	{
		boolean doubleWrapped = true;
		while (doubleWrapped)
		{
			doubleWrapped = false;
			int wrapStartPos = ans.indexOf("((");
			if (wrapStartPos != -1)
			{
				int bracketNum = 0;
				for (int i = wrapStartPos+1; i < ans.length()-1; ++i)
				{
					if (ans.charAt(i) == '(')
						++bracketNum;
					else if (ans.substring(i, i+2).equals("))") && --bracketNum == 0)
					{
						doubleWrapped = true;
						ans.deleteCharAt(i);
						ans.deleteCharAt(wrapStartPos);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * determine the operator position of the specified character, or -1 if the character is not an operator
	 * @param c the character to check for operator position
	 * @return the operator position of the specified character, or -1 if the character is not an operator
	 */
	private static int operatorPosition(char c) throws NotAnOperatorException
	{
		for (Operator o : Operator.values())
			if (o.DISPLAY_TEXT.charAt(0) == c)
				return o.SYMBOL_POSITION;
		throw new NotAnOperatorException(String.valueOf(c)); // TODO add error message
	}
	
	/**
	 * Counts the number of '(' or ')' in a string.
	 * @param exp the string to count parentheses in
	 * @return
	 * @throws UnmatchedParenthesesException
	 */
	private static int numMatchedParentheses(StringBuilder exp) throws UnmatchedParenthesesException
	{
		int numOpen = 0;
		int numClosed = 0;
		for (char i : exp.toString().toCharArray())
		{
			if (i == '(')
				numOpen++;
			else if (i == ')')
				numClosed++;
			if (numClosed > numOpen)
				throw new UnmatchedParenthesesException(exp.toString(), 0); // TODO indicate where in string the parens are
		}
		if (numOpen != numClosed)
			throw new UnmatchedParenthesesException(exp.toString(), exp.length());
		return numOpen;
	}
	
	/**
	 * sanitize the input expression in order to prepare it for parsing
	 * @param exp the string to sanitize
	 * @return the specified string sanitized for expression parsing
	 * @throws NotAnOperatorException 
	 * @throws UnmatchedParenthesesException 
	 */
	private static void sanitizeInput(StringBuilder ans) throws NotAnOperatorException, UnmatchedParenthesesException
	{
		//add parentheses around the expression if not already present
		numMatchedParentheses(ans);
		if (ans.charAt(0) != '(')
			ans.append(")").insert(0, '(');
		
//		System.out.println("before paren neg: " + exp);
		//add parentheses around all negations
		for (int i = 0; i < ans.length(); ++i)
		{
			if (ans.charAt(i) == '�' && (i == 0 || ans.charAt(i-1) != '('))
			{
				ans.insert(i, '(');
				ans.insert(i+2,' ');
				i+=3;
				//move forward until we find the second operator or same-level closed paren
				int r = i;
				int bracketCount = 0;
				while (++r < ans.length())
				{
					if (ans.charAt(r) == '(')
						++bracketCount;
					else if (ans.charAt(r) == ')')
					{
						if (--bracketCount == -1)
						{
							ans.insert(++r, ')');
							break;
						}
					}
					else if (Character.isAlphabetic(ans.charAt(r)))
					{
						ans.insert(++r, ')');
						break;
					}
				}
				i+=1; //move forward one space to make room for the new closed parenthesis
			}
		}
//		System.out.println("after paren neg: " + exp);
		
		//parenthesize all position 1 operators
		for (int i = 0; i < ans.length(); ++i)
		{
			if (charIsOperator(ans.charAt(i)) && operatorPosition(ans.charAt(i)) == 1)
			{
				//move back until we find the first operator or same-level open paren
				int r = i;
				int bracketCount = 0;
				while (--r >= 0)
				{
					if (ans.charAt(r) == ')')
						++bracketCount;
					else if (ans.charAt(r) == '(')
					{
						if (--bracketCount == 0)
						{
							ans.insert(r, '(');
							break;
						}
					}
					else if (Character.isAlphabetic(ans.charAt(r)))
					{
						ans.insert(r, '(');
						break;
					}
				}
				i+=1; //move forward one space to make room for the new open parenthesis
				//move forward until we find the second operator or same-level closed paren
				r = i;
				bracketCount = 0;
				while (++r < ans.length())
				{
					if (ans.charAt(r) == '(')
						++bracketCount;
					else if (ans.charAt(r) == ')')
					{
						if (--bracketCount == 0)
						{
							ans.insert(++r, ')');
							break;
						}
					}
					else if (Character.isAlphabetic(ans.charAt(r)))
					{
						ans.insert(++r, ')');
						break;
					}
				}
				i+=1; //move forward one space to make room for the new closed parenthesis
			}
		}		
		
		//System.out.println("before wrap: " + exp);
		removeDoubleWrapParens(ans);
	}
	
	/**
	 * convert the input expression from prefix into infix
	 * @param exp the expression to convert
	 * @return the specified expression in infix form
	 */
	private static StringBuilder infixToPrefix2(StringBuilder exp)
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
						exp.insert(++r, exp.charAt(i));
						//remove operator from old location
						exp.deleteCharAt(++i);
						//add a space before the second operator, if not already present
						if (exp.charAt(i) != ' ')
							exp.insert(i,' ');
						//decrement here so we don't skip over the next character
						i-=1;
						//add a space before the first operator, if not already present
						if (exp.charAt(++r) != ' ')
							exp.insert(r,' ');
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
			StringBuilder ans = new StringBuilder(exp);
			sanitizeInput(ans);
			LOGGER.fine(() -> "sanitized: " + ans);
			infixToPrefix2(ans);
			LOGGER.fine(() -> "sanitized prefix: " + ans);
			operatorsToEnglish(ans);
			LOGGER.fine(() -> "english prefix sanitized: " + ans);
			postSanitize(ans);
			LOGGER.fine(() -> "post sanitization: " + ans);
			removeDoubleWrapParens(ans);
			return create(ans.toString());
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
		List<Expression> terms = new ArrayList<>();
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
		return Function.constructUnsafe(op, terms);
	}
}