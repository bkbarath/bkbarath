import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ticket {
    private JPanel tc;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabbedPane3;
    private JComboBox soucom;
    private JComboBox descombo;
    private JTextField daytext;
    private JTextField month;
    private JTextField yrtext;
    private JButton button1;
    private JTabbedPane tabbedPane4;
    private JTextArea textArea1;
    private JTable table1;
    private DefaultTableModel model1;

    public Ticket() {
        soucom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c=null;
                try{
                    Class.forName("org.postgresql.Driver");
                    c= DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","kalvi123");

                    descombo.removeAllItems();
                    Statement st2=c.createStatement();
                    ResultSet rs2=st2.executeQuery("select*from "+ soucom.getSelectedItem());
                    String[]state=new String[30];
                    while (rs2.next()) {
                        for(int i=0;i<1;i++) {
                            state[i]=String.valueOf(rs2.getString(1));
                            descombo.addItem(state[i]);
                        }}
                }catch (Exception a){
                    a.printStackTrace();
                }
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String source=String.valueOf(soucom.getSelectedItem());
                String dest=String.valueOf(descombo.getSelectedItem());
                String date=daytext.getText()+"-"+month.getText()+"-"+yrtext.getText();
                SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");
                Date date1=new Date();
                try {
                    date1=formatter2.parse(date);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                Connection c=null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kalvi123");
                    Statement st = c.createStatement();
                    ResultSet rs=st.executeQuery("select * from businfo where source='"+source+"' and destination='"+dest+"' and date='"+date+"'");
                    String[]column={"busid","driverid","source","destination","date","time","fare"};
                    String data[][]=new String[20][20];
                    int a=0;
                    while (rs.next()){
                        for(int i=0;i<7;i++){
                            data[a][i]=String.valueOf(rs.getString(column[i]));
                        }a++;
                    }table1.setModel(new DefaultTableModel(data,column));
                }catch (Exception a) {
                    a.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ticket");
        frame.setContentPane(new Ticket().tc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
