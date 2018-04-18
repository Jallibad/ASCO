package logic;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.malformedexpression.InvalidArgumentsException;
import logic.malformedexpression.MalformedExpressionException;
import logic.malformedexpression.UnmatchedParenthesesException;

public class ExpParserTests
{
	@Test
	public void testParse() throws MalformedExpressionException
	{
		Expression literalA = new Literal("A");
		Expression literalB = new Literal("B");
		Expression literalC = new Literal("C");
		Expression literalD = new Literal("D");
		Expression andAB = new Function(Operator.AND, literalA, literalB);
		Expression orAB = new Function(Operator.OR, literalA, literalB);
		Expression andCD = new Function(Operator.AND, literalC, literalD);
		Expression andABCD = new Function(Operator.AND, andAB, andCD);
		Expression negA = negate(literalA);
		Expression negNegA = negate(negA);
		Expression andBNegA = new Function(Operator.AND, literalB, negA);
		Expression negNegNegC = negate(negate(negate(new Literal("C"))));
		Expression e1 = negate(new Function(Operator.OR, literalA, negNegNegC));
		Expression e2 = new Function(Operator.AND, negA, literalB);
		Expression e3 = new Function(Operator.AND, literalA, negate(orAB));
		
		assertEquals(literalA, ExpParser.parse("(((A)))"));
		assertEquals(andAB, ExpParser.parse("A∧B"));
		assertEquals(negA, ExpParser.parse("¬A"));
		assertEquals(andBNegA, ExpParser.parse("B∧ ¬A"));
		assertEquals(andBNegA, ExpParser.parse("(B∧ ¬A)"));
		assertEquals(negNegA, ExpParser.parse("¬¬A"));
		assertEquals(e1, ExpParser.parse("¬(A ∨ ¬(¬(¬C)))"));
		assertEquals(e1, ExpParser.parse("¬(A ∨ ¬¬¬C)"));
		assertEquals(e2, ExpParser.parse("(AND ((NEG A)) B)"));
		assertEquals(andABCD, ExpParser.parse("(A ∧ B) ∧ (C ∧ D)"));
		assertEquals(e3, ExpParser.parse("A ∧ ¬(A ∨ B)"));
	}
	
	@Test(expected = InvalidArgumentsException.class)
	public void testInvalidPosition() throws MalformedExpressionException
	{
		ExpParser.parse("A B AND");
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
	
	@Test(expected = UnmatchedParenthesesException.class)
	public void testUnmatchedNestedParen() throws MalformedExpressionException
	{
		ExpParser.parse("A OR (B");
	}
	
	@Test(expected = InvalidArgumentsException.class)
	public void testWrongNumArguments() throws MalformedExpressionException
	{
		ExpParser.parse("(AND A)");
	}
	
	private Expression negate(Expression e) throws InvalidArgumentsException
	{
		return new Function(Operator.NEG, e);
	}
}