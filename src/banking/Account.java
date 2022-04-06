package banking;

public class Account {

    //number of card
    protected String card_number;
    //pin-code
    protected String pin;
    //balance in USD
    protected double balance;

    public Account(String card_number, String pin) {
        this.card_number = card_number;
        this.pin = pin;
    }

}
