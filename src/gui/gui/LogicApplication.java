package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Expression;
import logic.MalformedExpressionException;
import logic.NormalForm;

public class LogicApplication extends Application
{
	ExpressionEntry expressionEntry = new ExpressionEntry();
	Stage primaryStage;
	Scene s;
	
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
		s = new Scene(root, 300, 300, Color.WHITESMOKE);
		
		setUpMenu(root);
		
		root.getChildren().add(expressionEntry);
		primaryStage.setScene(s);
		primaryStage.show();
	}
	
	private void exportScreen() {
		 WritableImage writableImage = s.snapshot(null);
         File file = new File("screenCap.png");
         try {
             ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
             System.out.println("Captured: " + file.getAbsolutePath());
         } catch (IOException ex) {
             System.out.println("Error: unable to write screen capture to output file.");
         }
	}
	
	private void setUpMenu(Pane root) // TODO I'm not sure if Pane is the best type here
	{
		MenuBar menuBar = new MenuBar();
		
		Menu menuFile = new Menu("File");
		MenuItem save = new MenuItem("Save");
		save.setOnAction(event ->
		{
			try
			{
				save(expressionEntry.getExpression());
			}
			catch (MalformedExpressionException e)
			{
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Error");
				error.setContentText("The current expression is invalid and cannot be saved");
				error.showAndWait();
			}
		});		
		
		MenuItem export = new MenuItem("Export");
		export.setOnAction(event -> {
			exportScreen();
		});
		
		MenuItem load = new MenuItem("Load");
		load.setOnAction(event ->
			loadFile().ifPresent(e -> expressionEntry.setExpression((Expression) e)));
		menuFile.getItems().addAll(save, load, export);
		
		Menu menuEdit = new Menu("Edit");
		menuEdit.setOnShowing(event ->
			menuEdit.getItems().forEach(item -> // It might be better to only disable certain options
				item.setDisable(!expressionEntry.validExpression())));
		
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
				Expression e;
				try
				{
					e = expressionEntry.getExpression();
				}
				catch (MalformedExpressionException exception)
				{
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Error");
					error.setContentText("The expression is malformed"); // TODO give helpful information here
					error.showAndWait();
					return;
				}
				Button b = new Button("Show steps?");
				ExpressionDisplay n = new ExpressionDisplay(t.transform(e), b);
				b.setOnAction(event2 ->
				{
					// TODO
					System.out.println("Showing steps");
					System.out.println("First removing show steps button");
					root.getChildren().remove(n);
					StepsDisplay s = new StepsDisplay(t.transformWithSteps(e));
					root.getChildren().add(s);
				});
				root.getChildren().add(n);
			});
		});
		
		MenuItem proveEquivalence = new MenuItem("Prove Equivalence");
		
		menuEdit.getItems().addAll(simplify, normalForm, proveEquivalence);
		
		menuBar.getMenus().addAll(menuFile, menuEdit);
		root.getChildren().add(menuBar);
	}
	
	private void save(Object toSave)
	{
		try
		{
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SER files (*.ser)", "*.ser");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setTitle("Save Resource File");
			// TODO set extension filters
			File toOpen = fileChooser.showSaveDialog(primaryStage);
			if (toOpen == null)
				return;
			FileOutputStream fileOut = new FileOutputStream(toOpen);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(toSave);
			out.close();
		}
		catch (IOException e)
		{
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setContentText("Could not save");
			error.showAndWait();
		}
	}
	
	private Optional<Object> loadFile()
	{
		try
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			// TODO set extension filters
			File toOpen = fileChooser.showOpenDialog(primaryStage);
			if (toOpen == null)
				return Optional.empty();
			FileInputStream fileIn = new FileInputStream(toOpen);
			Optional<Object> ans = Optional.of(new ObjectInputStream(fileIn).readObject());
			fileIn.close();
			return ans;
		}
		catch (IOException | ClassNotFoundException e)
		{
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setContentText("The chosen file could not be read");
			error.showAndWait();
			return Optional.empty();
		}
	}
}