package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LogicApplication extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("Logic++");
		VBox root = new VBox();
		Scene s = new Scene(root, 300, 300, Color.WHITESMOKE);
		root.getChildren().add(new ExpressionEntry());
		root.getChildren().add(new ExpressionEntry());
		root.getChildren().add(new ExpressionEntry());
		root.getChildren().add(new ExpressionEntry());
		root.getChildren().add(new ExpressionEntry());
		primaryStage.setScene(s);
		primaryStage.show();
	}
}