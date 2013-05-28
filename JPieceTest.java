import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;


/**
   Debugging client for the Piece class.
   The JPieceTest component draws all the rotations of a tetris piece.
   JPieceTest.main()  creates a frame  with one JPieceTest for each
   of the 7 standard tetris pieces.

   This is the starter file version --
   The outer shell is done. You need to complete paintComponent()
   and drawPiece().  Change this comment when you are done.
*/
class JPieceTest extends JComponent {
    protected Piece root;
    protected Font font;

    public JPieceTest(Piece piece, int width, int height) {
        super(); //(obviously?)

        setPreferredSize(new Dimension(width, height));

        root = piece;
        font = new Font("Monospaced", Font.BOLD, 12);
    }

    /**
       Draws the rotations from left to right.
       Each piece goes in its own little box.
    */
    public final int MAX_ROTATIONS = 4;
    public void paintComponent(Graphics g) {
        int x = 0, y = 0;
        int BOX_WIDTH = getWidth() / MAX_ROTATIONS, BOX_HEIGHT = getHeight();

        Piece currentRotation = root;
        do {
            drawPiece(g, currentRotation, new Rectangle(x, y, BOX_WIDTH, BOX_HEIGHT));
            x += BOX_WIDTH;
        } while (!(currentRotation = currentRotation.nextRotation()).equals(root));
    }

    /**
       Draw the piece inside the given rectangle.
    */
    private void drawPiece(Graphics g, Piece piece, Rectangle r) {
        int[] skirt = piece.getSkirt();
        int blockwidth = (int) (r.getWidth() / 4), blockheight = (int) (r.getHeight() / 4);
        for (Point p : piece.getBody()) {
            if (skirt[p.x] == p.y)
                g.setColor(Color.yellow);
            else
                g.setColor(Color.black);
            g.fillRect(r.x + blockwidth * p.x - 1, r.y + getHeight() - blockheight - (blockheight * p.y) - 1, blockwidth - 2, blockheight - 2);
        }
        g.setColor(Color.red);
        g.setFont(font);
        String s = String.format("w:%d h:%d", piece.getWidth(), piece.getHeight());
        Rectangle2D stringbounds = font.getStringBounds(s, ((Graphics2D) g).getFontRenderContext());
        g.drawString(s, (int) r.getMinX(), (int) (r.getMaxY() - stringbounds.getHeight() / 2));
    }


    /**
       Draws all the pieces by creating a JPieceTest for
       each piece, and putting them all in a frame.
    */
    static public void main(String[] args) {
        JFrame frame = new JFrame("Piece Tester");
        JComponent container = (JComponent)frame.getContentPane();

        // Put in a BoxLayout to make a vertical list
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        Piece[] pieces = TetrisPiece.getPieces();

        for (int i=0; i<pieces.length; i++) {
            JPieceTest test = new JPieceTest(pieces[i], 375, 75);
            container.add(test);
        }

        // Size the window and show it on screen
        frame.pack();
        frame.setVisible(true);

        // Quit on window close
        frame.addWindowListener(
        new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        }
        );
    }
}
