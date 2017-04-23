
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import oracle.jdbc.pool.OracleDataSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pk
 */
public class Util {

    static OracleDataSource ds = null;
    static Connection conn = null;

    //Creating JDBC connection
    void createConnection() throws Exception {
        ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:acad111");
        conn = ds.getConnection("pkolhe1", "ILCmti247");
        System.out.println("Connection created");
    }

    //Returning JDBC connection instance
    //Only one instance is created per application and same instance is returned
    static public Connection getDBConnection() {
        return conn;
    }

    //Create table data to show in tables based on result set
    DefaultTableModel createDataForTable(ResultSet rs) throws Exception {

        ResultSetMetaData meta = rs.getMetaData();

        Vector<String> columns = new Vector<String>();
        int count = meta.getColumnCount();
        for (int i = 1; i <= count; i++) {
            columns.add(meta.getColumnName(i));
        }

        Vector<Vector<Object>> columnData = new Vector<Vector<Object>>();
        while (rs.next()) {

            Vector<Object> vector = new Vector<Object>();
            for (int i = 1; i <= count; i++) {

                vector.add(rs.getObject(i));
            }
            columnData.add(vector);
        }

        return new DefaultTableModel(columnData, columns);
    }
  

    //Create drop down data to show in drop down based on result set
    DefaultComboBoxModel createDataForComboBox(ResultSet rs) throws Exception {

        ResultSetMetaData meta = rs.getMetaData();

        Vector<Object> columnData = new Vector<Object>();
        while (rs.next()) {
            columnData.add(rs.getObject(1));
        }

        return new DefaultComboBoxModel(columnData);
    }

    void closeConnection() throws Exception {
        if (conn != null) {
            conn.close();
        }
        if (ds != null) {
            ds.close();
        }
    }
}
