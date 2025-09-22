import java.util.*;

public class Board {
    private final int size;
    private final int winningPosition;
    private final List<Snake> snakes;
    private final List<Ladder> ladders;
    private final Map<Integer, Snake> snakeMap;  // For O(1) lookup
    private final Map<Integer, Ladder> ladderMap; // For O(1) lookup
    
    public Board() {
        this(100);
    }
    
    public Board(int size) {
        this.size = size;
        this.winningPosition = size;
        this.snakes = new ArrayList<>();
        this.ladders = new ArrayList<>();
        this.snakeMap = new HashMap<>();
        this.ladderMap = new HashMap<>();
        
        initializeDefaultSnakesAndLadders();
    }
    
    private void initializeDefaultSnakesAndLadders() {
        if (size == 100) {
            // Default snakes (head -> tail)
            addSnake(new Snake(99, 54));
            addSnake(new Snake(95, 67));
            addSnake(new Snake(88, 24));
            addSnake(new Snake(62, 19));
            addSnake(new Snake(64, 60));
            addSnake(new Snake(54, 34));
            addSnake(new Snake(17, 7));
            
            // Default ladders (bottom -> top)
            addLadder(new Ladder(4, 56));
            addLadder(new Ladder(12, 50));
            addLadder(new Ladder(14, 55));
            addLadder(new Ladder(22, 58));
            addLadder(new Ladder(41, 79));
            addLadder(new Ladder(54, 88));
        }
    }
    
    public void addSnake(Snake snake) {
        if (snake.getHead() > size || snake.getTail() < 1) {
            throw new IllegalArgumentException("Snake positions must be within board bounds!");
        }
        snakes.add(snake);
        snakeMap.put(snake.getHead(), snake);
    }
    

    public void addLadder(Ladder ladder) {
        if (ladder.getTop() > size || ladder.getBottom() < 1) {
            throw new IllegalArgumentException("Ladder positions must be within board bounds!");
        }
        ladders.add(ladder);
        ladderMap.put(ladder.getBottom(), ladder);
    }
    

    public int movePlayer(Player player, int diceRoll) {
        int currentPosition = player.getPosition();
        int newPosition = currentPosition + diceRoll;
        
        // Check if player overshoots the winning position
        if (newPosition > winningPosition) {
            // Rule: Must land exactly on winning position
            System.out.println(player.getName() + " rolled " + diceRoll + 
                             " but needs exactly " + (winningPosition - currentPosition) + 
                             " to win. No movement!");
            return currentPosition;
        }
        
        // Move player to new position
        player.setPosition(newPosition);
        System.out.println(player.getName() + " rolled " + diceRoll + 
                          " and moved to position " + newPosition);
        
        // Check for snakes
        if (snakeMap.containsKey(newPosition)) {
            Snake snake = snakeMap.get(newPosition);
            int slideToPosition = snake.getSlideToPosition();
            player.setPosition(slideToPosition);
            System.out.println("🐍 Oh no! " + player.getName() + 
                              " hit a snake and slid down to position " + slideToPosition);
            return slideToPosition;
        }
        
        // Check for ladders
        if (ladderMap.containsKey(newPosition)) {
            Ladder ladder = ladderMap.get(newPosition);
            int climbToPosition = ladder.getClimbToPosition();
            player.setPosition(climbToPosition);
            System.out.println("🪜 Great! " + player.getName() + 
                              " climbed a ladder to position " + climbToPosition);
            return climbToPosition;
        }
        
        return newPosition;
    }
    
    public boolean hasPlayerWon(Player player) {
        return player.getPosition() >= winningPosition;
    }
    
    public void displayBoard(List<Player> players) {
        System.out.println("\n=== Board Status ===");
        
        // Create a map of positions to players for display
        Map<Integer, List<Player>> positionMap = new HashMap<>();
        for (Player player : players) {
            int pos = player.getPosition();
            positionMap.computeIfAbsent(pos, k -> new ArrayList<>()).add(player);
        }
        
        // Display board in 10x10 grid format (for 100-cell board)
        if (size == 100) {
            displayStandardBoard(positionMap);
        } else {
            displaySimpleBoard(positionMap);
        }
        
        // Display snakes and ladders
        System.out.println("\n🐍 Snakes: " + snakes);
        System.out.println("🪜 Ladders: " + ladders);
        
        // Display current player positions
        System.out.println("\nPlayer Positions:");
        for (Player player : players) {
            System.out.println("  " + player);
        }
    }
    
    private void displayStandardBoard(Map<Integer, List<Player>> positionMap) {
        System.out.println("\nBoard Layout (100 = winning position):");
        
        // Display in snake pattern (alternating left-to-right and right-to-left)
        for (int row = 9; row >= 0; row--) {
            for (int col = 0; col < 10; col++) {
                int position;
                if (row % 2 == 1) {
                    // Odd rows: left to right
                    position = row * 10 + col + 1;
                } else {
                    // Even rows: right to left
                    position = row * 10 + (9 - col) + 1;
                }
                
                String cell = String.format("%3d", position);
                
                // Mark special positions
                if (snakeMap.containsKey(position)) {
                    cell += "🐍";
                } else if (ladderMap.containsKey(position)) {
                    cell += "🪜";
                } else {
                    cell += "  ";
                }
                
                // Add player symbols if present
                if (positionMap.containsKey(position)) {
                    List<Player> playersHere = positionMap.get(position);
                    for (Player p : playersHere) {
                        cell += p.getSymbol();
                    }
                }
                
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * Simple board display for non-standard sizes
     */
    private void displaySimpleBoard(Map<Integer, List<Player>> positionMap) {
        System.out.println("Positions 1 to " + size);
        for (int i = 1; i <= size; i++) {
            if (i % 10 == 1) System.out.println();
            
            String marker = ".";
            if (snakeMap.containsKey(i)) marker = "S";
            if (ladderMap.containsKey(i)) marker = "L";
            if (positionMap.containsKey(i)) {
                marker = "";
                for (Player p : positionMap.get(i)) {
                    marker += p.getSymbol();
                }
            }
            
            System.out.printf("%3d%s ", i, marker);
        }
        System.out.println();
    }
    
    // Getters
    public int getSize() {
        return size;
    }
    
    public int getWinningPosition() {
        return winningPosition;
    }
    
    public List<Snake> getSnakes() {
        return new ArrayList<>(snakes);
    }
    
    public List<Ladder> getLadders() {
        return new ArrayList<>(ladders);
    }
}