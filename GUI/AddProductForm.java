
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.ResultSet;
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
public class AddProductForm {


    Util util = new Util();

    CallableStatement cs = null;

    public AddProductForm() throws Exception {
        //Initializing frame
        InitiateFrame();
    }
     //Declaring all frames and components inside it.
    void InitiateFrame() throws Exception {
        JFrame frame = new JFrame("Add new Product");
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension scr = t.getScreenSize();
        int ht = scr.height;
        int wd = scr.width;
        frame.setSize(1500, 600);
        frame.setLocation(wd / 20, ht / 10);
        frame.add(panel);
        
        frame.setLayout(new GridLayout(5,1));
 
    
         JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(0, 13));
       panel.add(innerPanel);

        frame.add(panel);
        
         final JLabel labelId = new JLabel("Product Id");
        innerPanel.add(labelId);

        final JTextField idField = new JTextField();
        innerPanel.add(idField);
        
        idField.setDocument(new MaxLimit(4));
        

        final JLabel labelName = new JLabel("Name");
        innerPanel.add(labelName);

        final JTextField nameField = new JTextField();
        innerPanel.add(nameField);

        nameField.setDocument(new MaxLimit(15));
        
        final JLabel labelQoh = new JLabel("QOH");
        innerPanel.add(labelQoh);

        final JTextField qohField = new JTextField();
        innerPanel.add(qohField);

           qohField.setDocument(new MaxLimit(5));
        
        final JLabel labelQohThreshold = new JLabel("QOH Threshold");
        innerPanel.add(labelQohThreshold);

        final JTextField qohThresholdField = new JTextField();
        innerPanel.add(qohThresholdField);
        
        qohThresholdField.setDocument(new MaxLimit(4));


        final JLabel labelPrice = new JLabel("Original Price");
        innerPanel.add(labelPrice);

        final JTextField priceField = new JTextField();
        innerPanel.add(priceField);
        
        priceField.setDocument(new MaxLimit(9));


        //Product Id Combo Box
        final JLabel labelDiscRate = new JLabel("Discount Rate");

        final DefaultComboBoxModel discRateModel = new DefaultComboBoxModel();

        discRateModel.addElement("0.0");
        discRateModel.addElement("0.1");
        discRateModel.addElement("0.2");
        discRateModel.addElement("0.3");
        discRateModel.addElement("0.4");
        discRateModel.addElement("0.5");
        discRateModel.addElement("0.6");
        discRateModel.addElement("0.7");
        discRateModel.addElement("0.8");

        final JComboBox comboDiscRate = new JComboBox(discRateModel);
        comboDiscRate.setSelectedIndex(0);

        JScrollPane paneDiscRate = new JScrollPane(comboDiscRate);
        paneDiscRate.add(labelDiscRate);
        innerPanel.add(labelDiscRate);
        innerPanel.add(paneDiscRate);

        JButton addButton = new JButton("Add Product");
        addButton.setSize(300, 100);
        innerPanel.add(addButton);

        final JLabel labelConfirmMsg = new JLabel();
        labelConfirmMsg.setSize(300, 100);
        frame.add(labelConfirmMsg);

           frame.setVisible(true);

           
           //When add button is clicked this function will be called
         //This will call proc_add_product procedure and will add record in products
         //It also validates fields and handles exceptions.
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                double discRate = Double.parseDouble(String.valueOf(comboDiscRate.getItemAt(comboDiscRate.getSelectedIndex())));
                int qoh = 0;
                int qoh_threshold = 0;
                double price = 0.0;
                String name = "";
                String pid="";

               
                if (nameField.getText().equalsIgnoreCase("") || nameField.getText()==null) {
                    name = "";
                } 
                
                 
                  //Validate Pid
                if (idField.getText().equalsIgnoreCase("")) {
                    labelConfirmMsg.setText("Please enter Product Id");
                    return;
                } 
                pid=idField.getText();
                
                //Validate QOH
                if (qohField.getText().equalsIgnoreCase("")) {
                    labelConfirmMsg.setText("Please enter QOH");
                    return;
                }

                try {
                    qoh = Integer.parseInt(qohField.getText());
                } catch (Exception ex) {
                    labelConfirmMsg.setText("Invalid QOH");
                    return;
                }

                //Validate QOH Threshold
                if (qohThresholdField.getText().equalsIgnoreCase("")) {
                    labelConfirmMsg.setText("Please enter QOH Threshold");
                    return;
                }

                try {
                    qoh_threshold = Integer.parseInt(qohThresholdField.getText());
                } catch (Exception ex) {
                    labelConfirmMsg.setText("Invalid QOH Threshold");
                    return;
                }

                //Validate Original Price
                if (priceField.getText().equalsIgnoreCase("")) {
                    labelConfirmMsg.setText("Please enter Price");
                    return;
                }

                try {
                    price = Double.parseDouble(priceField.getText());
                } catch (Exception ex) {
                    labelConfirmMsg.setText("Invalid Price");
                    return;
                }

           
                try {

                    cs = Util.getDBConnection().prepareCall("begin project2.proc_add_product(:1,:2,:3,:4,:5,:6); end;");
                    cs.setString(1, pid);
                    //set the in parameter (the first parameter)     
                    cs.setString(2, name);
                    cs.setInt(3, qoh);
                    cs.setInt(4, qoh_threshold);
                    cs.setDouble(5, price);
                    cs.setDouble(6, discRate);
                    //execute the stored procedure
                    cs.executeQuery();

                    labelConfirmMsg.setText("Product has been successfully added.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


    }

   

}
