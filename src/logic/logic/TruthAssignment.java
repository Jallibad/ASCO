package logic;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class TruthAssignment
{
	Expression e;
	
	TruthAssignment(Expression e)
	{
		this.e = e;
	}
	
	public ArrayList<ArrayList<Boolean>> getTable() 
	{
		List<Expression> terms = ((Function)e).getTerms();
		//base case: if expression contains no nested expressions, simply return the operator table
		boolean foundNested = false;
		for (int i = 0; i < terms.size(); ++i) 
		{	
			if (!(terms.get(i) instanceof Literal)) {
				foundNested = true;
				break;
			}
		}
		if (!foundNested) {
			return e.getOperator().truthTable;
		}
		
		//recursive call: construct a table for nested expressions and merge up
		ArrayList<ArrayList<Boolean>> mergedTable = e.getOperator().truthTable;
		for (int i = 0; i < terms.size(); ++i) {
			//need to handle literals and expressions separately (also consider AND A,A vs AND A,B) 
			Expression curTerm = terms.get(i);
			if ((curTerm instanceof Literal))
			{
				//parse literal
				
			}
			else
			{
				//parse expression 
			}
		}
		return mergedTable;
	
	}
	
	@Override
	public String toString()
	{		
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
		appendTableString(tableString, getTable());
		
		System.out.println(tableString.toString());
		return tableString.toString();
	}
	
	/*
	 * add operator table to specified StringBuilder
	 */
	private void appendTableString(StringBuilder sb, ArrayList<ArrayList<Boolean>> ot) {
		for (int i = 0; i < ot.size(); ++i) {
			for (int r = 0; r < ot.get(i).size(); ++r) {
				sb.append(ot.get(i).get(r) == true ? 'T' : 'F');
				if (r != ot.get(i).size() - 1) {
					sb.append("|");	
				}	
			}
			if (i != ot.size() - 1) {
				sb.append('\n');	
			}
		}
	}
}