package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.malformedexpression.InvalidArgumentsException;
import logic.malformedexpression.MalformedExpressionException;
import logic.malformedexpression.NotAnOperatorException;
import logic.malformedexpression.UnmatchedParenthesesException;

public final class ExpParser
{
	private static final Logger LOGGER = Logger.getLogger(ExpParser.class.getName());
	
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
			ans = ans.replaceAll(op.DISPLAY_TEXT, " "+op.name()+" ");
		if (ans.matches(".*[^\\p{Alpha}\\s\\(\\)].*"))
			throw new NotAnOperatorException("Something's not an operator"); // TODO detailed error message
		exp.setLength(0);
		exp.append(ans);
		LOGGER.fine(exp.toString());
	}
	
	/**
	 * Removes leading and trailing, as well as repeated whitespace
	 * @param exp the expression to trim
	 */
	private static void removeExtraSpaces(StringBuilder exp)
	{
		StringBuffer buffer = new StringBuffer();
		Matcher trimMatcher = Pattern.compile("^\\s+|\\s+$|\\s+(?=\\s)|(?<=\\()\\s+|\\s+(?=\\))").matcher(exp);
		buffer.setLength(0);
		while (trimMatcher.find())
			trimMatcher.appendReplacement(buffer, "");
		trimMatcher.appendTail(buffer);
		
		exp.setLength(0);
		exp.append(buffer);
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
	 * Parses text for an expression
	 * @param exp the String to convert to an Expression
	 * @return an Expression object that is equivalent to the specified expression
	 */
	public static Expression parse(String exp) throws MalformedExpressionException
	{	
		StringBuilder ans = new StringBuilder(exp);
		numMatchedParentheses(ans);
		operatorsToEnglish(ans);
		removeExtraSpaces(ans);
		return readTerm(ans);
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
			return parse(exp);
		}
		catch (MalformedExpressionException e)
		{
			throw new Error(e.getMessage());
		}
	}
	
	private static Expression readTerm(StringBuilder exp) throws MalformedExpressionException
	{
		char firstChar = exp.charAt(0);
		if (firstChar == '(' && findNextParen(exp) == exp.length()-1)
		{
			int closeParen = findNextParen(exp); // Find the matching close paren
			Expression term = readTerm(new StringBuilder(exp.substring(1, closeParen)));
			exp.delete(0, closeParen+1);
			return term;
		}
		else if (exp.length() > 4 && exp.substring(0, 4).equals("NEG "))
		{
			exp.delete(0, 4); // Remove "NEG "
			return new Function(Operator.NEG, readTerm(exp));
		}
		else
		{
			List<StringBuilder> terms = getSubTerms(exp);
			// If there's one term in the list it should be a literal
			if (terms.size() == 1)
				return new Literal(terms.get(0).toString());
			
			moveOperatorToPrefix(terms);
			
			// Streams and lambdas won't work here because we want want exceptions to bubble up
			List<Expression> ansTerms = new ArrayList<>();
			for (int i=1; i<terms.size(); ++i)
				ansTerms.add(readTerm(terms.get(i)));
			return new Function(Operator.valueOf(terms.get(0).toString()), ansTerms);
		}
	}
	
	/**
	 * Gets a list of the high level terms in the given string.  A high level term is
	 * one that is not nested inside another expression.
	 * @param exp the string to pull terms from
	 * @return a list of the high level terms in the string.
	 */
	private static List<StringBuilder> getSubTerms(StringBuilder exp)
	{
		List<StringBuilder> ans = new ArrayList<>();
		int bracketCount = 0;
		int lastMatch = 0;
		for (int i=0; i<exp.length(); ++i)
			if (exp.charAt(i) == '(')
				bracketCount++;
			else if (exp.charAt(i) == ')')
				bracketCount--;
			else if (bracketCount == 0 && exp.charAt(i) == ' ' && !exp.substring(Math.max(i-3, 0), i).equals("NEG"))
			{
				ans.add(new StringBuilder(exp.substring(lastMatch, i)));
				lastMatch = i+1;
			}
		ans.add(new StringBuilder(exp.substring(lastMatch, exp.length())));
		return ans;
	}
	
	/**
	 * Takes a list of terms and puts the operator into the first index (prefix form).
	 * If there are multiple operators only the first one will be handled
	 * @param terms a list of terms with one operator within
	 * @throws InvalidArgumentsException if the list of terms is in neither prefix form nor correct infix form
	 */
	private static void moveOperatorToPrefix(List<StringBuilder> terms) throws InvalidArgumentsException
	{
		int op = operatorLocation(terms);
		Operator operator = Operator.valueOf(terms.get(op).toString());
		if (op != 0 && operator.SYMBOL_POSITION != op)
			throw new InvalidArgumentsException(String.format
			(
				"Operator %s found in position %d, should be 0 or %d",
				operator.DISPLAY_TEXT,
				op,
				operator.SYMBOL_POSITION
			));
		StringBuilder temp = terms.get(op);
		terms.set(op, terms.get(0));
		terms.set(0, temp);
	}
	
	/**
	 * Finds the index of the first operator in the list of terms
	 * @param terms the terms to search
	 * @return the index of the operator
	 * @throws InvalidArgumentsException if there is no operator in the given terms
	 */
	private static int operatorLocation(List<StringBuilder> terms) throws InvalidArgumentsException
	{
		for (int i=0; i<terms.size(); ++i)
			if (isOperator(terms.get(i).toString()))
				return i;
		throw new InvalidArgumentsException(terms.toString());
	}
	
	/**
	 * Tests whether the given string is an operator, either the name or the display symbol
	 * @param s the string to test
	 * @return true if the string is an operator, false otherwise
	 */
	private static boolean isOperator(String s)
	{
		for (Operator o : Operator.values())
			if (s.equals(o.DISPLAY_TEXT) || s.equals(o.name()))
				return true;
		return false;
	}
	
	/**
	 * Finds the index of the next top level closing parenthesis
	 * @param exp the string to search through
	 * @return the index of the parenthesis.  exp.charAt(returnValue) == ')'
	 * @throws UnmatchedParenthesesException if there is no closing top level parenthesis
	 */
	private static int findNextParen(StringBuilder exp) throws UnmatchedParenthesesException
	{
		int bracketNum = 0; // keep track of what level we're on
		for (int i=0; i<exp.length(); ++i)
			if (exp.charAt(i) == '(')
				bracketNum++;
			else if (exp.charAt(i) == ')' && --bracketNum == 0)
				return i;
		throw new UnmatchedParenthesesException(exp.toString(), 0);
	}
}