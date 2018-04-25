package gui;

import java.util.stream.Collectors;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class KeyBindingDisplay extends HBox
{
	private TextField replace;
	
	public static VBox display()
	{
		VBox ans = new VBox();
		VBox entries = new VBox(10);
		
		
		entries.getChildren().addAll(
				ExpressionEntry.KEY_REPLACEMENTS
				.keySet()
				.stream()
				.map(KeyBindingDisplay::new)
				.collect(Collectors.toList()));
		
		Button addNew = new Button("Add new key binding");
		addNew.setOnAction(event -> entries.getChildren().add(new KeyBindingDisplay()));
		
		ans.getChildren().addAll(entries, addNew);
		return ans;
	}
	
	public KeyBindingDisplay(String key)
	{
		replace = new TextField(ExpressionEntry.KEY_REPLACEMENTS.get(key));
		replace.textProperty().addListener((observable, oldVal, newVal) -> ExpressionEntry.KEY_REPLACEMENTS.put(key, newVal));
		Button remove = new Button("remove entry");
		remove.setOnAction(event ->
		{
			ExpressionEntry.KEY_REPLACEMENTS.remove(key);
			((VBox) this.getParent()).getChildren().remove(this);
		});
		getChildren().addAll(new Text(key), this.replace, remove);
	}
	
	public KeyBindingDisplay()
	{
		// TODO implement
	}
}