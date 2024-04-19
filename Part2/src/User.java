public class User {

    private int balance;
    private int betAmount;
    private int horseSelected;

    public User(int balance) {
        this.balance = balance;
        this.betAmount = 0;
        this.horseSelected = -1;
    }

    public int getBalance() {
        return balance;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public int getHorseSelected() {
        return horseSelected;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public void setHorseSelected(int horseSelected) {
        this.horseSelected = horseSelected;
    }

    public void updateBalance(int amount) {
        balance += amount;
    }

    public void placeBet(int amount, int horseSelected) {
        betAmount = amount;
        this.horseSelected = horseSelected;
    }

    public void resetBet() {
        betAmount = 0;
        horseSelected = -1;
    }

}
