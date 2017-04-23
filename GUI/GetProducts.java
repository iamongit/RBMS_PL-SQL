/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author pk
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import oracle.jdbc.*;

public class GetProducts  extends JFrame
{
     ResultSet rs=null;

       Util util=new Util();
       CallableStatement cs =null;
        
    public GetProducts () throws Exception
    {
         //Initializing frame       
        InitiateFrame();
    }
    
     //Declaring all frames and components inside it.
    void InitiateFrame() throws Exception{
        JFrame frame=new JFrame("Products");
        JPanel panel=new JPanel();
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
        
        DefaultTableModel model = null;
        getResultSet();
        model = util.createDataForTable(rs);
        table =new JTable(model);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
       scroll= new JScrollPane(table);
        panel.add(scroll);
        
               //close the result set, statement, and the connection
        cs.close();
    }
    //Get result set from procedure
    void getResultSet() throws Exception{
    
        //Prepare to call stored procedure:
         cs = Util.getDBConnection().prepareCall("begin ? := project2.get_products(); end;");
        
	//register the out parameter (the first parameter)
        cs.registerOutParameter(1, OracleTypes.CURSOR);
 
        // execute and retrieve the result set
        cs.execute();
        rs = (ResultSet)cs.getObject(1);

    }
      
}