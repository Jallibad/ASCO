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
		
		appendOperatorTable(tableString, e.getOperator());
		//recursively evaluate combinations of literals
		
		System.out.println(tableString.toString());
		return tableString.toString();
		//for (int i = 0; i < e.ter)
	}
	
	/*
	 * add operator table to specified StringBuilder
	 */
	private void appendOperatorTable(StringBuilder sb, Operator op) {
		boolean[][] ot = op.truthTable;
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