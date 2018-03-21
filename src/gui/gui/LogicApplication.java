package gui;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.MalformedExpressionException;
import logic.NormalForm;

public class LogicApplication extends Application
{
	ExpressionEntry e = new ExpressionEntry();
	
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
		
		setUpMenu(root);
		
		root.getChildren().add(e);
		primaryStage.setScene(s);
		primaryStage.show();
	}
	
	private void setUpMenu(Pane root) // TODO I'm not sure if Pane is the best type here
	{
		MenuBar menuBar = new MenuBar();
		
		Menu menuFile = new Menu("File");
		MenuItem load = new MenuItem("Load");
		menuFile.getItems().addAll(load);
		
		Menu menuEdit = new Menu("Edit");
		MenuItem normalForm = new MenuItem("Transform to Normal Form");
		normalForm.setOnAction(event ->
		{
			ChoiceDialog<NormalForm> dialog = new ChoiceDialog<NormalForm>(NormalForm.CONJUNCTIVE, NormalForm.values());
			dialog.setTitle("Test"); // TODO Change
			dialog.setContentText("Choose a normal form:");
			Optional<NormalForm> result = dialog.showAndWait();
			result.ifPresent(t ->
			{
				try
				{
					System.out.println(e.getExpression());
					System.out.println(t.transform(e.getExpression()));
				}
				catch (MalformedExpressionException e)
				{
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Error");
					error.setContentText("The expression is malformed"); // TODO give helpful information here
					error.showAndWait();
				}
			});
		});
		menuEdit.getItems().addAll(normalForm);
		
		menuBar.getMenus().addAll(menuFile, menuEdit);
		root.getChildren().add(menuBar);
	}
}