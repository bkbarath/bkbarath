import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

public class signin {
    public JPanel sgn;
    private JTextField fnametext;
    private JTextField lnametext;
    private JTextField unametext;
    private JTextField mailstext;
    private JButton OTPButton;
    private JTextField passtext;
    private JTextField otptext;
    private JButton signinButton;

    public signin() {
        OTPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                String num = "0987654321";
                char[] otp = new char[6];
                String mail = mailstext.getText();
                for (int i = 0; i < otp.length; i++) {
                    otp[i] = num.charAt(random.nextInt(num.length()));
                }
                String mes = "Your OTP is for Verify Your mail id that '" + String.valueOf(otp) + "'";
                String to = mail;
                String sub = "OTP for login into Madurai Library Member";
                signin.send(to, sub, mes);
                JOptionPane.showMessageDialog(sgn,"OTP sent successfully");
                signinButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Connection c = null;
                        try {
                            Class.forName("org.postgresql.Driver");
                            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/master", "postgres", "Kala@Barath123");
                            Statement st = c.createStatement();
                            String fname = fnametext.getText();
                            String lname = lnametext.getText();
                            String uname = unametext.getText();
                            String pasword = passtext.getText();
                            String otps = otptext.getText();
                            if (otps.equals(String.valueOf(otp))) {
                                st.addBatch("insert into lbuser (firstname,lastname,username,password,mail)values('" + fname +
                                        "','" + lname + "','" + uname + "','" + pasword + "','" + mail+"')");
                                st.executeBatch();
                                JOptionPane.showMessageDialog(sgn, "Sign in Successfully");
                            } else {
                                JOptionPane.showMessageDialog(sgn, "OTP is incorrect");
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
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
        JFrame frame = new JFrame("signin");
        frame.setContentPane(new signin().sgn);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
