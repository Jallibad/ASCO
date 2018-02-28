package logic;

import static org.junit.Assert.*;
import logic.Expression;
import logic.InferenceRule;

import org.junit.Test;

public class InferenceRuleTests
{
	@Test
	public void test()
	{
		Expression e1 = Expression.create("(NEG (OR A B))");
		Expression e2 = Expression.create("(AND (NEG A) (NEG B))");
		assertEquals(InferenceRule.DE_MORGANS.transform(e1), e2);
	}
}