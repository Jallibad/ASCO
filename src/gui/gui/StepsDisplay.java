package gui;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.layout.VBox;
import logic.TransformStep;

public class StepsDisplay extends VBox
{
	List<TransformStep> steps;
	
	StepsDisplay(List<TransformStep> steps)
	{
		this.steps = steps;
		getChildren().addAll(steps.stream().map(StepDisplay::new).collect(Collectors.toList()));
	}
}