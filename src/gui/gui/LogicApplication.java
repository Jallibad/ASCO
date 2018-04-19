package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

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
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import logic.Expression;
import logic.malformedexpression.MalformedExpressionException;
import logic.transform.NormalForm;
import logic.transform.TransformSteps;

public class LogicApplication extends Application
{
	private static final Logger LOGGER = Logger.getLogger(LogicApplication.class.getName());
	
	private static final FileChooser.ExtensionFilter EXT_FILTER = new FileChooser.ExtensionFilter("SER files (*.ser)", "*.ser");
	
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
		VBox secondary = new VBox();
		Scene s = new Scene(root, 300, 300, Color.WHITESMOKE);
			
		setUpMenus(root, secondary);
		
		root.getChildren().add(expressionEntry);
		root.getChildren().add(secondary);
		primaryStage.setScene(s);
		primaryStage.show();
	}
	
	private void setUpMenus(Pane root, Pane secondary)
	{
		MenuBar menuBar = new MenuBar();
		
		Menu menuFile = setUpFileMenu(root);
		Menu menuEdit = setUpEditMenu(root, secondary);
		Menu menuHelp = setUpHelpMenu(root);
		
		menuBar.getMenus().addAll(menuFile, menuEdit, menuHelp);
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
			LOGGER.info("Captured: " + file.getAbsolutePath());
		}
		catch (IOException e)
		{
			LOGGER.severe("Error: unable to write screen capture to output file."); // TODO error handling
		}
	}
	
	private void save(Object toSave)
	{
		try
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(EXT_FILTER);
			fileChooser.setTitle("Save Resource File");
			File file = fileChooser.showSaveDialog(primaryStage);
			if (file == null)
				return;
			try (FileOutputStream fileOut = new FileOutputStream(file))
			{
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(toSave);
				out.close();
			}
		}
		catch (IOException e)
		{
			showAlert("Could not save");
		}
	}
	
	private Optional<Object> loadFile()
	{
		try
		{
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(EXT_FILTER);
			fileChooser.setTitle("Open Resource File");
			File toOpen = fileChooser.showOpenDialog(primaryStage);
			if (toOpen == null)
				return Optional.empty();
			try (FileInputStream fileIn = new FileInputStream(toOpen))
			{
				return Optional.of(new ObjectInputStream(fileIn).readObject());
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			showAlert(e.getMessage());
			return Optional.empty();
		}
	}
	
	private void showAlert(String message)
	{
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Error");
		error.setContentText(message);
		error.showAndWait();
	}
	
	private Menu setUpEditMenu(Pane root, Pane secondary)
	{
		Menu menuEdit = new Menu("Edit");
		menuEdit.setOnShowing(event ->
			menuEdit.getItems().forEach(item -> // It might be better to only disable certain options
				item.setDisable(!expressionEntry.validExpression())));
				
		
		MenuItem clearWork = new MenuItem("Clear Work");
		clearWork.setOnAction(event ->
			secondary.getChildren().clear());
		
		
		MenuItem simplify = new MenuItem("Simplify");
		
		MenuItem checkForm = new MenuItem("Check Form");
		checkForm.setOnAction(event ->
			expressionEntry.getOptionalExpression().ifPresent(ex ->
				CheckFormDisplay.display(ex, primaryStage)));
		
		MenuItem normalForm = new MenuItem("Transform to Normal Form");
		normalForm.setOnAction(event ->
		{
			ChoiceDialog<NormalForm> dialog = new ChoiceDialog<>(NormalForm.CONJUNCTIVE, NormalForm.values());
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
					showAlert("The expression is malformed"); // TODO give helpful information here
					return;
				}
				Button b = new Button("Show steps?");
				ExpressionDisplay n = new ExpressionDisplay(t.transform(e), b);
				b.setOnAction(event2 ->
				{
					// TODO
					LOGGER.fine("Showing steps");
					LOGGER.fine("First removing show steps button");
					root.getChildren().remove(n);
					StepsDisplay s = new StepsDisplay(t.transformWithSteps(e));
					secondary.getChildren().add(s);
				});
				secondary.getChildren().add(n);
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
						secondary.getChildren().addAll(new Text("The expressions are equivalent"), new StepsDisplay(steps.get()));
					else
						secondary.getChildren().add(new Text("The expressions are not equivalent"));
				}
				catch (MalformedExpressionException error)
				{
					// TODO Auto-generated catch block
					LOGGER.severe(error.getMessage());
				}
			});
			secondary.getChildren().addAll(e2, b);
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
				try (FileInputStream fileIn = new FileInputStream(f))
				{
					Expression other = (Expression) new ObjectInputStream(fileIn).readObject();
					
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
				secondary.getChildren().add(toAdd);
			}
		});
		
		menuEdit.getItems().addAll(clearWork, simplify, normalForm, proveEquivalence, checkWork, checkForm);
		return menuEdit;
	}
	
	
	private Menu setUpFileMenu(Pane root)
	{
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
				error.setContentText(String.format
				(
						"The current expression is invalid and cannot be saved%n%s: %s",
						e.getClass().getSimpleName(),
						e.getMessage()
				));
				error.showAndWait();
			}
		});
		
		MenuItem export = new MenuItem("Export");
		export.setOnAction(event -> exportScreen(root));
		
		MenuItem load = new MenuItem("Load");
		load.setOnAction(event ->
			loadFile().ifPresent(e -> expressionEntry.setExpression((Expression) e)));
		
		
		menuFile.getItems().addAll(save, export, load);
		return menuFile;
	}
	
	private Menu setUpHelpMenu(Pane root)
	{
		Menu menuHelp = new Menu("Help");
		
		MenuItem settings = new Menu("Settings");
		settings.setOnAction(event ->
			SettingDisplay.display(primaryStage));
		
//		MenuItem about = new Menu("About");
//		about.setOnAction(event ->
//			AboutDisplay.display(primaryStage));
		
		menuHelp.getItems().add(settings);
		return menuHelp;
		
	}
}