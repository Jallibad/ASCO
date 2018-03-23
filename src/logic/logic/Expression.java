package logic;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

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
		Expression e1 = create("(AND A (OR B (AND D E)))");
		System.out.println(NormalForm.CONJUNCTIVE.transform(e1));
	}
	
	/**
	 * check whether or not the specified character is a letter or digit
	 * @param c the character to check
	 * @return whether c is a character or digit (true) or not (false)
	 */
	private static boolean isOperator(char c)
	{
	    return (!Character.isLetter(c) && !Character.isDigit(c));
	}
	 
	/**
	 * determine the arithmetic priority (order of evaluation) of the specified character
	 * @param C the character whose priority we wish to determine
	 * @return the arithmetic priority of character C
	 */
	private static int getPriority(char C)
	{
	    if (C == '-' || C == '+')
	        return 1;
	    else if (C == '*' || C == '/')
	        return 2;
	    else if (C == '^')
	        return 3;
	    return 0;
	}
	 
	/**
	 * convert an expression from infix to postfix form
	 * @param infix the expression in infix form
	 * @return the specified expression in postfix form
	 */
	private static String infixToPostfix(String infix)
	{
	    infix = '(' + infix + ')';
	    int l = infix.length();
	    Stack<Character> char_stack = new Stack<Character>();
	    String output = "";
	 
	    for (int i = 0; i < l; i++) {
	 
	        // If the scanned character is an 
	        // operand, add it to output.
	        if (Character.isLetter(infix.charAt(i)) || Character.isDigit(infix.charAt(i)))
	            output += infix.charAt(i);
	 
	        // If the scanned character is an
	        // �(�, push it to the stack.
	        else if (infix.charAt(i) == '(')
	            char_stack.push('(');
	 
	        // If the scanned character is an
	        // �)�, pop and output from the stack 
	        // until an �(� is encountered.
	        else if (infix.charAt(i) == ')') {
	 
	            while (char_stack.peek() != '(') {
	            	System.out.println("A");
	                output += char_stack.pop();
	            }
	 
	            // Remove '(' from the stack
	            char_stack.pop(); 
	        }
	 
	        // Operator found 
	        else {
	             
	            if (isOperator(char_stack.peek())) {
//	                //no concern of operator priority in FOL, so no need for this loop
//	            	while (getPriority(infix.charAt(i)) <= getPriority(char_stack.peek())) {
	                	System.out.println("B");
	                	output += char_stack.pop();
//	                }
	 
	                // Push current Operator on stack
	                char_stack.push(infix.charAt(i));
	            }
	        }
	    }
	    return output;
	}
	/*public static String infixToPrefix(String str){
        Stack stack = new Stack();
        String prefix = "";
        for(int i=str.length()-1;i>=0;i--){
            char c = str.charAt(i);
            if(Character.isLetter(c)){
                prefix = ((Character)c).toString() + prefix;
            }
            else if(c == '('){
                prefix = ((Character)stack.pop()).toString() + prefix;
            }
            else if(c == ')'){
                continue;
            }
            else{
                stack.push(c);
            }
        }
        return prefix;
 
    }*/
	
	/**
	 * return a new string from the specified string with the desired character replaced
@@ -121,43 +62,6 @@ public abstract class Expression
	private static String setCharString(String s, int loc, String s2) {
		return s.substring(0, loc) + s2 + s.substring(loc+1);
	}
	 
	/**
	 * convert an expression from infix to prefix form
	 * @param infix the expression in infix form to convert
	 * @return the new expression in prefix form
	 */
	private static String infixToPrefix(String infix)
	{
	    /* Reverse String
	     * Replace ( with ) and vice versa
	     * Get Postfix
	     * Reverse Postfix  *  */
	    int l = infix.length();
	 
	    // Reverse infix
	    infix = new StringBuilder(infix).reverse().toString();
	 
	    // Replace ( with ) and vice versa
	    for (int i = 0; i < l; i++) {
	 
	        if (infix.charAt(i) == '(') {
	        	infix = setChar(infix,i,')');
	            i++;
	        }
	        else if (infix.charAt(i) == ')') {
	        	infix = setChar(infix,i,'(');
	            i++;
	        }
	    }
	 
	    String prefix = infixToPostfix(infix);
	 
	    // Reverse postfix
	    prefix = new StringBuilder(prefix).reverse().toString();
	 
	    return prefix;
	}
	
	/**
	 * return a new string from the specified string with the desired character replaced
	 * @param s the original string
	 * @param loc the location of the character to replace
	 * @param c the character to write to string position loc
	 * @return a new string equal to s with character number loc replaced with c
	 */
	private static String setChar(String s, int loc, char c) {
		return s.substring(0,loc)+c+s.substring(loc+1);
	}
	
	/**
	 * return a new string from the specified string with the desired character replaced with the specified string
	 * @param s the original string
	 * @param loc the location of the character to replace
	 * @param c the string to write to string position loc
	 * @return a new string equal to s with character number loc replaced with string s
	 */
	private static String setCharString(String s, int loc, String s2) {
		return s.substring(0, loc) + s2 + s.substring(loc+1);
	}
	
	/**
	 * convert all operator characters to the operator name
	 * @param exp the expression in which to convert all operators
	 * @return the specified expression string with all operator characters replaced with the operator name
	 */
	private static String operatorsToEnglish(String exp) {
		for (int i = 0; i < exp.length(); ++i) {
			for (Operator opr : Operator.values()) {
				if (String.valueOf(exp.charAt(i)).equals(opr.DISPLAY_TEXT)) {
					exp = setCharString(exp,i,opr.name());
					i += opr.name().length()-1;
					continue;
				}
			}
		}
		return exp;
	}
	
	/**
	 * sanitize the input expression in order to prepare it for parsing
	 * @param exp the string to sanitize
	 * @return the specified string sanitized for expression parsing
	 */
	private static String sanitizeInput(String exp) {
		//add parenthesis around the expression, if not already present
		return exp.charAt(0) == '(' ? exp : "("+exp+")";
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
			System.out.println("sanitized: " + sanitizeInput(exp));
			System.out.println("sanitized infix: " + infixToPrefix(sanitizeInput(exp)));
			System.out.println("sanitized infix english: " + operatorsToEnglish(infixToPrefix(sanitizeInput(exp))));
			return create(operatorsToEnglish(infixToPrefix(sanitizeInput(exp))));
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
}