package logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class StepOrExpressionTests
{
	@Test
	public void test()
	{
		StepOrExpression s1 = new StepOrExpression(new TransformStep(null, null, null));
		assertTrue(s1.mapOver(left -> true, right -> false));
		StepOrExpression s2 = new StepOrExpression(ExpParser.create("A"));
		assertFalse(s2.mapOver(left -> true, right -> false));
	}
}