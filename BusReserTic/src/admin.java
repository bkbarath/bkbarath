import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class admin {
    public JPanel amn;
    public JTabbedPane tabbedPane1;
    public JTextField butext;
    public JTextField drtext;
    public JTextField sotext;
    public JTextField dentext;
    public JTextField dadetext;
    public JTextField dimedext;
    public JTextField baretext;
    public JButton busdeleteButton;
    public JTextField drivtext;
    public JTextField dnametext;
    public JTextField agtext;
    public JTextField pnotext;
    public JButton deriveupdatebtn;
    public JButton drivdeleteButton;
    public JButton drivaddButton;
    public JButton busaddbtn;
    public JButton busupdbtn;
    public JTextField captext;
    public JTextField availtext;
    public JTabbedPane tabbedPane2;
    public JTextField textField1;
    public JTextField textField2;
    public JComboBox comboBox2;
    public JTextField didtext;
    public JTextField entbus;
    public JComboBox buscombo;
    public JComboBox drivecombo;
    public JTable collectiontable;
    public JButton collectionsButton;
    public JTextField edatetext;
    public JButton showDetailsButton;
    public JTextField textField4;
    public DefaultTableModel model;

    public admin() {
    busaddbtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String date=String.valueOf(dadetext.getText());
            String time=String.valueOf(dimedext.getText());
            String querry="insert into businfo values ("+butext.getText()+","+drtext.getText()+",'"+
                    sotext.getText()+"','"+dentext.getText()+"','"+date+"','"+time+
                    "',"+baretext.getText()+","+captext.getText()+","+availtext.getText()+")";
            admin.busupdate(querry);
            JOptionPane.showMessageDialog(amn,"Bus details added");
        }
    });
    busupdbtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query="update businfo set "+buscombo.getSelectedItem()+" = '"+textField2.getText()+"' where busid= "+entbus.getText();
            admin.busupdate(query);
            JOptionPane.showMessageDialog(amn,"Bus details updated");
        }
    });
    busdeleteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query="delete from businfo where busid="+butext.getText();
            admin.busupdate(query);
            JOptionPane.showMessageDialog(amn,"Bus details deleted");
        }
    });
    drivaddButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query="insert into driverinfo values("+drivtext.getText()+",'"+dnametext.getText()+
                    "',"+agtext.getText()+","+ pnotext.getText()+")";
            admin.busupdate(query);
            JOptionPane.showMessageDialog(amn,"Driver details added");
        }
    });
    deriveupdatebtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query="update driverinfo set "+drivecombo.getSelectedItem()+"= '"+textField1.getText()+
                    "where driverid ="+didtext.getText();
            admin.busupdate(query);
            JOptionPane.showMessageDialog(amn,"Driver details updated");
        }
    });
    drivdeleteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query="delete from driverinfo where driverid ="+drivtext.getText();
            admin.busupdate(query);
            JOptionPane.showMessageDialog(amn,"Driver details deleted");
        }
    });

        collectionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kalvi123");
                    Statement stat = c.createStatement();
                    Statement stat2 = c.createStatement();
                    ResultSet resul=stat.executeQuery("select*from bookings where date='"+edatetext.getText()+"'");
                    String[] column = {"busid","bookedseats", "collection", "nonbookedseats", "cancelations" };
                    String[][] rows = new String[30][30];
                    int a = 0;
                    while (resul.next()){
                        for(int i=0;i<column.length;i++){
                            rows[a][i]=String.valueOf(resul.getString(column[i]));
                        }a++;
                    }
                    collectiontable.setModel(new DefaultTableModel(rows,column));
                }catch (Exception x){
                    x.printStackTrace();
                }
            }
        });
        showDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
public static void busupdate(String query){
    Connection c=null;
    try{
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kalvi123");
        Statement stat = c.createStatement();
        stat.addBatch(query);
        stat.executeBatch();
    }catch (Exception a){
        a.printStackTrace();
    }
}

    public static void main(String[] args) {
        JFrame frame = new JFrame("admin");
        frame.setContentPane(new admin().amn);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
