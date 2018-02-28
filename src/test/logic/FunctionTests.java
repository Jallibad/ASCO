package logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class FunctionTests
{
	@Test
	public void testEquality()
	{
		Function e1 = new Function(Operator.AND, "A", "B");
		assertEquals(e1, new Function(Operator.AND, "A", "B"));
		assertEquals(Expression.create("(AND A B)"), e1);
		assertNotEquals(e1, new Function(Operator.AND, "A", "A"));
		assertNotEquals(e1, new Function(Operator.OR, "A", "B"));
		assertNotEquals(e1, new Literal("A"));
	}
}
