package controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import application.utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ControllerInitMoney extends ControllerLogin{
	
    public ControllerInitMoney(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}

	public void initialize() {
    }
    
    @FXML
    private void handleInitMoneyBtn() {
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
				String query = null;	
				String deskS = deskTF.getText();
				String moneyS = initMoneyTF.getText();
				
				if(deskS.isBlank() || ! ((Integer)(Integer.parseInt(deskS))).toString().equals(deskS)) {
		    		handleClear();
					return ;
		    	}
		    	
		    	deskTF.setDisable(true);
		    	Integer desk = ((Integer)(Integer.parseInt(deskS)));
		    	Double money = BigDecimal.valueOf(((Double)(Double.parseDouble(moneyS)))).setScale(2, BigDecimal.ROUND_UP).doubleValue();
		    	if(money < 0) {
		    		handleClear();
		    		return;
		    	}
				query = "INSERT INTO fondo_cassa (numero_cassa, importo, data_aggiornamento, codice_dipendente) "
						+ "	VALUES(" + desk + ", "
								+ "" + money.toString() + ", NOW(), " + id + ");";
				System.out.println(query);
				int rs = stmt.executeUpdate(query);
				
				utils.showPopupPane("Fondocassa inizializzato con successo!");
		    	handleClear();

			} catch (SQLException e) {
				e.printStackTrace();
				utils.showPopupPane(e.toString());
			}
		}
    }
    
    @FXML
    private void handleClear() {
    	initializedMoneyInfo.setText("");
    	deskTF.setDisable(false);
    	deskTF.setText("");
    	initMoneyTF.setText("");

    }
    
    @FXML
    public void chooseDesk() {
    	String deskS = deskTF.getText();
    	
    	if(deskS.isBlank() || ! ((Integer)(Integer.parseInt(deskS))).toString().equals(deskS) ) {
    		return ;
    	}
    	
    	deskTF.setDisable(true);
    	Integer desk = ((Integer)(Integer.parseInt(deskS)));
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
				String query = "SELECT F.importo, F.data_aggiornamento, F.codice_dipendente, P.nome, P.cognome "
						+ "FROM fondo_cassa F, persona P, dipendente D "
						+ "WHERE F.codice_dipendente = D.codice_dipendente AND D.email = P.email AND F.numero_cassa = " + desk + " AND F.data_aggiornamento IN ("
						+ "	SELECT max(data_aggiornamento) FROM fondo_cassa WHERE numero_cassa = " + desk + ");";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				rs.next();
				
				
		    	initializedMoneyInfo.setText("fondocassa inizializzato dal dipendente :" + rs.getObject(3)+ " (" + rs.getObject(4) + " " + rs.getObject(5)
		    			+ ")\nIl " + rs.getObject(2).toString().replace("T", " alle ore ") + "\nImporto : " + rs.getObject(1));

				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
    	
    	
    	
    }

}
