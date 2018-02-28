package logic;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Expression
{
	public static void main(String[] args)
	{
		System.out.println(create("(AND (NEG A) B)"));
		Expression test = new Function(Operator.NEG, new Function(Operator.OR, "A", "B"));
		System.out.println(test);
		Expression result = InferenceRule.DE_MORGANS.transform(test);
		System.out.println(result);
	}
	
	// TODO add error checking/handling
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
	 * Creates a set of 
	 * @return
	 */
	public abstract Set<Literal> getVariables();
	public abstract List<TruthAssignment> getTruthAssignments();
}