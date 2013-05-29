import java.awt.*;
import java.util.*;


/**
   Represents a Tetris board -- essentially a 2-d grid of
   booleans. Supports tetris pieces and row clearning.  Has an "undo"
   feature that allows clients to add and remove pieces efficiently.
   Does not do any drawing or have any idea of pixels. Intead, just
   represents the abtsract 2-d board.

   This is the starter file version -- a few simple things are filled
   in already

   @author	Nick Parlante   w/changes by Walter Chang
   @version	1.0.1, Sep 19, 2004
*/
public class TetrisBoard implements Board {
    private static final boolean DEBUG = true;
    private boolean[][] grid, backupGrid;
    private int maxHeight = -1, backupMaxHeight = -1;
    private int[] rowWidths, backupRowWidths;
    private int[] columnHeights, backupColumnHeights;
    private int width, height;
    private boolean primed = false;


    /**
       Creates an empty board of the given width and height
       measured in blocks.
    */
    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new boolean[width][height];
        backupGrid = new boolean[width][height];
        rowWidths = new int[height];
        Arrays.fill(rowWidths, -1);
        backupRowWidths = new int[height];
        Arrays.fill(backupRowWidths, -1);
        columnHeights = new int[width];
        Arrays.fill(columnHeights, -1);
        backupColumnHeights = new int[width];
        Arrays.fill(backupColumnHeights, -1);
    }


    /**
       Returns the width of the board in blocks.
    */
    public int getWidth() {
        return width;
    }


    /**
       Returns the height of the board in blocks.
    */
    public int getHeight() {
        return height;
    }


    /**
       Returns the max column height present in the board.
       For an empty board this is 0.
    */
    public int getMaxHeight() {
        if (maxHeight != -1)
            return maxHeight;
        int mh = 0;
        for (int x = 0; x < width; x++) {
            int ch = getColumnHeight(x);
            if (mh < ch)
                mh = ch;
        }
        return maxHeight = mh;
    }


    /**
       Checks the board for internal consistency -- used
       for debugging.
    */
    public void sanityCheck() {
        if (DEBUG) {
            // consistency check the board state
        }
    }

    /**
       Given a piece and an x, returns the y
       value where the piece would come to rest
       if it were dropped straight down at that x.

       <p>
       Implementation: use the skirt and the col heights
       to compute this fast -- O(skirt length).
    */
    public int dropHeight(Piece piece, int x) {
        int[] skirt = piece.getSkirt();
        int max = 0;
        for (int ix = 0; ix < skirt.length; ix++) {
            int local = getColumnHeight(x + ix) - skirt[ix];
            if (max < local)
                max = local;
        }
        return max;
    }


    /**
       Returns the height of the given column --
       i.e. the y value of the highest block + 1.
       The height is 0 if the column contains no blocks.
    */
    public int getColumnHeight(int x) {
        if (columnHeights[x] != -1)
            return columnHeights[x];
        for (int y = height - 1; y >= 0; y--)
            if (getGrid(x, y))
                return columnHeights[x] = y + 1;
        return columnHeights[x] = 0;
    }


    /**
       Returns the number of filled blocks in
       the given row.
    */
    public int getRowWidth(int y) {
        if (rowWidths[y] != -1)
            return rowWidths[y];
        int a = 0;
        for (int x = 0; x < width; x++)
            if (getGrid(x, y))
                a++;
        return rowWidths[y] = a;
    }


    /**
       Returns true if the given block is filled in the board.
       Blocks outside of the valid width/height area
       always return true.
    */
    public final boolean getGrid(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return true;
        return grid[x][y];
    }


    public static final int PLACE_OK = 0;
    public static final int PLACE_ROW_FILLED = 1;
    public static final int PLACE_OUT_BOUNDS = 2;
    public static final int PLACE_BAD = 3;

    /**
       Attempts to add the body of a piece to the board.
       Copies the piece blocks into the board grid.
       Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
       for a regular placement that causes at least one row to be filled.

       Error cases:
       If part of the piece would fall out of bounds, the placement
       does not change the board at all, and PLACE_OUT_BOUNDS is
       returned.  If the placement is "bad" --interfering with
       existing blocks in the grid -- then the placement is halted
       partially complete and PLACE_BAD is returned.  An undo() will
       remove the bad placement.
    */
    public int place(Piece piece, int x, int y) {
        prime();
        maxHeight = -1;
        for (Point p : piece.getBody()) {
            int px = p.x + x;
            int py = p.y + y;
            // ^ I have to make the old values "stale"
            if (getGrid(px, py)) {
                if (px < 0 || py < 0 || px >= width || py >= height) {
                    System.out.println("Placement out of bounds.");
                    return PLACE_OUT_BOUNDS;
                } else {
                    System.out.println("Placement bad.");
                    return PLACE_BAD;
                }
            } else {
                rowWidths[py] = -1;
                columnHeights[px] = -1;
                grid[px][py] = true;
            }
        }
        
        for (int i = 0; i < height; i++)
            if (getRowWidth(i) == width)
                return PLACE_ROW_FILLED;
        
        return PLACE_OK;
    }

    /**
       Deletes rows that are filled all the way across, moving
       things above down. Returns true if any row clearing happened.

       <p>Implementation: This is complicated.
       Ideally, you want to copy each row down
       to its correct location in one pass.
       Note that more than one row may be filled.
    */
    public boolean clearRows() {
        Arrays.fill(rowWidths, -1); //todo: more efficient way for these two? VV
        Arrays.fill(columnHeights, -1);
        
        int offset = 0;
        for (int y = 0; y < height; y++) {
            if (getRowWidth(y) == width) {
                offset++;
            } else {
                for (int x = 0; x < width; x++)
                    grid[x][y - offset] = grid[x][y];
            }
        }
        
        return offset > 0;
    }



    /**
       If a place() happens, optionally followed by a clearRows(),
       a subsequent undo() reverts the board to its state before
       the place(). If the conditions for undo() are not met, such as
       calling undo() twice in a row, then the second undo() does nothing.
       See the overview docs.
    */
    public void undo() {
        if (!primed) {
            System.out.println("Not primed in undo!");
            return;
        } else {
            System.out.println("Prime undone!");
        }
        primed = false;
        for (int x = 0; x < width; x++)
            System.arraycopy(backupGrid[x], 0, grid[x], 0, height);
        System.arraycopy(backupRowWidths, 0, rowWidths, 0, height);
        System.arraycopy(backupColumnHeights, 0, columnHeights, 0, width);
        maxHeight = backupMaxHeight;
    }


    /**
       Puts the board in the committed state.
       See the overview docs.
    */
    public void commit() {
        if (!primed) {
            System.out.println("Not primed in commit!");
            return;
        } else {
            System.out.println("Prime committed.");
        }
        primed = false;
    }
    
    private void prime() {
        primed = true;
        for (int x = 0; x < width; x++)
            System.arraycopy(grid[x], 0, backupGrid[x], 0, height);
        System.arraycopy(rowWidths, 0, backupRowWidths, 0, height);
        System.arraycopy(columnHeights, 0, backupColumnHeights, 0, width);
        backupMaxHeight = maxHeight;
    }
}