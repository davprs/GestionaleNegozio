package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

public class ControllerAddProduct extends ControllerLogin{
	final ObservableList<String> dataSupp = FXCollections.observableArrayList();
	final ObservableList<ObservableList<String>> dataCat = FXCollections.observableArrayList();
			
	public ControllerAddProduct(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}

	public void initialize() {
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
					categoriesProdLV.getItems().addAll(rs.getObject(1).toString());
				}
								
				
				query = "SELECT nome FROM fornitore;";
				System.out.println(query);
				rs = stmt.executeQuery(query);
				
				while ( rs.next() ) { 
					supplierProdLV.getItems().addAll(rs.getObject(1).toString());
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				utils.showPopupPane(e.toString());
			}
		}
		
		
		categoriesProdLV.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				prodCatTF.setText(categoriesProdLV.getSelectionModel().getSelectedItem());
			}
		});
		
		supplierProdLV.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				prodSupplierTF.setText(supplierProdLV.getSelectionModel().getSelectedItem());
			}
		});
		
		
	}

	@FXML
	private void prodClean() {
		prodCodeTF.setText("");
		prodSupplierTF.setText("");
		prodNameTF.setText("");
		prodBuyTF.setText("");
		prodSellTF.setText("");
		prodCatTF.setText("");
	}
	
	@FXML
	private void prodAdd() {
		
		String code, pname, cat, bprice, sprice, sname;
		
		code = prodCodeTF.getText();
		pname = prodNameTF.getText();
		cat = prodCatTF.getText();
		bprice = prodBuyTF.getText();
		sprice = prodSellTF.getText();
		sname = prodSupplierTF.getText();
		
		if(code.isBlank() || pname.isBlank() || cat.isBlank() || bprice.isBlank() || sprice.isBlank() || sname.isBlank()) {
			return;
		}
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				
				String query = "SELECT nome_prodotto from prodotto where codice_prod = " + code + ";";
				System.out.println(query);
				ResultSet res = stmt.executeQuery(query);

				//se il prodotto è già in catalogo aggiorna i dati, altrimenti li scrive da zero
				if(res.next()) {
					query = "UPDATE prodotto SET "
							+ "nome_prodotto = \"" + pname + "\", "
							+ "categoria = \"" + cat + "\", "
							+ "prezzo_vendita = \"" + sprice + "\", "
							+ "prezzo_acquisto = \"" + bprice + "\" "
							+ "WHERE codice_prod = " + code + ";";
					System.out.println(query);
					int rs = stmt.executeUpdate(query);
				} else {
					 query = "INSERT INTO prodotto (codice_prod, nome_prodotto, categoria, prezzo_vendita, prezzo_acquisto, quantità_disponibile) VALUES "
								+ "(" + code + ", \"" + pname + "\", \"" + cat + "\", " + bprice + ", " + sprice + ", 0 );";
						System.out.println(query);
						int rs = stmt.executeUpdate(query);
				}
				
				
				
				
				
				 query = "SELECT codice_fornitore from fornitore where nome = \"" + sname + "\";";
				System.out.println(query);
				res = stmt.executeQuery(query);

				//se il fornitore è già nel DB come venditore di quel prodotto non fa nulla, altrimenti lo setta come fornitore del prodotto
				if(res.next()) {
					String scode = res.getObject(1).toString();
					query = "SELECT * FROM prodotto_di_fornitore WHERE codice_prod = " + code + ";";
					System.out.println(query);
					res = stmt.executeQuery(query);
					if(res.next()) {	//se il prodotto è distribuito da un altro fornitore, cambia fornitore
						query = "UPDATE prodotto_di_fornitore SET "
								+ "codice_fornitore = " + scode + " "
								+ "WHERE codice_prod = " + code + ";";
						System.out.println(query);
						int rs = stmt.executeUpdate(query);
					} else {
						query = "INSERT INTO prodotto_di_fornitore (codice_prod, codice_fornitore) VALUES ( "
								+ code + ", " + scode + ");";
						System.out.println(query);
						int rs = stmt.executeUpdate(query);
						
					}
					
				
				} else {
					query = "INSERT INTO fornitore(nome) VALUES (\"" + sname + "\");";
					System.out.println(query);
					int rs = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
					ResultSet lastId = stmt.getGeneratedKeys();
					lastId.next();
					query = "INSERT INTO prodotto_di_fornitore(codice_prod, codice_fornitore) VALUES (" + code + ", "+ lastId.getObject(1).toString() + ");";
					System.out.println(query);
					rs = stmt.executeUpdate(query);
				}

				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				utils.showPopupPane(e.toString());
			}
		}
	}
}	
