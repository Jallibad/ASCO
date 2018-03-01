package logic;

import static org.junit.Assert.*;
import logic.Expression;
import logic.InferenceRule;

import org.junit.Test;

public class InferenceRuleTests
{
	@Test
	public void testTransform()
	{
		Expression e1 = Expression.create("(NEG (OR A B))");
		Expression e2 = Expression.create("(AND (NEG A) (NEG B))");
		assertEquals(InferenceRule.DE_MORGANS_OR.transform(e1), e2);
		InferenceRule.DE_MORGANS_OR.transform(e2); // TODO test invalid transforms
	}
}