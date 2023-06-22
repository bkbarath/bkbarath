import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login {
    private JPanel lb;
    private JTabbedPane tabbedPane1;
    private JTextField muntext;
    private JPasswordField passwordField1;
    public JButton managelgnbtn;
    public JTextField textField2;
    private JTextField textField3;
    private JButton userlognbtn;
    private JButton usersignbtn;
    private JPasswordField passwordField2;

    public Login() {
        managelgnbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c=null;
                try{
                    Class.forName("org.postgresql.Driver");
                    c= DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st=c.createStatement();
                    String uname= muntext.getText();
                    String password=String.valueOf(passwordField1.getPassword());
                    ResultSet rs=st.executeQuery("select username,password from lblogin where username='"+uname+"' and password='"+password+"'");
                    if (rs.next()){
                        JOptionPane.showMessageDialog(lb,"Login Successfully");
                        JFrame frame = new JFrame("Management");
                        frame.setContentPane(new Management().mgmnt);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);

                    }
                    else{
                        JOptionPane.showMessageDialog(lb,"User name or password is incorrect");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        userlognbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c=null;
                try{
                    Class.forName("org.postgresql.Driver");
                    c= DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st=c.createStatement();
                    String usname=textField2.getText();
                    String pasword=String.valueOf(passwordField2.getPassword());
                    ResultSet rs=st.executeQuery("select username,password from lbuser where username='"+usname+
                            "' and password='"+pasword+"'");
                    if (rs.next()){
                        JOptionPane.showMessageDialog(lb,"Login Successfully");
                        JFrame frame = new JFrame("User");
                        frame.setContentPane(new User().usl);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    }
                    else{
                        JOptionPane.showMessageDialog(lb,"User name or password is incorrect");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        usersignbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("signin");
                frame.setContentPane(new signin().sgn);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().lb);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
