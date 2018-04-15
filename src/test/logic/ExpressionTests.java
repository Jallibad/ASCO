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
		Expression t1 = ExpParser.create("(NEG (OR A B))");
		assertEquals(e1,t1);
		Expression e2 = new Function(Operator.AND, new Function(Operator.OR, "A", "B"), e1);
		Expression t2 = ExpParser.create("(AND (OR A B) (NEG (OR A B)))");
		assertEquals(e2,t2);
		Expression e3 = new Function(Operator.AND, new Literal("A"), e1);
		Expression t3 = ExpParser.create("(AND A (NEG (OR A B)))");
		assertEquals(e3,t3);
	}
	
	@Test
	public void testParse() throws MalformedExpressionException
	{
		assertEquals(new Literal("A"), ExpParser.parse("(((A)))"));
		assertEquals(ExpParser.create("(AND A B)"), ExpParser.parse("A∧B"));
		assertEquals(ExpParser.create("(NEG A)"), ExpParser.parse("¬A"));
		assertEquals(ExpParser.create("(AND B (NEG A))"), ExpParser.parse("B∧ ¬A"));
		assertEquals(ExpParser.create("(AND B (NEG A))"), ExpParser.parse("(B∧ ¬A)"));
		assertEquals(ExpParser.create("(NEG (NEG A))"), ExpParser.parse("¬¬A"));
		assertEquals(ExpParser.create("(NEG (OR A (NEG (NEG (NEG C)))))"), ExpParser.parse("¬(A ∨ ¬(¬(¬C)))"));
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
		assertTrue(ExpParser.create("(AND A B)").proveEquivalence(ExpParser.create("(AND B A)")).isPresent());
		assertFalse(ExpParser.create("(AND A A)").proveEquivalence(ExpParser.create("(AND B A)")).isPresent());
		assertFalse(ExpParser.create("(AND B B)").proveEquivalence(ExpParser.create("(AND B A)")).isPresent());
		
		assertFalse(ExpParser.create("(AND A B)").proveEquivalence(new Literal("A")).isPresent());
		
		assertTrue(new Literal("A").proveEquivalence(new Literal("A")).isPresent());
		assertFalse(new Literal("A").proveEquivalence(new Literal("B")).isPresent());
	}
	
	@Test
	public void testGetVariables()
	{
		Set<Literal> variables = new HashSet<Literal>();
		variables.add(new Literal("A"));
		assertEquals(variables, ExpParser.create("A").getVariables());
		variables.add(new Literal("B"));
		assertEquals(variables, ExpParser.create("(AND A B)").getVariables());
	}
	
	@Test
	public void testMatches()
	{
		assertTrue(ExpParser.create("(AND A B)").matches("(AND A B)"));
		assertFalse(ExpParser.create("(AND (OR A B) B)").matches("(AND (AND A B) B)"));
	}
	
	@Test
	public void testPrettyPrint()
	{
		assertEquals("¬A", ExpParser.create("(NEG A)").prettyPrint());
		assertEquals("¬(A ∧ B)", ExpParser.create("(NEG (AND A B))").prettyPrint());
		assertEquals("¬(A ∧ (B ∨ C))", ExpParser.create("(NEG (AND A (OR B C)))").prettyPrint());
	}
}