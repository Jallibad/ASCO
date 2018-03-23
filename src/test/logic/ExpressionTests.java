package logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExpressionTests
{
	@Test
	public void testCreate()
	{
		Expression e1 = new Function(Operator.NEG, new Function(Operator.OR, "A", "B"));
		Expression t1 = Expression.create("(NEG (OR A B))");
		assertEquals(e1,t1);
		Expression e2 = new Function(Operator.AND, new Function(Operator.OR, "A", "B"), e1);
		Expression t2 = Expression.create("(AND (OR A B) (NEG (OR A B)))");
		assertEquals(e2,t2);
		Expression e3 = new Function(Operator.AND, new Literal("A"), e1);
		Expression t3 = Expression.create("(AND A (NEG (OR A B)))");
		assertEquals(e3,t3);
	}
	
	@Test
	public void testParse() 
	{
		try {
			Expression e1 = Expression.parse("¬A ∨ B");
			Expression t1 = Expression.create("(NEG (OR A B))");
			System.out.println(String.format("%s is not equal to %s",e1, t1));
			assertEquals(e1,t1);
		}
		catch (MalformedExpressionException e) {
			fail();
		}
	}
}