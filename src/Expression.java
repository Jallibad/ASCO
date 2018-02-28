import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class Expression
{
	public static void main(String[] args)
	{
		Expression test1 = new Function(Operator.AND, "A", "B");
		Expression test2 = new Function(Operator.NEG, "B");
		Expression test3 = new Function(Operator.NEG, Collections.singletonList(test1));
		System.out.println(test1);
		System.out.println(test1.getVariables());
		System.out.println(test2);
		System.out.println(test2.getVariables());
		System.out.println(test3);
		System.out.println(test3.getVariables());
	}
	/**
	 * Creates a set of 
	 * @return
	 */
	public abstract Set<Literal> getVariables();
	public abstract List<TruthAssignment> getTruthAssignments();
}