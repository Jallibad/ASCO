package logic.transform;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.ExpParser;
import logic.transform.StepOrExpression;
import logic.transform.TransformStep;

public class StepOrExpressionTests
{
	@Test
	public void test()
	{
		StepOrExpression s1 = new StepOrExpression(new TransformStep(null, null, null));
		assertTrue(s1.mapOver(left -> true, right -> false));
		StepOrExpression s2 = new StepOrExpression(ExpParser.parseUnsafe("A"));
		assertFalse(s2.mapOver(left -> true, right -> false));
	}
	
	@Test
	public void testEquality()
	{
		StepOrExpression s1 = new StepOrExpression(new TransformStep(null, null, null));
		StepOrExpression s2 = new StepOrExpression(ExpParser.parseUnsafe("A"));
		assertNotEquals(s1, s2);
		assertNotEquals(s1, "");
		StepOrExpression s3 = new StepOrExpression(ExpParser.parseUnsafe("B"));
		assertNotEquals(s2, s3);
		assertEquals(s1,s1);
		assertEquals(s2,s2);
	}
	
	@Test
	public void testToString()
	{
		StepOrExpression s1 = new StepOrExpression(new TransformStep(null, null, null));
		StepOrExpression s2 = new StepOrExpression(ExpParser.parseUnsafe("A"));
		assertEquals("TransformStep: null --- null --- null", s1.toString());
		assertEquals("Expression: A", s2.toString());
	}
}