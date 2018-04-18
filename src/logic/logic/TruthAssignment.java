package logic;
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
		String tableString = "A|*\nT|F\nF|T";
		return tableString;
		
		Set<Literal> vars;
		for (int i = 0; i < e.ter)
	}
}