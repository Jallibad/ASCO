package logic;

import java.io.Serializable;

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