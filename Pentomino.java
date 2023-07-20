
import java.util.Arrays;

/**
 * Pentomino.java
 *
 * Cameron Bradley - 3344991
 * Castipher McSkimming - 8287490
 * Jacob Cone - 3977920
 * Luke Tang - 4258935
 *
 */
public class Pentomino {

    private char type;
    private int[][] coords = new int[5][2];
    private int pivot = 0;
    private int[] pos = DEFAULT_POS;
    private int currentCombination = 1;
    private int combinations = 8;

    private static final int[][][] oCoords = new int[][][]{
        {{0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0}},
        {{0, 0}, {0, -1}, {0, -2}, {0, -3}, {0, -4}}
    };

    private static final int[][][] pCoords = new int[][][]{
        {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {2, 0}},
        {{0, 0}, {1, 0}, {0, -1}, {1, -1}, {0, -2}},
        {{0, 0}, {0, -1}, {-1, 0}, {-1, -1}, {-2, 0}},
        {{0, 0}, {-1, 0}, {0, 1}, {-1, 1}, {0, 2}},
        {{0, 0}, {0, -1}, {1, 0}, {1, -1}, {2, 0}},
        {{0, 0}, {-1, 0}, {0, -1}, {-1, -1}, {0, -2}},
        {{0, 0}, {0, 1}, {-1, 0}, {-1, 1}, {-2, 0}},
        {{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}},};

    private static final int[][][] qCoords = new int[][][]{
        {{0, 0}, {0, 1}, {1, 1}, {2, 1}, {3, 1}},
        {{0, 0}, {1, 0}, {1, -1}, {1, -2}, {1, -3}},
        {{0, 0}, {0, -1}, {-1, -1}, {-2, -1}, {-3, -1}},
        {{0, 0}, {-1, 0}, {-1, 1}, {-1, 2}, {-1, 3}},
        {{0, 0}, {0, -1}, {1, -1}, {2, -1}, {3, -1}},
        {{0, 0}, {-1, 0}, {-1, -1}, {-1, -2}, {-1, -3}},
        {{0, 0}, {0, 1}, {-1, 1}, {-2, 1}, {-3, 1}},
        {{0, 0}, {1, 0}, {1, 1}, {1, 2}, {1, 3}}
    };

    private static final int[][][] rCoords = new int[][][]{
        {{0, 1}, {0, 2}, {1, 0}, {1, 1}, {2, 1}},
        {{1, 0}, {2, 0}, {0, -1}, {1, -1}, {1, -2}},
        {{0, -1}, {0, -2}, {-1, 0}, {-1, -1}, {-2, -1}},
        {{-1, 0}, {-2, 0}, {0, 1}, {-1, 1}, {-1, 2}},
        {{0, -1}, {0, -2}, {1, 0}, {1, -1}, {2, -1}},
        {{-1, 0}, {-2, 0}, {0, -1}, {-1, -1}, {-1, -2}},
        {{0, 1}, {0, 2}, {-1, 0}, {-1, 1}, {-2, 1}},
        {{1, 0}, {2, 0}, {0, 1}, {1, 1}, {1, 2}}
    };

    private static final int[][][] sCoords = new int[][][]{
        {{0, 2}, {0, 3}, {1, 0}, {1, 1}, {1, 2}},
        {{2, 0}, {3, 0}, {0, -1}, {1, -1}, {2, -1}},
        {{0, -2}, {0, -3}, {-1, 0}, {-1, -1}, {-1, -2}},
        {{-2, 0}, {-3, 0}, {0, 1}, {-1, 1}, {-2, 1}},
        {{0, -2}, {0, -3}, {1, 0}, {1, -1}, {1, -2}},
        {{-2, 0}, {-3, 0}, {0, -1}, {-1, -1}, {-2, -1}},
        {{0, 2}, {0, 3}, {-1, 0}, {-1, 1}, {-1, 2}},
        {{2, 0}, {3, 0}, {0, 1}, {1, 1}, {2, 1}}
    };

    private static final int[][][] tCoords = new int[][][]{
        {{0, 0}, {0, 1}, {0, 2}, {1, 1}, {2, 1}},
        {{0, 0}, {1, 0}, {2, 0}, {1, -1}, {1, -2}},
        {{0, 0}, {0, -1}, {0, -2}, {-1, -1}, {-2, -1}},
        {{0, 0}, {-1, 0}, {-2, 0}, {-1, 1}, {-1, 2}}
    };

    private static final int[][][] uCoords = new int[][][]{
        {{0, 0}, {0, 2}, {1, 0}, {1, 1}, {1, 2}},
        {{0, 0}, {2, 0}, {0, -1}, {1, -1}, {2, -1}},
        {{0, 0}, {0, -2}, {-1, 0}, {-1, -1}, {-1, -2}},
        {{0, 0}, {-2, 0}, {0, 1}, {-1, 1}, {-2, 1}}
    };

    private static final int[][][] vCoords = new int[][][]{
        {{0, 0}, {1, 0}, {2, 0}, {2, 1}, {2, 2}},
        {{0, 0}, {0, -1}, {0, -2}, {1, -2}, {2, -2}},
        {{0, 0}, {-1, 0}, {-2, 0}, {-2, -1}, {-2, -2}},
        {{0, 0}, {0, 1}, {0, 2}, {-1, 2}, {-2, 2}}
    };

    private static final int[][][] wCoords = new int[][][]{
        {{0, 1}, {0, 2}, {1, 0}, {1, 1}, {2, 0}},
        {{1, 0}, {2, 0}, {0, -1}, {1, -1}, {0, -2}},
        {{0, -1}, {0, -2}, {-1, 0}, {-1, -1}, {-2, 0}},
        {{-1, 0}, {-2, 0}, {0, 1}, {-1, 1}, {0, 2}}
    };

    private static final int[][][] xCoords = new int[][][]{
        {{0, 1}, {1, 0}, {1, 1}, {1, 2}, {2, 1}}
    };

    private static final int[][][] yCoords = new int[][][]{
        {{0, 1}, {1, 0}, {1, 1}, {2, 1}, {3, 1}},
        {{1, 0}, {0, -1}, {1, -1}, {1, -2}, {1, -3}},
        {{0, -1}, {-1, 0}, {-1, -1}, {-2, -1}, {-3, -1}},
        {{-1, 0}, {0, 1}, {-1, 1}, {-1, 2}, {-1, 3}},
        {{0, -1}, {1, 0}, {1, -1}, {2, -1}, {3, -1}},
        {{-1, 0}, {0, -1}, {-1, -1}, {-1, -2}, {-1, -3}},
        {{0, 1}, {-1, 0}, {-1, 1}, {-2, 1}, {-3, 1}},
        {{1, 0}, {0, 1}, {1, 1}, {1, 2}, {1, 3}}
    };

    private static final int[][][] zCoords = new int[][][]{
        {{0, 0}, {0, 1}, {1, 1}, {2, 1}, {2, 2}},
        {{0, 0}, {1, 0}, {1, -1}, {1, -2}, {2, -2}},
        {{0, 0}, {0, -1}, {1, -1}, {2, -1}, {2, -2}},
        {{0, 0}, {-1, 0}, {-1, -1}, {-1, -2}, {-2, -2}}
    };

    static final int[] DEFAULT_POS = new int[]{-1, -1};

    public Pentomino() {
        System.out.println("ERROR: Parameter should be one of:");
        System.out.println("O, P, Q, R, S, T, U, V, W, X, Y or Z");
    }

    public Pentomino(char type) {
        setUp(type);
    }

    private void setUp(char type) {
        switch (Character.toUpperCase(type)) {
            case 'O':
                setUpO();
                break;
            case 'P':
                setUpP();
                break;
            case 'Q':
                setUpQ();
                break;
            case 'R':
                setUpR();
                break;
            case 'S':
                setUpS();
                break;
            case 'T':
                setUpT();
                break;
            case 'U':
                setUpU();
                break;
            case 'V':
                setUpV();
                break;
            case 'W':
                setUpW();
                break;
            case 'X':
                setUpX();
                break;
            case 'Y':
                setUpY();
                break;
            case 'Z':
                setUpZ();
                break;
            default:
                System.out.println("ERROR: Parameter should be one of:");
                System.out.println("O, P, Q, R, S, T, U, V, W, X, Y or Z");
        }
    }

    private void setUpO() {
        // Letter O
        // O . . . .
        // O . . . .
        // O . . . .
        // O . . . .
        // O . . . .
        type = 'O';
        coords = oCoords[currentCombination - 1];
        combinations = 2;
    }

    private void setUpP() {
        // Letter P
        //
        // P P . . .
        // P P . . .
        // P . . . .
        // . . . . .
        // . . . . .
        type = 'P';
        coords = pCoords[currentCombination - 1];
        combinations = 8;
    }

    private void setUpQ() {
        // Letter Q
        //
        // Q Q . . .
        // . Q . . .
        // . Q . . .
        // . Q . . .
        // . . . . .
        type = 'Q';
        coords = qCoords[currentCombination - 1];
        combinations = 8;
    }

    private void setUpR() {
        // Letter R
        //
        // . R R . .
        // R R . . .
        // . R . . .
        // . . . . .
        // . . . . .
        type = 'R';
        coords = rCoords[currentCombination - 1];
        combinations = 8;
    }

    private void setUpS() {
        // Letter S
        //
        // . . S S .
        // S S S . .
        // . . . . .
        // . . . . .
        // . . . . .
        type = 'S';
        coords = sCoords[currentCombination - 1];
        combinations = 8;
    }

    private void setUpT() {
        // Letter T
        //
        // T T T . .
        // . T . . .
        // . T . . .
        // . . . . .
        // . . . . .
        type = 'T';
        coords = tCoords[currentCombination - 1];
        combinations = 4;
    }

    private void setUpU() {
        // Letter U
        //
        // U . U . .
        // U U U . .
        // . .  . .
        // . . . . .
        // . . . . .
        type = 'U';
        coords = uCoords[currentCombination - 1];
        combinations = 4;
    }

    private void setUpV() {
        // Letter V
        //
        // V . . . .
        // V . . . .
        // V V V . .
        // . . . . .
        // . . . . .
        type = 'V';
        coords = vCoords[currentCombination - 1];
        combinations = 4;
    }

    private void setUpW() {
        // Letter W
        //
        // . W W . .
        // W W . . .
        // W . . . .
        // . . . . .
        // . . . . .
        type = 'W';
        coords = wCoords[currentCombination - 1];
        combinations = 4;
    }

    private void setUpX() {
        // Letter X
        //
        // . X . . .
        // X X X . .
        // . X . . .
        // . . . . .
        // . . . . .
        type = 'X';
        coords = xCoords[currentCombination - 1];
        combinations = 1;
    }

    private void setUpY() {
        // Letter Y
        //
        // . Y . . .
        // Y Y . . .
        // . Y . . .
        // . Y . . .
        // . . . . .
        type = 'Y';
        coords = yCoords[currentCombination - 1];
        combinations = 8;
    }

    private void setUpZ() {
        // Letter Z
        //
        // Z Z . . .
        // . Z . . .
        // . Z Z . .
        // . . . . .
        // . . . . .
        type = 'Z';
        coords = zCoords[currentCombination - 1];
        combinations = 4;
    }

    public char getType() {
        return type;
    }

    public int[][] getCoords() {
        return coords;
    }

    public int getPivot() {
        return pivot;
    }

    public int[] getPos() {
        return pos;
    }

    public int getCombinations() {
        return combinations;
    }

    public void setType(char type) {
        this.type = type;
    }

    public void setCoords(int[][] coords) {
        this.coords = coords;
    }

    public void setPivot(int pivot) {
        this.pivot = pivot;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    public void setCombinations(int combinations) {
        this.combinations = combinations;
    }

    /**
     * Check if the pivot is out of range.
     * 
     * The pivot should be an int value between 0 and 4.
     * 
     */
    public boolean canCyclePivots() {
        if (pivot < 0 || pivot > 4) {
            return false;
        }

        return true;
    }

    /**
     * Increases the pivot.
     * 
     */
    public void cyclePivot() {
        pivot++;
    }

    /**
     * Resets the pivot. 
     *
     */ 
    public void resetPivot() {
        pivot = 0;
    }


    /**
     * Check if the pentomino has more combinations to use.
     * 
     */ 
    public boolean hasCombinations() {
        if (currentCombination > combinations) {
            return false;
        }

        return true;
    }

    /**
     * Changes the pentomino to the next combination it can use.
     * 
     */ 
    public void nextCombination() {
        this.currentCombination++;

        if (this.currentCombination > combinations) {
            return;
        }

        setUp(type);
    }

    /**
     * Resets the pentomino to its initial combination.
     * 
     */ 
    public void resetCombinations() {
        this.currentCombination = 1;
        setUp(type);
    }

    /**
     * Rotates the pentomino's global coordinates.
	 *
     */
    public void rotate() {
        for (int i = 0; i < coords.length; i++) {
            int x = coords[i][0];
            int y = coords[i][1];

            coords[i][0] = x * 0 + y * 1;
            coords[i][1] = x * -1 + y * 0;
        }
    }

    /**
     * Mirrors the pentomino's global coordinates.
	 *
     */
    public void mirror() {
        for (int i = 0; i < coords.length; i++) {
            int x = coords[i][0];
            int y = coords[i][1];

            coords[i][0] = x * 1 + y * 0;
            coords[i][1] = x * 0 + y * -1;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(type);

        // for (int i = 0; i < coords.length; i++) {
        // 	s.append("(" + coords[i][0] + ", " + coords[i][1] + ")\n");
        // }
        
        return s.toString();
    }

    /**
     * Calculate hash code so that we can know if a Pentomino is unique or not.
     * For example, if we insert multiple Pentominos into a HashSet collection
     * which only allows unique Pentominoes, then two Pentominoes will be the
     * same only if their hash codes are the same (i.e., they have the same type
     * and position).
     *
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.type;
        hash = 17 * hash + Arrays.hashCode(this.pos);
        return hash;
    }

    /**
     * Check if two Pentominoes are the same based on their type and position.
     *
     * @param obj the other Pentomino to check for uniqueness
     *
     * @return a boolean indicating whether the Pentominoes are the same or not.
     *
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pentomino other = (Pentomino) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Arrays.equals(this.pos, other.pos)) {
            return false;
        }
        return true;
    }

}
