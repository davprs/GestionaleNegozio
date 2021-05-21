package controller;

import java.util.Calendar;

import javafx.fxml.FXML;

public class ControllerInitMoney extends ControllerLogin{
	
    public void initialize() {
    	float val = (float) 200.5;
    	@SuppressWarnings("deprecation")
		String date = Calendar.getInstance().getTime().toGMTString();
    	initializedMoneyInfo.setText("fondocassa inizializzato:" + val + "e alle ore" + date + "." );
    }
    
    @FXML
    public void handleInitMoneyBtn() {
    	
    }

}
