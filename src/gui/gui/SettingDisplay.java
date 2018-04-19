package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class SettingDisplay extends Stage
{
	
	public static void display(Stage owner)
	{
		new SettingDisplay(owner);
	}
	
	private SettingDisplay(Stage owner){
		
		setTitle("Settings");
		
		VBox box = new VBox();
		box.setPadding(new Insets(5,10,10,10));
		HBox hButtons = new HBox();
		
		Button closeButton = new Button("Close");
		closeButton.setOnAction(event -> close());
		hButtons.getChildren().add(closeButton);
		
		BorderPane spacing = new BorderPane();	
		spacing.setBottom(hButtons);
		
		
		Scene scene = new Scene(box, 600, 400);
		setScene(scene);
		
		box.getChildren().add(new Text("This is the settings page"));
		box.getChildren().add(hButtons);
		show();
	}
}
