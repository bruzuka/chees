package Util;

/**
 * @param <L> left type
 * @param <R> right type
 */
public class Pair<L, R> {
    public L left; // Left value
    public R right; // Right value

    /**
     * First parametrized constructor
     * @param left left value
     * @param right right value
     */
    public Pair(L left, R right){
        this.left = left;
        this.right = right;
    }

    /**
     * Second parametrized constructor (copy constructor)
     * @param p pair for copy
     */
    public Pair(Pair<L, R> p){
        left = p.left;
        right = p.right;
    }
}
