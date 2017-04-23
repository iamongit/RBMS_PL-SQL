
import java.awt.BorderLayout;
import java.awt.Dimension;
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

/**
 *
 * @author pk
 */
public class MonthlyReportForm {

    ResultSet rs = null;

    Util util = new Util();
    Statement stmt = null;
    CallableStatement cs = null;

    public MonthlyReportForm() throws Exception {
        //Initializing frame
        InitiateFrame();
    }
 
    //Declaring all frames and components inside it.
    void InitiateFrame() throws Exception {
        JFrame frame = new JFrame("Monthly Sale Report");
        JPanel panel = new JPanel();
        //panel.setLayout(new BorderLayout());

        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension scr = t.getScreenSize();
        int ht = scr.height;
        int wd = scr.width;
        frame.setSize(800, 600);
        frame.setLocation(wd / 4, ht / 4);

        frame.add(panel);
        frame.setVisible(true);

        final JLabel headerLabel = new JLabel("Product Id");
        DefaultComboBoxModel model = null;
        getResultSetForPid();
        model = util.createDataForComboBox(rs);

        frame.add(panel);

        final JComboBox pidCombo = new JComboBox(model);
        pidCombo.setSelectedIndex(0);
        JButton generateButton = new JButton("Generate");

        JScrollPane pane = new JScrollPane(pidCombo);
        panel.add(headerLabel);
        panel.add(pane);
        panel.add(generateButton);
        frame.setVisible(true);

        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
                try {
                    new MonthlyReport(String.valueOf(pidCombo.getItemAt(pidCombo.getSelectedIndex())));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        stmt.close();
    }

    void getResultSetForPid() throws Exception {
        stmt = Util.getDBConnection().createStatement();
        rs = stmt.executeQuery("SELECT pid FROM products");
    }

}
