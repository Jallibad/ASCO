package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
	 * @throws NotAnOperatorException 
	 */
	private static void operatorsToEnglish(StringBuilder exp) throws NotAnOperatorException
	{
		String ans = exp.toString();
		for (Operator op : Operator.values())
			ans = ans.replaceAll(op.DISPLAY_TEXT, op.name());
		if (ans.matches(".*[^\\p{Alpha}\\s\\(\\)].*"))
			throw new NotAnOperatorException("Something's not an operator"); // TODO detailed error message
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
	 * Unwraps parentheses from Literals. (A) -> A
	 * @param exp the expression to unwrap
	 */
	private static void unwrapLiterals(StringBuilder exp)
	{
		StringBuffer buffer = new StringBuffer();
		Matcher m = Pattern.compile("\\(([a-zA-Z]+)\\)").matcher(exp);
		while (m.find())
			m.appendReplacement(buffer, "$1");
		m.appendTail(buffer);
		
		exp.setLength(0);
		exp.append(buffer);
	}
	
	/**
	 * Removes leading and trailing, as well as repeated whitespace
	 * @param exp the expression to trim
	 */
	private static void removeExtraSpaces(StringBuilder exp)
	{
		StringBuffer buffer = new StringBuffer();
		Matcher trimMatcher = Pattern.compile("^\\s|\\s+(?=\\s)|\\s$").matcher(exp);
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
			if (wrapStartPos == -1)
				continue;
			int bracketNum = 0;
			int i;
			for (i = wrapStartPos+1; i < ans.length()-1; ++i)
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
			if (i < ans.length()-1)
				LOGGER.severe("got here");
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
	
	private static void insertClosing(StringBuilder ans, int i)
	{
		int bracketCount = 0;
		for (int r=i+1; r < ans.length(); ++r)
		{
			if (ans.charAt(r) == '(')
				++bracketCount;
			else if ((ans.charAt(r) == ')' && --bracketCount == -1) || Character.isAlphabetic(ans.charAt(r)))
			{
				ans.insert(r+1, ')');
				break;
			}
		}
	}
	
	private static void parenthesizeNegations(StringBuilder ans)
	{
		for (int i = 0; i < ans.length(); ++i)
			if (ans.charAt(i) == '¬' && (i == 0 || ans.charAt(i-1) != '('))
			{
				ans.insert(i, '(');
				ans.insert(i+2,' ');
				i+=3;
				//move forward until we find the second operator or same-level closed paren
				insertClosing(ans, i);
				continue; //move forward one space to make room for the new closed parenthesis
			}
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
		numMatchedParentheses(ans); // Check if expression has a valid number of parentheses
		if (ans.charAt(0) != '(')
			ans.append(")").insert(0, '(');
		
		//add parentheses around all negations
		parenthesizeNegations(ans);
		
		//parenthesize all position 1 operators
		for (int i = 0; i < ans.length(); ++i)
			if (charIsOperator(ans.charAt(i)) && operatorPosition(ans.charAt(i)) == 1)
			{
				moveBackToOpen(ans, i, true).ifPresent(r -> ans.insert(r, '('));
				insertClosing(ans, i+1);
				
				i+=1; //move forward one space to make room for the new open parenthesis
				//move forward until we find the second operator or same-level closed paren
				continue; //move forward one space to make room for the new closed parenthesis
			}	
		
		removeDoubleWrapParens(ans);
	}
	
	private static OptionalInt moveBackToOpen(StringBuilder exp, int i, boolean orOperator)
	{
		int bracketCount = 0;
		for (int r=i-1; r >= 0; --r)
		{
			if (exp.charAt(r) == ')')
				++bracketCount;
			else if ((exp.charAt(r) == '(' && --bracketCount < 0) || (orOperator && Character.isAlphabetic(exp.charAt(r))))
				return OptionalInt.of(r);
		}
		return OptionalInt.empty();
	}
	
	/**
	 * convert the input expression from prefix into infix
	 * @param exp the expression to convert
	 * @return the specified expression in infix form
	 */
	private static void infixToPrefix(StringBuilder exp)
	{
		for (int i = 0; i < exp.length(); ++i)
			if (charIsOperator(exp.charAt(i)))
			{
				int r = moveBackToOpen(exp, i, false).getAsInt();
				//move operator to just ahead of same-level opening paren
				exp.insert(r+1, exp.charAt(i));
				//remove operator from old location
				exp.deleteCharAt(i+1);
				//add a space before the second operator, if not already present
				if (exp.charAt(i+1) != ' ')
					exp.insert(i+1,' ');
				//add a space before the first operator, if not already present
				if (exp.charAt(r+2) != ' ')
					exp.insert(r+2,' ');
			}
	}
	
	/**
	 * Parses text for an expression
	 * @param exp the String to convert to an Expression
	 * @return an Expression object that is equivalent to the specified expression
	 */
	public static Expression parse(String exp) throws MalformedExpressionException
	{
		if (exp.isEmpty())
			throw new MalformedExpressionException("The empty string \"\" is not a valid expression");
		StringBuilder ans = new StringBuilder(exp);
		sanitizeInput(ans);
		LOGGER.fine(() -> "sanitized: " + ans);
		infixToPrefix(ans);
		LOGGER.fine(() -> "sanitized prefix: " + ans);
		operatorsToEnglish(ans);
		LOGGER.fine(() -> "english prefix sanitized: " + ans);
		unwrapLiterals(ans);
		removeExtraSpaces(ans);
		LOGGER.fine(() -> "post sanitization: " + ans);
		removeDoubleWrapParens(ans);
		return createSafe(ans.toString());
	}
	
	/**
	 * Takes a String representing FOL using prefix notation (ie. "(NEG (AND A B))")
	 * @param exp the String to convert to an Expression
	 * @return An Expression object that is equivalent to the specified expression
	 */
	public static Expression createSafe(String exp) throws MalformedExpressionException
	{
		if (exp.isEmpty())
			throw new MalformedExpressionException("The expression is empty");
		if (exp.charAt(0) != '(') // Every Function is wrapped in ()
			return new Literal(exp);
		int endIndex = exp.indexOf(' ');
		String currOperator = exp.substring(1,endIndex);
		if (Stream.of(Operator.values()).allMatch(o -> !o.name().equals(currOperator)))
			throw new NotAnOperatorException(currOperator);
		Operator op = Operator.valueOf(currOperator);
		List<Expression> terms = new ArrayList<>();
		int numParentheses = 0;
		for (int i=++endIndex; i<exp.length(); ++i)
		{
			if (numParentheses == 0)
			{
				if (exp.charAt(i) == ' ')
				{
					terms.add(createSafe(exp.substring(endIndex,i)));
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
		terms.add(createSafe(exp.substring(endIndex,exp.length()-1)));
		return new Function(op, terms);
	}
	
	// TODO add error checking/handling
	/**
	 * Takes a String representing FOL using prefix notation (ie. "(NEG (AND A B))")
	 * Does not do any error checking or handling
	 * @param exp the String to convert to an Expression
	 * @return An Expression object that is equivalent to the specified expression
	 */
	public static Expression create(String exp)
	{
		try
		{
			return createSafe(exp);
		}
		catch (MalformedExpressionException e)
		{
			throw new Error(e.getMessage());
		}
	}
}