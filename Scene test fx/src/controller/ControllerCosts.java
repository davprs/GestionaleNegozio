package controller;

import java.awt.TextArea;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.EventListener;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

public class ControllerCosts extends ControllerLogin{
	private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();;
	
	public ControllerCosts(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}
	
	public void initialize() {
		lblName.setText("");
		lblHired.setText("");
		lblHStd.setText("");
		lblHEff.setText("");
		lblSalBase.setText("");
		lblIsRes.setText("");
		
		productNameLbl.setText("");
		
		sub20btn.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				if( ! salaryTF.equals("")) {
					Double val = Double.parseDouble(salaryTF.getText());
					if ( val - 20 > 0 ) {
						salaryTF.setText(((Double)(val - 20)).toString());						
					} else {
						salaryTF.setText("0.00");
					}
				}
			}
		});
		
		add20btn.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				if( ! salaryTF.equals("")) {
					Double val = Double.parseDouble(salaryTF.getText());
					if ( val + 20 < Double.MAX_VALUE ) {
						salaryTF.setText(((Double)(val + 20)).toString());						
					} else {
						salaryTF.setText("0.00");
					}
				}
			}
		});
		
		
		salaryTF.setDisable(true);
		salaryTF.setOpacity(200);
		
		sub20btn.setDisable(true);
		add20btn.setDisable(true);
		
		
		showWorkers();
	}
	
	private void showWorkers() {
		LocalDate dtD = LocalDate.now();
		LocalDate dtM = LocalDate.now();
		
		dtM = dtD.minusMonths(1);
		dtD = dtD.plusDays(1);

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
				String query = "select  nome, cognome, data_assunzione, ore_lavoro_mensili, T.tot_ore_mensili, stipendio_base, è_responsabile, D.codice_dipendente "
						+ "from Persona P, dipendente D , (select codice_dipendente, SEC_TO_TIME(SUM(TIME_TO_SEC(durata))) as tot_ore_mensili "
						+ "            from turno "
						+ "            where data between \"" + dtM.toString() + "\" and \"" + dtD.toString() + "\" "
						+ "            group by codice_dipendente) as T "
						+ "where P.email = D.email AND T.codice_dipendente = D.codice_dipendente;";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				
				while ( rs.next() ) { 
					ObservableList<String> row = FXCollections.observableArrayList();
					String name = rs.getObject(1).toString();
					String surname = rs.getObject(2).toString();
					row.add(name);
					row.add(surname);
					row.add(rs.getObject(3).toString());
					row.add((rs.getObject(4)).toString());
					row.add(((Time)rs.getObject(5)).toString());
					row.add(((BigDecimal)rs.getObject(6)).toString());
					row.add((rs.getObject(7)).toString());
					row.add((rs.getObject(8)).toString());
					System.out.println((rs.getObject(7)).toString());
					System.out.println(row.toString());
					data.add(row);
					
					workersNamesLV.getItems().add(name + " " + surname);
					workersNamesLV.setOnMouseClicked(new EventHandler<Event>() {

						@Override
						public void handle(Event arg0) {
							// TODO Auto-generated method stub
							int i = workersNamesLV.getSelectionModel().getSelectedIndex();
							lblName.setText(data.get(i).get(0) + " " + data.get(i).get(1));
							lblHired.setText(data.get(i).get(2));
							lblHStd.setText(data.get(i).get(3));
							lblHEff.setText(data.get(i).get(4));
							lblSalBase.setText(data.get(i).get(5));
							
							Boolean res = data.get(i).get(6).equals("1");

							lblIsRes.setText(res ? "Sì" : "No");
							
							sub20btn.setDisable(res);
							add20btn.setDisable(res);
							
							salaryTF.setText(data.get(i).get(5));
						}
					});
					
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
	
	}

	@FXML
	private void handleSubmitCost() {
		String title = costTitleTF.getText();
		String description = costDescriptionTA.getText();
		String amount= costImportTF.getText();
		
		try {
			Double.parseDouble(amount);
		} catch (Exception e) {
			costImportTF.setText("");
			return;
		}
		
		LocalDate dtD = LocalDate.now();

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
				String query = "INSERT INTO costi_di_gestione(causale, breve_descrizione, data, importo, giorno_saldo, codice_dipendente) \r\n"
						+ "VALUES (\"" + title + "\", \"" + description+ "\", \"" + dtD.toString() + "\", " + amount.toString() + ", \"" + dtD.toString() + "\", " + id.toString() + ");";
				System.out.println(query);
				stmt.executeUpdate(query);
				
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
		
		costTitleTF.setText("");
		costDescriptionTA.setText("");
		costImportTF.setText("");
		application.utils.showPopupPane("Spesa salvata con successo!");

	}
	
	@FXML
	private void payWorker() {
		LocalDate dtD = LocalDate.now();

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
				int i = workersNamesLV.getSelectionModel().getSelectedIndex();
				String query = "INSERT INTO stipendio(codice_beneficiario, importo, data, giorno_saldo, codice_assegnatore) "
						+ "VALUES (" + data.get(i).get(7) + ", " + salaryTF.getText() + ", \"" + dtD.toString() + "\", \"" + dtD.toString() + "\", " + id.toString() + ");";
				System.out.println(query);
				stmt.executeUpdate(query);
								
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
		
		costTitleTF.setText("");
		costDescriptionTA.setText("");
		costImportTF.setText("");
		
		application.utils.showPopupPane("Stipendio salvato con successo!");
	}
}
