import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

public class BusTic {
    private JPanel tic;
    private JTabbedPane tabbedPane1;
    private JTabbedPane tabbedPane3;
    private JTabbedPane tabbedPane4;
    private JTextField datetext;
    private JComboBox sourcecomb;
    private JComboBox<String> descombo;
    private JButton searchbus;
    private JTable table1;
    private JTextField selebustext;
    private JTextField seattext;
    private JButton selectBusButton;
    private JTextField tftext;
    private JTextField fromtext;
    private JTextField passnametext;
    private JTextField emailtext;
    private JButton verifyEmailButton;
    private JTextField OTPtext;
    private JButton bookBusButton;
    private JTextField timetext;
    private JTextField totext;
    private JTextField faretext;
    private JTextField sdatetext;
    private JButton enterButton;
    private JTextField ticcttext;
    private JTextField smstext;
    private JTextArea heyHelloWelcomeToTextArea;
    private JTextArea textArea1;
    private JButton showTicketButton;
    private JButton sentMailButton;
    private JTextField adtext;
    private JButton loginButton;
    private JPasswordField bassfield;
    private JTextField avaiseattext;
    private JTextField capatext;
    private JButton cancelTicketButton;
    private DefaultTableModel model1;

    public BusTic() {
    sourcecomb.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection c = null;
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master","postgres","Kala@Barath123");
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("select* from "+sourcecomb.getSelectedItem()+" order by desname asc");
                String[]des=new String[50];
                descombo.removeAllItems();
                while (rs.next()) {
                    for (int i = 0; i < 1; i++) {
                        des[i] = String.valueOf(rs.getString(1));
                        descombo.addItem(des[i]);
                    }
                }
            } catch (Exception a) {
                a.printStackTrace();
            }
        }
    });
    searchbus.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection c=null;
            try{
                Class.forName("org.postgresql.Driver");
                c=DriverManager.getConnection("jdbc:postgresql://localhost:5432/master","postgres","Kala@Barath123");
                Statement st=c.createStatement();
                ResultSet rs=st.executeQuery("select*from businfo where source= '"+sourcecomb.getSelectedItem()+
                        "' and destination= '"+descombo.getSelectedItem()+"' and date='"+datetext.getText()+"'");
                String[]column={"busid","driverid","source","destination","date","time","fare","capacity","available"};
                String[][]row=new String[50][50];
                int a=0;
                while(rs.next()){
                    for(int i=0;i<column.length;i++){
                        row[a][i]=String.valueOf(rs.getString(column[i]));
                    }a++;
                }table1.setModel(new DefaultTableModel(row,column));
            }catch (Exception a){
                a.printStackTrace();
            }
        }
    });
        selectBusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row=table1.getSelectedRow();
                String busid=table1.getValueAt(row,0).toString();
                selebustext.setText(busid);
                String from=table1.getValueAt(row,2).toString();
                fromtext.setText(from);
                String to=table1.getValueAt(row,3).toString();
                totext.setText(to);
                String time=table1.getValueAt(row,5).toString();
                timetext.setText(time);
                String fare=table1.getValueAt(row,6).toString();
                faretext.setText(fare);
                String sdate=table1.getValueAt(row,4).toString();
                sdatetext.setText(sdate);
                String capacity=table1.getValueAt(row,7).toString();
                capatext.setText(capacity);
                String available=table1.getValueAt(row,8).toString();
                avaiseattext.setText(available);


            }
        });

        verifyEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String to=emailtext.getText();
                String sub="Bus ticket booking verification OTP from Madurai Travels :)";

                Random random=new Random();
                String num="0987654321";
                char[]otp=new char[6];
                for(int i=0;i<otp.length;i++){
                    otp[i]=num.charAt(random.nextInt(num.length()));
                }
                String mes="Your OTP is for Verify Your mail id that '"+String.valueOf(otp)+"'";
                BusTic.send(to,sub,mes);
                JOptionPane.showMessageDialog(tic,"OTP sent Successfully");

                bookBusButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (OTPtext.getText().equals(String.valueOf(otp))) {
                            Connection c = null;
                            int totalfare=Integer.parseInt(tftext.getText());
                            try {
                                Class.forName("org.postgresql.Driver");
                                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kalvi123");
                                Statement st = c.createStatement();
                                st.addBatch("insert into passinfo values(nextval('Ticket'),'" + passnametext.getText() + "','" + emailtext.getText() +
                                        "','" + fromtext.getText() + "','" + totext.getText() + "','" + datetext.getText() + "','" + timetext.getText()
                                        +"',"+ faretext.getText()+","+seattext.getText()+ ","+tftext.getText()+","+selebustext.getText()+")");
                                st.executeBatch();
                                JOptionPane.showMessageDialog(tic, "Ticket Booked successfully");
                            } catch (Exception a) {
                                a.printStackTrace();
                            }
                            Connection con = null;
                            try {
                                Class.forName("org.postgresql.Driver");
                                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master","postgres","Kala@Barath123");
                                Statement stat = con.createStatement();
                                ResultSet rst = stat.executeQuery("select ticket from passinfo where mail='"+emailtext.getText()+"'");
                                String sub="Your booking is confirmed ";
                                String mesage="";
                                while (rst.next()) {
                                    mesage="Your Booked Ticket no is'"+rst.getString(1)+"' Enter in the booked Ticket to view " +
                                            "Full Detailed Ticket Thank you for Choosing us!  Ride Safe :)";
                                }int cap=Integer.parseInt(capatext.getText());
                                int seat=Integer.parseInt(seattext.getText());
                                int availseat=cap-seat;
                                int bookseat=cap-availseat;
                                int bookedseat=bookseat+seat;
                                ResultSet resl=stat.executeQuery("select collection,cancelations from bookings");
                                int collecti=0;
                                while(resl.next()) {
                                    collecti = Integer.parseInt(rst.getString(1));
                                    int collect = collecti + totalfare;
                                    stat.addBatch("update businfo set available=" + availseat + " where busid=" + selebustext.getText());
                                    stat.addBatch("insert into bookings values (" + selebustext.getText() + ",'" + datetext.getText() + "'," + bookedseat + "," +
                                            collect + "," + avaiseattext.getText() + "," +resl.getString(2));
                                    stat.executeBatch();
                                    BusTic.send(to, sub, mesage);
                                    JOptionPane.showMessageDialog(tic, mesage);
                                }
                            } catch (Exception a) {
                                a.printStackTrace();
                            }
                        }else{
                            JOptionPane.showMessageDialog(tic,"OTP is incorrect");
                        }
                    }
                });
            }
        });

        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seat=Integer.parseInt(seattext.getText());
                int fee=Integer.parseInt(faretext.getText());
                int total_fee=seat*fee;
                tftext.setText(String.valueOf(total_fee));
            }
        });
        showTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master","postgres","Kala@Barath123");
                    Statement stat = con.createStatement();
                    ResultSet rs=stat.executeQuery("select*from passinfo where ticket="+ticcttext.getText()+" and mail='"+smstext.getText()+"'");
                    if (rs.next()){
                        textArea1.setText("*****************************************************\n"+
                                "**************  Madurai -----*----- Travels  ***********\n"+
                                "*****************************************************\n" +
                                "** Ticket no\t*********************"+rs.getString(1)+"\t*****\n"+
                                "** BusId\t*********************"+rs.getString(11)+"\t*****\n"+
                                "** Name\t*********************"+rs.getString(2)+"\t*****\n" +
                                "** Source\t*********************"+rs.getString(4)+"\t*****\n" +
                                "** Destination\t*********************"+rs.getString(5)+"\t*****\n"+
                                "** Date\t*********************"+rs.getString(6)+"\t*****\n"+
                                "** Time\t*********************"+rs.getString(7)+"\t*****\n"+
                                "** Seat count\t*********************"+rs.getString(9)+"\t*****\n"+
                                "** Fare\t*********************"+rs.getString(8)+"\t*****\n"+
                                "** Total Fare\t*********************"+rs.getString(10)+"\t*****\n"+
                                "*****************************************************");
                    }
                    else{
                        JOptionPane.showMessageDialog(tic,"Enter valid mail or ticket no");
                    }
                }catch (Exception a){
                    a.printStackTrace();
                }
            }
        });
        sentMailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mesge= textArea1.getText();
                String sbj="Your Travel Booked Ticket from Madurai Travels";
                BusTic.send(smstext.getText(),sbj,mesge);
                JOptionPane.showMessageDialog(tic,"Ticket Sent to your Email Address");
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection conn = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master","postgres","Kala@Barath123");
                    Statement statement= conn.createStatement();
                    String admin=adtext.getText();
                    String bss=String.valueOf(bassfield.getPassword());
                    ResultSet resultSet=statement.executeQuery("select admin,password from tadmin where admin='"+admin+"' and password='"+
                            bss+"'");
                    if(resultSet.next()){
                        JOptionPane.showMessageDialog(tic,"Login successful");
                        JFrame frame = new JFrame("admin");
                        frame.setContentPane(new admin().amn);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.pack();
                        frame.setVisible(true);
                    }else{
                        JOptionPane.showMessageDialog(tic,"Admin name or password is incorrect");
                    }
                }catch (Exception a){
                    a.printStackTrace();
                }
            }
        });
        cancelTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection conn = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "kalvi123");
                    Statement statement = conn.createStatement();
                    Statement statement2 = conn.createStatement();
                    ResultSet rs1=statement.executeQuery("select seat,busid from passinfo where ticket= "+ticcttext.getText()+" and mail='"+smstext.getText()+"'");
                    int seat,availseat=0,cancelation=0;
                    while(rs1.next()) {
                        seat = Integer.parseInt(rs1.getString(1));
                        ResultSet rs2 = statement2.executeQuery("select available from businfo where busid=" + rs1.getString(2));
                        while (rs2.next())
                            availseat = Integer.parseInt(rs2.getString(1));

                        availseat  += seat;
                        cancelation=seat;
                        Statement statement1 = conn.createStatement();
                        statement1.addBatch("delete from passinfo where ticket=" + ticcttext.getText() + " and mail='" + smstext.getText() + "'");
                        statement1.addBatch("update businfo set available="+availseat +" where busid="+rs1.getString(2));
                        statement1.addBatch("update bookings set cancelation="+cancelation+" where busid="+rs1.getString(2));
                        statement1.addBatch("insert into businfo");
                        statement1.executeBatch();
                        String msg="Your Ticket canceled Thank you";
                        String sub="Madurai bus cancellation ";
                        JOptionPane.showMessageDialog(tic, msg);
                        BusTic.send(smstext.getText(), sub,msg);

                    }
                } catch (SQLException | ClassNotFoundException a) {
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
        JFrame frame = new JFrame("BusTic");
        frame.setContentPane(new BusTic().tic);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
