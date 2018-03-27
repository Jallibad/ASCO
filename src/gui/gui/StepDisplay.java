package gui;

import javafx.scene.layout.Pane;
import logic.TransformStep;

public class StepDisplay extends Pane 
{
	TransformStep step;
	
	public StepDisplay(TransformStep step)
	{
		System.out.println(step);
		this.step = step;
		ExpressionDisplay before = new ExpressionDisplay(step.before);
		getChildren().add(before);
	}
}