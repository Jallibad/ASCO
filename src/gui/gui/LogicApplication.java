package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.Expression;
import logic.MalformedExpressionException;
import logic.NormalForm;
import logic.TransformSteps;

public class LogicApplication extends Application
{
	ExpressionEntry expressionEntry = new ExpressionEntry();
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
		
		root.getChildren().add(expressionEntry);
		primaryStage.setScene(s);
		primaryStage.show();
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
		export.setOnAction(event -> exportScreen(root));
		
		MenuItem load = new MenuItem("Load");
		load.setOnAction(event ->
			loadFile().ifPresent(e -> expressionEntry.setExpression((Expression) e)));
		menuFile.getItems().addAll(save, export, load);
		
		Menu menuEdit = new Menu("Edit");
		menuEdit.setOnShowing(event ->
			menuEdit.getItems().forEach(item -> // It might be better to only disable certain options
				item.setDisable(!expressionEntry.validExpression())));
		
		MenuItem simplify = new MenuItem("Simplify");
		
		MenuItem checkForm = new MenuItem("Check Form");
		checkForm.setOnAction(event ->
		{
			Expression ex;
			try
			{
				ex = expressionEntry.getExpression();
			}
			catch (MalformedExpressionException exception)
			{
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("Error");
				error.setContentText("The expression is malformed"); // TODO give helpful information here
				error.showAndWait();
				return;
			}
			
			Stage formStage = new Stage();
			VBox box = new VBox();
			
			Text conjunctiveText = new Text( NormalForm.CONJUNCTIVE.inForm(ex) 
					? "Expression is in conjunctive normal form" : "Expression is not in conjunctive normal form" );
			
			Text disjunctiveText = new Text( NormalForm.DISJUNCTIVE.inForm(ex) 
					? "Expression is in disjunctive normal form" : "Expression is not in disjunctive normal form" );
			
			Text negationText = new Text( NormalForm.CONJUNCTIVE.inForm(ex) 
					? "Expression is in negation normal form" : "Expression is not in negation normal form" );
			
			box.getChildren().add(conjunctiveText);
			box.getChildren().add(disjunctiveText);
			box.getChildren().add(negationText);

			Scene scene = new Scene(box, 300, 250);
			formStage.setScene(scene);
			
			formStage.show();
	
		});
		
		
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
		proveEquivalence.setOnAction(event ->
		{
			ExpressionEntry e2 = new ExpressionEntry();
			Button b = new Button("Ready to prove equivalence");
			b.setOnAction(event2 ->
			{
				root.getChildren().removeAll(e2, b);
				try
				{
					Optional<TransformSteps> steps = expressionEntry.getExpression().proveEquivalence(e2.getExpression());
					if (steps.isPresent())
						root.getChildren().addAll(new Text("The expressions are equivalent"), new StepsDisplay(steps.get()));
					else
						root.getChildren().add(new Text("The expressions are not equivalent"));
				}
				catch (MalformedExpressionException error)
				{
					// TODO Auto-generated catch block
					error.printStackTrace();
				}
			});
			root.getChildren().addAll(e2, b);
		});
		
		MenuItem checkWork = new MenuItem("Check Work");
		checkWork.setOnAction(event ->
		{
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SER files (*.ser)", "*.ser");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setTitle("Select files to check");
			List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
			if (files == null)
				return;
			for (File f : files)
			{
				// TODO say which expression is which
				Node toAdd;
				try
				{
					FileInputStream fileIn = new FileInputStream(f);
					Expression other = (Expression) new ObjectInputStream(fileIn).readObject();
					fileIn.close();
					
					Optional<TransformSteps> steps = expressionEntry.getExpression().proveEquivalence(other);
					if (steps.isPresent())
						toAdd = new StepsDisplay(steps.get());
					else
						toAdd = new Text("The expressions are not equivalent");
				}
				catch (IOException | ClassNotFoundException | MalformedExpressionException e)
				{
					toAdd = new Text(String.format("The file \"%s\" could not be read", f.toString()));
				}
				root.getChildren().add(toAdd);
			}
		});
		
		menuEdit.getItems().addAll(simplify, normalForm, proveEquivalence, checkWork, checkForm);
		
		menuBar.getMenus().addAll(menuFile, menuEdit);
		root.getChildren().add(menuBar);
	}
	
	private void exportScreen(Node toExport)
	{
		WritableImage writableImage = toExport.snapshot(new SnapshotParameters(), null);
		try
		{
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setTitle("Export Steps");
			File file = fileChooser.showSaveDialog(primaryStage);
			if (file == null)
				return;
			ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
			System.out.println("Captured: " + file.getAbsolutePath());
		}
		catch (IOException e)
		{
			System.out.println("Error: unable to write screen capture to output file.");
		}
	}
	
	private void save(Object toSave)
	{
		try
		{
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SER files (*.ser)", "*.ser");
			fileChooser.getExtensionFilters().add(extFilter);
			fileChooser.setTitle("Save Resource File");
			File file = fileChooser.showSaveDialog(primaryStage);
			if (file == null)
				return;
			FileOutputStream fileOut = new FileOutputStream(file);
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
			error.setContentText(e.getMessage());
			error.showAndWait();
			return Optional.empty();
		}
	}
}