package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

import controller.ControllerLogin;
import controller.ControllerSearchItems;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class utils {
	
	private static void changeUI(String path, ControllerLogin controller) {
		//FXMLLoader fxml = application.Main.getFXMLLoader();
		
		FXMLLoader fxml = new FXMLLoader();
		fxml.setController(controller);
		
		
		fxml.setLocation((URL) MainApp.class.getResource(path));
		Parent root = null;
		try {
			root = fxml.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scene scene = new Scene(root,1200,700);
		
		//scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
		Stage stage = application.MainApp.getStage();
		stage.setScene(scene);
		application.MainApp.setStage(stage);
		stage.setMinWidth(1200);
		stage.setMinHeight(700);
		
		stage.show();
	}
	
	public static void createClientUI(Connection conn) {
		changeUI("UsersUI.fxml", new controller.ControllerSearchItems(conn));
		
	}
	
	public static void createWorkerUI(Connection conn) {
		changeUI("WorkerUI.fxml", new controller.ControllerLogin(conn));
	}
	
	public static void swapPane(Pane innerPane, ControllerLogin controller, String UIPath) {
		Object newPane = null;
		SplitPane newLoadedPane = null;
		VBox newLoadedVbox = null;
		FXMLLoader fxml = new FXMLLoader();
		fxml.setController(controller);
		fxml.setLocation((URL) MainApp.class.getResource(UIPath));
		try {
			newPane = fxml.load();
			if (newPane instanceof SplitPane) {
				newLoadedPane = (SplitPane) newPane;
			} else if (newPane instanceof VBox) {
				newLoadedVbox = (VBox) newPane;
			} else {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (newLoadedPane != null) {
			innerPane.getChildren().setAll(newLoadedPane);	
			
			
			VBox root = ((VBox) innerPane.getParent());
	
			newLoadedPane.prefWidthProperty().bind(root.widthProperty());
			newLoadedPane.prefHeightProperty().bind(root.heightProperty());
			
			root.widthProperty().addListener((obs, oldVal, newVal) -> {
				System.out.println("lllll\n");
			});
		} else { /*if (newLoadedVbox != null) */
			innerPane.getChildren().setAll(newLoadedVbox);
			
			
			VBox root = ((VBox) innerPane.getParent());
	
			newLoadedVbox.prefWidthProperty().bind(root.widthProperty());
			newLoadedVbox.prefHeightProperty().bind(root.heightProperty());
			
			for (Node reg : newLoadedVbox.getChildren()) {
				
				((Pane)reg).prefWidthProperty().bind(newLoadedVbox.widthProperty());
				((Pane)reg).prefHeightProperty().bind(newLoadedVbox.heightProperty());
			}
			
			root.widthProperty().addListener((obs, oldVal, newVal) -> {
				System.out.println("lllll\n");
			});
		}
	}

}
