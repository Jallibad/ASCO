package logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class FunctionTests
{
	@Test
	public void testEquality() throws MalformedExpressionException
	{
		Function e = new Function(Operator.AND, "A", "B");
		assertEquals(e, new Function(Operator.AND, "A", "B"));
		assertEquals(Expression.create("(AND A B)"), e);
		assertNotEquals(e, new Function(Operator.AND, "A", "A"));
		assertNotEquals(e, new Function(Operator.OR, "A", "B"));
		assertNotEquals(e, new Literal("A"));
	}
	
	@Test
	public void testToString() throws MalformedExpressionException
	{
		Function e = new Function(Operator.AND, "A", "B");
		assertEquals("(AND A B)", e.toString());
	}
	
	@Test
	public void testGetVariables() throws MalformedExpressionException
	{
		Function e1 = new Function(Operator.NEG, "A");
		Set<Literal> testVariables = new HashSet<Literal>();
		testVariables.add(new Literal("A"));
		assertEquals(testVariables, e1.getVariables());
		Function e2 = new Function(Operator.AND, "A", "B");
		testVariables.add(new Literal("B"));
		assertEquals(testVariables, e2.getVariables());
		Function e3 = new Function(Operator.NEG, e2);
		assertEquals(testVariables, e3.getVariables());
	}
	
	@Test
	public void testMatches()
	{
		// TODO write test cases here
	}
	
	@Test
	public void testFillMatches()
	{
		// TODO write test cases here
	}
}
