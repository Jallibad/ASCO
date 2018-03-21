package logic;

import java.util.List;
import java.util.stream.Collectors;

public enum NormalForm implements Transform
{
	CONJUNCTIVE, // TODO implement CNF
	DISJUNCTIVE, // TODO implement DNF
	NEGATION;

	@Override
	public Expression transform(Expression orig)
	{
		switch (this)
		{
			case CONJUNCTIVE:
				orig = NEGATION.transform(orig);
				
				if (orig instanceof Function)
				{
					Function f = (Function) orig;
					List<Expression> normalFormTerms =
						f.getTerms().stream().map(CONJUNCTIVE::transform).collect(Collectors.toList());
					orig = new Function(f.operator, normalFormTerms);
				}
				if (Expression.create("(OR P (AND Q R))").matches(orig))
					orig = transform(InferenceRule.OR_DISTRIBUTION.transform(orig));
				
				return orig;
			case DISJUNCTIVE:
				orig = NEGATION.transform(orig);
				
				if (orig instanceof Function)
				{
					Function f = (Function) orig;
					List<Expression> normalFormTerms =
						f.getTerms().stream().map(DISJUNCTIVE::transform).collect(Collectors.toList());
					orig = new Function(f.operator, normalFormTerms);
				}
				if (Expression.create("(AND P (OR Q R))").matches(orig))
					orig = transform(InferenceRule.AND_DISTRIBUTION.transform(orig));
				
				return orig;
			case NEGATION:
				if (orig instanceof Function)
				{
					Function f = (Function) orig;
					List<Expression> normalFormTerms =
						f.getTerms().stream().map(NEGATION::transform).collect(Collectors.toList());
					orig = new Function(f.operator, normalFormTerms);
				}
				if (Expression.create("(NEG (OR A B))").matches(orig))
					orig = transform(InferenceRule.DE_MORGANS_OR.transform(orig));
				else if (Expression.create("(NEG (AND A B))").matches(orig))
					orig = transform(InferenceRule.DE_MORGANS_AND.transform(orig));
				else if (Expression.create("(NEG (NEG A))").matches(orig))
					orig = InferenceRule.DOUBLE_NEGATION.transform(orig);
				return orig;
		}
		return null;
	}

	public boolean inForm(Expression e)
	{
		switch (this)
		{
			case CONJUNCTIVE:
				break;
			case DISJUNCTIVE:
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
}