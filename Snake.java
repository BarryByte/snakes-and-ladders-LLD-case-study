
public class Snake {
    private final int head;  // Where the snake's head is (higher number)
    private final int tail;  // Where the snake's tail is (lower number)
    
    public Snake(int head, int tail) {
        if (head <= tail) {
            throw new IllegalArgumentException("Snake head must be at a higher position than tail!");
        }
        this.head = head;
        this.tail = tail;
    }
    
    public int getHead() {
        return head;
    }
    
    public int getTail() {
        return tail;
    }
    
    public boolean affectsPosition(int position) {
        return position == head;
    }

    public int getSlideToPosition() {
        return tail;
    }
    
    @Override
    public String toString() {
        return "Snake(" + head + "→" + tail + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Snake snake = (Snake) obj;
        return head == snake.head && tail == snake.tail;
    }
    
    @Override
    public int hashCode() {
        return head * 31 + tail;
    }
}