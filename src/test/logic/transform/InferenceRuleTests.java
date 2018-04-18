package logic.transform;

import static org.junit.Assert.*;

import logic.ExpParser;
import logic.Expression;
import logic.transform.InferenceRule;

import org.junit.Test;

public class InferenceRuleTests
{
	@Test
	public void testTransform()
	{
		Expression e1 = ExpParser.create("(NEG (OR A B))");
		Expression e2 = ExpParser.create("(AND (NEG A) (NEG B))");
		assertEquals(InferenceRule.DE_MORGANS_OR.transform(e1), e2);
		assertEquals(InferenceRule.DE_MORGANS_OR.transform(e2), e1);
		// TODO test invalid transforms
	}
	
	@Test
	public void testToString()
	{
		assertEquals("double negation", InferenceRule.DOUBLE_NEGATION.toString());
		assertEquals("DeMorgan's or", InferenceRule.DE_MORGANS_OR.toString());
	}
}