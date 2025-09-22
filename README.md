# Snake and Ladders Low Level Design

## Core Classes and Responsibilities
![Class Diagram](https://github.com/BarryByte/snakes-and-ladders-LLD-case-study/blob/main/snake-and-ladder-UML-diagram.png)

### 1. Player Class
**Attributes:**
- `String name` - Player's display name
- `int position` - Current position on board (0 = start, 1-100 = board positions)
- `char symbol` - Player's display symbol for board visualization
- `boolean isWinner` - Flag indicating if player has won

**Methods:**
- `Player(String name, char symbol)` - Constructor
- `String getName()` - Returns player's name
- `int getPosition()` - Returns current position
- `void setPosition(int position)` - Sets player position
- `char getSymbol()` - Returns player's symbol
- `boolean isWinner()` - Returns win status
- `void setWinner(boolean winner)` - Sets win status
- `void move(int steps)` - Moves player by given steps
- `String toString()` - String representation of player state

**Responsibility:** Stores and manages individual player state and information

---

### 2. Dice Class
**Attributes:**
- `Random random` - Random number generator for dice rolls
- `int sides` - Number of sides on the dice (default 6)

**Methods:**
- `Dice()` - Default constructor (6-sided dice)
- `Dice(int sides)` - Constructor with custom sides
- `int roll()` - Rolls dice once and returns result (1 to sides)
- `int rollMultiple(int numberOfDice)` - Rolls multiple dice and returns sum
- `int getSides()` - Returns number of sides
- `void setSeed(long seed)` - Sets random seed for testing

**Responsibility:** 
- Encapsulates dice rolling mechanics
- Provides randomness for game progression
- Supports different dice configurations

---

### 3. Snake Class (Immutable)
**Attributes:**
- `final int head` - Snake head position (higher number)
- `final int tail` - Snake tail position (lower number)

**Methods:**
- `Snake(int head, int tail)` - Constructor with validation
- `int getHead()` - Returns head position
- `int getTail()` - Returns tail position
- `boolean affectsPosition(int position)` - Checks if snake affects given position
- `int getSlideToPosition()` - Returns position player slides to
- `String toString()` - String representation
- `boolean equals(Object obj)` - Equality comparison
- `int hashCode()` - Hash code for collections

**Responsibility:** 
- Immutable representation of a snake
- Validates snake positions (head > tail)
- Provides snake-specific behavior

---

### 4. Ladder Class (Immutable)
**Attributes:**
- `final int bottom` - Ladder bottom position (lower number)
- `final int top` - Ladder top position (higher number)

**Methods:**
- `Ladder(int bottom, int top)` - Constructor with validation
- `int getBottom()` - Returns bottom position
- `int getTop()` - Returns top position
- `boolean affectsPosition(int position)` - Checks if ladder affects given position
- `int getClimbToPosition()` - Returns position player climbs to
- `String toString()` - String representation
- `boolean equals(Object obj)` - Equality comparison
- `int hashCode()` - Hash code for collections

**Responsibility:** 
- Immutable representation of a ladder
- Validates ladder positions (bottom < top)
- Provides ladder-specific behavior

---

### 5. Board Class
**Attributes:**
- `final int size` - Board size (typically 100)
- `final int winningPosition` - Position needed to win (equals size)
- `List<Snake> snakes` - List of all snakes on board
- `List<Ladder> ladders` - List of all ladders on board
- `Map<Integer, Snake> snakeMap` - HashMap for O(1) snake lookup by position
- `Map<Integer, Ladder> ladderMap` - HashMap for O(1) ladder lookup by position

**Methods:**
- `Board()` - Default constructor (100-cell board with standard snakes/ladders)
- `Board(int size)` - Constructor with custom size
- `void addSnake(Snake snake)` - Adds snake to board with validation
- `void addLadder(Ladder ladder)` - Adds ladder to board with validation
- `int movePlayer(Player player, int diceRoll)` - Handles complete player movement
- `boolean hasPlayerWon(Player player)` - Checks win condition
- `void displayBoard(List<Player> players)` - Visual board representation
- `int getSize()` - Returns board size
- `int getWinningPosition()` - Returns winning position
- `List<Snake> getSnakes()` - Returns copy of snakes list
- `List<Ladder> getLadders()` - Returns copy of ladders list

**Responsibility:** 
- Manages board state and special elements
- Handles movement rules (exact landing, overshooting)
- Applies snake and ladder effects
- Provides board visualization
- Validates win conditions

---

### 6. Game Class
**Attributes:**
- `List<Player> players` - List of all players in game
- `Board board` - Game board instance
- `Dice dice` - Dice instance for rolling
- `int currentPlayerIndex` - Index of current player's turn
- `boolean gameEnded` - Flag indicating if game has ended
- `Player winner` - Reference to winning player (null if no winner yet)

**Methods:**
- `Game(List<Player> players, Board board, Dice dice)` - Constructor
- `void startGame()` - Interactive game loop with manual dice rolling
- `void playQuickGame()` - Automated game loop for quick play
- `Player getCurrentPlayer()` - Returns current player
- `void switchToNextPlayer()` - Moves to next player in rotation
- `void takeTurn(Player player)` - Processes one complete turn
- `boolean checkWinCondition()` - Checks if any player has won
- `void endGame()` - Handles game ending
- `void displayFinalResults()` - Shows final standings and statistics
- `List<Player> getPlayers()` - Returns copy of players list
- `Player getWinner()` - Returns winner (for testing)
- `boolean isGameEnded()` - Returns game status

**Responsibility:**
- Orchestrates entire game flow
- Manages turn rotation among multiple players
- Coordinates between Player, Board, and Dice
- Handles different game modes (interactive vs automatic)
- Provides game statistics and results

---

## Key Design Decisions Made

### 1. **Immutable Special Elements**
- **Snake and Ladder are immutable** - Once created, cannot be modified
- **Benefits**: Thread-safe, predictable, can be safely shared
- **Validation in constructor** - Ensures valid positions at creation time

### 2. **HashMap for Performance**
- **O(1) lookup for snakes/ladders** instead of O(n) list scanning
- **Separate maps for snakes and ladders** for clear separation
- **Trade memory for speed** - acceptable for board games

### 3. **Flexible Game Modes**
- **startGame()** for interactive play with user input
- **playQuickGame()** for automated simulation
- **Same core logic, different input handling**

### 4. **Circular Player Management**
- **Modulo arithmetic** for turn rotation: `(index + 1) % playerCount`
- **Scales to any number of players** without code changes
- **Clean, mathematical approach** to multi-player games

### 5. **Exact Landing Rule**
- **Must land exactly on winning position** - realistic game rule
- **Overshooting handled gracefully** - no movement, turn continues
- **Prevents infinite games** - player must roll exact number

### 6. **Visual Board Representation**
- **10x10 grid display** for standard 100-cell board
- **Snake pattern layout** - alternating left-right, right-left rows
- **Player position indicators** - shows all players on board
- **Special element markers** - visual snakes (🐍) and ladders (🪜)

## Class Relationships

```
Game HAS-A List<Player> (composition) - 2-8 players
Game HAS-A Board (composition) - 1 board instance  
Game HAS-A Dice (composition) - 1 dice instance
Board HAS-A List<Snake> (composition) - multiple snakes
Board HAS-A List<Ladder> (composition) - multiple ladders
Board HAS-A Map<Integer, Snake> (composition) - position lookup
Board HAS-A Map<Integer, Ladder> (composition) - position lookup

Game USES Board methods for movement and win checking
Game USES Dice methods for random number generation
Game USES Player methods for state management
Board USES Snake and Ladder methods for special effects
```

## Sample Game Flow

1. **Initialization:** 
   ```java
   List<Player> players = createPlayers();
   Board board = new Board(); // or new Board(customSize)
   Dice dice = new Dice(); // or new Dice(customSides)
   Game game = new Game(players, board, dice);
   ```

2. **Game Loop:** `game.startGame()` or `game.playQuickGame()`

3. **Turn Processing:**
   - Get current player: `getCurrentPlayer()`
   - Roll dice: `dice.roll()`
   - Move player: `board.movePlayer(player, diceRoll)`
     - Calculate new position
     - Check for overshooting (exact landing rule)
     - Apply snake effect if hit snake head
     - Apply ladder effect if hit ladder bottom
   - Check win: `board.hasPlayerWon(player)`
   - Switch turns: `switchToNextPlayer()`

4. **Game End:** Display winner and final statistics

## Advanced Features

### 1. **Multiple Game Modes**
- **Interactive Mode**: Manual dice rolling, real-time display
- **Quick Mode**: Automated play with brief pauses
- **Custom Mode**: User-defined board size, snakes, ladders, dice

### 2. **Extensible Architecture**
- **Easy to add new board elements** (implement similar to Snake/Ladder)
- **Simple to modify rules** (change in Board class methods)
- **Pluggable dice types** (different probability distributions)

### 3. **Error Handling & Validation**
- **Input validation** for all user inputs
- **Boundary checking** for positions
- **Graceful error recovery** with user-friendly messages
- **Prevents infinite games** with maximum turn limits

### 4. **Performance Optimizations**
- **HashMap lookups** for O(1) snake/ladder checks
- **Immutable objects** for safe sharing and caching
- **Efficient turn management** with modulo arithmetic

