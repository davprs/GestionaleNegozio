package controller;

import java.sql.Connection;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ControllerFindSell extends ControllerLogin{

    public ControllerFindSell(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}

	public void initialize() {
        String[] keys =
        {
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "C", "0", "d"
        };
        
        findTextSell.setDisable(true);
        findTextSell.setOpacity(200);
        
        GridPane numPad = new GridPane();
        //numPad.setPrefWidth(findSellNumPane.getPrefWidth()+100);

        for (int i = 0; i < 12; i++)
        {
            Button button = new Button(keys[i]);
            button.setPadding(new Insets(18, 30, 18, 30));
            //button.getStyleClass().add("num-button");
            if (! button.getText().equals(keys[9])) {
            	
            	if (button.getText().equals(keys[11])) {
            		button.setOnAction(new EventHandler<ActionEvent>() {
    					
    					@Override
    					public void handle(ActionEvent arg0) {
    						// TODO Auto-generated method stub
    						String txt = findTextSell.getText();
    						if (txt.length() > 0) {
    							findTextSell.setText(txt.substring(0, txt.length()-1));
    						}
    					}
            		});
            	} else {
	            	
		            button.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent arg0) {
							// TODO Auto-generated method stub
							findTextSell.setText(findTextSell.getText() + button.getText());
						}
					});
            	}
            } else {
            	button.setStyle("-fx-background-color: #ffaaaa");

            		button.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						findTextSell.setText("");
					}
				});
            }
            numPad.add(button, i % 3, (int) Math.ceil(i / 3));
        }
        
        
        Button call = new Button("Cerca");
        call.setId("findSell");
        call.setStyle("-fx-background-color: #aaffaa");
        
        call.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				findTextSell.setText("impl");
			}
		});
        
        
        call.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        numPad.add(call, 0, 4);
        
        GridPane.setColumnSpan(call, 3);
        GridPane.setHgrow(call, Priority.ALWAYS);
        
        findSellNumPane.getChildren().addAll(numPad);
    }

}
