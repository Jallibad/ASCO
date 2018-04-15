package logic;

public class TruthAssignment
{
	Expression e;
	
	TruthAssignment(Expression e)
	{
		this.e = e;
	}
	
	public String getTable()
	{
		//String tableString = "A|B|*\nT|T|T\nT|F|F\nF|T|F\nF|F|F";
		//String tableString = "A|B|*\nT|T|T\nT|F|T\nF|T|T\nF|F|F";
		String tableString = "A|*\nT|F\nF|T";
		return tableString;
	}
}