package logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import logic.malformedexpression.InvalidArgumentsException;
import logic.transform.MiscTransform;
import logic.transform.TransformSteps;

public class ExpressionTests
{
	@Test
	public void testEquivalence() throws InvalidArgumentsException
	{	
		// Test commutativity
		Optional<TransformSteps> s1 = ExpParser.create("(AND A B)").proveEquivalence(ExpParser.create("(AND B A)"));
		assertTrue(s1.isPresent());
		TransformSteps ans1 = new TransformSteps(ExpParser.create("(AND A B)"));
		ans1.addStep(MiscTransform.COMMUTE);
		//assertEquals(ans1, s1.get());
		
		assertFalse(ExpParser.create("(AND A A)").proveEquivalence(ExpParser.create("(AND B A)")).isPresent());
		assertFalse(ExpParser.create("(AND B B)").proveEquivalence(ExpParser.create("(AND B A)")).isPresent());
		
		assertFalse(ExpParser.create("(AND A B)").proveEquivalence(new Literal("A")).isPresent());
		
		assertTrue(new Literal("A").proveEquivalence(new Literal("A")).isPresent());
		assertFalse(new Literal("A").proveEquivalence(new Literal("B")).isPresent());
	}
	
	@Test
	public void testGetVariables() throws InvalidArgumentsException
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
	
	@Test
	public void testComplexity()
	{
		assertEquals(4, ExpParser.create("(NEG (AND A B))").complexity());
	}
}