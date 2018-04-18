package logic;

import static org.junit.Assert.*;
import logic.transform.InferenceRule;
import logic.transform.NormalForm;

import org.junit.Test;

/**
 * These tests are solely to cover things that JUnit improperly marks as uncovered
 * @author Jallibad
 *
 */
public class FalseTests
{
	@Test
	public void test()
	{
		assertEquals(OperatorTrait.ASSOCIATIVE, OperatorTrait.valueOf(OperatorTrait.ASSOCIATIVE.name()));
		assertEquals(InferenceRule.AND_DISTRIBUTION, InferenceRule.valueOf(InferenceRule.AND_DISTRIBUTION.name()));
		assertEquals(NormalForm.CONJUNCTIVE, NormalForm.valueOf(NormalForm.CONJUNCTIVE.name()));
	}
}