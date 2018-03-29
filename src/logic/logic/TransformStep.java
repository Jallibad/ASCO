package logic;

import java.io.Serializable;

/**
 * An immutable container class representing a single transform, along with the expressions before and after
 * @author Jallibad
 *
 */
public class TransformStep implements Serializable
{
	private static final long serialVersionUID = -1364619449009668270L;
	public final Expression before;
	public final Transform step;
	public final Expression after;
	
	public TransformStep(Expression before, Transform step, Expression after)
	{
		this.step = step;
		this.before = before;
		this.after = after;
	}
	
	@Override
	public String toString()
	{
		return before+" --- "+step+" --- "+after; // TODO Implement
	}
}