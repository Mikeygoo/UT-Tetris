import java.awt.*;
import java.util.*;

/**
   An immutable representation of a tetris piece in a particular rotation.
   Each piece is defined by the blocks that make up its body.

   This is the starter file version -- a few simple things are filled
   in already.

   @author	Nick Parlante
   @version	1.0, Mar 1, 2001
*/
public final class TetrisPiece extends Piece {
    private static Piece[] pieces;
    private Point[] points;
    //private TetrisPiece next;
    private int width, height;
    private int[] skirt;
    
    /**
       Defines a new piece given the Points that make up its body.
       Makes its own copy of the array and the Point inside it.
       Does not set up the rotations.

       This constructor is PRIVATE -- if a client
       wants a piece object, they must use Piece.getPieces().
    */
    public TetrisPiece(Point[] points) {
        this.points = points;
        
        for (Point p : points) {
            if (width <= p.x)
                width = p.x + 1;
            
            if (height <= p.y)
                height = p.y + 1;
        }
        
        skirt = new int[width];
        Arrays.fill(skirt, Integer.MAX_VALUE);
        for (Point p : points) {
            if (skirt[p.x] > p.y)
                skirt[p.x] = p.y;
        }
    }

    /**
       Returns the width of the piece measured in blocks.
    */
    public int getWidth() {
        return width;
    }

    /**
       Returns the height of the piece measured in blocks.
    */
    public int getHeight() {
        return height;
    }

    /**
       Returns a pointer to the piece's body. The caller
       should not modify this array.
    */
    public Point[] getBody() {
        return points;
    }

    /**
       Returns a pointer to the piece's skirt. For each x value
       across the piece, the skirt gives the lowest y value in the body.
       This useful for computing where the piece will land.
       The caller should not modify this array.
    */
    public int[] getSkirt() {
        return skirt;
    }


    /**
       Returns a piece that is 90 degrees counter-clockwise
       rotated from the receiver.

       <p>Implementation:
       The Piece class pre-computes all the rotations once.
       This method just hops from one pre-computed rotation
       to the next in constant time.
    */
    public Piece nextRotation() {
        return next;
    }


    /**
       Returns true if two pieces are the same --
       their bodies contain the same points.
       Interestingly, this is not the same as having exactly the
       same body arrays, since the points may not be
       in the same order in the bodies. Used internally to detect
       if two rotations are effectively the same.
    */
    public boolean equals(Piece other) {
        if (other.getBody().length != points.length || other.getWidth() != width || other.getHeight() != height)
            return false;
        
        HashSet<Point> pset = new HashSet<>();
        pset.addAll(Arrays.asList(points));
        
        for (Point p : other.getBody())
            if (!pset.remove(p))
                return false;
        
        return true;
    }

    public void setNext(TetrisPiece next) {
        this.next = next;
    }

    /**
       Returns an array containing the first rotation of
       each of the 7 standard tetris pieces.
       The next (counterclockwise) rotation can be obtained
       from each piece with the {@link #nextRotation()} message.
       In this way, the client can iterate through all the rotations
       until eventually getting back to the first rotation.
    */
    public static Piece[] getPieces() {
        if (pieces == null) {
            Piece[] pieces = new Piece[7];
            
            for (int i = 0; i < pieceStrings.length; i++) {
                String pieceString = pieceStrings[i];
                Point[] body = parsePoints(pieceString);
                pieces[i] = createRotations(body);
            }

            return TetrisPiece.pieces = pieces;
        } else {
            return TetrisPiece.pieces;
        }
    }

    private static Piece createRotations(Point[] originalBody) {
        TetrisPiece initial = new TetrisPiece(originalBody);
        TetrisPiece last = initial;
        TetrisPiece current = initial;
        while (true) {
            Point[] oldBody = current.getBody();
            Point[] newBody = new Point[oldBody.length];
            for (int i = 0; i < newBody.length; i++) {
                newBody[i] = new Point(-oldBody[i].y + current.getHeight() - 1, oldBody[i].x);
            }
            current = new TetrisPiece(newBody);
            if (current.equals(initial)) {
                last.setNext(initial);
                //System.out.println("done");
                break;
            } else {
                //System.out.println("rotating...");
                last.setNext(current);
            }
            last = current;
        }
        return initial;
    }
}

//OLD HARD-CODING.

/*
 * TetrisPiece stick1 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3)});
            TetrisPiece stick2 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0)});
            stick1.setNext(stick2);
            stick2.setNext(stick1);
            pieces[0] = stick1;

            TetrisPiece lr1 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(1, 0)});
            TetrisPiece lr2 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1)});
            TetrisPiece lr3 = new TetrisPiece(new Point[] {new Point(0, 2), new Point(1, 0), new Point(1, 1), new Point(1, 2)});
            TetrisPiece lr4 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)});
            lr1.setNext(lr2);
            lr2.setNext(lr3);
            lr3.setNext(lr4);
            lr4.setNext(lr1);
            pieces[1] = lr1;

            TetrisPiece ll1 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2)});
            TetrisPiece ll2 = new TetrisPiece(new Point[] {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0)});
            TetrisPiece ll3 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(1, 2)});
            TetrisPiece ll4 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(2, 0)});
            ll1.setNext(ll2);
            ll2.setNext(ll3);
            ll3.setNext(ll4);
            ll4.setNext(ll1);
            pieces[2] = ll1;

            TetrisPiece dr1 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)});
            TetrisPiece dr2 = new TetrisPiece(new Point[] {new Point(0, 2), new Point(0, 1), new Point(1, 1), new Point(1, 0)});
            dr1.setNext(dr2);
            dr2.setNext(dr1);
            pieces[3] = dr1;

            TetrisPiece dl1 = new TetrisPiece(new Point[] {new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(2, 0)});
            TetrisPiece dl2 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)});
            dl1.setNext(dl2);
            dl2.setNext(dl1);
            pieces[4] = dl1;

            TetrisPiece square = new TetrisPiece(new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)});
            square.setNext(square);
            pieces[5] = square;

            TetrisPiece t1 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(1, 1)});
            TetrisPiece t2 = new TetrisPiece(new Point[] {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 1)});
            TetrisPiece t3 = new TetrisPiece(new Point[] {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 0)});
            TetrisPiece t4 = new TetrisPiece(new Point[] {new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(1, 1)});
            t1.setNext(t2);
            t2.setNext(t3);
            t3.setNext(t4);
            t4.setNext(t1);
            pieces[6] = t1;
 */