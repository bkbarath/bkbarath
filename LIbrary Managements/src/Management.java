import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Management {
    public JPanel mgmnt;
    private JTabbedPane tabbedPane1;
    private JTextField bknametext;
    private JTextField arnametext;
    private JTextField editext;
    private JTextField pbtext;
    private JTable addtable;
    private JTextField yrtext;
    private JButton addButton;
    private JTextField avtext;
    private JTextField upbukid;
    private JTable upttable;
    private JComboBox edicombo;
    private JTextField chngtext;
    private JButton deleteButton;
    private JButton updateButton;
    private JTextField inptbdate;
    private JButton entrbtn;
    private JTable bookingtable;
    private JTable librarytable;
    private JTextField untxt;
    private JButton sbmtbtn;
    private JTextField inptrdate;
    private DefaultTableModel model;

    public Management() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st = c.createStatement();
                    String bname = bknametext.getText();
                    String aname = arnametext.getText();
                    String pblisher = pbtext.getText();
                    int edition = Integer.parseInt(editext.getText());
                    int available = Integer.parseInt(avtext.getText());
                    int year = Integer.parseInt(yrtext.getText());
                    int result = JOptionPane.showConfirmDialog(mgmnt, "Sure? You want to add?", "BOOK ADD",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        st.addBatch("insert into library(Bookid,BookName,AuthourName,Publisher,Edition,Available,year)values(nextval('bukid'),'" +
                                bname + "','" + aname + "','" + pblisher + "'," + edition + "," + available + "," + year + ")");
                        st.executeBatch();
                        JOptionPane.showMessageDialog(mgmnt, "BOOK added Successfully");
                        Statement st1 = c.createStatement();
                        ResultSet rs = st1.executeQuery("select*from library");
                        String[] column = {"Bookid", "BookName", "AuthourName", "Publisher", "Edition", "Available", "year"};
                        String[][] data = new String[30][30];
                        int a = 0;
                        while (rs.next()) {
                            for (int i = 0; i < column.length; i++) {
                                data[a][i] = String.valueOf(rs.getString(column[i]));
                            }
                            a++;
                        }
                        addtable.setModel(new DefaultTableModel(data, column));
                    } else if (result == JOptionPane.NO_OPTION) {
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st = c.createStatement();
                    String name = String.valueOf(edicombo.getSelectedItem());
                    String input = chngtext.getText();
                    int buksid = Integer.parseInt(upbukid.getText());
                    int result = JOptionPane.showConfirmDialog(mgmnt, "Make Sure You Want To Update", "BOOK UPDATE",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        st.addBatch("update library set " + name + "='" + input + "' where bookid=" + buksid);
                        st.executeBatch();
                        JOptionPane.showMessageDialog(mgmnt, "BOOK details updated Successfully");
                        Statement st1 = c.createStatement();
                        ResultSet rs = st1.executeQuery("select*from library");
                        String[] column = {"Bookid", "BookName", "AuthourName", "Publisher", "Edition", "Available", "year"};
                        String[][] data = new String[30][30];
                        int a = 0;
                        while (rs.next()) {
                            for (int i = 0; i < column.length; i++) {
                                data[a][i] = String.valueOf(rs.getString(column[i]));
                            }
                            a++;
                        }
                        upttable.setModel(new DefaultTableModel(data, column));
                    } else if (result == JOptionPane.NO_OPTION) {

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st = c.createStatement();
                    String name = String.valueOf(edicombo.getSelectedItem());
                    String input = chngtext.getText();
                    int buksid = Integer.parseInt(upbukid.getText());
                    int result = JOptionPane.showConfirmDialog(mgmnt, "Make Sure You Want To delete?", "BOOK DELETE",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        st.addBatch("delete from library where bookid=" + buksid + " and " + name + " = '" + input + "'");
                        st.executeBatch();
                        JOptionPane.showMessageDialog(mgmnt, "BOOK details deleted Successfully");
                        Statement st1 = c.createStatement();
                        ResultSet rs = st1.executeQuery("select*from library");
                        String[] column = {"Bookid", "BookName", "AuthourName", "Publisher", "Edition", "Available", "year"};
                        String[][] data = new String[30][30];
                        int a = 0;
                        while (rs.next()) {
                            for (int i = 0; i < column.length; i++) {
                                data[a][i] = String.valueOf(rs.getString(column[i]));
                            }
                            a++;
                        }
                        upttable.setModel(new DefaultTableModel(data, column));
                    } else if (result == JOptionPane.NO_OPTION) {
                    }
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        });

        entrbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st = c.createStatement();
                    String bdate=inptbdate.getText();
                    String rdate=inptrdate.getText();
                    ResultSet rs2 = st.executeQuery("select*from lbbookings where borrowdate between '"+bdate+"' and '"+rdate+"'");
                    String[] column = {"username", "bookid","borrowid", "bookname", "borrowdate", "returndate","returned"};
                    String[][] data = new String[30][30];
                    int a = 0;
                    while (rs2.next()) {
                        for (int i = 0; i < column.length; i++) {
                            data[a][i] = String.valueOf(rs2.getString(column[i]));
                        }
                        a++;
                    }bookingtable.setModel(new DefaultTableModel(data,column));
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        });
        sbmtbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st = c.createStatement();
                    ResultSet rs=st.executeQuery("select username from lblogin where username='"+untxt.getText()+"'");
                    if(rs.next()){
                        ResultSet rs2=st.executeQuery("select*from library");
                        String[] column = {"Bookid", "BookName", "AuthourName", "Publisher", "Edition", "Available", "year"};
                        String[][] data = new String[30][30];
                        int a = 0;
                        while (rs2.next()) {
                            for (int i = 0; i < column.length; i++) {
                                data[a][i] = String.valueOf(rs2.getString(column[i]));
                            }
                            a++;
                        }librarytable.setModel(new DefaultTableModel(data, column));
                    }else {
                        JOptionPane.showMessageDialog(mgmnt,"Library Not found :(");
                    }

                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Management");
        frame.setContentPane(new Management().mgmnt);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
