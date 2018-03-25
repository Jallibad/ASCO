package gui;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.MalformedExpressionException;
import logic.NormalForm;

public class LogicApplication extends Application
{
	ExpressionEntry e = new ExpressionEntry();
	Stage primaryStage;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		this.primaryStage = primaryStage;
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
		load.setOnAction(event ->
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File toOpen = fileChooser.showOpenDialog(primaryStage);
			//toOpen.canRead(); // TODO load file
		});
		menuFile.getItems().addAll(load);
		
		Menu menuEdit = new Menu("Edit");
		menuEdit.setOnShowing(event ->
			menuEdit.getItems().forEach(item -> // It might be better to only disable certain options
				item.setDisable(!e.validExpression())));
		
		MenuItem simplify = new MenuItem("Simplify");
		
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
					Button b = new Button("Show steps?");
					ExpressionDisplay n = new ExpressionDisplay(t.transform(e.getExpression()), b);
					b.setOnAction(event2 ->
					{
						// TODO
						System.out.println("Showing steps");
						System.out.println("First removing show steps button");
						root.getChildren().remove(n);
					});
					root.getChildren().add(n);
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
		
		MenuItem proveEquivalence = new MenuItem("Prove Equivalence");
		
		menuEdit.getItems().addAll(simplify, normalForm, proveEquivalence);
		
		menuBar.getMenus().addAll(menuFile, menuEdit);
		root.getChildren().add(menuBar);
	}
}