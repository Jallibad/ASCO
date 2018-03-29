package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum NormalForm implements Transform
{
	CONJUNCTIVE,
	DISJUNCTIVE,
	NEGATION;

	@Override
	public Expression transform(Expression orig)
	{
		switch (this)
		{
			case CONJUNCTIVE:
				return transformHelper(NEGATION.transform(orig), InferenceRule.OR_DISTRIBUTION);
			case DISJUNCTIVE:
				return transformHelper(NEGATION.transform(orig), InferenceRule.AND_DISTRIBUTION);
			case NEGATION:
				return transformHelper(orig,
					InferenceRule.DE_MORGANS_OR,
					InferenceRule.DE_MORGANS_AND,
					InferenceRule.DOUBLE_NEGATION
				);
			default:
				throw new Error("A normal form transform has been applied without an implementation");
		}
	}

	// o shit
		public static boolean checkAllOr(Expression e)
		{
			
			if(e instanceof Function)
			{
				Function f = (Function) e;
				return
				(
						f.operator == Operator.OR && (f.getTerm(0) instanceof Literal && f.getTerm(1) instanceof Literal)
				) ||
				(
						f.operator == Operator.OR &&(checkAllOr(f.getTerm(0)) && checkAllOr(f.getTerm(1)))					
				);
			}
			return true;
		}
		
		public static boolean checkAllAnd(Expression e)
		{
			if(e instanceof Function)
			{
				Function f = (Function) e;
				return
				(
						f.operator == Operator.AND && (f.getTerm(0) instanceof Literal && f.getTerm(1) instanceof Literal)
				) ||
				(
						f.operator == Operator.AND &&(checkAllAnd(f.getTerm(0)) && checkAllAnd(f.getTerm(1)))					
				);
			}
			return true;
		}

		public boolean inForm(Expression e)
		{
			switch (this)
			{
				case CONJUNCTIVE: // TODO check if in CNF
				
					
					if (e instanceof Function)
					{
						Function f = (Function) e;
						return
						(
							checkAllOr(f)
						) ||
						(
							(
								f.operator == Operator.AND
							) &&
								f.getTerms().stream().allMatch(CONJUNCTIVE::inForm) 
						);
						
					}
				
					return true;
					
				case DISJUNCTIVE: // TODO check if in DNF
					
					if (e instanceof Function)
					{
						Function f = (Function) e;
						return
						(
							checkAllAnd(f)
						) ||
						(
							(
								f.operator == Operator.OR
							) &&
								f.getTerms().stream().allMatch(DISJUNCTIVE::inForm) 
						);
						
					}
			
					return true;
					
				case NEGATION:
					if (e instanceof Function)
					{
						Function f = (Function) e;
						return // TODO this is the abyss staring back
						(
							f.operator == Operator.NEG && (f.getTerm(0) instanceof Literal)
						) ||
						(
							(
								f.operator == Operator.AND ||
								f.operator == Operator.OR
							) &&
							f.getTerms().stream().allMatch(NEGATION::inForm)
						); 
					}
					return true;
			}
			return false; // TODO implement
		}
	

	@Override
	public TransformSteps transformWithSteps(Expression orig)
	{
		switch (this)
		{
			// TODO for CNF and DNF show NNF transform steps as well
			case CONJUNCTIVE:
				return transformHelperWithSteps(NEGATION.transform(orig), InferenceRule.OR_DISTRIBUTION);
			case DISJUNCTIVE:
				return transformHelperWithSteps(NEGATION.transform(orig), InferenceRule.AND_DISTRIBUTION);
			case NEGATION:
				return transformHelperWithSteps(orig,
					InferenceRule.DE_MORGANS_OR,
					InferenceRule.DE_MORGANS_AND,
					InferenceRule.DOUBLE_NEGATION
				);
			default:
				throw new Error("A normal form transform has been applied without an implementation");
		}
	}
	
	private Expression transformHelper(Expression orig, InferenceRule... inferenceRules)
	{
		if (orig instanceof Function)
		{
			Function f = (Function) orig;
			List<Expression> normalFormTerms =
				f.getTerms().stream().map(t -> transformHelper(t, inferenceRules)).collect(Collectors.toList());
			orig = new Function(f.operator, normalFormTerms);
		}
		for (InferenceRule i : inferenceRules)
			orig = i.transformLeft(orig);
		return orig;
	}
	
	private TransformSteps transformHelperWithSteps(Expression orig, InferenceRule... inferenceRules)
	{
		TransformSteps steps = new TransformSteps(orig);
		if (orig instanceof Function)
		{
			Function f = (Function) orig;
			List<Expression> getTerms = f.getTerms();
			List<Expression> normalFormTerms = new ArrayList<Expression>();
			for (int i=0; i<getTerms.size(); ++i)
			{
				TransformSteps partialSteps = transformHelperWithSteps(getTerms.get(i), inferenceRules);
				normalFormTerms.add(partialSteps.result());
				steps.combine(partialSteps, i);
			}
		}
		for (InferenceRule i : inferenceRules)
			i.transformLeftWithSteps(steps);
		return steps;
	}
}