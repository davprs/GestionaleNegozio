package controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import javafx.util.Pair;

public class ControllerDoSell extends ControllerUI{
	
	final private LinkedList<LinkedList<String>> productsInSell = new LinkedList<>();
	final private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
	private TextField customerCodeSellTF = null;
    
	public ControllerDoSell(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}

	public void initialize() {
		totalDoSellTF.setOpacity(200);
		
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
				addProductInSell();
				textSell.setText("");

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
				productsInSell.removeAll(productsInSell);
				updateOnScreenList();
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
				saveSell();
				productsInSell.removeAll(productsInSell);
				textSell.setText("");
			}
		});
        
        
        call.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        numPad.add(call, 3, 4);
        
        GridPane.setColumnSpan(call, 4);
        GridPane.setHgrow(call, Priority.ALWAYS);
        
        sellNumPane.getChildren().addAll(numPad);
        
        customerCodeSellTF = new TextField();
        Region p = new Region();
        p.setPrefSize(50, 0.0);
        sellNumPane.getChildren().add(p);
        sellNumPane.getChildren().add(new Label("Codice Cliente :"));
        sellNumPane.getChildren().add(customerCodeSellTF);
    }

	private void addProductInSell() {
		String txtField = textSell.getText();
		String code = txtField;
		String qty = "1";
		
		
		if (txtField.contains("#")) {
			code = txtField.split("#")[0];
			qty = txtField.split("#")[1];
		} else {
			textSell.setText("");
		}
		
		Pair<String, String> productInfo = checkProduct(code, qty);
		
		if(productInfo != null) {
			LinkedList<String> inner = new LinkedList<String>();
			inner.addAll(Arrays.asList(productInfo.getKey().toString(), code, qty, ((Double)Double.parseDouble((String) productInfo.getValue())).toString()));
			
			productsInSell.add( inner);	
			updateOnScreenList();
		}

	}
	
	private Pair<String, String> checkProduct(String code, String qty) {
		Statement stmt = null;
		String cost = null;
		String name = null;
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			application.utils.showPopupPane(e.toString());
		}
		
		if(stmt != null) {
			try {
				String query = "select nome_prodotto, prezzo_vendita from prodotto where codice_prod = " + code + " LIMIT 1;";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				
				while ( rs.next() ) { 
					
					ObservableList<String> row = FXCollections.observableArrayList();
					name = rs.getObject(1).toString();
					cost = ((BigDecimal)rs.getObject(2)).toString();

					
					
					
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
		
		if (name != null && cost != null) {
			return new Pair<String, String>(name, cost);			
		} else {
			return null;
		}
		
	}
	
	private void updateOnScreenList() {
		LinkedList<String> colNames = new LinkedList<>();
		colNames.addAll(Arrays.asList("Nome", "Codice", "Quantità", "Costo"));
		
		productsInSellTableView.getItems().clear();
		productsInSellTableView.getColumns().clear();
		
		productsInSellTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		for(String name : colNames) {
			int j = colNames.indexOf(name);
			TableColumn col = new TableColumn(name);
			col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
	            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
	                return new SimpleStringProperty(param.getValue().get(j).toString());                        
	            }                    
	        });
			productsInSellTableView.getColumns().add(col);
		}
		
		
		for(LinkedList<String> inner : productsInSell) {
			ObservableList<String> row = FXCollections.observableArrayList();
			row.add(inner.get(0));
			row.add(inner.get(1));
			row.add(inner.get(2));
			row.add(inner.get(3));

			data.add(row);
			productsInSellTableView.setItems(data);
		}
		
		Double total = 0.0;
		for(ObservableList<String> row : data) {
			total += Double.parseDouble(row.get(3)) * Double.parseDouble(row.get(2));
			total = BigDecimal.valueOf(total).setScale(2, BigDecimal.ROUND_UP).doubleValue();
		}
		totalDoSellTF.setText(Double.toString(total));
		
		
	}
	
	@FXML
	private void saveSell() {
		Statement stmt = null;
		LocalDate ld = LocalDate.now();
		
		
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			application.utils.showPopupPane(e.toString());
		}
		
		if(stmt != null) {
			try {
				String customerCode = customerCodeSellTF.getText();
				String query = null;
				
				String comp = null;
				try {
					comp = ((Integer)(Integer.parseInt(customerCode))).toString();					
				} catch (Exception e) {
					e.printStackTrace();
					comp = "";
				}
				if (comp.equals(customerCode) && ! customerCode.isBlank()) {
					query = "INSERT INTO vendita (codice_scontrino, giorno_saldo, codice_dipendente, numero_cliente_tesserato) "
							+ "VALUES (NULL, \"" +  ld.toString()  + "\", " + id.toString() + ", " + customerCode + ");";	
				} else {
					query = "INSERT INTO vendita (codice_scontrino, giorno_saldo, codice_dipendente, numero_cliente_tesserato) "
							+ "VALUES (NULL, \"" +  ld.toString()  + "\", " + id.toString() + ", NULL);";					
				}
						
				System.out.println(query);
				stmt = conn.createStatement();
				int res = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet lastId = stmt.getGeneratedKeys();
				customerCodeSellTF.setText("");
				
				stmt = conn.createStatement();
				
				String piece = "INSERT INTO prodotto_in_vendita(codice_scontrino, codice_prod, quantità, prezzo_quando_venduto) VALUES ";
				lastId.next();
				String n_scontrino = lastId.getString(1);
				if(res != 0) {
					int i = 0;
					for (LinkedList<String> prod : productsInSell) {
						if(i != 0) {
							piece += ",";
						}
						piece += "(" + n_scontrino+ ", " + prod.get(1) + ", " + prod.get(2) + ", " + prod.get(3) + ")";
						i++;
					}
				
				
					piece += ";";
					System.out.println(piece);
					stmt.executeUpdate(piece);
					
					String update = "UPDATE saldo_giornaliero set entrate = entrate + "
							+ "							(SELECT sum(prezzo_vendita * Q) from prodotto,"
							+ "							(SELECT codice_prod C, quantità Q "
							+ "                            from prodotto_in_vendita\r\n"
							+ "                            where codice_scontrino = " + lastId.getString(1) + ") as proddd"
							+ "                            where codice_prod = C)"
							+ "							where data = \"" + ld.toString() + "\";";
					
					System.out.println(update);
					stmt.executeUpdate(update);
					application.utils.showPopupPane("Vendita effettuata con successo!\n\nCODICE SCONTRINO : " + n_scontrino);
					productsInSell.removeAll(productsInSell);
					updateOnScreenList();
					textSell.setText("");
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
	}
}
