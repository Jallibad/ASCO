package logic;
import java.util.Iterator;
import java.util.Set;

public class TruthAssignment
{
	Expression e;
	
	TruthAssignment(Expression e)
	{
		this.e = e;
	}
	
	@Override
	public String toString()
	{
		//String tableString = "A|B|*\nT|T|T\nT|F|F\nF|T|F\nF|F|F";
		//String tableString = "A|B|*\nT|T|T\nT|F|T\nF|T|T\nF|F|F";
		//String tableString = "A|*\nT|F\nF|T";
		StringBuilder tableString = new StringBuilder();
		
		Set<Literal> vars = e.getVariables();
		//initialize table string with columns for each variable and result
		Iterator<Literal> itr = vars.iterator(); 
		while (itr.hasNext()) 
		{
			tableString.append(itr.next());
			tableString.append("|");
		}
		tableString.append("*\n");
		
		//recursively evaluate combinations of literals
		
		
		System.out.println(tableString.toString());
		return tableString.toString();
		//for (int i = 0; i < e.ter)
	}
}