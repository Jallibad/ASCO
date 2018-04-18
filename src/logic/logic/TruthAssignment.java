package logic;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TruthAssignment
{
	Expression e;
	boolean[][] table;
	
	TruthAssignment(Expression e)
	{
		this.e = e;
		this.table = getTable();
	}
	
	public boolean[][] getTable() 
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
		
		for (int i = 0; i < terms.size(); ++i) {
			Expression curTerm = terms.get(i);
			if (!(curTerm instanceof Literal)) {
//				System.out.println(curTerm);
			}
		}
		return new boolean[0][0];
	
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
	private void appendTableString(StringBuilder sb, boolean[][] ot) {
		for (int i = 0; i < ot.length; ++i) {
			for (int r = 0; r < ot[i].length; ++r) {
				sb.append(ot[i][r] == true ? 'T' : 'F');
				if (r != ot[i].length - 1) {
					sb.append("|");	
				}	
			}
			if (i != ot.length - 1) {
				sb.append('\n');	
			}
		}
	}
}