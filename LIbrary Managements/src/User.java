import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class User {
    public JPanel usl;
    private JTabbedPane tabbedPane1;
    private JTextField sbntext;
    private JTable bbtable;
    private JTextField santext;
    private JTextField seditext;
    private JTextField borrowtext;
    private JComboBox srccombo;
    private JComboBox sltcombo;
    private JButton borrowButton;
    private JButton selectButton;
    private JTextField rtntext;
    private JTextField sbkidtext;
    private JTextField spbtext;
    private JTextField syrtext;
    private JTextField inusrtext;
    private JTextField rbidtext;
    private JTextField rustext;
    private JTextField tdydte;
    private JButton retenterbtn;
    private JButton YESButton;
    private JButton OKButton;
    private JTextField finetext;
    private JButton returnButton;
    private JTextField rbntext;
    private JTextField rantext;
    private JTextField rbdtext;
    private JTextField rrdtext;

    public User() {
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row=bbtable.getSelectedRow();
                String bookname=bbtable.getValueAt(row,1).toString();
                String aname=bbtable.getValueAt(row,2).toString();
                String Edition=bbtable.getValueAt(row,4).toString();
                String bookid=bbtable.getValueAt(row,0).toString();
                String pblisher=bbtable.getValueAt(row,3).toString();
                String year=bbtable.getValueAt(row,6).toString();
                sbkidtext.setText(bookid);
                sbntext.setText(bookname);
                santext.setText(aname);
                seditext.setText(Edition);
                spbtext.setText(pblisher);
                syrtext.setText(year);
            }
        });
        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c=null;
                try{
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");

                    Statement st2 = c.createStatement();
                    String uname=inusrtext.getText();
                    int bkid=Integer.parseInt(sbkidtext.getText());
                    String bknme=sbntext.getText();
                    String brdte=borrowtext.getText();
                    String rtdte=rtntext.getText();
                    int row=bbtable.getSelectedRow();
                    int fill=Integer.parseInt(bbtable.getValueAt(row,5).toString());

                        ResultSet rs=st2.executeQuery("select username from lbuser where username='"+uname+"'");
                        if (rs.next()) {
                            Statement st = c.createStatement();
                            st.addBatch("insert into lbbookings (borrowid,username,bookid,bookname,borrowdate,returndate,returned)values (nextval('boid'),'" + uname + "'," + bkid + ",'" +
                                    bknme + "','" + brdte + "','" + rtdte + "','NO')");
                            fill -= 1;
                            st.addBatch("update library set available=" + fill + " where bookid =" + bkid);
                            st.executeBatch();
                            ResultSet rs3=st2.executeQuery("select mail from lbuser where username='"+uname+"'");
                            ResultSet rs4=st2.executeQuery("select username from lbbookings where username='"+uname+"'");
                            String to=null;
                            String bid=null;
                            while (rs3.next())
                                to=rs3.getString(1);
                            while(rs4.next())
                                bid=rs4.getString(1);
                            User.send(to,"BOOking form Madurai Library ","Your Borrow id is "+bid);
                            JOptionPane.showMessageDialog(usl, "Thank You for Visiting Us!\nSpread Education EveryWhere");
                        } else {
                            JOptionPane.showMessageDialog(usl, "Username not Found:(");
                        }
                }catch (Exception a){
                    a.printStackTrace();
                }
            }
        });
        srccombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c=null;
                try{
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st = c.createStatement();
                    String nme=String.valueOf(srccombo.getSelectedItem());
                    ResultSet rs=st.executeQuery("select "+nme+" from library order by "+nme+" asc");
                    String []ins=new String[50];
                    sltcombo.removeAllItems();
                    HashSet <String>set=new HashSet<>();
                    while(rs.next()) {
                        for (int i = 0; i < 1; i++) {
                            set.add(rs.getString(1));
                        }
                    }
                    ArrayList<String>ui=new ArrayList<>(set);
                    for(String item:ui){
                        sltcombo.addItem(item);
                    }
                    String input= String.valueOf(sltcombo.getSelectedItem());
                    Statement st1 = c.createStatement();
                    ResultSet rs1 = st1.executeQuery("select*from library where "+nme+" = '"+input+"'");
                    String[] column = {"Bookid", "BookName", "AuthourName", "Publisher", "Edition", "Available", "year"};
                    String[][] data = new String[30][30];
                    int a = 0;
                    while (rs.next()) {
                        for (int i = 0; i < column.length; i++) {
                            data[a][i] = String.valueOf(rs1.getString(column[i]));
                        }
                        a++;
                    }
                    bbtable.setModel(new DefaultTableModel(data, column));
                }catch (Exception a){
                    a.printStackTrace();
                }
            }
        });
        sltcombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c=null;
                try{
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    String nme=String.valueOf(srccombo.getSelectedItem());
                    String input= String.valueOf(sltcombo.getSelectedItem());
                    Statement st1 = c.createStatement();
                    ResultSet rs1 = st1.executeQuery("select*from library where "+nme+" = '"+input+"'");
                    String[] column = {"Bookid", "BookName", "AuthourName", "Publisher", "Edition", "Available", "year"};
                    String[][] data = new String[30][30];
                    int a = 0;
                    while (rs1.next()) {
                        for (int i = 0; i < column.length; i++) {
                            data[a][i] = String.valueOf(rs1.getString(column[i]));
                        }
                        a++;
                    }
                    bbtable.setModel(new DefaultTableModel(data, column));
                }catch (Exception a){
                    a.printStackTrace();
                }
            }
        });
        retenterbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c=null;
                try{
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st=c.createStatement();
                    String usname=rustext.getText();
                    int boid=Integer.parseInt(rbidtext.getText());
                    ResultSet rs=st.executeQuery("select*from lbbookings where borrowid='"+usname+"' and bookid="+boid);
                    String[] column = {"username", "bookid", "bookname", "borrowdate", "returndate"};
                    String[][] data = new String[30][30];
                        if(rs.next()){
                        for(int i=0;i<column.length;i++){
                            data[i][1]=rs.getString(column[2]);
                            data[i][3]=rs.getString(column[3]);
                            data[i][4]=rs.getString(column[4]);
                            rbntext.setText(data[i][1]);
                            rbdtext.setText(data[i][3]);
                            rrdtext.setText(data[i][4]);
                        }
                    }
                        else{
                            JOptionPane.showMessageDialog(usl,"Borrow Id or bookid ..missmatch or not found");
                        }
                }catch (Exception a){
                    a.printStackTrace();
                }
            }
        });
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    String rdate=tdydte.getText();
                    String sdate=rrdtext.getText();
                    Date dateBefore = sdf.parse(sdate);
                    Date dateAfter = sdf.parse(rdate);
                    if(dateBefore.compareTo(dateAfter)<0) {
                        long timeDiff = Math.abs(dateAfter.getTime() - dateBefore.getTime());
                        long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
                        long fine = daysDiff * 20;
                        finetext.setText(String.valueOf(fine));
                    }else
                        finetext.setText("0");
                }catch(Exception a){
                    a.printStackTrace();
                }
            }
        });
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c=null;
                try{
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                    Statement st=c.createStatement();
                    Statement st2=c.createStatement();
                    String usname=rustext.getText();
                    int boid=Integer.parseInt(rbidtext.getText());
                    int result = JOptionPane.showConfirmDialog(usl, "Make Sure You Want To Return", "BOOK RETURN",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        ResultSet rs = st2.executeQuery("select available from library where bookid=" + boid);
                        while (rs.next()) {

                            int avail = Integer.parseInt(rs.getString(1));
                            avail += 1;
                            st.addBatch("update library set available =" + avail+" where bookid="+boid);
                            st.executeBatch();
                            JOptionPane.showMessageDialog(usl, "Thank You For Visiting Us! Welcome Again:)");
                        }
                        ResultSet rs2=st2.executeQuery("select username from lbbookings where borrowid=" + usname);
                        while(rs2.next()) {
                            st.addBatch("update lbbookings set returned='YES' where username='" + rs2.getString(1) + "'");
                            st.executeBatch();
                        }
                    }else if(result == JOptionPane.NO_OPTION){

                    }
                }catch (Exception a){
                    a.printStackTrace();
                }
            }
        });
    }
    public static void send(String to,String sub,String mess){
        String from="mymail09bar@gmail.com";
        String pass="jzfgadvlwxtngoco";
        Properties prop=new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session=Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,pass);
            }
        });
        try{
            MimeMessage message=new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            message.setText(mess);
            Transport.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("User");
        frame.setContentPane(new User().usl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
