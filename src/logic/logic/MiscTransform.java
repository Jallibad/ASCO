package logic;

public enum MiscTransform implements BiDirectionalTransform
{
	ASSOCIATE,
	COMMUTE;

	@Override
	public Expression transform(Expression orig)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransformSteps transformWithSteps(Expression orig)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression left()
	{
		switch (this)
		{
			case ASSOCIATE:
				break;
			case COMMUTE:
				
				break;
			default:
				break;
			
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression right()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
