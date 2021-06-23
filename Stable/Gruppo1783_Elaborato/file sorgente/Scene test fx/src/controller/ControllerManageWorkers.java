package controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ControllerManageWorkers extends ControllerUI{
	
	final private static String BALANCE = "Bilancio";
	final private static String INCOME = "Entrate";
	final private static String COSTS = "Spese";
	final private static String WEEK = "Settimana";
	final private static String DAY= "Giorno";
	final private static String MONTH = "Mese";
	final private static String YEAR = "Anno";

	final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
	final ObservableList<ObservableList<String>> dataBal = FXCollections.observableArrayList();

	private final static String showAllBtnTxt = "Tutto";
	private static String selCat = showAllBtnTxt;
	private static String currentTime;
	private static String aMonthAgo;
	
	
	
	
    public ControllerManageWorkers(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public void initialize() {
		
		cleanMonitorWorkersBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
		    	emailWorkersMonitor.setDisable(false);
		    	emailWorkersMonitor.setEditable(true);;
		    	nameWorkerMonitor.setText("");
		    	surnameWorkerMonitor.setText("");
		    	baseSalaryWorkerMonitor.setText("");
		    	baseWorkTimeWork.setText("");
		    	hiringDateWorkersMonitor.setValue(null);
		    	cityWorkersMonitor.setText("");
		    	isResponsableWorkersMonitor.setSelected(false);
		    	emailWorkersMonitor.setText("");
		    	psswMonitorPF.setText("");
		    	workerCodeLbl.setText("");
		    	
		    	hiringDateWorkersMonitor.setDisable(false);
		    	hiringDateWorkersMonitor.setEditable(true);
		    	hiringDateWorkersMonitor.setOpacity(100);

			}
		});
		java.util.Date dtN = new java.util.Date();
		java.util.Date dtP = new java.util.Date();
		dtN.setDate((dtN.getDate() + 1) % 32);
		dtP.setMonth((dtN.getMonth() - 1) % 12); 

		currentTime = sdf.format(dtN);
		aMonthAgo = sdf.format(dtP);
		
		
		balanceTypeChoiseBox.getItems().addAll(BALANCE, INCOME, COSTS);
		balanceIntervalChoiseBox.getItems().addAll(DAY, WEEK, MONTH, YEAR);
		
		
		
		showAllWorkers(aMonthAgo, currentTime);
    }
    
    private void showAllWorkers(String startDate, String endDate) {
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
				String query = "select nome, cognome, ore_lavoro_mensili, T.tot_ore_mensili, Lavoro.sta_lavorando, D.email, D.stipendio_base, D.data_assunzione, D.è_responsabile, P.città, D.codice_dipendente"
						+ "		from Persona P, dipendente D , ("
						+ "			select codice_dipendente, SEC_TO_TIME(SUM(TIME_TO_SEC(durata))) as tot_ore_mensili "
						+ "            from turno "
						+ "            where data between \"" + startDate + "\" and \"" + endDate + "\" "
						+ "            group by codice_dipendente) as T, "
						+ "            ("
						+ "            select codice_dipendente as CD, EXISTS(select * from turno where durata is null and CD = codice_dipendente) as sta_lavorando "
						+ "                from turno "
						+ "                group by codice_dipendente "
						+ "            ) AS Lavoro "
						+ "where P.email = D.email AND D.codice_dipendente = T.codice_dipendente AND D.codice_dipendente = Lavoro.CD  AND D.licenziato = false "
						+ "group by D.codice_dipendente;";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				
				workersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				int numCol =rs.getMetaData().getColumnCount(); 
				for ( int i = 1 ; i <= numCol - 6 ; i++ ) 
				{
					final int j = i -1;                
					TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i));
					col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
			            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
			                return new SimpleStringProperty(param.getValue().get(j).toString());                        
			            }                    
			        });
					workersTableView.getColumns().add(col); 						
				}
				
				
				while ( rs.next() ) { 
					
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add((String)rs.getObject(1));
					row.add((String)rs.getObject(2));
					row.add(((Object)rs.getObject(3)).toString());
					row.add(((Time)rs.getObject(4)).toString());
					row.add(((Integer)rs.getObject(5)).equals(1) ? "Sì" : "No");
					
					row.add(rs.getObject(6).toString());
					row.add(((BigDecimal)rs.getObject(7)).toString());
					row.add(((Date)rs.getObject(8)).toString());
					row.add(rs.getObject(9).toString());
					row.add(rs.getObject(10).toString());
					row.add(rs.getObject(11).toString());
					
					data.add(row);
					workersTableView.setItems(data);
					
					
					
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
    private void handleUpdateWorkingStatus() {
    	workersTableView.getItems().clear();
    	workersTableView.getColumns().clear();
    	
    	java.util.Date dtN = new java.util.Date();
		java.util.Date dtP = new java.util.Date();
		dtN.setDate((dtN.getDate() + 1) % 32);
		dtP.setMonth((dtN.getMonth() - 1) % 12); 
		java.text.SimpleDateFormat sdf = 
			     new java.text.SimpleDateFormat("yyyy-MM-dd");

		currentTime = sdf.format(dtN);
		aMonthAgo = sdf.format(dtP);
    	
    	showAllWorkers(aMonthAgo, currentTime);
    }
    
    @FXML
    private void handleUpdateBalanceStatus() {
    	balanceTableView.getItems().clear();
    	balanceTableView.getColumns().clear();
    	
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
				String txtStart, txtStop, txtBalCol;
				
				if (balanceTypeChoiseBox.getSelectionModel().getSelectedItem().equals(BALANCE)) {
					txtStart = "Bilancio ";
				} else if (balanceTypeChoiseBox.getSelectionModel().getSelectedItem().equals(INCOME)) {
					txtStart = "Entrate ";
				}else if (balanceTypeChoiseBox.getSelectionModel().getSelectedItem().equals(COSTS)) {
					txtStart = "Uscite ";
				} else {
					return;
				}
								
				LocalDate dtN = LocalDate.now();
				LocalDate dtD = LocalDate.now();
				
				dtD = dtD.plusDays(1);
				
				
				String end = dtD.toString();
				String start = null;
				if (balanceIntervalChoiseBox.getSelectionModel().getSelectedItem().equals(DAY)) {
					if (txtStart.equals("Bilancio ")) {
						txtStop = "Giornaliero";
					} else {
						txtStop = "Giornaliere";						
					}
					start = dtN.toString();
				} else if (balanceIntervalChoiseBox.getSelectionModel().getSelectedItem().equals(WEEK)) {		
					txtStop = "Settimanali";
					start = dtN.minusWeeks(1).toString();
				}else if (balanceIntervalChoiseBox.getSelectionModel().getSelectedItem().equals(MONTH)) {
					txtStop = "Mensili";
					start = dtN.minusMonths(1).toString();
				}else if (balanceIntervalChoiseBox.getSelectionModel().getSelectedItem().equals(YEAR)) {
					txtStop = "Annuale";
					start = dtN.minusYears(1).toString();
				} else {
					return;
				}
				
				txtBalCol = txtStart + txtStop;
				
				ResultSet rs = null;
				
				if (balanceTypeChoiseBox.getSelectionModel().getSelectedItem().equals(INCOME)) {
					query = "select ROUND(SUM(entrate), 2) "
							+ "from saldo_giornaliero "
							+ "where data between \"" + start + "\" and \"" + end + "\";";
					rs = stmt.executeQuery(query);
				} else if (balanceTypeChoiseBox.getSelectionModel().getSelectedItem().equals(BALANCE)) {
					query = "select ROUND(SUM(entrate) - SUM(uscite), 2) "
							+ "from saldo_giornaliero "
							+ "where data between \"" + start + "\" and \"" + end + "\";";
					rs = stmt.executeQuery(query);
				} else if (balanceTypeChoiseBox.getSelectionModel().getSelectedItem().equals(COSTS)) {
					query = "select ROUND(SUM(uscite), 2) from saldo_giornaliero "
							+ "where data between \"" + start + "\" and \"" + end + "\";";
					rs = stmt.executeQuery(query);
				}
				System.out.println(query);
				
				balanceTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				int numCol =rs.getMetaData().getColumnCount(); 
				             
				TableColumn col = new TableColumn(txtBalCol);
				col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
		            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
		                return new SimpleStringProperty(param.getValue().get(0).toString());                        
		            }
		        });
				col.setStyle( "-fx-alignment: CENTER;");
				balanceTableView.getColumns().add(col); 						
				
				
				while ( rs.next() ) { 
					
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add(((BigDecimal)rs.getObject(1)).toString());
					
					dataBal.add(row);
					balanceTableView.setItems(dataBal);
					
					
					
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
    private void handleUpdateWorkersInfo() {
    	String name, surname, base_salary, montly_hours, hiring_date, city, is_responsable, email, optString;
    	name = nameWorkerMonitor.getText();
    	surname = surnameWorkerMonitor.getText();
    	base_salary = baseSalaryWorkerMonitor.getText();
    	montly_hours = baseWorkTimeWork.getText();
    	hiring_date = sdf.format(Date.valueOf(hiringDateWorkersMonitor.getValue()));
    	city = cityWorkersMonitor.getText();
    	is_responsable = (isResponsableWorkersMonitor.isSelected()? "1": "0");
    	email = emailWorkersMonitor.getText();
    	
    	optString = "";
    	
    	if(name.isBlank() || surname.isBlank() || base_salary.isBlank() || montly_hours.isBlank()  || hiring_date.isBlank()  || city.isBlank()) {
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
				String query = null;
				
				if(emailWorkersMonitor.isDisabled()) {
				
					query = "UPDATE persona SET "
							+ "nome = \"" + name + "\", "
							+ "cognome = \"" + surname + "\", "
							+ "città = \"" + city + "\" "
							+ "WHERE email = \"" + email + "\";";
					System.out.println(query);
					int rs = stmt.executeUpdate(query);
					
					
					query = "UPDATE dipendente SET "
							+ "data_assunzione = \"" + hiring_date + "\", "
							+ "stipendio_base = " + base_salary + ", "
							+ "ore_lavoro_mensili = " + montly_hours + ", "
							+ "è_responsabile = \"" + is_responsable + "\", "
							+ "password = \"" + psswMonitorPF.getText() + "\" "
							+ "WHERE email = \"" + email + "\";";
					System.out.println(query);
					rs = stmt.executeUpdate(query);
					
					stmt.close();
				} else {
					query = "SELECT codice_dipendente FROM dipendente "
							+ "WHERE email = \"" + email + "\";";
					System.out.println(query);
					ResultSet rs = stmt.executeQuery(query);
					if(rs.next()) {
						query = "UPDATE dipendente SET licenziato = false "
								+ "WHERE email = \"" + email + "\";";
						System.out.println(query);
						int res = stmt.executeUpdate(query);
						
						query = "UPDATE persona SET "
								+ "nome = \"" + name + "\", "
								+ "cognome = \"" + surname + "\", "
								+ "città = \"" + city + "\" "
								+ "WHERE email = \"" + email + "\";";
						System.out.println(query);
						res = stmt.executeUpdate(query);
						
						
						query = "UPDATE dipendente SET "
								+ "data_assunzione = \"" + hiring_date + "\", "
								+ "stipendio_base = " + base_salary + ", "
								+ "ore_lavoro_mensili = " + montly_hours + ", "
								+ "è_responsabile = \"" + is_responsable + "\" "
								+ "WHERE email = \"" + email + "\";";
						System.out.println(query);
						res = stmt.executeUpdate(query);
						
						stmt.close();
						
					} else {
						query = "INSERT INTO persona(nome, cognome, email, città) VALUES ("
								+ "\"" + name + "\", "
								+ "\"" + surname + "\", "
								+ "\"" + email + "\", "
								+ "\"" + city + "\");";
						System.out.println(query);
						int res = stmt.executeUpdate(query);
						
						
						query = "INSERT INTO dipendente(data_assunzione, stipendio_base, ore_lavoro_mensili, codice_dipendente, email, è_responsabile, password) VALUES ("
								+ "\"" + hiring_date + "\", "
								+ "" + base_salary + ", "
								+ "" + montly_hours + ", "
								+ "NULL, "
								+ "\"" + email + "\", "
								+ "\"" + is_responsable + "\", "
								+ "\"" + psswMonitorPF.getText() + "\");";
						System.out.println(query);
						res = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
						ResultSet lastId = stmt.getGeneratedKeys();
						lastId.next();
						String newWorkerCode = lastId.getObject(1).toString();
						workerCodeLbl.setText(newWorkerCode);
						optString = "\nCodice nuovo dipendente : " + newWorkerCode;
						query = "INSERT INTO cliente_tesserato(data_tesseramento, numero_tessera, email) VALUES ("
								+ "\"" + hiring_date + "\", "
								+ "NULL, "
								+ "\"" + email + "\");";
						System.out.println(query);
						res = stmt.executeUpdate(query);
						
						query = "INSERT INTO turno(codice_dipendente, data, durata) VALUES ("
								+ "" + newWorkerCode + ", "
								+ "\"" + LocalDateTime.now().toString() + "\", "
								+ "\"" + "00:00:00" + "\");";
						System.out.println(query);
						res = stmt.executeUpdate(query);
						
					}
					
				}
				
				application.utils.showPopupPane("Dati salvati con successo!" + optString);
			} catch (SQLException e) {
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
    	
    }
    
    @FXML
    private void handleFireWorker() {
    	String email = emailWorkersMonitor.getText();
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
				String query = "UPDATE dipendente SET licenziato = true WHERE email = \"" + email + "\";";
				System.out.println(query);
				int rs = stmt.executeUpdate(query);
				
				stmt.close();
				application.utils.showPopupPane("Dipendente licenziato con successo! :(");
			} catch (SQLException e) {
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		}
    }
    
    @FXML
    private void handleShowWorkersInfo() {
    	emailWorkersMonitor.setDisable(true);
    	emailWorkersMonitor.setEditable(false);
    	
    	hiringDateWorkersMonitor.setDisable(true);
    	hiringDateWorkersMonitor.setEditable(false);
    	hiringDateWorkersMonitor.setOpacity(200);
    	
    	nameWorkerMonitor.setText(workersTableView.getSelectionModel().getSelectedItem().get(0));
    	surnameWorkerMonitor.setText(workersTableView.getSelectionModel().getSelectedItem().get(1));
    	baseSalaryWorkerMonitor.setText(workersTableView.getSelectionModel().getSelectedItem().get(6));
    	baseWorkTimeWork.setText(workersTableView.getSelectionModel().getSelectedItem().get(2));
    	hiringDateWorkersMonitor.setValue(LocalDate.parse(workersTableView.getSelectionModel().getSelectedItem().get(7)));
    	emailWorkersMonitor.setText(workersTableView.getSelectionModel().getSelectedItem().get(5));
    	cityWorkersMonitor.setText(workersTableView.getSelectionModel().getSelectedItem().get(9));
    	isResponsableWorkersMonitor.setSelected(workersTableView.getSelectionModel().getSelectedItem().get(8).equals("true"));
    	workerCodeLbl.setText(workersTableView.getSelectionModel().getSelectedItem().get(10));
    	psswMonitorPF.setText("");
    }
}
