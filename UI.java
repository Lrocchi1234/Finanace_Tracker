package Finance_Tracker_App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.table.DefaultTableModel;

public class UI extends JFrame implements ActionListener {
	private JTextField expenseField;
	private JTextField amountField;
	private JButton saveButton;
	private JButton exitButton;
	private JPanel contentArea;
	private CardLayout cardLayout;
	private JTable expensesTable;
	private DefaultTableModel tableModel;
	private JLabel totalAmountLabel; // Add a class variable for the total amount label
	private static final String FILE_PATH = "expenses.csv";

	public UI() {
		super("Finance Tracker");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 620);

		// Create the main layout
		BorderLayout layout = new BorderLayout();
		setLayout(layout);

		// Create the top header
		JLabel headerLabel = new JLabel("Finance Tracker");
		headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
		headerLabel.setForeground(Color.WHITE);
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPanel.setBackground(new Color(51, 102, 153));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		headerPanel.add(headerLabel);
		add(headerPanel, BorderLayout.NORTH);

		// Create the left sidebar
		JPanel sidebar = new JPanel();
		sidebar.setBackground(new Color(244, 244, 244));
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setPreferredSize(new Dimension(200, getHeight()));
		sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JButton addExpenseButton = createSidebarButton("Add Expense");
		JButton editExpenseButton = createSidebarButton("Edit Expense");
		JButton checkExpenseButton = createSidebarButton("Check Expense");

		sidebar.add(addExpenseButton);
		sidebar.add(checkExpenseButton);
		sidebar.add(editExpenseButton);

		add(sidebar, BorderLayout.WEST);

		// Create the content area
		contentArea = new JPanel();
		cardLayout = new CardLayout();
		contentArea.setLayout(cardLayout);

		JPanel addExpensePanel = addExpensePanel();
		JPanel editExpensePanel = editExpensePanel();
		JPanel checkExpensePanel = checkExpensePanel();

		contentArea.add(addExpensePanel, "Add Expense");
		contentArea.add(editExpensePanel, "Edit Expense");
		contentArea.add(checkExpensePanel, "Check Expense");

		add(contentArea, BorderLayout.CENTER);

		// Set the initial view to the dashboard
		cardLayout.show(contentArea, "Dashboard");

		// Set the frame visible
		setVisible(true);
	}

	private JButton createSidebarButton(String label) {
		JButton button = new JButton(label);
		button.addActionListener(this);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setMaximumSize(new Dimension(180, 30));
		return button;
	}

	private JPanel addExpensePanel() {
		JPanel panel = createTabPanel("Add Expense", "Add Expense");

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
		JPanel panelContent = new JPanel(new GridLayout(3, 2));
		panelContent.setBackground(Color.WHITE);
		panelContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panelContent.add(expenseLabel);
		panelContent.add(expenseField);
		panelContent.add(amountLabel);
		panelContent.add(amountField);
		panelContent.add(saveButton);
		panelContent.add(exitButton);

		panel.add(panelContent);

		return panel;
	}

	private JPanel editExpensePanel() {
		JPanel panel = createTabPanel("Edit Expense", "Edit Expense");

		// Create the table to display statistics
		tableModel = new DefaultTableModel();
		tableModel.addColumn("Date");
		tableModel.addColumn("Expense");
		tableModel.addColumn("Amount");
		expensesTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(expensesTable);
		panel.add(scrollPane);

		// Create the "Save Changes" button
		JButton saveChangesButton = new JButton("Save Changes");
		saveChangesButton.addActionListener(this);
		panel.add(saveChangesButton);

		// Create the label to display the total amount spent
		totalAmountLabel = new JLabel("Total Amount Spent: £0.00");
		totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(totalAmountLabel, BorderLayout.SOUTH);

		// Update the total amount spent label
		updateTotalAmount();

		return panel;
	}

	private JPanel createTabPanel(String tabName, String message) {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel label = new JLabel(message);
		label.setFont(new Font("Arial", Font.BOLD, 18));
		panel.add(label);

		return panel;
	}

	private JPanel checkExpensePanel() {
	    JPanel panel = createTabPanel("Check Expense", "Check Expense");

	    // Create the panel to hold the components
	    JPanel panelContent = new JPanel(new BorderLayout());
	    panelContent.setBackground(Color.WHITE);
	    panelContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	    // Create the table to display expenses
	    JTable expensesTable = new JTable(tableModel);
	    JScrollPane scrollPane = new JScrollPane(expensesTable);
	    panelContent.add(scrollPane, BorderLayout.CENTER);

	    // Create the label for the total amount spent
	    totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
	    panelContent.add(totalAmountLabel, BorderLayout.SOUTH);

	    // Add the panel to the main panel
	    panel.add(panelContent);

	    // Update the total amount spent label
	    updateTotalAmount();

	    return panel;
	}


	public void actionPerformed(ActionEvent e) {
	    String actionCommand = e.getActionCommand();

	    if (actionCommand.equals("Add Expense")) {
	        cardLayout.show(contentArea, "Add Expense");
	    } else if (actionCommand.equals("Edit Expense")) {
	        cardLayout.show(contentArea, "Edit Expense");
	    } else if (actionCommand.equals("Check Expense")) {
	        cardLayout.show(contentArea, "Check Expense");
	    } else if (actionCommand.equals("Save")) {
	        // Get the expense and amount
	        String expense = expenseField.getText();
	        String amount = amountField.getText();

	        // Get the current date
	        String currentDate = getCurrentDate();

	        // Add the expense to the statistics table
	        Object[] rowData = {currentDate, expense, "£" + amount};
	        tableModel.addRow(rowData);

	        // Clear the fields
	        expenseField.setText("");
	        amountField.setText("");

	        // Update the total amount spent label
	        updateTotalAmount();

	        // Update the expenses table in the "Check Expense" panel
	        expensesTable.setModel(tableModel);
	    } else if (actionCommand.equals("Exit")) {
	        exit();
	    } else if (actionCommand.equals("Save Changes")) {
	        int rowCount = tableModel.getRowCount();
	        for (int i = 0; i < rowCount; i++) {
	            String date = (String) tableModel.getValueAt(i, 0);
	            String expense = (String) tableModel.getValueAt(i, 1);
	            String amount = (String) tableModel.getValueAt(i, 2);

	            // Update the expenses.csv file or perform other operations as needed
	            // Here, we will just print the updated data for demonstration purposes
	            System.out.println("Updated data - Date: " + date + ", Expense: " + expense + ", Amount: " + amount);
	        }

	        // Update the expenses table in the "Check Expense" panel
	        expensesTable.setModel(tableModel);

	        // Update the total amount spent label
	        updateTotalAmount();
	    }
	}


	private void updateTotalAmount() {
	    double totalAmount = calculateTotalAmount();
	    DecimalFormat decimalFormat = new DecimalFormat("0.00");
	    String totalAmountFormatted = decimalFormat.format(totalAmount);
	    totalAmountLabel.setText("Total Amount Spent: £" + totalAmountFormatted);
	}

	private double calculateTotalAmount() {
	    double totalAmount = 0.0;
	    int rowCount = tableModel.getRowCount();
	    for (int i = 0; i < rowCount; i++) {
	        String amountString = (String) tableModel.getValueAt(i, 2);
	        double amount = Double.parseDouble(amountString.substring(1));
	        totalAmount += amount;
	    }
	    return totalAmount;
	}

	private String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public void exit() {
		saveDataToFile();
		System.out.println("Exiting application...");
		System.exit(0);
	}

	private void saveDataToFile() {
		try (FileWriter writer = new FileWriter(FILE_PATH)) {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				String date = (String) tableModel.getValueAt(i, 0);
				String expense = (String) tableModel.getValueAt(i, 1);
				String amount = (String) tableModel.getValueAt(i, 2);
				writer.write(date + "," + expense + "," + amount + "\n");
			}
			System.out.println("Data saved to file: " + FILE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadDataFromFile() {
	    try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            String[] data = line.split(",");
	            if (data.length == 3) {
	                tableModel.addRow(data);
	            }
	        }
	        expensesTable.setModel(tableModel); // Assign the table model to the expensesTable
	        System.out.println("Data loaded from file: " + FILE_PATH);
	    } catch (FileNotFoundException e) {
	        // The file doesn't exist yet, it will be created when saving the data
	        System.out.println("No data file found. New file will be created: " + FILE_PATH);
	    }
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			UI app = new UI();
			app.loadDataFromFile();
		});
	}

}
