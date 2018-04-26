package gui;

import javafx.scene.layout.VBox;
import logic.transform.StepOrExpression;
import logic.transform.TransformSteps;

public class StepsDisplay extends VBox
{
	TransformSteps steps;
	
	StepsDisplay(TransformSteps steps)
	{
		this.steps = steps;
		for (StepOrExpression se : steps)
			getChildren().add(se.mapOver(StepDisplay::new, ExpressionDisplay::new));
	}
}