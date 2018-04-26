package gui;

import java.util.Map;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class KeyBindingDisplay extends HBox
{
	private TextField replace;
	
	private static Map<String, String> map = ExpressionEntry.KEY_REPLACEMENTS;
	
	public static VBox display()
	{
		VBox ans = new VBox();
		VBox entries = new VBox(10);
		
		
		entries.getChildren().addAll(
				map
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
		replace = new TextField(map.get(key));
		replace.textProperty().addListener((observable, oldVal, newVal) ->
			map.put(key, newVal));
		Button remove = new Button("remove entry");
		remove.setOnAction(this::remove);
		getChildren().addAll(new Text(key), replace, remove);
	}
	
	public KeyBindingDisplay()
	{
		TextField keyEntry = new TextField();
		getChildren().add(keyEntry);
		keyEntry.focusedProperty().addListener((observable, oldVal, newVal) ->
		{
			if (!newVal)
			{
				String text = keyEntry.getText();
				if (text.isEmpty())
					remove(null);
				map.put(keyEntry.getText(), "");
				// Replace the TextField with a Text
				getChildren().replaceAll(n -> n == keyEntry ? new Text(keyEntry.getText()) : n);
			}
		});
		
		replace = new TextField();
		replace.textProperty().addListener((observable, oldVal, newVal) ->
			map.put(((Text) getChildren().get(0)).getText(), newVal));
		Button remove = new Button("remove entry");
		remove.setOnAction(this::remove);
		getChildren().addAll(replace, remove);
	}
	
	private void remove(ActionEvent event)
	{
		map.remove(((Text) getChildren().get(0)).getText());
		((VBox) this.getParent()).getChildren().remove(this);
	}
}