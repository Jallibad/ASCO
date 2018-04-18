package logic.transform;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.ExpParser;
import logic.Expression;
import logic.transform.InferenceRule;
import logic.transform.TransformSteps;

public class BiDirectionalTransformTests
{
	@Test
	public void testTransformLeftRight()
	{
		Expression e1 = ExpParser.parseUnsafe("P");
		Expression e2 = InferenceRule.DOUBLE_NEGATION.transformRight(e1);
		assertEquals(ExpParser.parseUnsafe("(NEG (NEG P))"), e2);
		Expression e3 = InferenceRule.DOUBLE_NEGATION.transformLeft(e2);
		assertEquals(e1, e3);
	}
	
	@Test
	public void testTransformLeftRightSteps()
	{
		TransformSteps steps = new TransformSteps(ExpParser.parseUnsafe("P"));
		InferenceRule.DOUBLE_NEGATION.transformRightWithSteps(steps);
		assertEquals(ExpParser.parseUnsafe("(NEG (NEG P))"), steps.result());
		InferenceRule.DOUBLE_NEGATION.transformLeftWithSteps(steps);
		assertEquals(ExpParser.parseUnsafe("P"), steps.result());
	}
	
	@Test
	public void testInvalidTransforms()
	{
		Expression e1 = ExpParser.parseUnsafe("(NEG (OR P Q))");
		assertEquals(e1, InferenceRule.DE_MORGANS_OR.transformRight(e1));
		Expression e2 = ExpParser.parseUnsafe("(AND (NEG P) (NEG Q))");
		assertEquals(e2, InferenceRule.DE_MORGANS_OR.transformLeft(e2));
	}
	
	@Test
	public void testIn()
	{
		Expression e1 = InferenceRule.DE_MORGANS_OR.left();
		assertTrue(InferenceRule.DE_MORGANS_OR.inLeft(e1));
		Expression e2 = InferenceRule.DE_MORGANS_OR.right();
		assertTrue(InferenceRule.DE_MORGANS_OR.inRight(e2));
	}
}
