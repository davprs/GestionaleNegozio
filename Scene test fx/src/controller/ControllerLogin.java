package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.MainApp;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
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
	
	@FXML
	TextField txtWorkerID;
	@FXML
	TextField txtWorkerPSSW;
	@FXML
	Button loginBtn;
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
	FlowPane findSellNumPane;
	@FXML
	TextField findTextSell;
	@FXML
	VBox initMoneyVBox;
	@FXML
	Label initializedMoneyInfo;
	@FXML
	VBox findCustomerVbox;
	
	public ControllerLogin(Connection conn) {
		this.conn = conn;
	}
	
	@FXML
	public void initialize() {
		
	}
	
	@FXML
    private void handleClientBtnAction() {
        //label.setText("Hello World!");
		loginBtn.setText("attimo");
		loginBtn.setDisable(true);
		
		application.utils.createClientUI(conn);
	}
	
	@FXML
    private void handleLoginBtnAction() {
        //label.setText("Hello World!");
		Boolean status = false;
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(stmt != null) {
			try {
				String query = "SELECT * FROM dipendente WHERE codice_dipendente = " + txtWorkerID.getText() + ";";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				if(rs.next()) {
					status = true;
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
			application.utils.createWorkerUI(conn);
			
		} else {
			txtWorkerID.setText("");
			txtWorkerPSSW.setText("");

		}
	}

	
	
	@FXML
    private void handleNameSearchAction() {
        System.out.println("Hello World!");
	}
	
	
	@FXML
    private void handleSearchMenu() {
		application.utils.swapPane(workerPane, new ControllerSearchItems(conn), "/application/UsersUI.fxml");

	}
	
	@FXML
    private void handleNewCustomerMenu() {
		application.utils.swapPane(workerPane, new ControllerLogin(conn), "/application/NewCustomerUI.fxml");
	}
	
	@FXML
    private void handleFindCustomerMenu() {
		application.utils.swapPane(workerPane, new ControllerFindCustomer(conn), "/application/FindCustomerUI.fxml");
	}
	
	@FXML
	private void handleDoSellMenu() {
		application.utils.swapPane(workerPane, new ControllerDoSell(conn), "/application/DoSellUI.fxml");
	}
	
	@FXML
	private void handleFindSellMenu() {
		application.utils.swapPane(workerPane, new ControllerFindSell(conn), "/application/FindSellUI.fxml");
	}
	
	@FXML
	private void handleInitMoneyMenu() {
		application.utils.swapPane(workerPane, new ControllerInitMoney(conn), "/application/InitMoneyUI.fxml");
	}
	
	
	
	
	
	@FXML
    private void turnStart() {
		turnOnMenu.setDisable(true);
	}
	
	
	
	
	
	

}
