package gui;

import java.awt.Button;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

public class AboutDisplay extends Stage
{
	public AboutDisplay(Stage owner)
	{
		setTitle("About");
		
		final StackPane root = new StackPane();
		AnchorPane editRoot = new AnchorPane();
		
		Scene scene = new Scene(root, 300, 250);
		setScene(scene);
		show();
		
		HBox fileRoot = new HBox();
		VBox sideMenu = new VBox();
		
		sideMenu.fillWidthProperty();
		Button aboutUsButton = new Button("About Us");
		
		
		
	}

	public static void display(Stage owner)
	{
		new AboutDisplay(owner);
	}
}
