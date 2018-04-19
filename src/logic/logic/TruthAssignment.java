package logic;
import java.util.ArrayList;
import java.util.List;

import logic.malformedexpression.InvalidArgumentsException;
import logic.malformedexpression.MalformedExpressionException;

public class TruthAssignment
{
	public static void main(String[] args) throws MalformedExpressionException
	{
		TruthAssignment a = ExpParser.parse("A").getTruthAssignments();
		System.out.println(a);
		TruthAssignment b = Operator.AND.getTruthTable();
		System.out.println(b);
		//b.merge(a, 0);
	}
	
	List<Expression> columns = new ArrayList<>();
	List<List<Boolean>> table = new ArrayList<>();
	
	TruthAssignment(Literal a)
	{
		table.add(getTFList());
		table.add(getTFList());
		columns.add(a);
		columns.add(a);
	}
	
	public TruthAssignment(boolean[][] truthTable)
	{
		for (int y=0; y<truthTable[0].length; ++y)
		{
			List<Boolean> partial = new ArrayList<>();
			for (int x=0; x<truthTable.length; ++x)
				partial.add(truthTable[x][y]);
			table.add(partial);
		}
		
		char currLiteral='A';
		for (int i=0; i<truthTable[0].length-1; ++i, ++currLiteral)
			try
			{
				columns.add(new Literal(Character.toString(currLiteral)));
			}
			catch (InvalidArgumentsException e)
			{
				throw new Error();
			}
	}

	private static List<Boolean> getTFList()
	{
		List<Boolean> ans = new ArrayList<>();
		ans.add(true);
		ans.add(false);
		return ans;
	}

	public void merge(TruthAssignment sub)
	{
		// multiply this and sub
		//for ()
		
		// filter for contradictions
		
		// remove result rows
		//for (int )
	}
	
	public String toString()
	{
		StringBuilder ans = new StringBuilder();
		
		for (Expression e : columns)
			ans.append(e.toString()+"|");
		ans.append("\n");
		
		for (int y=0; y<table.get(0).size(); ++y)
		{
			for (int x=0; x<table.size(); ++x)
				ans.append((table.get(x).get(y) ? 'T' : 'F')+"|");
			ans.append('\n');
		}
		
		return ans.toString();
	}
}