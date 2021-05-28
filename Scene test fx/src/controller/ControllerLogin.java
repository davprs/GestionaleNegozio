package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;

import application.MainApp;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControllerLogin {
	
	Connection conn;
	Integer id;
	
	@FXML
	TextField txtWorkerID;
	@FXML
	TextField txtWorkerPSSW;
	@FXML
	Button loginBtn;
	@FXML
	TextField findItemsTxtField;
	@FXML
	Button clientBtn;
	@FXML
	Button searchItemNameBtn;
	@FXML
	VBox categoryVbox;
	@FXML
	VBox workerVBox;
	@FXML
	CheckMenuItem turnOnMenu;
	@FXML
	Pane workerPane;
	@FXML
	TableView<ObservableList<String>> productsTableView;
	
	
	@FXML
	SplitPane doSellPane;
	@FXML
	FlowPane sellNumPane;
	@FXML
	TextField textSell;
	@FXML
	TableView<ObservableList<String>> productsInSellTableView;
	@FXML
	FlowPane findSellNumPane;
	@FXML
	TextField findTextSell;
	@FXML
	VBox initMoneyVBox;
	@FXML
	Label initializedMoneyInfo;
	@FXML
	VBox findCustomerVbox;
	
	@FXML
	TableView<ObservableList<String>> workersTableView;
	@FXML
	TableView<ObservableList<String>> balanceTableView;
	@FXML
	ChoiceBox<String> balanceTypeChoiseBox;
	@FXML
	ChoiceBox<String> balanceIntervalChoiseBox;
	@FXML
	Button updateBalanceBtn;
	@FXML
	Button updateTurnsBtn;
	
	
	
	
	
	public ControllerLogin(Connection conn, Integer id) {
		this.conn = conn;
		this.id = id;
	}
	
	@FXML
	public void initialize() {
		
	}
	
	@FXML
    private void handleClientBtnAction() {
        //label.setText("Hello World!");
		loginBtn.setText("attimo");
		loginBtn.setDisable(true);
		
		application.utils.createClientUI(conn, -1);
	}
	
	@FXML
    private void handleLoginBtnAction() {
        //label.setText("Hello World!");
		Boolean status = false;
		Boolean is_resp = false;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				String query = "SELECT è_responsabile FROM dipendente WHERE codice_dipendente = " + txtWorkerID.getText() + ";";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				if(rs.next()) {
					status = true;
					is_resp = rs.getBoolean(1);
					System.out.println(is_resp);
				}
				/*while ( rs.next() ) { 
					int numCol =rs.getMetaData().getColumnCount(); 
					for ( int i = 1 ; i <= numCol ; i++ ) 
					{ // I numeri di colonna iniziano da 1. 
					System.out.println( "COL" + i + "=" 
					+rs.getObject(i));
					} 
				}*/
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		
		if(status) {
			Integer id = Integer.parseInt(txtWorkerID.getText());
			if (is_resp) {
				application.utils.createResponsableUI(conn, id);
			} else {
				application.utils.createWorkerUI(conn, id);				
			}
			
		} else {
			txtWorkerID.setText("");
			txtWorkerPSSW.setText("");

		}
	}	
	
	@FXML
    private void handleSearchMenu() {
		application.utils.swapPane(workerPane, new ControllerSearchItems(conn, id), "/application/UsersUI.fxml");

	}
	
	@FXML
    private void handleNewCustomerMenu() {
		application.utils.swapPane(workerPane, new ControllerLogin(conn,id), "/application/NewCustomerUI.fxml");
	}
	
	@FXML
    private void handleFindCustomerMenu() {
		application.utils.swapPane(workerPane, new ControllerFindCustomer(conn, id), "/application/FindCustomerUI.fxml");
	}
	
	@FXML
	private void handleDoSellMenu() {
		application.utils.swapPane(workerPane, new ControllerDoSell(conn, id), "/application/DoSellUI.fxml");
	}
	
	@FXML
	private void handleFindSellMenu() {
		application.utils.swapPane(workerPane, new ControllerFindSell(conn, id), "/application/FindSellUI.fxml");
	}
	
	@FXML
	private void handleInitMoneyMenu() {
		application.utils.swapPane(workerPane, new ControllerInitMoney(conn, id), "/application/InitMoneyUI.fxml");
	}
	
	@FXML
	private void handleManageWorkers() {
		application.utils.swapPane(workerPane, new ControllerManageWorkers(conn, id), "/application/MonitorWorkersActivityUI.fxml");
	}
	
	
	
	@FXML
    private void turnStart() {
		turnOnMenu.setDisable(true);
		java.util.Date dt = new java.util.Date();

		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String currentTime = sdf.format(dt);
		System.out.println(currentTime);
		
		String query = "INSERT INTO turno(codice_dipendente, data) VALUES (" + id + ",\"" + currentTime + "\");";
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void endTurn() {
		turnOnMenu.setDisable(false);
		turnOnMenu.setSelected(false);
		java.util.Date dt = new java.util.Date();

		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String currentTime = sdf.format(dt);
		String startTime = null;
		System.out.println(currentTime);
		
		String query = "SELECT data FROM turno WHERE codice_dipendente = " + id + " AND durata IS NULL LIMIT 1;";
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				ResultSet rs = stmt.executeQuery(query);
				
				while ( rs.next() ) { 
					int numCol =rs.getMetaData().getColumnCount(); 
					for ( int i = 1 ; i <= numCol ; i++ ) 
					{
						java.util.Date date;
						Timestamp timestamp = rs.getTimestamp(i);
						date = new java.util.Date(timestamp.getTime());
						startTime = sdf.format(date);
					} 
				}
				
				rs.close(); 
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Long diff = null;
		Long diffMinutes = null;
		Long diffHours = null;
		Long diffSeconds = null;
		try {
			diff = sdf.parse(currentTime).getTime() - sdf.parse(startTime).getTime();
			diffSeconds = diff / (1000) % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			diffHours = diff / (60 * 60 * 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		query = "UPDATE turno SET durata=\'" + diffHours + ":" + diffMinutes + ":" + diffSeconds + "\' WHERE codice_dipendente = " + id + " AND durata IS NULL LIMIT 1;";
		System.out.println(query);
		stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
			
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	@FXML
	private void logoutAndClose() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
