package logic.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import logic.Expression;
import logic.Function;
import logic.Literal;
import logic.Operator;

public enum NormalForm implements Transform
{
	/**
	 * <p>
	 * Enum representing a transformation of conjunctive normal form
	 * </p>
	 * 
	 * Conjunctive normal form is a conjunction of one or more clauses,
	 * where a clause is a disjunction of literals
	 */
	CONJUNCTIVE,
	
	/**
	 * <p>
	 * Enum representing a transformation of disjunctive normal form
	 * </p>
	 * Disjunctive normal form is a disjunction of one or more clauses,
	 * where a clause is a conjunction of literals
	 * 
	 */
	DISJUNCTIVE,
	
	/**
	 * <p>
	 * Enum representing a transformatoin of negation normal form
	 * </p>
	 * 
	 * Negation normal form is when a negation operator is applied to the variabls in an expression
	 */
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
				throw new UnsupportedOperationException("A normal form transform has been applied without an implementation");
		}
	}
	
	private static boolean checkAll(Operator op, Expression e)
	{
		return
			e instanceof Literal ||
			e.equalWithoutLiterals("(NEG A)") ||
			e.mapPredicate(t -> checkAll(op,t), op);
	}
	
	/**
	 * Checks if the expression is in either conjunctive, disjunctive, or negation normal form
	 * 
	 * @param e the Expression, who's form is being checked
	 * @return True if the expression is in the indicated form, otherwise False
	 */
	public boolean inForm(Expression e)
	{
		switch (this)
		{
			case CONJUNCTIVE:
				return
					e instanceof Literal ||
					checkAll(Operator.OR, (Function) e) ||
					e.mapPredicate(CONJUNCTIVE::inForm, Operator.AND);
				
			case DISJUNCTIVE:
				return
					e instanceof Literal ||
					checkAll(Operator.AND, (Function) e) ||
					e.mapPredicate(DISJUNCTIVE::inForm, Operator.OR);
				
			case NEGATION:
				return
					e instanceof Literal ||
					e.equalWithoutLiterals("(NEG A)") ||
					e.mapPredicate(NEGATION::inForm, Operator.AND, Operator.OR); 
		}
		return false; // TODO implement
	}
	

	@Override
	public TransformSteps transformWithSteps(Expression orig)
	{
		switch (this)
		{
			case CONJUNCTIVE:
				return transformHelperWithSteps(NEGATION.transformWithSteps(orig), InferenceRule.OR_DISTRIBUTION);
			case DISJUNCTIVE:
				return transformHelperWithSteps(NEGATION.transformWithSteps(orig), InferenceRule.AND_DISTRIBUTION);
			case NEGATION:
				return transformHelperWithSteps(orig,
					InferenceRule.DE_MORGANS_OR,
					InferenceRule.DE_MORGANS_AND,
					InferenceRule.DOUBLE_NEGATION
				);
			default:
				throw new UnsupportedOperationException("A normal form transform has been applied without an implementation");
		}
	}
	
	private TransformSteps transformHelperWithSteps(TransformSteps orig, InferenceRule inferenceRules)
	{
		return orig.combine(transformHelperWithSteps(orig.result(), inferenceRules));
	}

	private Expression transformHelper(Expression orig, InferenceRule... inferenceRules)
	{
		for (InferenceRule i : inferenceRules)
			orig = i.transformLeft(orig);
		if (orig instanceof Function)
		{
			Function f = (Function) orig;
			List<Expression> normalFormTerms =
				f.getTerms().stream().map(t -> transformHelper(t, inferenceRules)).collect(Collectors.toList());
			orig = Function.constructUnsafe(f.operator, normalFormTerms);
		}
		return orig;
	}
	
	private TransformSteps transformHelperWithSteps(Expression orig, InferenceRule... inferenceRules)
	{
		TransformSteps steps = new TransformSteps(orig);
		for (InferenceRule i : inferenceRules)
			i.transformLeftWithSteps(steps);
		if (steps.result() instanceof Function)
		{
			Function f = (Function) steps.result();
			List<Expression> getTerms = f.getTerms();
			List<Expression> normalFormTerms = new ArrayList<>();
			for (int i=0; i<getTerms.size(); ++i)
			{
				TransformSteps partialSteps = transformHelperWithSteps(getTerms.get(i), inferenceRules);
				normalFormTerms.add(partialSteps.result());
				steps.combine(partialSteps, i);
			}
		}
		return steps;
	}
	
	@Override
	public String toString()
	{
		return name().toLowerCase();
	}
}