package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Expression;
import logic.transform.NormalForm;

public class CheckFormDisplay extends Stage
{
	public static void display(Expression ex, Stage owner)
	{
		new CheckFormDisplay(ex, owner);
	}
	
	private CheckFormDisplay(Expression ex, Stage owner)
	{
		setTitle(String.format("Info on \"%s\"", ex.prettyPrint()));
		VBox box = new VBox();
		
		for (NormalForm normalForm : NormalForm.values())
		{
			Text text = new Text(String.format(
					"\"%s\" is%s in %s normal form",
					ex.prettyPrint(),
					normalForm.inForm(ex) ? "" : " not",
					normalForm.toString()
			));
			// TODO set background color
			box.getChildren().add(text);
		}
		
		box.getChildren().add(new TruthAssignmentDisplay(ex));
		
		Button closeButton = new Button("Close");
		closeButton.setOnAction(event -> close());
		
		box.getChildren().add(closeButton);

		Scene scene = new Scene(box, 300, 250);
		setScene(scene);
		initModality(Modality.APPLICATION_MODAL);
		initOwner(owner);
		show();
	}
}