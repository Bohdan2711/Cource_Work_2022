package banking;

import java.util.Random;
import java.util.Scanner;

public class Banking {

    //Connection to database
    DbHandler dbHandler = new DbHandler();

    public void start() {
        Scanner scanner = new Scanner(System.in);
        dbHandler.createTable();

        boolean exit = false;
        while (!exit) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");

            switch (scanner.nextShort()) {
                case 1 -> createAccount();
                case 2 -> loginAccount();
                case 0 -> exit = true;
            }

        }

    }

    private void createAccount() {

        Random random = new Random();

        String number;
        String pin;

        long number_;
        do {
            number_ = random.nextLong(4000_0000_0000_0000L, 4999_9999_9999_9999L);
            number = String.valueOf(number_);
        } while (!isLuna(number));

        int pin_ = random.nextInt(0, 9999);
        pin = String.format("%04d", pin_);

        Account account = new Account(number, pin);
        dbHandler.addAccount(account);

        System.out.println("Account was created");

    }

    private boolean isLuna(String num) {
        int nSum = 0;
        boolean isSecond = false;
        for (int i = 15; i >= 0; i--) {
            int d = num.charAt(i) - '0';
            if (isSecond)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    private void loginAccount() {
        Scanner scanner = new Scanner(System.in);

        String number;
        String pin;

        System.out.print("Print your card number : ");
        number = scanner.nextLine();

        System.out.print("Print your pin-code : ");
        pin = scanner.nextLine();

        if (dbHandler.tryLoginAccount(number, pin)) {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");

            switch (scanner.nextShort()) {
                case 1 -> checkBalance(number);
                case 2 -> addIncome(number);
                case 3 -> doTransfer(number);
            }

        }

    }

    private void checkBalance(String number) {
        System.out.println("Your balance: " + dbHandler.getBalance(number));
    }

    private void addIncome(String number) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter value: ");

        double value = scanner.nextDouble();
        dbHandler.addIncome(number, value);
    }

    private void doTransfer(String number1) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do transfer about number: ");
        String number2 = scanner.nextLine();

        System.out.println("How much bucks: ");
        double value = scanner.nextDouble();

        double balance = dbHandler.getBalance(number1);
        if (balance > value) {
            dbHandler.doTransfer(number1, number2, value);
            System.out.println("Successful transfer");
        } else {
            System.out.println("Insufficient funds");
        }

    }

}