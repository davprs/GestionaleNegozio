package controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
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

public class ControllerManageWorkers extends ControllerLogin{
	
	final private static String BALANCE = "Bilancio";
	final private static String INCOME = "Entrate";
	final private static String COSTS = "Spese";
	final private static String WEEK = "Settimana";
	final private static String DAY= "Giorno";
	final private static String MONTH = "Settimana";
	final private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
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
		java.util.Date dtN = new java.util.Date();
		java.util.Date dtP = new java.util.Date();
		dtN.setDate((dtN.getDate() + 1) % 32);
		dtP.setMonth((dtN.getMonth() - 1) % 12); 

		currentTime = sdf.format(dtN);
		aMonthAgo = sdf.format(dtP);
		
		
		balanceTypeChoiseBox.getItems().addAll(BALANCE, INCOME, COSTS);
		balanceIntervalChoiseBox.getItems().addAll(WEEK, DAY, MONTH);
		
		
		
		showAllWorkers(aMonthAgo, currentTime);
    }
    
    private void showAllWorkers(String startDate, String endDate) {
    	Statement stmt = null;
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				String query = "select nome, cognome, ore_lavoro_mensili, T.tot_ore_mensili, Lavoro.sta_lavorando"
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
						+ "where P.email = D.email AND D.codice_dipendente = T.codice_dipendente AND D.codice_dipendente = Lavoro.CD "
						+ "group by D.codice_dipendente;";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				
				workersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
					workersTableView.getColumns().add(col); 						
				}
				
				
				while ( rs.next() ) { 
					
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add((String)rs.getObject(1));
					row.add((String)rs.getObject(2));
					row.add(((Object)rs.getObject(3)).toString());
					row.add(((Time)rs.getObject(4)).toString());
					row.add(((Integer)rs.getObject(5)).toString());

					System.out.println("AAAAAAAAAAAAAAAAAAAAAAA" + row);
					
					data.add(row);
					workersTableView.setItems(data);
					
					
					
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				} else {
					return;
				}
				
				txtBalCol = txtStart + txtStop;
				
				query = "select ROUND(SUM(entrate), 2) "
						+ "from saldo_giornaliero "
						+ "where data between \"" + start + "\" and \"" + end + "\";";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				
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
					row.add(((Double)rs.getObject(1)).toString());
					
					dataBal.add(row);
					balanceTableView.setItems(dataBal);
					
					
					
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
