package logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ExpressionTests
{
	@Test
	public void testCreate() throws MalformedExpressionException
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
	public void testParse() throws MalformedExpressionException
	{
		assertEquals(new Literal("A"), Expression.parse("(((A)))"));
		assertEquals(Expression.create("(AND A B)"), Expression.parse("A∧B"));
		assertEquals(Expression.create("(NEG A)"), Expression.parse("¬A"));
		assertEquals(Expression.create("(AND B (NEG A))"), Expression.parse("B∧ ¬A"));
		assertEquals(Expression.create("(AND B (NEG A))"), Expression.parse("(B∧ ¬A)"));
		assertEquals(Expression.create("(NEG (NEG A))"), Expression.parse("¬¬A"));
		assertEquals(Expression.create("(NEG (OR A (NEG (NEG (NEG C)))))"), Expression.parse("¬(A ∨ ¬(¬(¬C)))"));
	}
	
	@Test(expected = MalformedExpressionException.class)
	public void testConstructor() throws MalformedExpressionException
	{
		new Function(Operator.AND, new Literal("A"));
	}
	
	@Test
	public void testEquivalence()
	{	
		// Test commutativity
		assertTrue(Expression.create("(AND A B)").proveEquivalence(Expression.create("(AND B A)")).isPresent());
		assertFalse(Expression.create("(AND A A)").proveEquivalence(Expression.create("(AND B A)")).isPresent());
		assertFalse(Expression.create("(AND B B)").proveEquivalence(Expression.create("(AND B A)")).isPresent());
		
		assertFalse(Expression.create("(AND A B)").proveEquivalence(new Literal("A")).isPresent());
		
		assertTrue(new Literal("A").proveEquivalence(new Literal("A")).isPresent());
		assertFalse(new Literal("A").proveEquivalence(new Literal("B")).isPresent());
	}
	
	@Test
	public void testGetVariables()
	{
		Set<Literal> variables = new HashSet<Literal>();
		variables.add(new Literal("A"));
		assertEquals(variables, Expression.create("A").getVariables());
		variables.add(new Literal("B"));
		assertEquals(variables, Expression.create("(AND A B)").getVariables());
	}
	
	@Test
	public void testMatches()
	{
		assertTrue(Expression.create("(AND A B)").matches("(AND A B)"));
		assertFalse(Expression.create("(AND (OR A B) B)").matches("(AND (AND A B) B)"));
	}
	
	@Test
	public void testPrettyPrint()
	{
		assertEquals("¬A", Expression.create("(NEG A)").prettyPrint());
		assertEquals("¬(A ∧ B)", Expression.create("(NEG (AND A B))").prettyPrint());
		assertEquals("¬(A ∧ (B ∨ C))", Expression.create("(NEG (AND A (OR B C)))").prettyPrint());
	}
}