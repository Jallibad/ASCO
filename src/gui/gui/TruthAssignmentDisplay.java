package gui;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import logic.Expression;
import logic.TruthAssignment;

public class TruthAssignmentDisplay extends GridPane
{
	public TruthAssignmentDisplay(Expression exp)
	{
		TruthAssignment truthAssignments = new TruthAssignment(exp);
		setGridLinesVisible(true);
		for (int i=0; i<truthAssignments.columns.size(); ++i)
			add(new ExpressionDisplay(truthAssignments.columns.get(i)), i, 0);
		add(new ExpressionDisplay(exp), truthAssignments.columns.size(), 0);
		for (int x=0; x<truthAssignments.table.size(); ++x)
			for (int y=0; y<truthAssignments.table.get(x).size(); ++y)
				add(new Text(truthAssignments.table.get(x).get(y).toString()), y, x+1);
	}
}