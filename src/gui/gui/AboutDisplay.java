package gui;

import javafx.scene.control.Button;
import java.awt.Dimension;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

public class AboutDisplay extends Stage
{
	
	public static void display(Stage owner)
	{
		new AboutDisplay(owner);
	}
	
	
	private AboutDisplay(Stage owner)
	{
		setTitle("About");
		
		final StackPane root = new StackPane();
		
		
		HBox fileRoot = new HBox();
		VBox displayStuff = new VBox();
		HBox menu = new HBox();
		
		menu.setPadding(new Insets(15, 12, 15, 12));
		
		
		BorderPane border = new BorderPane();
		border.setTop(menu);
		border.setBottom(displayStuff);
		
		
		Button aboutUsButton = new Button("About Us");
		
		Text aboutText = new Text("Logic++ is a first order logic application that supports blah");
		
		aboutUsButton.setOnAction(event ->
		{
			displayStuff.getChildren().clear();
			displayStuff.getChildren().add(aboutText);
		});
		
		Button syntaxButton = new Button("Syntax");
		syntaxButton.setPrefWidth(100);
		
		displayStuff.getChildren().add(new Text("Testing"));
		
		menu.setAlignment(Pos.TOP_CENTER);
		displayStuff.setAlignment(Pos.CENTER_LEFT);
		
		menu.getChildren().addAll(aboutUsButton, syntaxButton, displayStuff);
		root.getChildren().addAll(menu);
		
		Scene scene = new Scene(root, 400, 400);
		setScene(scene);
		show();
		
		
		
		
	}

}
