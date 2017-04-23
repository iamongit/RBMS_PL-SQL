
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pk
 */
public class AddPurchaseForm {

    ResultSet rsPid = null;
    ResultSet rsEid = null;
    ResultSet rsCid = null;
 ResultSet rset;
    
    Util util = new Util();
    Statement pidStmt = null;
    Statement eidStmt = null;
    Statement cidStmt = null;

    CallableStatement cs = null;
      Statement selectStmt =null;

    public AddPurchaseForm() throws Exception {
        //Initializing frame
        InitiateFrame();
    }
 //Declaring all frames and components inside it.
    void InitiateFrame() throws Exception {
        JFrame frame = new JFrame("Add new purchase");
        JPanel panel = new JPanel();
        
    panel.setLayout(new FlowLayout());
    Toolkit t = Toolkit.getDefaultToolkit();
        Dimension scr = t.getScreenSize();
        int ht = scr.height;
        int wd = scr.width;
        frame.setSize(1100, 600);
        frame.setLocation(wd / 4, ht / 4);
        frame.add(panel);
        
        
        frame.setLayout(new GridLayout(5, 1));
        

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(0, 9));
       panel.add(innerPanel);
       
        //Product Id Combo Box
        final JLabel labelPid = new JLabel("Product Id");
        DefaultComboBoxModel modelPid = null;
        getResultSetForPid();
        modelPid = util.createDataForComboBox(rsPid);

        final JComboBox comboPid = new JComboBox(modelPid);
        comboPid.setSelectedIndex(0);

        JScrollPane panePid = new JScrollPane(comboPid);
        panePid.add(labelPid);
        innerPanel.add(labelPid);
        innerPanel.add(panePid);

        //Product Id Combo Box
        final JLabel labelCid = new JLabel("Customer Id");
        DefaultComboBoxModel modelCid = null;
        getResultSetForCid();
        modelCid = util.createDataForComboBox(rsCid);

        final JComboBox comboCid = new JComboBox(modelCid);
        comboCid.setSelectedIndex(0);

        JScrollPane paneCid = new JScrollPane(comboCid);
        paneCid.add(labelCid);
        innerPanel.add(labelCid);
        innerPanel.add(paneCid);

        //Product Id Combo Box
        final JLabel labelEid = new JLabel("Employee Id");
        DefaultComboBoxModel modelEid = null;
        getResultSetForEid();
        modelEid = util.createDataForComboBox(rsEid);

        final JComboBox comboEid = new JComboBox(modelEid);
        comboEid.setSelectedIndex(0);

        JScrollPane paneEid = new JScrollPane(comboEid);
        paneEid.add(labelEid);
        innerPanel.add(labelEid);
        innerPanel.add(paneEid);

        final JLabel labelQuantity = new JLabel("Quantity");
        innerPanel.add(labelQuantity);

        final JTextField quantityField = new JTextField();
        innerPanel.add(quantityField);
        
        quantityField.setDocument(new MaxLimit(5));


        JButton generateButton = new JButton("Add Purchase");
        generateButton.setSize(300, 100);
        innerPanel.add(generateButton);

 
        final JLabel labelConfirmMsg = new JLabel("",JLabel.CENTER);
        labelConfirmMsg.setSize(300, 100);
        frame.add(labelConfirmMsg);
        
                final JLabel labelConfirmMsg1 = new JLabel("",JLabel.CENTER);
        labelConfirmMsg1.setSize(300, 100);
        frame.add(labelConfirmMsg1);
        
                final JLabel labelConfirmMsg2 = new JLabel("",JLabel.CENTER);
        labelConfirmMsg2.setSize(300, 100);
        frame.add(labelConfirmMsg2);

         
         frame.setVisible(true);
        

         //When generate button is clicked this function will be called
         //This will call proc_add_purchase procedure and will add record in purchase
         //It also validates fields and handles exceptions.
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String pid = String.valueOf(comboPid.getItemAt(comboPid.getSelectedIndex()));
                String eid = String.valueOf(comboEid.getItemAt(comboEid.getSelectedIndex()));
                String cid = String.valueOf(comboCid.getItemAt(comboCid.getSelectedIndex()));
                int quantity = 0;

                if (quantityField.getText().equalsIgnoreCase("")) {
                    labelConfirmMsg.setText("Please enter Quantity");
                    return;
                }

                try {
                    quantity = Integer.parseInt(quantityField.getText());
                } catch (Exception ex) {
                    labelConfirmMsg.setText("Invalid Quantity");
                    return;
                }

                if (quantity <= 0) {
                    labelConfirmMsg.setText("Quantity should be greater than 0.");
                    return;
                }
                try {
                    
                    selectStmt= Util.getDBConnection().createStatement (); 
                    
                   String selectQuery="SELECT qoh,qoh_threshold FROM products where pid='"+pid+"'";
                   
                    rset = selectStmt.executeQuery (selectQuery);
                    long qohSelect=0;
                    long qohThresholdSelect=0;
                    while (rset.next ()) {
                        qohSelect=Long.parseLong(rset.getString (1));
                         qohThresholdSelect=Long.parseLong(rset.getString (2));
                       
                      }
                    
                    
    
                    cs = Util.getDBConnection().prepareCall("begin project2.proc_add_purchase(:1,:2,:3,:4); end;");

                    //set the in parameter (the first parameter)     
                    cs.setString(1, eid);
                    cs.setString(2, cid);
                    cs.setString(3, pid);
                    cs.setInt(4, quantity);
                    //execute the stored procedure
                    cs.executeQuery();

                    System.out.print(qohSelect-quantity);
                    if ((qohSelect - quantity) < 0) {
                        labelConfirmMsg.setText("Insufficient quantity in stock.");
                    } else if ((qohSelect - quantity) < qohThresholdSelect) {
                        labelConfirmMsg.setText("Current qoh of the product is below the required threshold and new supply is required.");
                        rset = selectStmt.executeQuery(selectQuery);
                        long qohSelectNew = 0;
                        while (rset.next()) {
                            qohSelectNew = Long.parseLong(rset.getString(1));

                        }
                        labelConfirmMsg1.setText("New value of the qoh of the product is: "+qohSelectNew);
                        labelConfirmMsg2.setText("Purchase has been successfully added.");
                    } else {
                        labelConfirmMsg.setText("Purchase has been successfully added.");
                    }

                   
                } catch (SQLException ex) {
                    
                      labelConfirmMsg.setText(ex.getMessage());
                      System.out.println(ex.getMessage());
                      
                }
            }
        });

        pidStmt.close();
        eidStmt.close();
        cidStmt.close();
        // cs.close();

        rsPid.close();
        rsCid.close();
        rsEid.close();
    }

    //Get result set from products
    void getResultSetForPid() throws Exception {
        pidStmt = Util.getDBConnection().createStatement();
        rsPid = pidStmt.executeQuery("SELECT pid FROM products");
    }
    //Get result set from employees
    void getResultSetForEid() throws Exception {
        eidStmt = Util.getDBConnection().createStatement();
        rsEid = eidStmt.executeQuery("SELECT eid FROM employees");
    }
    //Get result set from customers
    void getResultSetForCid() throws Exception {
        cidStmt = Util.getDBConnection().createStatement();
        rsCid = cidStmt.executeQuery("SELECT cid FROM customers");
    }

}
