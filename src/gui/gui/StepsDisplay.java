package gui;

import javafx.scene.layout.VBox;
import logic.TransformSteps;

public class StepsDisplay extends VBox
{
	TransformSteps steps;
	
	StepsDisplay(TransformSteps steps)
	{
		this.steps = steps;
		//getChildren().addAll(steps.stream().map(StepDisplay::new).collect(Collectors.toList()));
		for (int i=0; i<steps.steps.size(); ++i)
		{
			ExpressionDisplay e = new ExpressionDisplay(steps.fullIntermediaries.get(i));
			getChildren().add(e);
			StepDisplay s = new StepDisplay(steps.getStep(i));
			getChildren().add(s);
		}
		getChildren().add(new ExpressionDisplay(steps.result()));
	}
}