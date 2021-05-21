package controller;

import java.io.IOException;
import java.net.URL;

import application.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
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
	
	
	
	@FXML
	public void initialize() {
		
	}
	
	@FXML
    private void handleClientBtnAction() {
        //label.setText("Hello World!");
		loginBtn.setText("attimo");
		loginBtn.setDisable(true);
		
		application.utils.createClientUI();
	}
	
	@FXML
    private void handleLoginBtnAction() {
        //label.setText("Hello World!");
		loginBtn.setText("attimo");
		loginBtn.setDisable(true);
		
		application.utils.createWorkerUI();
	}

	
	
	@FXML
    private void handleNameSearchAction() {
        System.out.println("Hello World!");
	}
	
	
	@FXML
    private void handleSearchMenu() {
		application.utils.swapPane(workerPane, new ControllerSearchItems(), "/application/UsersUI.fxml");

	}
	
	@FXML
    private void handleNewCustomerMenu() {
		application.utils.swapPane(workerPane, new ControllerLogin(), "/application/NewCustomerUI.fxml");
	}
	
	@FXML
    private void handleFindCustomerMenu() {
		application.utils.swapPane(workerPane, new ControllerFindCustomer(), "/application/FindCustomerUI.fxml");
	}
	
	@FXML
	private void handleDoSellMenu() {
		application.utils.swapPane(workerPane, new ControllerDoSell(), "/application/DoSellUI.fxml");
	}
	
	@FXML
	private void handleFindSellMenu() {
		application.utils.swapPane(workerPane, new ControllerFindSell(), "/application/FindSellUI.fxml");
	}
	
	@FXML
	private void handleInitMoneyMenu() {
		application.utils.swapPane(workerPane, new ControllerInitMoney(), "/application/InitMoneyUI.fxml");
	}
	
	
	
	
	
	@FXML
    private void turnStart() {
		turnOnMenu.setDisable(true);
	}
	
	
	
	
	
	

}
