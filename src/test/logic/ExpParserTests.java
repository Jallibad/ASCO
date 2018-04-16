package logic;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.malformedexpression.InvalidArgumentsException;
import logic.malformedexpression.MalformedExpressionException;
import logic.malformedexpression.UnmatchedParenthesesException;

public class ExpParserTests
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
	
	@Test(expected = MalformedExpressionException.class)
	public void testInvalidOperator() throws MalformedExpressionException
	{
		ExpParser.parse("(A B)");
	}
	
	@Test(expected = MalformedExpressionException.class)
	public void testInvalidArgument() throws MalformedExpressionException
	{
		ExpParser.parse("(¬∧)");
	}
	
	@Test(expected = MalformedExpressionException.class)
	public void testCornerCase() throws MalformedExpressionException
	{
		ExpParser.parse("(A|B)");
	}
	
	@Test(expected = MalformedExpressionException.class)
	public void testLoneOperator() throws MalformedExpressionException
	{
		ExpParser.parse("∧");
	}
	
	@Test(expected = UnmatchedParenthesesException.class)
	public void testUnmatchedParentheses1() throws MalformedExpressionException
	{
		ExpParser.parse("((A");
	}
	
	@Test(expected = UnmatchedParenthesesException.class)
	public void testUnmatchedParentheses2() throws MalformedExpressionException
	{
		ExpParser.parse("A))");
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
		assertEquals(ExpParser.create("(AND (NEG A) B)"), ExpParser.parse("(AND ((NEG A)) B)"));
		assertEquals(ExpParser.create("(AND (AND A B) (AND C D))"), ExpParser.parse("(A ∧ B) ∧ (C ∧ D)"));
	}
	
	@Test(expected = InvalidArgumentsException.class)
	public void testWrongNumArguments() throws MalformedExpressionException
	{
		ExpParser.createSafe("(AND A)");
	}
}