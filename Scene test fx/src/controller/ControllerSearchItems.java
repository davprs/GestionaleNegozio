package controller;

import java.util.LinkedList;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class ControllerSearchItems extends ControllerLogin{
	
    public void initialize() {
    	LinkedList <String> categorie = new LinkedList<String>();
    	categorie.add("ciao");
    	categorie.add("lollone");
    	
    	for(String cat : categorie) {
    		addButton(cat);
    	}
    }
    
    private void addButton(String categoryName) {
    	Button button = new Button(categoryName);
    	button.setPrefWidth(categoryVbox.getPrefWidth());
    	button.setAlignment(Pos.TOP_CENTER);
    	categoryVbox.setSpacing(5);
    	categoryVbox.setAlignment(Pos.TOP_CENTER);
    	
    	button.setOnAction(e->{

    	      //register and execute query
    		System.out.println("tastone categoria");
    	   });
    	
    	
    	categoryVbox.getChildren().add(button);
    }

}
