package logic;

/**
 * An interface representing Transforms that can be undone.  By default the "left" side
 * of the expression is the more complex part, and the "right" side is simpler.  Transforming
 * left is transforming from left to right, and vice versa.
 * @author Jallibad
 *
 */
public interface BiDirectionalTransform extends Transform
{
	/**
	 * Gets the more complex "left" expression
	 * @return an expression to be matched against
	 */
	public Expression left();
	
	/**
	 * Get the simpler "right" expression
	 * @return an expression to be matched against
	 */
	public Expression right();
	
	/**
	 * Transforms the expression from the left form to the right form if applicable.
	 * Returns the original expression if the transform couldn't be applied.
	 * By convention this should result in a simpler expression
	 * @param orig the expression to transform
	 * @return the transformed expression
	 */
	default public Expression transformLeft(Expression orig)
	{
		return left().matches(orig) ? Transform.transform(left().fillMatches(orig), right()) : orig;
	}
	
	/**
	 * Transforms the expression from the left form to the right form if applicable.
	 * Returns the steps used along the way
	 * By convention this should result in a simpler expression
	 * @param orig the expression to transform
	 * @return the transformation steps.  The default implementation returns a TransformSteps object
	 * with one step, the current one.
	 */
	default public TransformSteps transformLeftWithSteps(Expression orig)
	{
		TransformSteps ans = new TransformSteps(orig);
		if (left().matches(orig))
			ans.addStep(this);
		return ans;
	}
	
	/**
	 * Transforms the result() of the argument from the left form to the right form if applicable.
	 * By convention this should result in a simpler expression
	 * @param orig the steps to append to
	 */
	default public void transformLeftWithSteps(TransformSteps steps)
	{
		if (left().matches(steps.result()))
			steps.addStep(this);
	}
	
	/**
	 * Transforms the expression from the right form to the left form if applicable.
	 * Returns the original expression if the transform couldn't be applied.
	 * By convention this should result in a more complex expression.
	 * @param orig the expression to transform
	 * @return the transformed expression
	 */
	default public Expression transformRight(Expression orig)
	{
		return right().matches(orig) ? Transform.transform(right().fillMatches(orig), left()) : orig;
	}
	
	/**
	 * Transforms the expression from the right form to the left form if applicable.
	 * Returns the steps used along the way.
	 * By convention this should result in a more complex expression
	 * @param orig the expression to transform
	 * @return the transformation steps.  The default implementation returns a TransformSteps object
	 * with one step, the current one.
	 */
	default public TransformSteps transformRightWithSteps(Expression orig)
	{
		TransformSteps ans = new TransformSteps(orig);
		if (right().matches(orig))
			ans.addStep(this);
		return ans;
	}
	
	/**
	 * Transforms the result() of the argument from the right form to the left form if applicable.
	 * By convention this should result in a more complex expression
	 * @param orig the steps to append to
	 */
	default public void transformRightWithSteps(TransformSteps steps)
	{
		if (right().matches(steps.result()))
			steps.addStep(this);
	}
	
	/**
	 * Tests if the given expression matches the left() expression
	 * @param orig the expression to test
	 * @return true if the expression matches the left() expression, false otherwise
	 */
	default public boolean inLeft(Expression orig)
	{
		return left().matches(orig);
	}

	/**
	 * Tests if the given expression matches the right() expression
	 * @param orig the expression to test
	 * @return true if the expression matches the right() expression, false otherwise
	 */
	default public boolean inRight(Expression orig)
	{
		return right().matches(orig);
	}
}