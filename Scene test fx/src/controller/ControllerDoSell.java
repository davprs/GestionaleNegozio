package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ControllerDoSell extends ControllerLogin{

    public void initialize() {
    	
    	String[] keys =
        {
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "#", "0", "d"
        };
    	
    	textSell.setDisable(true);
    	textSell.setOpacity(200);
        
        GridPane numPad = new GridPane();
        numPad.setPrefWidth(sellNumPane.getPrefWidth()+100);
        numPad.setMaxWidth(Double.MAX_VALUE);
        for (int i = 0; i < 12; i++)
        {
            Button button = new Button(keys[i]);
            button.setPadding(new Insets(18, 30, 18, 30));
            button.setPrefWidth(numPad.getPrefWidth());
            button.setMaxWidth(Double.MAX_VALUE);
            //button.getStyleClass().add("num-button");
            if (! button.getText().equals(keys[11])) {
	            button.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						textSell.setText(textSell.getText() + button.getText());
					}
				});
            } else {
            		button.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						String txt = textSell.getText();
						if (txt.length() > 0) {
							textSell.setText(txt.substring(0, txt.length()-1));
						}
					}
				});
            }
            numPad.add(button, i % 3, (int) Math.ceil(i / 3));
        }

        Button enter = new Button("Enter");
        enter.setId("enterSellBtn");
        
        enter.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				textSell.setText("impl");
			}
		});
        
        enter.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        enter.setMinWidth(80);
        enter.setStyle("-fx-background-color: #aaffaa");
        numPad.add(enter, 4, 0);
        
        GridPane.setRowSpan(enter, 4);
        GridPane.setVgrow(enter, Priority.ALWAYS);
        GridPane.setHgrow(enter, Priority.ALWAYS);
        
        Button del = new Button("Cancella");
        del.setId("delSellBtn");
        
        del.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				textSell.setText("");
			}
		});
        
        del.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        numPad.add(del, 0, 4);
        del.setPadding(new Insets(20, 0, 20, 0));
        
        GridPane.setColumnSpan(del, 4);
        GridPane.setHgrow(del, Priority.ALWAYS);
        
        
        Button call = new Button("Termina");
        call.setId("saveSellBtn");
        call.setStyle("-fx-background-color: #ffaaaa");
        call.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				textSell.setText("impl");
			}
		});
        
        
        call.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        numPad.add(call, 3, 4);
        
        GridPane.setColumnSpan(call, 4);
        GridPane.setHgrow(call, Priority.ALWAYS);
        
        sellNumPane.getChildren().addAll(numPad);
    }

}
