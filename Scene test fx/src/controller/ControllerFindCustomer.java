package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class ControllerFindCustomer extends ControllerLogin{
	
	public ControllerFindCustomer(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}

	public void initialize() {
		Button reg = new Button("Registra");
		Button agg = new Button("Aggiorna");
		Button pul = new Button("Pulisci");
		Button eli = new Button("Elimina");

		reg.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				newCustomer();
				customerNameTF.setText("");
				customerSurnameTF.setText("");
				customerMailTF.setText("");
				customerCityTF.setText("");
				customerCodeTF.setText("");
			}
		});
		agg.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				updateCustomer();
				customerNameTF.setText("");
				customerSurnameTF.setText("");
				customerMailTF.setText("");
				customerCityTF.setText("");
				customerCodeTF.setText("");

			}
		});
		pul.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				customerMailTF.setDisable(false);
				customerNameTF.setText("");
				customerSurnameTF.setText("");
				customerMailTF.setText("");
				customerCityTF.setText("");
				customerCodeTF.setText("");
			}
		});
		
		eli.setOnAction(new EventHandler<ActionEvent>() {
	
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				removeCustomer();
				customerNameTF.setText("");
				customerSurnameTF.setText("");
				customerMailTF.setText("");
				customerCityTF.setText("");
				customerCodeTF.setText("");
			}
		});
		
		searchCustomerBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				customerMailTF.setDisable(false);
				customerNameTF.setText("");
				customerSurnameTF.setText("");
				customerMailTF.setText("");
				customerCityTF.setText("");
				customerCodeTF.setText("");
				findCustomer();
			}
		});
		
		
		addButton(reg);
		addButton(agg);
		addButton(pul);
		addButton(eli);
	}
	
	private void addButton(Button button) {
		button.setPrefWidth(findCustomerVbox.getPrefWidth());
		button.setAlignment(Pos.TOP_CENTER);
		findCustomerVbox.setSpacing(5);
		findCustomerVbox.setAlignment(Pos.TOP_CENTER);
		button.setMaxWidth(Double.MAX_VALUE);
		//button.setDisable(true);
		
		findCustomerVbox.getChildren().add(button);
	}
	
	private void newCustomer() {
		String email = customerMailTF.getText();
		if (email.isBlank() || ! email.contains("@")) {
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
				String query = "INSERT INTO persona(nome, cognome, email, città) "
						+ "VALUES (\"" + customerNameTF.getText() + "\", \"" + customerSurnameTF.getText() + "\", \"" + email + "\", \"" + customerCityTF.getText() + "\");";
				System.out.println(query);
				int rs = stmt.executeUpdate(query);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			String query = "INSERT INTO cliente_tesserato(data_tesseramento, email) "
					+ "VALUES (NOW(), \"" + email + "\");";
			System.out.println(query);
			try {
				int rs = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet lastId = stmt.getGeneratedKeys();
				lastId.next();
				String customerCode = lastId.getObject(1).toString();
				customerCodeTF.setText(customerCode);
				application.utils.showPopupPane("Dati salvati con successo!\n\nCodice nuovo cliente : " + customerCode);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void updateCustomer() {
		String email = customerMailTF.getText();
		if (email.isBlank() || ! email.contains("@")) {
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
				String query = "UPDATE persona SET "
						+ "nome = \"" + customerNameTF.getText() + "\", "
								+ "cognome = \"" + customerSurnameTF.getText() + "\", "
										+ "città = \"" + customerCityTF.getText() + "\" "
												+ "WHERE email = \"" + email + "\";";
				System.out.println(query);
				int rs = stmt.executeUpdate(query);
				
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
	}

	private void findCustomer() {
		String search = searchCustomerTF.getText();
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
				if(search.contains("@")) {
					if(showCustomersCB.isSelected()) {
						query = "SELECT nome, cognome, P.email, città, C.numero_tessera from persona P, cliente_tesserato C "
								+ "where P.email = C.email AND P.email = \"" + search + "\";";
						ResultSet rs = stmt.executeQuery(query);
						
						rs.next();
						customerNameTF.setText(rs.getString(1));
						customerSurnameTF.setText(rs.getString(2));
						customerMailTF.setText(rs.getString(3));
						customerCityTF.setText(rs.getString(4));
						System.out.println(rs.getObject(5));
						customerCodeTF.setText(rs.getObject(5).toString());
						rs.close();

					} else {
						query = "SELECT nome, cognome, P.email, città from persona P "
								+ "where P.email = \"" + search + "\";";	
						ResultSet rs = stmt.executeQuery(query);
												
						rs.next();
						customerNameTF.setText(rs.getString(1));
						customerSurnameTF.setText(rs.getString(2));
						customerMailTF.setText(rs.getString(3));
						customerCityTF.setText(rs.getString(4));

						query = "SELECT numero_tessera FROM cliente_tesserato C "
								+ "where C.email = \"" + search + "\";";	
						rs = stmt.executeQuery(query);
						
						rs.next();
						System.out.println(rs.getObject(1));
						customerCodeTF.setText(rs.getObject(1).toString());
						rs.close();

						
					}
				} else if( ((Integer)(Integer.parseInt(search))).toString().equals(search) ){
					query = "SELECT P.nome, P.cognome, P.email, P.città, C.numero_tessera "
							+ "FROM persona P, cliente_tesserato C "
							+ "WHERE P.email = C.email AND C.numero_tessera = " + search + ";";
					ResultSet rs = stmt.executeQuery(query);
					
					rs.next();
					customerNameTF.setText(rs.getString(1));
					customerSurnameTF.setText(rs.getString(2));
					customerMailTF.setText(rs.getString(3));
					customerCityTF.setText(rs.getString(4));
					System.out.println(rs.getObject(5));
					customerCodeTF.setText(rs.getObject(5).toString());
					rs.close();

				} else {
					return;
				}
				System.out.println(query);
				
				customerMailTF.setDisable(true);
				
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
	}
	
	private void removeCustomer() {
		String email = customerMailTF.getText();
		if (email.isBlank() || ! email.contains("@")) {
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
				String query = "DELETE FROM cliente_tesserato WHERE email = \"" + email + "\";";
				System.out.println(query);
				int rs = stmt.executeUpdate(query);
				
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
	}
}
