package gui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ExpressionEntry extends VBox
{
	private TextField textField = new TextField();
	private HBox buttons = new HBox();
	
	public ExpressionEntry()
	{
		buttons.managedProperty().bind(buttons.visibleProperty());
		buttons.setVisible(textField.focusedProperty().get());
		textField.focusedProperty().addListener((obs, oldVal, newVal) ->
		{
			buttons.setVisible(newVal);
		});
		textField.setFont(Font.font("Monospaced"));
		getChildren().add(textField);
		
		for (String symbol : new String[] {"¬","∧","∨"})
		{
			Button button = new Button(symbol);
			button.focusTraversableProperty().set(false);
			button.setOnAction(e ->
			{
				Button curr = (Button) e.getSource();
				textField.insertText(textField.getCaretPosition(), curr.getText());
			});
			buttons.getChildren().add(button);
		}
		getChildren().add(buttons);
	}
}