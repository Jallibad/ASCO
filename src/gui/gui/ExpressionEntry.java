package gui;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import logic.ExpParser;
import logic.Expression;
import logic.malformedexpression.MalformedExpressionException;

/**
 * A subclass of Node that represent a text entry field with buttons for symbol input
 * and expression validation.
 * @author Jallibad
 *
 */
public class ExpressionEntry extends VBox
{
	private static final Logger LOGGER = Logger.getLogger(ExpressionEntry.class.getName());
	
	private static final String[] BAR_SYMBOLS = new String[] {"¬","∧","∨"};
	private static final Map<String, String> KEY_REPLACEMENTS = new HashMap<>();
	static
	{
		KEY_REPLACEMENTS.put("&", "∧");
		KEY_REPLACEMENTS.put("|", "∨");
		KEY_REPLACEMENTS.put("~", "¬");
	}
	
	private TextField textField = new TextField();
	private HBox buttons = new HBox();
	
	public ExpressionEntry()
	{
		// Hide the buttons when the related TextField is unfocused, and vice versa
		buttons.managedProperty().bind(buttons.visibleProperty()); // Remove from layout when hidden
		buttons.setVisible(textField.focusedProperty().get()); // Set initial visibility
		buttons.visibleProperty().bind(textField.focusedProperty()); // Attach visibility to textField focus
		
		textField.setFont(Font.font("Monospaced"));
		textField.setOnKeyTyped(event ->
			Optional.ofNullable(KEY_REPLACEMENTS.get(event.getCharacter())).ifPresent(newText ->
			{
				LOGGER.fine(() -> String.format("Replaced key \"%s\" with character \"%s\"", event.getCharacter(), newText));
				textField.insertText(textField.getCaretPosition(), newText);
				event.consume();
			})
		);
		getChildren().add(textField);
		
		for (String symbol : BAR_SYMBOLS)
		{
			Button button = new Button(symbol);
			button.focusTraversableProperty().set(false); // Disallow focus on the button
			button.setOnAction(e -> textField.insertText(textField.getCaretPosition(), symbol));
			buttons.getChildren().add(button);
		}
		getChildren().add(buttons);
	}
	
	// TODO validate rather than try/catch exception
	public boolean validExpression()
	{
		try
		{
			getExpression();
			return true;
		}
		catch (MalformedExpressionException e)
		{
			LOGGER.info(e.getMessage());
			return false;
		}
	}
	
	public Optional<Expression> getOptionalExpression()
	{
		try
		{
			return Optional.of(getExpression());
		}
		catch (MalformedExpressionException e)
		{
			return Optional.empty();
		}
	}
	
	public Expression getExpression() throws MalformedExpressionException
	{
		return ExpParser.parse(textField.getText());
	}
	
	/**
	 * Sets the text of the inner text field to a string representation of the expression
	 * @param e the expression to be set to
	 */
	public void setExpression(Expression e)
	{
		textField.setText(e.prettyPrint());
	}
}