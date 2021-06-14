package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

import controller.ControllerLogin;
import controller.ControllerSearchItems;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class utils {
	
	public static void showPopupPane(String txt) {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(txt));
        Button btn = new Button("Chiudi");
        btn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dialog.close();
			}
		});
        dialogVbox.getChildren().add(btn);
        Scene dialogScene = new Scene(dialogVbox, 400, 200);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
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
	
	public static void createClientUI(Connection conn, Integer id) {
		changeUI("UsersUI.fxml", new controller.ControllerSearchItems(conn, id));
		
	}
	
	public static void createWorkerUI(Connection conn, Integer id) {
		changeUI("WorkerUI.fxml", new controller.ControllerLogin(conn, id));
	}
	
	public static void createResponsableUI(Connection conn, Integer id) {
		changeUI("ResponsableUI.fxml", new controller.ControllerLogin(conn, id));
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
