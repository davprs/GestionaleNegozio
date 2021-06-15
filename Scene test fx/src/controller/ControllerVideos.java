package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ControllerVideos extends ControllerLogin{
	final ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

	public ControllerVideos(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}
	
	@FXML
	public void initialize() {		
		updateVideoBtn.setPrefWidth(videoSidePane.getPrefWidth());
		playVideoBtn.setPrefWidth(videoSidePane.getPrefWidth());
		deleteVideoBtn.setPrefWidth(videoSidePane.getPrefWidth());

		updateVideoBtn.setAlignment(Pos.TOP_CENTER);
		playVideoBtn.setAlignment(Pos.TOP_CENTER);
		deleteVideoBtn.setAlignment(Pos.TOP_CENTER);
		
		updateVideoTable();
		
		updateVideoBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				updateVideoTable();
			}
		});
		
		playVideoBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String path = videoTV.getSelectionModel().getSelectedItem().get(3);
				if(path != null) {
					playVideo(path);
				}
			}
		});
		
		deleteVideoBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String id_videocamera = videoTV.getSelectionModel().getSelectedItem().get(0);
				String data_inizio = videoTV.getSelectionModel().getSelectedItem().get(1);
				removeVideo(id_videocamera , data_inizio);
			}
		});
	}
	
	
	private void updateVideoTable() {
		data.removeAll(data);
		videoTV.getItems().clear();
		videoTV.getColumns().clear();
		
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
				String query = "select id_videocamera, data_inizio, timediff(data_fine, data_inizio) as durata, video_path "
						+ "from video "
						+ "where valido = true;";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				
				videoTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
					videoTV.getColumns().add(col); 						
				}
				
				
				while ( rs.next() ) { 
					
					ObservableList<String> row = FXCollections.observableArrayList();
					row.add(((Integer)rs.getObject(1)).toString());
					row.add(((LocalDateTime)rs.getObject(2)).toString());
					row.add(((Time)rs.getObject(3)).toString());
					row.add((String)rs.getObject(4));
		
					System.out.println("AAAAAAAAAAAAAAAAAAAAAAA" + row);
					
					data.add(row);
					videoTV.setItems(data);
					
					
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
		
	private void playVideo(String path) {
		try {
			Desktop.getDesktop().open(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	private void removeVideo(String id_videocamera, String data) {
		//UPDATE video SET valido = false WHERE id_videocamera = 1 AND data_inizio = "2021-06-15 8:00:00";
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
				String query = "UPDATE video SET valido = false WHERE id_videocamera = " + id_videocamera + " AND data_inizio = \"" + data + "\";";
				System.out.println(query);
				int rs = stmt.executeUpdate(query);
				
				stmt.close();
				updateVideoTable();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				application.utils.showPopupPane(e.toString());
			}
		
		
		}
		
	}
}
