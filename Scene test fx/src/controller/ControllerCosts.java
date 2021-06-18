package controller;

import java.awt.TextArea;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;

import application.utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.Pair;

public class ControllerCosts extends ControllerLogin{
	private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
	private ObservableList<ObservableList<String>> cart = FXCollections.observableArrayList();

	final private ObservableList<ObservableList<String>> dataProd = FXCollections.observableArrayList();
	private Integer qtyCounter = 1;
	private Double cartTotal = 0.0;

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
		lblLastSalary.setText("");
		lblLastSalaryImp.setText("");
		productNameLbl.setText("");
		cartTotalLbl.setText("");
		
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
		
		ResultSet rs = null;
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
				String query = "SELECT nome FROM fornitore;";

				System.out.println(query);
				rs = stmt.executeQuery(query);
				
				
				while(rs.next()) {
					supplierLV.getItems().add(rs.getObject(1).toString());
				}
				
				supplierLV.setOnMouseClicked(new EventHandler<Event>() {

					@Override
					public void handle(Event arg0) {
						// TODO Auto-generated method stub
						Statement stmt = null;
						try {
							stmt  = conn.createStatement();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							application.utils.showPopupPane(e.toString());
						}
						
						if(stmt != null) {
							String supplier = supplierLV.getSelectionModel().getSelectedItem();
							
							String query = "SELECT F.nome, P.nome_prodotto, P.prezzo_acquisto, F.codice_fornitore, P.codice_prod "
									+ "FROM fornitore F, prodotto P, prodotto_di_fornitore PF "
									+ "WHERE F.nome = \"" + supplier + "\" AND F.codice_fornitore = PF.codice_fornitore AND P.codice_prod = PF.codice_prod;";
	
							productsBuyTV.getItems().clear();
							productsBuyTV.getColumns().clear();
							//productsBuyTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
							
							
							System.out.println(query);
							ResultSet rs = null;
							try {
								rs = stmt.executeQuery(query);
								
								int numCol =rs.getMetaData().getColumnCount(); 
								for ( int i = 1 ; i <= numCol - 2; i++ ) 
								{
									System.out.println(rs.getMetaData().getColumnName(i));
									final int j = i -1;                
									TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i));
									col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
							            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
							                return new SimpleStringProperty(param.getValue().get(j).toString());                        
							            }                    
							        });
									productsBuyTV.getColumns().add(col); 						
								}
								
								
								while(rs.next()) {
									ObservableList<String> row = FXCollections.observableArrayList();
									row.add(rs.getObject(1).toString());
									row.add(rs.getObject(2).toString());
									row.add(((BigDecimal)(rs.getObject(3))).toString());
									row.add(((Integer)rs.getObject(4)).toString());
									row.add(((Integer)rs.getObject(5)).toString());

									
									dataProd.add(row);
								}
								productsBuyTV.setItems(dataProd);

								
								rs.close();
								stmt.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						supplierLV.setDisable(true);
					}
				});
				
				rs.close();
				stmt.close();
				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
		
		salaryTF.setDisable(true);
		salaryTF.setOpacity(200);
		
		sub20btn.setDisable(true);
		add20btn.setDisable(true);
		
		cartTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		List<String> colNames= Arrays.asList("Fornitore", "Prodotto", "Prezzo", "Quantità");
		for ( int i = 1 ; i <= 4; i++ ) 
		{
			final int j = i -1;                
			TableColumn col = new TableColumn(colNames.get(i-1));
			col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
	            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
	                return new SimpleStringProperty(param.getValue().get(j).toString());                        
	            }                    
	        });
			cartTV.getColumns().add(col); 						
		}
		
		productsBuyTV.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				qtyTF.setText("1");
				qtyMin10Btn.setDisable(true);
				qtyMinBtn.setDisable(true);
				productNameLbl.setText(productsBuyTV.getSelectionModel().getSelectedItem().get(1));
			}
		});
		
		showWorkers();
	}
	
	@FXML
	private void showWorkers() {
		
		workersNamesLV.getItems().clear();
		data.removeAll(data);
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
				
				String query = "SELECT nome, cognome, data_assunzione, ore_lavoro_mensili, tot_ore_mensili, stipendio_base, è_responsabile, codice_dipendente, giorno_saldo, importo "
						+ "FROM "
						+ "	(select  nome, cognome, data_assunzione, ore_lavoro_mensili, T.tot_ore_mensili, stipendio_base, è_responsabile, D.codice_dipendente "
						+ "		from Persona P, dipendente D , (select codice_dipendente, SEC_TO_TIME(SUM(TIME_TO_SEC(durata))) as tot_ore_mensili "
						+ "						            from turno "
						+ "						            where data between \"" + dtM.toString() + "\" and \"" + dtD.toString() + "\" "
						+ "						            group by codice_dipendente) as T "
						+ "						where P.email = D.email AND T.codice_dipendente = D.codice_dipendente ) as R "
						+ "	LEFT OUTER JOIN (select codice_beneficiario, giorno_saldo, importo "
						+ "							from stipendio where (codice_beneficiario, giorno_saldo) in ( "
						+ "							select codice_beneficiario, max(giorno_saldo) as giorno_saldo "
						+ "							from stipendio "
						+ "							group by codice_beneficiario)) as LS "
						+ "	ON R.codice_dipendente = LS.codice_beneficiario;";
				
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
					row.add((rs.getObject(9) == null ? "Mai" : rs.getObject(9).toString()));
					row.add((rs.getObject(10) == null ? "Nessuno" : rs.getObject(10).toString()));
					
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

							Boolean res = data.get(i).get(6).equals("true");
							lblIsRes.setText(res ? "Sì" : "No");
							lblLastSalary.setText(data.get(i).get(8));
							lblLastSalaryImp.setText(data.get(i).get(9));
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
				String query = "INSERT INTO costi_di_gestione(causale, breve_descrizione, importo, giorno_saldo, codice_dipendente) \r\n"
						+ "VALUES (\"" + title + "\", \"" + description+ "\", " + amount.toString() + ", \"" + dtD.toString() + "\", " + id.toString() + ");";
				System.out.println(query);
				stmt.executeUpdate(query);
				
				stmt.close();
				application.utils.showPopupPane("Spesa salvata con successo!");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
		
		costTitleTF.setText("");
		costDescriptionTA.setText("");
		costImportTF.setText("");

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
				String query = "INSERT INTO stipendio(codice_beneficiario, importo, giorno_saldo, codice_assegnatore) "
						+ "VALUES (" + data.get(i).get(7) + ", " + salaryTF.getText() + ", \"" + dtD.toString() + "\", " + id.toString() + ");";
				System.out.println(query);
				stmt.executeUpdate(query);
								
				stmt.close();
				application.utils.showPopupPane("Stipendio salvato con successo!");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
		
		costTitleTF.setText("");
		costDescriptionTA.setText("");
		costImportTF.setText("");
		
	}

	@FXML
	private void qtyPlus() {
		qtyCounter ++;
		if(qtyMinBtn.isDisabled()) {
			qtyMinBtn.setDisable(false);
		}
		
		if(qtyCounter > 10) {
			qtyMin10Btn.setDisable(false);
		}
		qtyTF.setText(qtyCounter.toString());
		
	}
	
	@FXML
	private void qtyMin() {
		qtyCounter --;
		if(qtyCounter < 1) {
			qtyCounter = 1;
			qtyMin10Btn.setDisable(false);
			qtyMinBtn.setDisable(true);
		}
		
		if(qtyCounter <= 10) {
			qtyMin10Btn.setDisable(true);
		}
		qtyTF.setText(qtyCounter.toString());
	}
	
	@FXML
	private void qtyPlus10() {
		qtyCounter += 10;
		if(qtyMin10Btn.isDisabled() || qtyMinBtn.isDisabled()) {
			qtyMin10Btn.setDisable(false);
			qtyMinBtn.setDisable(false);
		}

		qtyTF.setText(qtyCounter.toString());
		
	}
	
	@FXML
	private void qtyMin10() {
		qtyCounter -= 10;
		if(qtyCounter < 1) {
			qtyCounter = 1;
			qtyMin10Btn.setDisable(true);
			qtyMinBtn.setDisable(true);
		}
		
		if(qtyCounter <= 10) {
			qtyMin10Btn.setDisable(true);
		}
		qtyTF.setText(qtyCounter.toString());
	}
	
	@FXML
	private void addToCart() {
		ObservableList<String> selectedItem = productsBuyTV.getSelectionModel().getSelectedItem();
		System.out.println(selectedItem);
		ObservableList<String> cartRow = FXCollections.observableArrayList();

		cartRow.add(selectedItem.get(0));
		cartRow.add(selectedItem.get(1));
		cartRow.add(selectedItem.get(2));
		cartRow.add(qtyTF.getText());
		cartRow.add(selectedItem.get(3));
		cartRow.add(selectedItem.get(4));

		
		if(selectedItem != null) {
			cart.add(cartRow);
		}
		cartTV.setItems(cart);
		
		cartTotal += ((Double)(Double.parseDouble(cartRow.get(2)))) * qtyCounter;
		
		cartTotal = BigDecimal.valueOf(cartTotal).setScale(2, BigDecimal.ROUND_UP).doubleValue();	//utile per soldi
		
		cartTotalLbl.setText(cartTotal.toString());
		qtyCounter = 1;
		qtyTF.setText(qtyCounter.toString());

	}
	
	@FXML
	private void clearCart() {
		cartTotal = 0.0;
		cartTotalLbl.setText("");
		cartTV.getItems().clear();
		cart.removeAll(cart);
		supplierLV.setDisable(false);
	}
	
	@FXML
	private void buyProducts() {
		String query = null;
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
				LocalDate ld = LocalDate.now();
				query = "INSERT INTO ordine(codice_dipendente, giorno_saldo, codice_fornitore) "
						+ "VALUES (" + id.toString() + ", \"" + ld.toString() + "\", " + dataProd.get(0).get(3) + ");";	
				System.out.println(query);
				int res = stmt.executeUpdate(query);
				
				query = "INSERT INTO composizione(codice_prod, quantità, codice_dipendente, giorno_saldo, codice_fornitore) "
						+ "VALUES ";
				
				int i = 0;
				String piece = "";
				for (ObservableList<String> prod : cart) {
					if(i != 0) {
						piece += ",";
					}
					piece += "(" + cart.get(i).get(5)+ ", " + cart.get(i).get(3) + ", " + id + ", \"" + ld.toString() + "\", " + cart.get(i).get(4) + ")";
					i++;
				}
			
			
				piece += ";";
				query += piece;

				System.out.println(query);
				res = stmt.executeUpdate(query);
				utils.showPopupPane("Ordine salvato con successo!");
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
		
		
		clearCart();
	}
	
	
}
