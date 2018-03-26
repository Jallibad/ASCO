package gui;

import javafx.scene.layout.Pane;
import logic.TransformStep;

public class StepDisplay extends Pane 
{
	TransformStep step;
	
	public StepDisplay(TransformStep step)
	{
		this.step = step;
	}
}