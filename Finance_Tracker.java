package Finance_Tracker_App;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

public class Finance_Tracker extends JFrame implements ActionListener {
	private JTextField expenseField;
	private JTextField amountField;
	private JButton saveButton;
	private JButton exitButton;

	public Finance_Tracker() {
		super("Finance Tracker");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);

		// Create the expense label and field
		JLabel expenseLabel = new JLabel("Expense:");
		expenseField = new JTextField(20);

		// Create the amount label and field
		JLabel amountLabel = new JLabel("Amount:");
		amountField = new JTextField(10);

		// Create the save button
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		// Create the exit button
				exitButton = new JButton("Exit");
				exitButton.addActionListener(this);

		// Create the panel to hold the components
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		panel.add(expenseLabel);
		panel.add(expenseField);
		panel.add(amountLabel);
		panel.add(amountField);
		panel.add(saveButton);
		panel.add(exitButton);

		// Add the panel to the frame and make it visible
		add(panel);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == saveButton) {
			String expense = expenseField.getText();
			double amount = Double.parseDouble(amountField.getText());

			try {
				FileWriter writer = new FileWriter("expenses.csv", true);
				writer.write(expense + ", " + "£" + amount + "\n");
				writer.close();
				JOptionPane.showMessageDialog(this, "Expense saved.");
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "An error occurred while saving the expense: " + ex.getMessage());
			}

			expenseField.setText("");
			amountField.setText("");
			
		}
		if (e.getSource() == exitButton) {
            exit();
        }
	}
	public void exit() {
        System.out.println("Exiting application...");
        System.exit(0);
    }

	public static void main(String[] args) {
		new Finance_Tracker();
	}
}
