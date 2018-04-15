package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A mutable class representing a collection of steps and intermediary expressions
 * @author Jallibad
 *
 */
public class TransformSteps implements Serializable, Iterable<StepOrExpression>
{
	private static final long serialVersionUID = -5016027615991197605L;
	private List<Transform> steps = new ArrayList<>();
	private List<Expression> intermediaries = new ArrayList<>();
	private List<Expression> fullIntermediaries = new ArrayList<>();
	
	private TransformSteps()
	{
		
	}
	
	public TransformSteps(Expression orig)
	{
		intermediaries.add(null);
		fullIntermediaries.add(orig);
		checkRep();
	}
	
	/**
	 * Transforms the current last result and then adds it to the list
	 * 
	 * @param step the step to be added
	 */
	public void addStep(Transform step)
	{
		steps.add(step);
		fullIntermediaries.add(step.transform(result()));
		intermediaries.add(null);
		checkRep();
	}
	
	/**
	 * Combine
	 * 
	 * @param s
	 * @param index
	 */
	public void combine(TransformSteps s, int index)
	{
		if (s.steps.size() == 0)
			return;
		// Remove the possibly null last element to make room for the new original
		// TODO this sounds wrong...
		List<Expression> newTerms = ((Function) result()).getTerms();
		Operator o = result().getOperator();
		intermediaries.remove(intermediaries.size()-1);
		fullIntermediaries.remove(fullIntermediaries.size()-1);
		List<Expression> newIntermediaries = s.intermediaries;
		for (int i=0; i<newIntermediaries.size(); ++i)
		{
			newTerms.set(index, s.fullIntermediaries.get(i));
			fullIntermediaries.add(Function.constructUnsafe(o, newTerms));
			if (newIntermediaries.get(i) == null)
				newIntermediaries.set(i, s.fullIntermediaries.get(i));
		}
		intermediaries.addAll(newIntermediaries);
		steps.addAll(s.steps);
		checkRep();
	}
	
	/**
	 * Returns the final Expression formed by the application of the contained steps
	 * @return the final expression
	 */
	public Expression result()
	{
		return fullIntermediaries.get(fullIntermediaries.size()-1);
	}
	
	@Override
	public String toString()
	{
		StringBuilder ans = new StringBuilder("-----\n");
		for (int i=0; i<steps.size(); ++i)
		{
			ans.append(fullIntermediaries.get(i)+"\n");
			ans.append(steps.get(i)+"\n");
		}
		return ans.toString()+result()+"\n-----";
	}
	
	/**
	 * Checks the representation invariant.  Should not do anything in the working
	 * production version of the application.
	 */
	private void checkRep()
	{
		assert(intermediaries.size() == fullIntermediaries.size());
		assert(intermediaries.size() == steps.size()+1);
	}
	
	public TransformStep getStep(int i)
	{
		return new TransformStep(get(i), steps.get(i), get(i+1));
	}
	
	public Expression get(int i)
	{
		if (intermediaries.get(i) == null)
			return fullIntermediaries.get(i);
		return intermediaries.get(i);
	}
	
	public TransformSteps reverse()
	{
		TransformSteps ans = new TransformSteps();
		Collections.reverse(ans.fullIntermediaries);
		Collections.reverse(ans.intermediaries);
		Collections.reverse(ans.steps);
		return ans;
	}
	
	@Override
	public Iterator<StepOrExpression> iterator()
	{
		List<StepOrExpression> ans = new ArrayList<>();
		for (int i=0; i<steps.size(); ++i)
		{
			ans.add(new StepOrExpression(fullIntermediaries.get(i)));
			ans.add(new StepOrExpression(getStep(i)));
		}
		ans.add(new StepOrExpression(result()));
		return ans.iterator();
	}

	/**
	 * Concatenates this and other into one TransformSteps object
	 * @param other
	 * @return
	 */
	public TransformSteps combine(TransformSteps other)
	{
		TransformSteps ans = new TransformSteps();
		ans.fullIntermediaries.addAll(fullIntermediaries);
		ans.fullIntermediaries.remove(fullIntermediaries.size()-1);
		ans.fullIntermediaries.addAll(other.fullIntermediaries);
		ans.intermediaries.addAll(intermediaries);
		ans.intermediaries.remove(intermediaries.size()-1);
		ans.intermediaries.addAll(other.intermediaries);
		ans.steps.addAll(steps);
		ans.steps.addAll(other.steps);
		checkRep();
		return ans;
	}
}