package logic.transform;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.ExpParser;
import logic.Expression;
import logic.transform.NormalForm;

public class NormalFormTests
{

	@Test
	public void testNNFinForm()
	{
		assertTrue(NormalForm.NEGATION.inForm(
			ExpParser.parseUnsafe("(AND (OR A B) C)")));
		
		assertTrue(NormalForm.NEGATION.inForm(
			ExpParser.parseUnsafe("(OR (AND A (AND (OR (NEG B) C) (NEG C))) D)")));
		
		assertFalse(NormalForm.NEGATION.inForm(
			ExpParser.parseUnsafe("(NEG (OR A (NEG C)))")));
		
		assertFalse(NormalForm.NEGATION.inForm(
			ExpParser.parseUnsafe("(AND A (NEG (OR B C)))")));
	}
	
	@Test
	public void testCNFtransform()
	{
		Expression e1 = ExpParser.parseUnsafe("(AND (NEG A) (OR B C))");
		Expression e2 = ExpParser.parseUnsafe("(AND A (OR B (AND D E)))");
		//Expression e3 = Expression.create("(OR (AND A B) (AND C D))");
		assertEquals(e1,NormalForm.CONJUNCTIVE.transform(e1));
		assertEquals(ExpParser.parseUnsafe("(AND A (AND (OR B D) (OR B E)))"), NormalForm.CONJUNCTIVE.transform(e2));
		//assertEquals(Expression.create(""), NormalForm.CONJUNCTIVE.transform(e3));
	}
	
	@Test
	public void testCNFinform()
	{
		assertTrue(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parseUnsafe("(AND A B)")));
		
		assertTrue(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parseUnsafe("(AND (OR A B) (OR B C))")));
		
		assertTrue(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parseUnsafe("(AND (AND (OR A (NEG A)) B) (AND (NEG C) (OR (NEG B) D)))")));
		
		assertFalse(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parseUnsafe("(OR C (AND A B))")));
		
		assertFalse(NormalForm.CONJUNCTIVE.inForm(
			ExpParser.parseUnsafe("(NEG (OR B C))")));
	}
	
	@Test
	public void testDNFtransform()
	{
		Expression e1 = ExpParser.parseUnsafe("(OR (NEG A) (AND B C))");
		Expression e2 = ExpParser.parseUnsafe("(OR A (AND B (OR D E)))");
		assertEquals(e1,NormalForm.DISJUNCTIVE.transform(e1));
		assertEquals(ExpParser.parseUnsafe("(OR A (OR (AND B D) (AND B E)))"),NormalForm.DISJUNCTIVE.transform(e2));
	}
	
	@Test 
	public void testDNFinform()
	{
		assertTrue(NormalForm.DISJUNCTIVE.inForm(
			ExpParser.parseUnsafe("(OR C (AND A B))")));
		
		assertTrue(NormalForm.DISJUNCTIVE.inForm(
			ExpParser.parseUnsafe("(AND A B)")));
		
		assertTrue(NormalForm.DISJUNCTIVE.inForm(
			ExpParser.parseUnsafe("(OR C (OR A B))")));
		
		assertFalse(NormalForm.DISJUNCTIVE.inForm(
			ExpParser.parseUnsafe("(NEG (OR A B))")));
	}
	
	@Test
	public void testNNFtransform()
	{
		Expression e1 = ExpParser.parseUnsafe("(AND (OR A B) C)");
		Expression e2 = ExpParser.parseUnsafe("(OR (AND A (AND (OR (NEG B) C) (NEG C))) D)");
		Expression e3 = ExpParser.parseUnsafe("(NEG (OR A (NEG C)))");
		Expression e4 = ExpParser.parseUnsafe("(AND A (NEG (OR B C)))");
		Expression e5 = ExpParser.parseUnsafe("(NEG (AND A B))");
		assertEquals(e1,NormalForm.NEGATION.transform(e1));
		assertEquals(e2,NormalForm.NEGATION.transform(e2));
		assertEquals(ExpParser.parseUnsafe("(AND (NEG A) C)"),NormalForm.NEGATION.transform(e3));
		assertEquals(ExpParser.parseUnsafe("(AND A (AND (NEG B) (NEG C)))"),NormalForm.NEGATION.transform(e4));
		assertEquals(ExpParser.parseUnsafe("(OR (NEG A) (NEG B))"),NormalForm.NEGATION.transform(e5));
	}
}