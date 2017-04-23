
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import oracle.jdbc.OracleTypes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pk
 */
public class MonthlyReport {

    ResultSet rs = null;

    Util util = new Util();
    CallableStatement cs = null;
    String pid;

     //Declaring all frames and components inside it.
    public MonthlyReport(String p_pid) throws Exception {
        //Initializing frame
        this.pid = p_pid;
        InitiateFrame();
    }
//Declaring all frames and components inside it.
    void InitiateFrame() throws Exception {
        JFrame frame = new JFrame("Monthly Report");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JTable table;
        JScrollPane scroll;

        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension scr = t.getScreenSize();
        int ht = scr.height;
        int wd = scr.width;
        frame.setSize(800, 600);
        frame.setLocation(wd / 4, ht / 4);
        frame.add(panel);
        frame.setVisible(true);

        DefaultTableModel model = new DefaultTableModel();
        
        getResultSet();
        model = util.createDataForTable(rs);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        panel.add(scroll);

        //close the result set, statement, and the connection
        cs.close();
    }

    //Get result set from procedure
    void getResultSet() throws Exception {

        //Prepare to call stored procedure:
        cs = Util.getDBConnection().prepareCall(" begin ? := project2.report_monthly_sale(?); end;");

        cs.registerOutParameter(1, OracleTypes.CURSOR);
        //set the in parameter (the first parameter)     
        cs.setString(2, pid);


        cs.execute();
        rs = (ResultSet) cs.getObject(1);

    }
}
