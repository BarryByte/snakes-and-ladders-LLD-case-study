import java.util.*;

public class Game {
    private final List<Player> players;
    private final Board board;
    private final Dice dice;
    private int currentPlayerIndex;
    private boolean gameEnded;
    private Player winner;
    
    public Game(List<Player> players, Board board, Dice dice) {
        if (players.size() < 2) {
            throw new IllegalArgumentException("Need at least 2 players to play!");
        }
        
        this.players = new ArrayList<>(players);
        this.board = board;
        this.dice = dice;
        this.currentPlayerIndex = 0;
        this.gameEnded = false;
        this.winner = null;
        
        assignSymbols();
    }
    
    private void assignSymbols() {
        char[] symbols = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        
        for (int i = 0; i < players.size() && i < symbols.length; i++) {
            // Create new player with assigned symbol
            Player oldPlayer = players.get(i);
            Player newPlayer = new Player(oldPlayer.getName(), symbols[i]);
            newPlayer.setPosition(oldPlayer.getPosition());
            players.set(i, newPlayer);
        }
    }
    
 
    public void startGame() {
        System.out.println("🎲 Welcome to Snake and Ladders! 🎲");
        System.out.println("Players: ");
        for (Player player : players) {
            System.out.println("  " + player.getName() + " (" + player.getSymbol() + ")");
        }
        System.out.println("Goal: Reach position " + board.getWinningPosition() + " exactly!");
        
        board.displayBoard(players);
        
        Scanner scanner = new Scanner(System.in);
        
        while (!gameEnded) {
            Player currentPlayer = getCurrentPlayer();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println(currentPlayer.getName() + "'s turn!");
            System.out.print("Press Enter to roll dice...");
            scanner.nextLine();
            
            takeTurn(currentPlayer);
            
            if (checkWinCondition()) {
                endGame();
            } else {
                switchToNextPlayer();
            }
            
            // Small delay for better game experience
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        displayFinalResults();
    }
    
    /**
     * Handles one player's complete turn
     */
    private void takeTurn(Player player) {
        int diceRoll = dice.roll();
        System.out.println("🎲 " + player.getName() + " rolled: " + diceRoll);
        
        int initialPosition = player.getPosition();
        int finalPosition = board.movePlayer(player, diceRoll);
        
        // Display movement summary
        if (finalPosition != initialPosition + diceRoll) {
            if (finalPosition < initialPosition + diceRoll) {
                System.out.println("Snake bite! Moved from " + (initialPosition + diceRoll) + 
                                 " to " + finalPosition);
            } else {
                System.out.println("Ladder boost! Climbed from " + (initialPosition + diceRoll) + 
                                 " to " + finalPosition);
            }
        }
        
        board.displayBoard(players);
    }
    
    /**
     * Gets current player
     */
    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Switches to next player
     */
    private void switchToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    
    /**
     * Checks if any player has won
     */
    private boolean checkWinCondition() {
        for (Player player : players) {
            if (board.hasPlayerWon(player)) {
                winner = player;
                player.setWinner(true);
                return true;
            }
        }
        return false;
    }
    
    private void endGame() {
        gameEnded = true;
        System.out.println("\n" + "%".repeat(20));
        System.out.println("$ GAME OVER! $");
        System.out.println(winner.getName() + " has won the game!");
        System.out.println("%".repeat(20));
    }
    
    /**
     * Displays final game statistics
     */
    private void displayFinalResults() {
        System.out.println("\n=== FINAL RESULTS ===");
        
        // Sort players by position (winner first, then by position)
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort((p1, p2) -> {
            if (p1.isWinner()) return -1;
            if (p2.isWinner()) return 1;
            return Integer.compare(p2.getPosition(), p1.getPosition()); // Higher position first
        });
        
        System.out.println("Final Standings:");
        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player player = sortedPlayers.get(i);
            String status = player.isWinner() ? " 🏆 WINNER!" : " (Position: " + player.getPosition() + ")";
            System.out.println((i + 1) + ". " + player.getName() + status);
        }
        
        // Game statistics
        System.out.println("\nGame Statistics:");
        System.out.println("- Total players: " + players.size());
        System.out.println("- Board size: " + board.getSize());
        System.out.println("- Snakes on board: " + board.getSnakes().size());
        System.out.println("- Ladders on board: " + board.getLadders().size());
        
        System.out.println("\nThank you for playing Snake and Ladders! 🎲");
    }
    
    /**
     * Quick game option - auto-play without manual input
     */
    public void playQuickGame() {
        System.out.println("🎲 Quick Snake and Ladders Game! 🎲");
        System.out.println("Auto-playing...");
        
        int turnCount = 0;
        final int MAX_TURNS = 1000; // Prevent infinite games
        
        while (!gameEnded && turnCount < MAX_TURNS) {
            Player currentPlayer = getCurrentPlayer();
            System.out.println("\nTurn " + (turnCount + 1) + ": " + currentPlayer.getName());
            
            takeTurn(currentPlayer);
            turnCount++;
            
            if (checkWinCondition()) {
                endGame();
            } else {
                switchToNextPlayer();
            }
            
            // Brief pause for readability
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        if (turnCount >= MAX_TURNS) {
            System.out.println("Game ended due to maximum turn limit reached.");
            // Find player closest to winning
            winner = players.stream()
                    .max(Comparator.comparingInt(Player::getPosition))
                    .orElse(players.get(0));
            winner.setWinner(true);
        }
        
        displayFinalResults();
    }
    
    // Getters for testing
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    
    public Player getWinner() {
        return winner;
    }
    
    public boolean isGameEnded() {
        return gameEnded;
    }
}