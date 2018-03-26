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
	}
}