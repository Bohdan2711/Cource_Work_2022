package banking;

import org.sqlite.JDBC;

import java.sql.*;
import java.util.Objects;

public class DbHandler {

    private static final String URL = "jdbc:sqlite:E:/JavaProjects/BankingSystem/src/banking/Accounts.db";
    private Connection connection = null;

    public DbHandler(){
        connect();
    }

    public void connect() {

        try {
            DriverManager.registerDriver(new JDBC());
            this.connection = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite done!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {

        String sql = "CREATE TABLE IF NOT EXISTS Accounts\n" +
                "(\n" +
                "    id          INTEGER PRIMARY KEY,\n" +
                "    card_number TEXT NOT NULL,\n" +
                "    pin         TEXT NOT NULL,\n" +
                "    balance     INTEGER DEFAULT 0\n" +
                ")";

        try {
            if (connection != null) {

                Statement statement = connection.createStatement();
                statement.execute(sql);
                System.out.println("Table was created");

            } else {
                System.out.println("Connection is null!");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAccount(Account account) {
        String query = "INSERT INTO Accounts(card_number, pin, balance) VALUES (?,?,?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, account.card_number);
            statement.setString(2, account.pin);
            statement.setDouble(3, account.balance);
            statement.executeUpdate();

            System.out.println("New card was created.\nNumber: " + account.card_number +
                    "\nBal: " + account.balance);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean tryLoginAccount(String card_number, String pin) {
        String query = "SELECT pin FROM Accounts WHERE card_number = " + card_number;

        boolean access = false;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.next()) {
                System.out.println("Account not find.");
            } else {
                if (Objects.equals(resultSet.getString("pin"), pin)) {
                    System.out.println("You're logged in.");
                    access = true;
                } else {
                    System.out.println("Wrong pin.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error!:  "+ e.getMessage());
        }

        return access;
    }

    public double getBalance(String card_number) {
        String query = "SELECT balance FROM Accounts WHERE card_number = " + card_number;

        double balance = -1;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            balance = resultSet.getDouble(1);

        } catch (SQLException e) {
            System.out.println("Error!:  "+ e.getMessage());
        }
        return balance;
    }

    public void addIncome(String number, double val) {
        String query = "UPDATE Accounts\n" +
                "SET balance = balance + (?)\n" +
                "WHERE card_number = (?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, val);
            statement.setString(2, number);
            statement.executeUpdate();

            System.out.println("Card balance was edited: " + getBalance(number));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void doTransfer(String number1, String number2, double val) {

        String query = "UPDATE Accounts\n" +
                "SET balance = balance - (?)\n" +
                "WHERE card_number = (?)";

        String query2 = "UPDATE Accounts\n" +
                "SET balance = balance + (?)\n" +
                "WHERE card_number = (?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, val);
            statement.setString(2, number1);
            statement.executeUpdate();

            System.out.println("Card balance was edited: " + getBalance(number1));

            statement = connection.prepareStatement(query2);
            statement.setDouble(1, val);
            statement.setString(2, number2);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}