package gui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import logic.Expression;
import logic.MalformedExpressionException;

public class ExpressionEntry extends VBox
{
	private TextField textField = new TextField();
	private HBox buttons = new HBox();
	
	public ExpressionEntry()
	{
		// Hide the buttons when the related TextField is unfocused, and vice versa
		buttons.managedProperty().bind(buttons.visibleProperty()); // Remove from layout when hidden
		buttons.setVisible(textField.focusedProperty().get()); // Set initial visibility
		buttons.visibleProperty().bind(textField.focusedProperty()); // Attach visibility to textField focus
		textField.setFont(Font.font("Monospaced"));
		getChildren().add(textField);
		
		for (String symbol : new String[] {"¬","∧","∨"})
		{
			Button button = new Button(symbol);
			button.focusTraversableProperty().set(false); // Disallow focus on the button
			button.setOnAction(e -> textField.insertText(textField.getCaretPosition(), symbol));
			buttons.getChildren().add(button);
		}
		getChildren().add(buttons);
	}
	
	public Expression getExpression() throws MalformedExpressionException
	{
		return Expression.parse(textField.getText());
	}
}