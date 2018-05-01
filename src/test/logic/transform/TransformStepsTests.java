package logic.transform;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

import logic.ExpParser;
import logic.Expression;
import logic.malformedexpression.MalformedExpressionException;
import logic.transform.InferenceRule;
import logic.transform.StepOrExpression;
import logic.transform.TransformStep;
import logic.transform.TransformSteps;

public class TransformStepsTests
{
	@Test
	public void testIterator() throws MalformedExpressionException
	{
		TransformSteps s1 = new TransformSteps(ExpParser.parse("NEG NEG A"));
		s1.addStep(InferenceRule.DOUBLE_NEGATION);
		
		Iterator<StepOrExpression> correctSteps = makeSteps
		(
			ExpParser.parse("NEG NEG A"),
			new TransformStep(ExpParser.parse("NEG NEG A"), InferenceRule.DOUBLE_NEGATION, ExpParser.parse("A")),
			ExpParser.parse("A")
		);
		
		for (StepOrExpression se : s1)
			assertEquals(correctSteps.next(), se);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testCombination() throws MalformedExpressionException
	{
		TransformSteps s1 = NormalForm.CONJUNCTIVE.transformWithSteps(ExpParser.parse("A AND (B OR (D AND NEG NEG E))"));
		Iterator<StepOrExpression> correctSteps = makeSteps
		(
			ExpParser.parse("A AND (B OR (D AND NEG NEG E))"),
			new TransformStep(ExpParser.parse("A AND (B OR (D AND NEG NEG E))"), InferenceRule.DOUBLE_NEGATION, ExpParser.parse("AND A (OR B (AND D E))")),
			ExpParser.parse("AND A (OR B (AND D E))"),
			new TransformStep(ExpParser.parse("AND A (OR B (AND D E))"), InferenceRule.OR_DISTRIBUTION, ExpParser.parse("(AND A (AND (OR B D) (OR B E)))"))
		);
		
//		for (StepOrExpression se : s1)
//			assertEquals(correctSteps.next(), se);
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