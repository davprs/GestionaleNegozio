package controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

public class ControllerFindSell extends ControllerLogin{
	final private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    public ControllerFindSell(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}

	public void initialize() {
		totalFindSellTF.setOpacity(200);
		customerNameLbl.setVisible(false);
		
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
				displaySell();
			}
		});
        
        
        call.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        numPad.add(call, 0, 4);
        
        GridPane.setColumnSpan(call, 3);
        GridPane.setHgrow(call, Priority.ALWAYS);
        
        findSellNumPane.getChildren().addAll(numPad);
    }
	
	private void displaySell() {
		String sellCode = findTextSell.getText();
		if(sellCode.isBlank()) {
			return;
		}
		
		Statement stmt = null;
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			application.utils.showPopupPane(e.toString());
		}
		
		if(stmt != null) {
			try {
				String query = "(SELECT PV.codice_prod, P.nome_prodotto, PV.prezzo_quando_venduto as prezzo, PV.quantità, V.numero_cliente_tesserato, J.nome, J.cognome, V.giorno_saldo "
						+ "			FROM prodotto_in_vendita PV, vendita V, prodotto P, ( "
						+ "				select nome, cognome from persona P , cliente_tesserato CT, vendita V "
						+ "					WHERE P.email = CT.email AND CT.numero_tessera = V.numero_cliente_tesserato AND V.codice_scontrino = " + sellCode + ") as J "
						+ "				WHERE PV.codice_scontrino = V.codice_scontrino AND PV.codice_prod = P.codice_prod AND V.codice_scontrino = " + sellCode + ")"
						+ "		UNION "
						+ "			(SELECT PV.codice_prod, P.nome_prodotto, PV.prezzo_quando_venduto as prezzo, PV.quantità, null, null, null, V.giorno_saldo "
						+ "				FROM prodotto_in_vendita PV, vendita V, prodotto P "
						+ "				WHERE PV.codice_scontrino = V.codice_scontrino AND PV.codice_prod = P.codice_prod AND V.codice_scontrino = " + sellCode + ") LIMIT 1;";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				findSellTV.getItems().clear();
				findSellTV.getColumns().clear();
				
				findSellTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				int numCol =rs.getMetaData().getColumnCount(); 
				for ( int i = 1 ; i <= numCol-4; i++ ) 
				{
					System.out.println(rs.getMetaData().getColumnName(i));
					final int j = i -1;                
					TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i));
					col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
			            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
			                return new SimpleStringProperty(param.getValue().get(j).toString());                        
			            }                    
			        });
					findSellTV.getColumns().add(col); 						
				}
				
				 customerNameLbl.setVisible(false);
				while ( rs.next() ) { 
					
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add(((Integer)rs.getObject(1)).toString());
					row.add((String)rs.getObject(2));
					row.add(((BigDecimal)rs.getObject(3)).toString());
					row.add(((Integer)rs.getObject(4)).toString());
					if(rs.getObject(5) != null) {
						System.out.println();
						customerNameLbl.setText("Cliente : " + ((Integer)rs.getObject(5)).toString() + ", " + rs.getString(6) + " " + rs.getString(7));
						customerNameLbl.setVisible(true);
					}
					sellDateLbl.setText(rs.getString(8));

					
					System.out.println("AAAAAAAAAAAAAAAAAAAAAAA" + row);
					
					data.add(row);
					findSellTV.setItems(data);
					
					
					
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
		
		Double total = 0.0;
		for(ObservableList<String> row : data) {
			total += Double.parseDouble(row.get(2)) * Double.parseDouble(row.get(3));
			total = BigDecimal.valueOf(total).setScale(2, BigDecimal.ROUND_UP).doubleValue();
		}
		totalFindSellTF.setText(Double.toString(total));
		
		
		
		
	}

}
