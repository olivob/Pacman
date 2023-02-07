package Pacman;

/**
 * This class is an immutable representation of some coordinate within the
 * Pacman board world. As the board consists of square blocks arranged in a
 * 23x23 grid, all elements/blocks in the game must exist within this coordinate
 * space. *However*, when creating targets in Chase mode, your target may be out
 * of the bounds of the board -- that is okay for this scenario only. Therefore,
 * you can explicitly override the bounds-checking functionality by setting
 * isTarget to true in the constructor's third parameter. You should *only*
 * set this parameter to true if, because of the game logic, you specifically
 * want to allow out of bounds squares to be created (essentially, only when
 * creating targets).
 *
 * "Immutable" simply means that once you create the object, you can't change
 * anything about it - notice that while we have getter methods for the row and
 * column, there aren't any setters!
 *
 * There are two purposes to using this class instead of a vanilla
 * java.awt.Point or javafx.geometry.Point2D. First, we are able to bound the
 * ranges that the row and columns for this coordinate are able to take on,
 * thereby reducing bugs which would otherwise lead to ArrayIndexOutOfBounds
 * exceptions being thrown in mysterious ways. Second, by naming the dimensions
 * "row" and "column" rather than X and Y, the hope is to make clearer the
 * "true" location of each coordinate location.
 */
public class BoardCoordinate {

    private final int _row;
    private final int _column;
    private static final int _ROW_MAX = 22;
    private static final int _COL_MAX = 22;

    /**
     * The constructor. it takes in a row and a column whose location this
     * instance will model, and a boolean of whether the square is a target
     * square (see header comments for more on this).
     */
    public BoardCoordinate(int row, int column, boolean isTarget) {
        if (!isTarget) {
            this.checkValidity(row, column);
        }
        _row = row;
        _column = column;
    }

    /** Returns the row index that this BoardCoordinate represents. */
    public int getRow() {
        return _row;
    }

    /** Returns the column index that this BoardCoordinate represents. */
    public int getColumn() {
        return _column;
    }

    /**
     * Checks that the row and index passed into this class' constructor are
     * bounded by 0 and the _ROW_MAX for the row and the _COL_MAX for the column,
     * respectively.
     *
     * NOTE: You've seen exceptions like ArrayIndexOutOfBounds exceptions being
     * thrown before, but you haven't seen what code that generates these
     * Exceptions looks like - Here, we throw an IllegalArgumentException if the
     * row and column parameters are invalid! Try instantiating an instance of
     * this class with invalid coordinates, e.g. (-4, 50) and see what happens
     * when you run the code! Runtime Exceptions like IllegalArgumentExceptions
     * and ArrayIndexOutOfBounds Exceptions usually indicate that something is
     * wrong with the code, and needs to be fixed.
     */
    private void checkValidity(int row, int column) {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException(
                      "Board Coordinates must not be negative: " +
                      " Given row = " + row + " col = " + column);
        } else if (row > _ROW_MAX || column > _COL_MAX) {
            throw new IllegalArgumentException(
                      "Board Coordinates must not exceed board dimensions: " +
                      " Given row = " + row + " col = " + column);
        }
    }
}
