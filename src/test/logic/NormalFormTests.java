package logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class NormalFormTests
{

	@Test
	public void testNNFinForm()
	{
		Expression e1 = Expression.create("(AND (OR A B) C)");
		Expression e2 = Expression.create("(OR (AND A (AND (OR (NEG B) C) (NEG C))) D)");
		Expression e3 = Expression.create("(NEG (OR A (NEG C)))");
		Expression e4 = Expression.create("(AND A (NEG (OR B C)))");
		assertTrue(NormalForm.NEGATION.inForm(e1));
		assertTrue(NormalForm.NEGATION.inForm(e2));
		assertFalse(NormalForm.NEGATION.inForm(e3));
		assertFalse(NormalForm.NEGATION.inForm(e4));
	}
	
	@Test
	public void testCNFtransform()
	{
		Expression e1 = Expression.create("(AND (NEG A) (OR B C))");
		Expression e2 = Expression.create("(AND A (OR B (AND D E)))");
		assertEquals(e1,NormalForm.CONJUNCTIVE.transform(e1));
		assertEquals(Expression.create("(AND A (AND (OR B D) (OR B E)))"),NormalForm.CONJUNCTIVE.transform(e2));
	}
	
	@Test
	public void testDNFtransform()
	{
		Expression e1 = Expression.create("(OR (NEG A) (AND B C))");
		Expression e2 = Expression.create("(OR A (AND B (OR D E)))");
		assertEquals(e1,NormalForm.DISJUNCTIVE.transform(e1));
		assertEquals(Expression.create("(OR A (OR (AND B D) (AND B E)))"),NormalForm.DISJUNCTIVE.transform(e2));
	}
	
	@Test
	public void testNNFtransform()
	{
		Expression e1 = Expression.create("(AND (OR A B) C)");
		Expression e2 = Expression.create("(OR (AND A (AND (OR (NEG B) C) (NEG C))) D)");
		Expression e3 = Expression.create("(NEG (OR A (NEG C)))");
		Expression e4 = Expression.create("(AND A (NEG (OR B C)))");
		Expression e5 = Expression.create("(NEG (AND A B))");
		assertEquals(e1,NormalForm.NEGATION.transform(e1));
		assertEquals(e2,NormalForm.NEGATION.transform(e2));
		assertEquals(Expression.create("(AND (NEG A) C)"),NormalForm.NEGATION.transform(e3));
		assertEquals(Expression.create("(AND A (AND (NEG B) (NEG C)))"),NormalForm.NEGATION.transform(e4));
		assertEquals(Expression.create("(OR (NEG A) (NEG B))"),NormalForm.NEGATION.transform(e5));
	}
}