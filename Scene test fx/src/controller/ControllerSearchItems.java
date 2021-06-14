package controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;

import com.mysql.cj.result.Row;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ControllerSearchItems extends ControllerLogin{
	
	final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
	private final static String showAllBtnTxt = "Tutto";
	private static String selCat = showAllBtnTxt;
	
    public ControllerSearchItems(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public void initialize() {
		
		LinkedList <String> categorie = new LinkedList<String>();
		categorie.add(showAllBtnTxt);
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				String query = "SELECT nome FROM categoria;";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				while ( rs.next() ) { 
					int numCol =rs.getMetaData().getColumnCount(); 
					for ( int i = 1 ; i <= numCol ; i++ ) 
					{
						categorie.add((String) rs.getObject(i));
					} 
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    	for(String cat : categorie) {
    		addButton(cat);
    	}
    	showProducts();
    }
    
    private void addButton(String categoryName) {
    	Button button = new Button(categoryName);
    	button.setPrefWidth(categoryVbox.getPrefWidth());
    	button.setAlignment(Pos.TOP_CENTER);
    	categoryVbox.setSpacing(5);
    	categoryVbox.setAlignment(Pos.TOP_CENTER);
    	
    	button.setOnAction(e->{
    		
    		for(Node n : categoryVbox.getChildren()) {
    			n.setStyle(null);
    		}
    		
    		button.setStyle("-fx-background-color: #aaaaff");
    		findItemsTxtField.setText("");
    		selCat = categoryName;
    		showProducts(categoryName);
    		saveCategoryStat(categoryName);
    	   });
    	if(categoryName.equals(showAllBtnTxt)) {
    		button.setStyle("-fx-background-color: #aaaaff");
    	}
    	
    	
    	categoryVbox.getChildren().add(button);
    }

    
    private void showProducts() {
    	Statement stmt = null;
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				String query = "SELECT nome_prodotto AS NOME, categoria AS CATEGORIA, prezzo_vendita AS PREZZO, quantità_disponibile as QTA  FROM prodotto;";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				
				productsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				int numCol =rs.getMetaData().getColumnCount(); 
				for ( int i = 1 ; i <= numCol ; i++ ) 
				{
					System.out.println(rs.getMetaData().getColumnName(i));
					final int j = i -1;                
					TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i));
					col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
			            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
			                return new SimpleStringProperty(param.getValue().get(j).toString());                        
			            }                    
			        });
					productsTableView.getColumns().add(col); 						
				}
				
				
				while ( rs.next() ) { 
					
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add((String)rs.getObject(1));
					row.add((String)rs.getObject(2));
					row.add(((BigDecimal)rs.getObject(3)).toString());
					row.add(((Integer)rs.getObject(4)).toString());

					System.out.println("AAAAAAAAAAAAAAAAAAAAAAA" + row);
					
					data.add(row);
					productsTableView.setItems(data);
					
					
					
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
    }
    
    
    private void showProducts(String categoryName) {
    	Statement stmt = null;
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				String query = null;
				if (categoryName.equals(showAllBtnTxt)) {
					query = "SELECT nome_prodotto AS NOME, categoria AS CATEGORIA, prezzo_vendita AS PREZZO, quantità_disponibile as QTA  FROM prodotto;";
				} else {
					query = "SELECT nome_prodotto AS NOME, categoria AS CATEGORIA, prezzo_vendita AS PREZZO, quantità_disponibile as QTA  FROM prodotto WHERE CATEGORIA = \""+ categoryName + "\";";
				}
				
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				productsTableView.getItems().clear(); 
				productsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				
				while ( rs.next() ) { 
					
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add((String)rs.getObject(1));
					row.add((String)rs.getObject(2));
					row.add(((BigDecimal)rs.getObject(3)).toString());
					row.add(((Integer)rs.getObject(4)).toString());

					System.out.println("AAAAAAAAAAAAAAAAAAAAAAA" + row);
					
					data.add(row);
					productsTableView.setItems(data);
					
					
					
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
    }
    
    
    private void showProductsFilter(String uQuery) {
    	Statement stmt = null;
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				String query = null;
				if(selCat.equals(showAllBtnTxt)) {
					query = "SELECT nome_prodotto AS NOME, categoria AS CATEGORIA, prezzo_vendita AS PREZZO, quantità_disponibile as QTA  FROM prodotto WHERE nome_prodotto LIKE \'%"+ uQuery + "%\';";
				} else {
					query = "SELECT nome_prodotto AS NOME, categoria AS CATEGORIA, prezzo_vendita AS PREZZO, quantità_disponibile as QTA  "+
							"FROM prodotto WHERE nome_prodotto LIKE \'%"+ uQuery + "%\' AND categoria = \'" + selCat + "\';";					
				}
								
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				productsTableView.getItems().clear(); 
				productsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				
				while ( rs.next() ) { 
					
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add((String)rs.getObject(1));
					row.add((String)rs.getObject(2));
					row.add(((BigDecimal)rs.getObject(3)).toString());
					row.add(((Integer)rs.getObject(4)).toString());

					System.out.println("AAAAAAAAAAAAAAAAAAAAAAA" + row);
					
					data.add(row);
					productsTableView.setItems(data);
					
					
					
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
    }
    
    private void saveCategoryStat(String categoryName) {
    	Statement stmt = null;
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDate lt = LocalDate.now();
		if(stmt != null) {
			try {
				String query = null;
				query = "INSERT INTO ricerca(data, categoria) "
						+ "VALUES(\"" + lt.toString() + "\", \"" + categoryName + "\");";
								
				System.out.println(query);
				stmt.executeUpdate(query);
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    private void saveSearchName(String searchName) {
    	Statement stmt = null;
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDate lt = LocalDate.now();
		if(stmt != null) {
			try {
				String query = null;
				query = "INSERT INTO ricerca(data, nome) "
						+ "VALUES(\"" + lt.toString() + "\", \"" + searchName + "\");";
								
				System.out.println(query);
				stmt.executeUpdate(query);
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    @FXML
    private void handleNameSearchAction() {
    	String searchName = findItemsTxtField.getText();
    	showProductsFilter(searchName);
    	saveSearchName(searchName);
    }
    
}
