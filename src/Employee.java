import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTextField txtempnumber;
    private JTextField txtfirstname;
    private JTextField txtlastname;
    private JTextField txtsalary;
    private JTextField txtemailaddress;
    private JTextField txtphonenum;
    private JButton btnSave;
    private JTable table1;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTextField txtSearch;
    private JButton btnSearch;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    Connection con;
    PreparedStatement pst;

    public void connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/employeedb", "root","admin");
            System.out.println("Successs");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }


    public Employee() {
        connect();
        table_load();
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empnum, fname, lname, salary, emailadd, phonenum;

                empnum = txtempnumber.getText();
                fname = txtfirstname.getText();
                lname = txtlastname.getText();
                salary = txtsalary.getText();
                emailadd = txtemailaddress.getText();
                phonenum = txtphonenum.getText();

                try {
                        pst = con.prepareStatement("insert into accounts(empID,empfname,emplname,Salary,empEmail,empPhone)values(?,?,?,?,?,?)");
                    pst.setString(1, empnum);
                    pst.setString(2, fname);
                    pst.setString(3, lname);
                    pst.setString(4, salary);
                    pst.setString(5, emailadd);
                    pst.setString(6, phonenum);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added");
                    table_load();

                    txtempnumber.setText("");
                    txtfirstname.setText("");
                    txtlastname.setText("");
                    txtsalary.setText("");
                    txtemailaddress.setText("");
                    txtphonenum.setText("");
                    txtempnumber.requestFocus();
                }

                catch (SQLException e1)
                {

                    e1.printStackTrace();
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String pid = txtSearch.getText();
                    pst = con.prepareStatement("SELECT empID,empfname,emplname,Salary,empEmail,empPhone FROM accounts WHERE empNumber = ?");
                    pst.setString(1, pid);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next() == true) {
                        String empID = rs.getString(1);
                        String fname = rs.getString(2);
                        String lname= rs.getString(3);
                        String salary= rs.getString(4);
                        String emailadd= rs.getString(5);
                        String phonenum= rs.getString(6);

                        txtempnumber.setText(empID);
                        txtfirstname.setText(fname);
                        txtlastname.setText(lname);
                        txtsalary.setText(salary);
                        txtemailaddress.setText(emailadd);
                        txtphonenum.setText(phonenum);

                    } else {
                        txtempnumber.setText("");
                        txtfirstname.setText("");
                        txtlastname.setText("");
                        txtsalary.setText("");
                        txtemailaddress.setText("");
                        txtphonenum.setText("");
                        JOptionPane.showMessageDialog(null, "Invalid Employee ID");
                        table_load();
                    }
                }catch (SQLException ex){
                    ex.printStackTrace();
                }

            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empID, empnum, fname, lname, salary, emailadd, phonenum;
                empnum = txtempnumber.getText();
                fname = txtfirstname.getText();
                lname = txtlastname.getText();
                salary = txtsalary.getText();
                emailadd = txtemailaddress.getText();
                phonenum = txtphonenum.getText();
                empID = txtSearch.getText();

                try {
                    pst = con.prepareStatement("UPDATE accounts SET empID=?,empfname=?,emplname=?,Salary=?,empEmail=?,empPhone=? WHERE empNumber = ?");
                    pst.setString(1, empnum);
                    pst.setString(2, fname);
                    pst.setString(3, lname);
                    pst.setString(4, salary);
                    pst.setString(5, emailadd);
                    pst.setString(6, phonenum);
                    pst.setString(7, empID);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Record Update!");
                    table_load();
                    txtempnumber.setText("");
                    txtfirstname.setText("");
                    txtlastname.setText("");
                    txtsalary.setText("");
                    txtemailaddress.setText("");
                    txtphonenum.setText("");
                    txtempnumber.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empid;
                empid = txtSearch.getText();

                try {
                    pst = con.prepareStatement("DELETE FROM accounts  where empNumber = ?");

                    pst.setString(1, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Delete!");
                    table_load();
                    txtempnumber.setText("");
                    txtfirstname.setText("");
                    txtlastname.setText("");
                    txtsalary.setText("");
                    txtemailaddress.setText("");
                    txtphonenum.setText("");
                    txtempnumber.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }
    void table_load(){
            try {
                    pst = con.prepareStatement("SELECT * FROM accounts");
                ResultSet rs = pst.executeQuery();
                table1.setModel(DbUtils.resultSetToTableModel(rs));
            }catch (SQLException e){
                    e.printStackTrace();
            }
    }
}
