package logic;

import java.util.Optional;

/**
 * A container object for use with TransformSteps.  StepOrExpression is guaranteed to contain
 * either a TransformStep or an Expression.
 * @author Jallibad
 *
 */
public class StepOrExpression
{
	private final Optional<TransformStep> step;
	private final Optional<Expression> exp;
	
	StepOrExpression(TransformStep step)
	{
		this.step = Optional.of(step);
		this.exp = Optional.empty();
	}
	StepOrExpression(Expression exp)
	{
		this.step = Optional.empty();
		this.exp = Optional.of(exp);
	}
	
	/**
	 * A higher order function that takes two functions, one that accepts a TransformStep
	 * and one that accepts an Expression.  Both functions should return the same thing, and
	 * the StepOrExpression will choose which to apply based on its content, returning the result of
	 * the correct one. 
	 * @param left the function to apply to a TransformStep
	 * @param right the function to apply to an Expression
	 * @return the result of applying the correct function to the contained object
	 */
	public <T> T mapOver(java.util.function.Function<TransformStep, T> left, java.util.function.Function<Expression, T> right)
	{
		T ans;
		if (step.isPresent())
			ans = left.apply(step.get());
		else
			ans = right.apply(exp.get());
		return ans;
	}
	
	@Override
	public String toString()
	{
		if (step.isPresent())
			return "TransformStep: "+step.get();
		else
			return "Expression: "+exp.get();
	}
}