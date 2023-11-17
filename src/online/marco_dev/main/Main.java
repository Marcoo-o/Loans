package online.marco_dev.main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Marco Fernandez Torres
 * @version 1.0
 *
 * This Project is to calculate your own loan by its
 * hours and to display it with a graphic.
 *
 */

public class Main {

    static Config cfg = new Config();

    /** The currency in which everything is calculated */
    static String currency = cfg.getProperty("Currency");

    /** The hours which you have worked */
    static double hours;

    /** The loan per hour */
    static double loan;

    /** Setter - The default Method, it creates a Panel to calculate your loan which add it to your MySQL account */
    public static void main(String[] args) throws IOException {
        if(cfg.getProperty("Database-Hostname") == null) {
            Properties prop = new Properties();
            prop.setProperty("Database-Hostname", "localhost");
            prop.setProperty("Database-Name", "loan");
            prop.setProperty("Database-User", "YourUser");
            prop.setProperty("Database-Password", "YourPassword");
            prop.setProperty("Currency", "€ (Euro)");
            prop.store(new FileOutputStream("./config/config.properties"), null);
        }

        JFrame frame = new JFrame("Loan-Calculation");
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MySQL.connect();

        JPanel panel = new JPanel();
        JLabel label;
        if (MySQL.con != null) {
            label = new JLabel("Loan per hour:");
            JTextField textField = new JTextField(5);
            textField.addActionListener(e -> {
                String text = textField.getText();
                textField.setText(text);
            });
            JLabel label1 = new JLabel("Hours you've worked:");
            JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 12, 8);
            JLabel label2 = new JLabel(String.valueOf(slider.getValue()));
            slider.addChangeListener(e -> label2.setText(String.valueOf(slider.getValue())));

            JButton button = new JButton("OK");

            panel.add(label);
            panel.add(textField);
            panel.add(label1);
            panel.add(slider);
            panel.add(label2);
            panel.add(button);
            button.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    add(Double.parseDouble(textField.getText()), slider.getValue());
                }
            });
        } else {
            frame.setSize(600, 200);
            label = new JLabel("Unfortunately there is no MySQL Connection, check your credentials.");
            JButton button = new JButton("OK");
            button.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });

            panel.add(label);
            panel.add(button);
        }
        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     *
     * Add the gained loan to the database
     *
     * @param newhours - The hours you have worked
     * @param newloan  - The loan per hour you have got
     *
     */
    public static void add(double newloan, double newhours) {
        loan = newloan;
        hours = newhours;

        MySQL.add(loan, hours);
        System.out.println("A total of " + (loan * hours) + currency + " have been credited to your account.");
    }
}