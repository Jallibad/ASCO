package gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import logic.Expression;

public class ExpressionDisplay extends BorderPane
{
	private static final Border BORDER;
	static
	{
		BORDER = new Border
		(
			new BorderStroke
			(
				Color.BLACK,
				BorderStrokeStyle.SOLID,
				CornerRadii.EMPTY,
				BorderWidths.DEFAULT
			)
		);
	}
	
	private Expression exp;
	private Text display;
	
	ExpressionDisplay(Expression exp)
	{
		setBorder(BORDER);
		this.exp = exp;
		display = new Text(exp.prettyPrint());
		setLeft(display);
		setAlignment(display, Pos.CENTER_LEFT);
	}
	ExpressionDisplay(Expression exp, Node rightHalf)
	{
		this(exp);
		setRight(rightHalf);
	}
	
	public void setExpression(Expression exp)
	{
		this.exp = exp;
	}
	
	public Expression getExpression()
	{
		return exp;
	}
}