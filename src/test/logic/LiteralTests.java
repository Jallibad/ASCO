package logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Set;

import org.junit.Test;

import logic.malformedexpression.InvalidArgumentsException;

public class LiteralTests
{

	@Test
	public void testEquality() throws InvalidArgumentsException
	{
		Literal a = new Literal("A");
		Literal b = new Literal("B");
		assertNotEquals(a,b);
		assertNotEquals(a, ExpParser.parseUnsafe("(NEG A)"));
	}
	
	@Test
	public void testGetVariables() throws InvalidArgumentsException
	{
		Literal a = new Literal("A");
		Set<Literal> vars = a.getVariables();
		assertEquals(1, vars.size());
		assertEquals(a, vars.iterator().next());
	}
}