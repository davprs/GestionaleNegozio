package controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Set;

import application.utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ControllerStat extends ControllerLogin{
    
    public ControllerStat(Connection conn, Integer id) {
		super(conn, id);
		// TODO Auto-generated constructor stub
	}

	public void initialize() {
        statBar.getXAxis().setAnimated(false);
        statLine.getXAxis().setAnimated(false);

        for (Node node : statLine.lookupAll(".chart-legend-item")) {
            if (node instanceof Label) {
                System.out.println("Label instance");
                ((Label) node).setWrapText(true);
                ((Label) node).setManaged(true);
                ((Label) node).setPrefWidth(380);
                ((Label) node).setText("ciao");
            }
        }
        
        statBar.setVisible(false);
        statLine.setVisible(false);
        statChoise.getItems().add("Categoria");
        statChoise.getItems().add("Bilancio");
		statBar.setLegendVisible(false);
		statLine.setLegendVisible(false);
        
    }
	
	@FXML
	private void showStat() {
		String start = startStat.getValue().toString();
		String end   = endStat.getValue().toString();
		
		if(start == null || end == null) {
			return;
		}
		
		if(startStat.getValue().isAfter(endStat.getValue())) {
			String tmp;
			tmp= end;
			end = start;
			start = tmp;
		}
		if(statChoise.getSelectionModel().getSelectedItem().equals("Categoria")) {
			statLine.setVisible(false);
			statBar.setVisible(true);
			showCatData(start, end);			
		} else if(statChoise.getSelectionModel().getSelectedItem().equals("Bilancio")) {
			statLine.setVisible(true);
			statBar.setVisible(false);
			showBalanceData(start, end);			
			
		}
	}
	
	private void showCatData(String start, String end) {
		statBar.getData().clear();
		
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
				String query = "SELECT categoria, COUNT(*) FROM ricerca "
						+ "WHERE nome IS NULL AND data between \"" + start + "\" and \"" + end + "\" "
						+ "GROUP BY categoria having categoria not in (\"Tutto\");";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				
				while ( rs.next() ) { 
					
					Series<String, Integer> cat = new XYChart.Series<>();
					cat.getData().add(new XYChart.Data<>(rs.getString(1), rs.getInt(2)));
					statBar.getData().addAll(cat);
					
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

	private void showBalanceData(String start, String end) {
		statLine.getData().clear();

		Statement stmt = null;
		String query = null;
		try {
			stmt  = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			application.utils.showPopupPane(e.toString());
		}
		
		if(stmt != null) {
			try {
				query = "SELECT qb.dy as giorno, COALESCE(entrate, 0) as entrate, COALESCE(uscite, 0) as uscite, COALESCE(sum(entrate - uscite) over (order by sg.data), 0) as bal from saldo_giornaliero sg "
						+ "right join ( "
						+ "    select gen_date as dy from "
						+ "	(select adddate('1970-01-01',t4*10000 + t3*1000 + t2*100 + t1*10 + t0) as gen_date from "
						+ "	(select 0 t0 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, "
						+ "	(select 0 t1 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, "
						+ "	(select 0 t2 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, "
						+ "	(select 0 t3 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, "
						+ "	(select 0 t4 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) as D "
						+ "	where gen_date between \"" + start + "\" and \"" + end + "\" "
						+ "    ) as qb "
						+ "on sg.data = qb.dy;";
				ResultSet rs = stmt.executeQuery(query);
				
				Series<String, BigDecimal> bal = new XYChart.Series<>();
				Series<String, BigDecimal> inc = new XYChart.Series<>();
				Series<String, BigDecimal> exp = new XYChart.Series<>();
				bal.setName("Bilancio");
				inc.setName("Entrate");
				exp.setName("Espese");
				while(rs.next()) {
					
					inc.getData().add(new XYChart.Data<>(rs.getString(1), (BigDecimal)rs.getObject(2)));
					exp.getData().add(new XYChart.Data<>(rs.getString(1), ((BigDecimal)rs.getObject(3)).negate()));
					bal.getData().add(new XYChart.Data<>(rs.getString(1), (BigDecimal)rs.getObject(4)));

				}
				statLine.getData().addAll(Arrays.asList(inc, exp, bal));
				
				inc.getNode().setStyle("-fx-stroke: #aaeeaa;");
				exp.getNode().setStyle("-fx-stroke: #eeaaaa;");
				bal.getNode().setStyle("-fx-stroke: #5555dd;");
				
				for (Data<String, BigDecimal> entry : inc.getData()) {      
				    entry.getNode().setStyle("-fx-background-color: #aaeeaa, white;\n"
				        + "    -fx-background-insets: 0, 2;\n"
				        + "    -fx-background-radius: 5px;\n"
				        + "    -fx-padding: 5px;");
				}
				
				for (Data<String, BigDecimal> entry : exp.getData()) {      
				    entry.getNode().setStyle("-fx-background-color: #eeaaaa, white;\n"
				        + "    -fx-background-insets: 0, 2;\n"
				        + "    -fx-background-radius: 5px;\n"
				        + "    -fx-padding: 5px;");
				}
				
				for (Data<String, BigDecimal> entry : bal.getData()) {      
				    entry.getNode().setStyle("-fx-background-color: #5555dd, white;\n"
				        + "    -fx-background-insets: 0, 2;\n"
				        + "    -fx-background-radius: 5px;\n"
				        + "    -fx-padding: 5px;");
				}


				
			} catch (SQLException e) {
				e.printStackTrace();
				utils.showPopupPane(e.toString());
			}
		}
	}

}
