public class Player {
    private String name;
    private int position;
    private char symbol;
    private boolean isWinner;
    
    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
        this.position = 0; // Start before the board (position 1 is first cell)
        this.isWinner = false;
    }
    
    public String getName() {
        return name;
    }
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    public char getSymbol() {
        return symbol;
    }
    
    public boolean isWinner() {
        return isWinner;
    }
    
    public void setWinner(boolean winner) {
        isWinner = winner;
    }
    
    public void move(int steps) {
        this.position += steps;
    }
    
    @Override
    public String toString() {
        return name + " (" + symbol + ") at position " + position;
    }
}