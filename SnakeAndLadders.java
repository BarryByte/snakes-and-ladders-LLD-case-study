import java.util.*;

public class SnakeAndLadders {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("🎲 Welcome to Snake and Ladders! 🎲");
        System.out.println("========================================");
        
        try {
            // Choose game mode
            GameMode mode = chooseGameMode();
            
            switch (mode) {
                case INTERACTIVE:
                    playInteractiveGame();
                    break;
                case QUICK:
                    playQuickGame();
                    break;
                case CUSTOM:
                    playCustomGame();
                    break;
            }
            
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    private enum GameMode {
        INTERACTIVE, QUICK, CUSTOM
    }
    
    /**
     * Let user choose game mode
     */
    private static GameMode chooseGameMode() {
        System.out.println("Choose game mode:");
        System.out.println("1. Interactive Game (manual dice rolling)");
        System.out.println("2. Quick Game (automatic play)");
        System.out.println("3. Custom Game (custom board setup)");
        System.out.print("Enter choice (1-3): ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1: return GameMode.INTERACTIVE;
                case 2: return GameMode.QUICK;
                case 3: return GameMode.CUSTOM;
                default:
                    System.out.println("Invalid choice, defaulting to Interactive mode.");
                    return GameMode.INTERACTIVE;
            }
        } catch (Exception e) {
            System.out.println("Invalid input, defaulting to Interactive mode.");
            scanner.nextLine(); // Clear invalid input
            return GameMode.INTERACTIVE;
        }
    }
    
    /**
     * Play standard interactive game
     */
    private static void playInteractiveGame() {
        List<Player> players = createPlayers();
        Board board = new Board(); // Standard 100-cell board
        Dice dice = new Dice(); // Standard 6-sided dice
        
        Game game = new Game(players, board, dice);
        game.startGame();
    }
    
    /**
     * Play quick auto-play game
     */
    private static void playQuickGame() {
        List<Player> players = createPlayers();
        Board board = new Board(); // Standard 100-cell board
        Dice dice = new Dice(); // Standard 6-sided dice
        
        Game game = new Game(players, board, dice);
        game.playQuickGame();
    }
    
    /**
     * Play game with custom board setup
     */
    private static void playCustomGame() {
        System.out.println("\n=== Custom Game Setup ===");
        
        // Get board size
        System.out.print("Enter board size (default 100): ");
        int boardSize = 100;
        try {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                boardSize = Integer.parseInt(input);
                if (boardSize < 10) {
                    System.out.println("Board size too small, using minimum 10");
                    boardSize = 10;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, using default size 100");
        }
        
        // Create custom board
        Board board = new Board(boardSize);
        
        // Add custom snakes and ladders if desired
        if (boardSize != 100) {
            addCustomSnakesAndLadders(board);
        }
        
        // Get dice configuration
        Dice dice = createCustomDice();
        
        // Create players
        List<Player> players = createPlayers();
        
        // Start game
        Game game = new Game(players, board, dice);
        
        System.out.print("Play interactively (y) or quick auto-play (n)? ");
        String playMode = scanner.nextLine().toLowerCase();
        
        if (playMode.startsWith("y")) {
            game.startGame();
        } else {
            game.playQuickGame();
        }
    }
    
    /**
     * Create players based on user input
     */
    private static List<Player> createPlayers() {
        List<Player> players = new ArrayList<>();
        
        System.out.print("Enter number of players (2-8): ");
        int numPlayers = 2;
        try {
            numPlayers = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (numPlayers < 2) {
                System.out.println("Minimum 2 players required, using 2 players.");
                numPlayers = 2;
            } else if (numPlayers > 8) {
                System.out.println("Maximum 8 players allowed, using 8 players.");
                numPlayers = 8;
            }
        } catch (Exception e) {
            System.out.println("Invalid input, using 2 players.");
            scanner.nextLine(); // Clear invalid input
        }
        
        // Create players
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine().trim();
            
            if (name.isEmpty()) {
                name = "Player" + i;
            }
            
            players.add(new Player(name, '0')); // Symbol will be reassigned by Game class
        }
        
        return players;
    }
    
    /**
     * Add custom snakes and ladders to the board
     */
    private static void addCustomSnakesAndLadders(Board board) {
        System.out.println("Do you want to add custom snakes and ladders? (y/n): ");
        String response = scanner.nextLine().toLowerCase();
        
        if (!response.startsWith("y")) {
            return;
        }
        
        // Add snakes
        System.out.print("Enter number of snakes to add (0-10): ");
        try {
            int numSnakes = scanner.nextInt();
            scanner.nextLine();
            
            for (int i = 0; i < numSnakes && i < 10; i++) {
                System.out.println("Snake " + (i + 1) + ":");
                System.out.print("  Head position (higher number): ");
                int head = scanner.nextInt();
                System.out.print("  Tail position (lower number): ");
                int tail = scanner.nextInt();
                scanner.nextLine();
                
                try {
                    board.addSnake(new Snake(head, tail));
                    System.out.println("  Added snake: " + head + " → " + tail);
                } catch (Exception e) {
                    System.out.println("  Error adding snake: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input for snakes.");
            scanner.nextLine();
        }
        
        // Add ladders
        System.out.print("Enter number of ladders to add (0-10): ");
        try {
            int numLadders = scanner.nextInt();
            scanner.nextLine();
            
            for (int i = 0; i < numLadders && i < 10; i++) {
                System.out.println("Ladder " + (i + 1) + ":");
                System.out.print("  Bottom position (lower number): ");
                int bottom = scanner.nextInt();
                System.out.print("  Top position (higher number): ");
                int top = scanner.nextInt();
                scanner.nextLine();
                
                try {
                    board.addLadder(new Ladder(bottom, top));
                    System.out.println("  Added ladder: " + bottom + " → " + top);
                } catch (Exception e) {
                    System.out.println("  Error adding ladder: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input for ladders.");
            scanner.nextLine();
        }
    }
    
    /**
     * Create custom dice configuration
     */
    private static Dice createCustomDice() {
        System.out.print("Enter number of sides for dice (default 6): ");
        int sides = 6;
        
        try {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                sides = Integer.parseInt(input);
                if (sides < 2) {
                    System.out.println("Dice must have at least 2 sides, using 6.");
                    sides = 6;
                } else if (sides > 20) {
                    System.out.println("Maximum 20 sides allowed, using 20.");
                    sides = 20;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, using standard 6-sided dice.");
        }
        
        return new Dice(sides);
    }
    
    /**
     * Demo method for testing - creates a quick demo game
     */
    public static void runDemo() {
        System.out.println("🎲 Running Snake and Ladders Demo 🎲");
        
        // Create demo players
        List<Player> players = Arrays.asList(
            new Player("Alice", '1'),
            new Player("Bob", '2'),
            new Player("Charlie", '3')
        );
        
        // Create standard board and dice
        Board board = new Board();
        Dice dice = new Dice();
        
        // Run quick game
        Game game = new Game(players, board, dice);
        game.playQuickGame();
    }
}