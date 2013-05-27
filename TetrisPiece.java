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

    /**
       Defines a new piece given the Points that make up its body.
       Makes its own copy of the array and the Point inside it.
       Does not set up the rotations.

       This constructor is PRIVATE -- if a client
       wants a piece object, they must use Piece.getPieces().
    */
    private TetrisPiece(Point[] points) {
	// your code here
    }

    /**
       Returns the width of the piece measured in blocks.
    */
    public int getWidth() {
	// your code here
	return -1;
    }

    /**
       Returns the height of the piece measured in blocks.
    */
    public int getHeight() {
	// your code here
	return -1;
    }

    /**
       Returns a pointer to the piece's body. The caller
       should not modify this array.
    */
    public Point[] getBody() {
	// your code here
	return null;
    }

    /**
       Returns a pointer to the piece's skirt. For each x value
       across the piece, the skirt gives the lowest y value in the body.
       This useful for computing where the piece will land.
       The caller should not modify this array.
    */
    public int[] getSkirt() {
	// your code here
	return null;
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
	// your code here
	return null;
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
	// your code here
	return false;
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
	// your code here
	return null;
    }

}
