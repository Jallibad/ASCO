package logic;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a collection of steps and intermediary expressions
 * @author Jallibad
 *
 */
public class TransformSteps
{
	private List<Transform> steps = new ArrayList<Transform>();
	private List<Expression> intermediaries = new ArrayList<Expression>();
	private List<Expression> fullIntermediaries = new ArrayList<Expression>();
	
	public TransformSteps()
	{
		
	}
	
	public TransformSteps(Expression orig)
	{
		intermediaries.add(orig);
		fullIntermediaries.add(orig);
	}
	
	public void addStep(Transform step)
	{
		steps.add(step);
		// Transform the current last result and then add it to the list
		fullIntermediaries.add(step.transform(result()));
		intermediaries.add(null);
	}
	
	public void combine(TransformSteps s, int index)
	{
		// TODO combine steps
	}
	
	/**
	 * Returns the final Expression formed by the application of the contained steps
	 * @return the final expression
	 */
	public Expression result()
	{
		return fullIntermediaries.get(fullIntermediaries.size()-1);
	}
}