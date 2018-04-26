package gui;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import logic.transform.TransformStep;

public class StepDisplay extends HBox 
{
	TransformStep step;
	
	public StepDisplay(TransformStep step)
	{
		this.step = step;
		ExpressionDisplay before = new ExpressionDisplay(step.before);
		Text stepText = new Text(step.step.toString());
		ExpressionDisplay after = new ExpressionDisplay(step.after);
		getChildren().addAll(before, stepText, after);
	}
}