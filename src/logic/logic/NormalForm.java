package logic;

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
				return transformHelper(orig, InferenceRule.DE_MORGANS_OR, InferenceRule.DE_MORGANS_AND, InferenceRule.DOUBLE_NEGATION);
//				if (orig instanceof Function)
//				{
//					Function f = (Function) orig;
//					List<Expression> normalFormTerms =
//						f.getTerms().stream().map(NEGATION::transform).collect(Collectors.toList());
//					orig = new Function(f.operator, normalFormTerms);
//				}
//				if (Expression.create("(NEG (OR A B))").matches(orig))
//					orig = transform(InferenceRule.DE_MORGANS_OR.transform(orig));
//				else if (Expression.create("(NEG (AND A B))").matches(orig))
//					orig = transform(InferenceRule.DE_MORGANS_AND.transform(orig));
//				else if (Expression.create("(NEG (NEG A))").matches(orig))
//					orig = InferenceRule.DOUBLE_NEGATION.transform(orig);
//				return orig;
		}
		return null;
	}

	public boolean inForm(Expression e)
	{
		switch (this)
		{
			case CONJUNCTIVE: // TODO check if in CNF
				break;
			case DISJUNCTIVE: // TODO check if in DNF
				break;
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
	public List<TransformStep> transformWithSteps(Expression orig)
	{
		// TODO Auto-generated method stub
		return null;
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
			orig = i.leftToRightTransform(orig);
		return orig;
	}
	
	private Expression transformHelperWithSteps(Expression orig, InferenceRule... inferenceRules)
	{
		if (orig instanceof Function)
		{
			Function f = (Function) orig;
			List<Expression> normalFormTerms =
				f.getTerms().stream().map(t -> transformHelperWithSteps(t, inferenceRules)).collect(Collectors.toList());
			orig = new Function(f.operator, normalFormTerms);
		}
		for (InferenceRule i : inferenceRules)
			orig = i.leftToRightTransform(orig);
		return orig;
	}
}