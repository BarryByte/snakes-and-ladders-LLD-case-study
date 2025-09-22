
public class Ladder {
    private final int bottom;  // Where the ladder starts (lower number)
    private final int top;     // Where the ladder ends (higher number)
    
    public Ladder(int bottom, int top) {
        if (bottom >= top) {
            throw new IllegalArgumentException("Ladder bottom must be at a lower position than top!");
        }
        this.bottom = bottom;
        this.top = top;
    }
    
    public int getBottom() {
        return bottom;
    }
    
    public int getTop() {
        return top;
    }
    
    /**
     * Checks if this ladder affects the given position
     */
    public boolean affectsPosition(int position) {
        return position == bottom;
    }
    
    /**
     * Returns the position player climbs to when they hit this ladder
     */
    public int getClimbToPosition() {
        return top;
    }
    
    @Override
    public String toString() {
        return "Ladder(" + bottom + "→" + top + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ladder ladder = (Ladder) obj;
        return bottom == ladder.bottom && top == ladder.top;
    }
    
    @Override
    public int hashCode() {
        return bottom * 31 + top;
    }
}