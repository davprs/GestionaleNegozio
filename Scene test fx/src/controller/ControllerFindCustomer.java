package controller;

import java.sql.Connection;
import java.util.LinkedList;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class ControllerFindCustomer extends ControllerLogin{
	
	public ControllerFindCustomer(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	public void initialize() {
		LinkedList <String> categorie = new LinkedList<String>();
		categorie.add("Modifica");
		categorie.add("Rimuovi");
		
		for(String cat : categorie) {
			addButton(cat);
		}
	}
	
	private void addButton(String categoryName) {
		Button button = new Button(categoryName);
		button.setPrefWidth(findCustomerVbox.getPrefWidth());
		button.setAlignment(Pos.TOP_CENTER);
		findCustomerVbox.setSpacing(5);
		findCustomerVbox.setAlignment(Pos.TOP_CENTER);
		button.setMaxWidth(Double.MAX_VALUE);
		button.setDisable(true);
		
		button.setOnAction(e->{
	
		      //register and execute query
			System.out.println("tastone categoria");
		   });
		
		
		findCustomerVbox.getChildren().add(button);
	}

}
