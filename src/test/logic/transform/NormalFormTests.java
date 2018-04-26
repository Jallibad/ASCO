package logic.transform;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.ExpParser;
import logic.Expression;
import logic.malformedexpression.MalformedExpressionException;
import logic.transform.NormalForm;

public class NormalFormTests
{

	@Test
	public void testNNFinForm() throws MalformedExpressionException
	{
		assertTrue(NormalForm.NEGATION.inForm(
			ExpParser.parse("(A∨B)∧C")));
		
		assertTrue(NormalForm.NEGATION.inForm(
			ExpParser.parse("(A∧(((¬B)∨C)∧ ¬C))∨D")));
		
		assertFalse(NormalForm.NEGATION.inForm(
			ExpParser.parse("¬(A∨(¬C))")));
		
		assertFalse(NormalForm.NEGATION.inForm(
			ExpParser.parse("A∧(¬(B∨C))")));
	}
	
	@Test
	public void testCNFtransform() throws MalformedExpressionException
	{
		Expression e1 = ExpParser.parse("(¬A)∧(B∨C)");
		Expression e2 = ExpParser.parse("A∧(B∨(D∧E))");
		Expression e3 = ExpParser.parse("(A∧B)∨C");
		assertEquals(e1, NormalForm.CONJUNCTIVE.transform(e1));
		assertEquals(ExpParser.parse("A∧((B∨D)∧(B∨E))"), NormalForm.CONJUNCTIVE.transform(e2));
		assertEquals(ExpParser.parse("(C∨A)∧(C∨B)"), NormalForm.CONJUNCTIVE.transform(e3));
	}
	
	@Test
	public void testCNFinform() throws MalformedExpressionException
	{
		assertTrue(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parse("A∧B")));
		
		assertTrue(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parse("(A∨B)∧(B∨C)")));
		
		assertTrue(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parse("(AND (AND (OR A (NEG A)) B) (AND (NEG C) (OR (NEG B) D)))")));
		
		assertFalse(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parse("(OR C (AND A B))")));
		
		assertFalse(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parse("(NEG (OR B C))")));
	}
	
	@Test
	public void testDNFtransform() throws MalformedExpressionException
	{
		Expression e1 = ExpParser.parse("(OR (NEG A) (AND B C))");
		Expression e2 = ExpParser.parse("(OR A (AND B (OR D E)))");
		assertEquals(e1,NormalForm.DISJUNCTIVE.transform(e1));
		assertEquals(ExpParser.parse("(OR A (OR (AND B D) (AND B E)))"),NormalForm.DISJUNCTIVE.transform(e2));
	}
	
	@Test 
	public void testDNFinform() throws MalformedExpressionException
	{
		assertTrue(NormalForm.DISJUNCTIVE.inForm(
			ExpParser.parse("(OR C (AND A B))")));
		
		assertTrue(NormalForm.DISJUNCTIVE.inForm(
			ExpParser.parse("(AND A B)")));
		
		assertTrue(NormalForm.DISJUNCTIVE.inForm(
			ExpParser.parse("(OR C (OR A B))")));
		
		assertFalse(NormalForm.DISJUNCTIVE.inForm(
			ExpParser.parse("(NEG (OR A B))")));
	}
	
	@Test
	public void testNNFtransform() throws MalformedExpressionException
	{
		Expression e1 = ExpParser.parse("(AND (OR A B) C)");
		Expression e2 = ExpParser.parse("(OR (AND A (AND (OR (NEG B) C) (NEG C))) D)");
		Expression e3 = ExpParser.parse("(NEG (OR A (NEG C)))");
		Expression e4 = ExpParser.parse("(AND A (NEG (OR B C)))");
		Expression e5 = ExpParser.parse("(NEG (AND A B))");
		assertEquals(e1,NormalForm.NEGATION.transform(e1));
		assertEquals(e2,NormalForm.NEGATION.transform(e2));
		assertEquals(ExpParser.parse("(AND (NEG A) C)"),NormalForm.NEGATION.transform(e3));
		assertEquals(ExpParser.parse("(AND A (AND (NEG B) (NEG C)))"),NormalForm.NEGATION.transform(e4));
		assertEquals(ExpParser.parse("(OR (NEG A) (NEG B))"),NormalForm.NEGATION.transform(e5));
	}
}