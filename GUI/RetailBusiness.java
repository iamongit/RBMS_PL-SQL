
/**
 *
 * @author pk
 */
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RetailBusiness implements ActionListener {

    
    //Declaring all frames and components inside it.
    JButton buttonProducts = new JButton("Show Products");
    JButton buttonCustomers = new JButton("Show customers");
    JButton buttonPurchases = new JButton("Show purchases");
    JButton buttonSuppy = new JButton("Show supply");
    JButton buttonSuppliers = new JButton("Show suppliers");
    JButton buttonEmployees = new JButton("Show Employees");

    JButton buttonMonthylReport = new JButton("Generate Monthly Report");
    JButton buttonAddPurchase = new JButton("Add Purchase");
    JButton buttonAddProduct = new JButton("Add Product");

    JFrame frame = new JFrame("Retail Business Management System");
    JPanel buttonPanel = new JPanel();

    public RetailBusiness() {
        //Initializing frame
        InitializeFrame();

    }

    void InitializeFrame() {
        
       
        frame.add(buttonPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(800, 300);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        buttonPanel.setLayout(new FlowLayout());

        buttonProducts.addActionListener(this);
        buttonProducts.setActionCommand("buttonProducts");

        buttonCustomers.addActionListener(this);
        buttonCustomers.setActionCommand("buttonCustomers");

        buttonPurchases.addActionListener(this);
        buttonPurchases.setActionCommand("buttonPurchases");

        buttonSuppy.addActionListener(this);
        buttonSuppy.setActionCommand("buttonSuppy");

        buttonSuppliers.addActionListener(this);
        buttonSuppliers.setActionCommand("buttonSuppliers");

        buttonEmployees.addActionListener(this);
        buttonEmployees.setActionCommand("buttonEmployees");

        buttonMonthylReport.addActionListener(this);
        buttonMonthylReport.setActionCommand("buttonMonthylReport");

        buttonAddPurchase.addActionListener(this);
        buttonAddPurchase.setActionCommand("buttonAddPurchase");

        buttonAddProduct.addActionListener(this);
        buttonAddProduct.setActionCommand("buttonAddProduct");

        buttonPanel.add(buttonProducts);
        buttonPanel.add(buttonCustomers);
        buttonPanel.add(buttonPurchases);
        buttonPanel.add(buttonSuppy);
        buttonPanel.add(buttonSuppliers);
        buttonPanel.add(buttonEmployees);
        buttonPanel.add(buttonMonthylReport);
        buttonPanel.add(buttonAddPurchase);
        buttonPanel.add(buttonAddProduct);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //This function will be called when user clicks on any button.
    //We are intializing respective frame after clicking button.
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        try {
            if (cmd.equals("buttonProducts")) {
                new GetProducts();
            } else if (cmd.equals("buttonCustomers")) {
                new GetCustomers();
            } else if (cmd.equals("buttonPurchases")) {
                new GetPurchases();
            } else if (cmd.equals("buttonSuppy")) {
                new GetSupply();
            } else if (cmd.equals("buttonSuppliers")) {
                new GetSuppliers();
            } else if (cmd.equals("buttonEmployees")) {
                new GetEmployees();
            } else if (cmd.equals("buttonMonthylReport")) {
                new MonthlyReportForm();
            } else if (cmd.equals("buttonAddPurchase")) {
                new AddPurchaseForm();
            } else if (cmd.equals("buttonAddProduct")) {
                new AddProductForm();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Util util = new Util();
            //Creating JDBC connection for first time
            util.createConnection();
            //Initiazling frame
            RetailBusiness window = new RetailBusiness();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
