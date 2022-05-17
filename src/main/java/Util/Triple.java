package Util;

/**
 * @param <L> left
 * @param <M> medium
 * @param <R> right
 */
public class Triple <L, M, R> {
    public L left; // left value
    public M medium; // medium value
    public R right; // right value

    /**
     * Default constructor
     * @param left left value
     * @param medium medium value
     * @param right right value
     */
    public Triple(L left, M medium, R right) {
        this.left = left;
        this.medium = medium;
        this.right = right;
    }
}
