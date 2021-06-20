package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.sun.media.jfxmedia.locator.ConnectionHolder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class MainApp extends Application {
	
	public static FXMLLoader fxmlloader;
	public static Stage stage;
	private static Connection conn = null;
	
	
	@Override
	public void start(Stage primaryStage) {
		String connectionUrl = "jdbc:mysql://";
		try {
			try (BufferedReader reader = new BufferedReader(new FileReader(new File("config")))) {

		        String line;
		        int c = 0;
		        while ((line = reader.readLine()) != null) {
		        	if(c == 0) {
		        		connectionUrl += line + "/";		        		
		        	} else if(c == 1) {
		        		connectionUrl += line + "?user=";
		        	} else if(c == 2) {
		        		connectionUrl += line + "&password=";
		        	} else if(c == 3) {
		        		connectionUrl += line;
		        	}
		        	
		        	c++;
		        }
		        
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			//conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/db_prog2?user=root&password=password");
			conn = DriverManager.getConnection(connectionUrl);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			application.utils.showPopupPane(e.toString());
		}
		
		stage = primaryStage;
		
		try {

			fxmlloader = new FXMLLoader();
			fxmlloader.setController(new controller.ControllerUI(conn, -1));
			fxmlloader.setLocation((URL) getClass().getResource("Sample.fxml"));
			
			Parent root = fxmlloader.load();
			
			Scene scene = new Scene(root,1200,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println(conn);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static FXMLLoader getFXMLLoader() {
		return fxmlloader;
	}
	
	public void setFXMLLoader(FXMLLoader fxml) {
		MainApp.fxmlloader = fxml;
	}
	
	public static Stage getStage() {
		return stage;
	}
	
	public static void setStage(Stage stage) {
		MainApp.stage = stage;
	}
	
	public static Connection getConnection() {
		return conn;
	}
}
