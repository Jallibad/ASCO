package logic.transform;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

import logic.ExpParser;
import logic.Expression;
import logic.transform.InferenceRule;
import logic.transform.StepOrExpression;
import logic.transform.TransformStep;
import logic.transform.TransformSteps;

public class TransformStepsTests
{
	@Test
	public void testIterator()
	{
		TransformSteps s1 = new TransformSteps(ExpParser.parseUnsafe("(NEG (NEG A))"));
		s1.addStep(InferenceRule.DOUBLE_NEGATION);
		
		Iterator<StepOrExpression> correctSteps = makeSteps
		(
				ExpParser.parseUnsafe("(NEG (NEG A))"),
			new TransformStep(ExpParser.parseUnsafe("(NEG (NEG A))"), InferenceRule.DOUBLE_NEGATION, ExpParser.parseUnsafe("A")),
			ExpParser.parseUnsafe("A")
		);
		
		for (StepOrExpression se : s1)
			assertEquals(correctSteps.next(), se);
	}
	
	@Test
	public void testCombination()
	{
		
	}
	
	private Iterator<StepOrExpression> makeSteps(Object... step)
	{
		Queue<StepOrExpression> correctSteps = new LinkedList<StepOrExpression>();
		for (Object o : step)
			if (o instanceof TransformStep)
				correctSteps.add(new StepOrExpression((TransformStep) o));
			else if (o instanceof Expression)
				correctSteps.add(new StepOrExpression((Expression) o));
			else
				return null;
		return correctSteps.iterator();
	}
}