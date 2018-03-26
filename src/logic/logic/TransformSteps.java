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
	
	public TransformSteps(Expression orig)
	{
		intermediaries.add(null);
		fullIntermediaries.add(orig);
		checkRep();
	}
	
	public void addStep(Transform step)
	{
		steps.add(step);
		// Transform the current last result and then add it to the list
		fullIntermediaries.add(step.transform(result()));
		intermediaries.add(null);
		checkRep();
	}
	
	public void combine(TransformSteps s, int index)
	{
		if (s.steps.size() == 0)
			return;
		// Remove the possibly null last element to make room for the new original
		List<Expression> newTerms = ((Function) result()).getTerms();
		Operator o = result().getOperator();
		intermediaries.remove(intermediaries.size()-1);
		fullIntermediaries.remove(fullIntermediaries.size()-1);
		List<Expression> newIntermediaries = s.intermediaries;
		for (int i=0; i<newIntermediaries.size(); ++i)
		{
			newTerms.set(index, s.fullIntermediaries.get(i));
			fullIntermediaries.add(new Function(o, newTerms));
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
//		String ans = "TransformSteps::toString not implemented yet";
		String ans = "-----\n";
		for (int i=0; i<steps.size(); ++i)
		{
			ans += fullIntermediaries.get(i)+"\n";
			ans += steps.get(i)+"\n";
		}
		// TODO implement
		return ans+result()+"\n-----";
	}
	
	private void checkRep()
	{
		if (intermediaries.size() != fullIntermediaries.size())
			throw new Error();
		if (intermediaries.size() != steps.size()+1)
			throw new Error();
	}
}