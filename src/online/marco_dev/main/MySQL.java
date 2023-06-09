package online.marco_dev.main;

import java.sql.*;
import java.util.Calendar;

/**
 *
 * This class connects the Project
 * to your MySQL-Database
 *
 */

public class MySQL {

    /** The credentials to connect to your MySQL-Database */
    static String hostname = "localhost",
            database = "loan",
            username = "root",
            password = "";

    /** The connection which is going to establish (or not) */
    static Connection con = null;

    /** The current balance which is credited */
    public static double balance;


    /** Setter - Connect the Application with your database*/
    public static void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + database + "?user=" + username + "&password=" + password);
            System.out.println("The connection was successfully established.");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    /** Setter - Adds the product of loan multiplied by the hours you have worked
     * @param loan - The loan you get per hour
     * @param hours - The hours you have worked and would like to credit */
    public static void add(double loan, double hours) {
        String sql = "INSERT INTO `loan`(`Date`, `Loan`, `Hours`, `Balance`) VALUES (?,?,?,?)";
        String sql2 = "SELECT `Balance` FROM `Loan` ORDER BY id DESC LIMIT 1 ";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            PreparedStatement ps2 = con.prepareStatement(sql2);

            Date date = new Date(Calendar.getInstance().getTime().getTime());
            ps.setDate(1, date);
            ps.setDouble(2, loan);
            ps.setDouble(3, hours);

            ResultSet rs = ps2.executeQuery();
            while(rs.next()) {
                balance = rs.getDouble("Balance");
            }

            ps.setDouble(4, (loan*hours) + balance);
            ps.executeUpdate();
            ps2.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static double getBalance() {
        return balance;
    }

}