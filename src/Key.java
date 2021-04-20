/*
 * Key.java
 *
 * Contains a key object that is used to control sorting the
 * TreeMaps in the other classes
 *
 * author: Chris Tremblay (cst1465)
 * Created 3/14/2021, PI Day!
 */

import java.util.Objects;

/**
 * Encapsulates the numerator and denonimator of the abundancy index
 * and the ratio stored as a double. The logic for comparing keys
 * is based on the double representation of the fraction
 *
 * @author Chris Tremblay (cst1465)
 * @version 1.0
 */
public class Key implements Comparable<Key>{

    /** The numerator */
    long numer;

    /** The denominator */
    long denom;

    /** numer / denom */
    double ratio;

    /**
     * Create a new Key object, and calc ratio
     *
     * @param n the numerator
     * @param d the denominator
     */
    public Key(long n, long d){
        this.numer = n;
        this.denom = d;
        this.ratio = (double)n/(double)d;
    }

    /**
     * Checks equality of Key objects
     * @param obj the object to test
     * @return true if ratios are same, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Key){
            return ((Key)obj).ratio == this.ratio;
        }
        return false;
    }

    /**
     * Hash base on ratio field
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }

    /**
     * Format the string nicely
     * @return the formatted string
     */
    @Override
    public String toString(){
        if(denom == 0)
            return "0/0";
        return String.format("(%d/%d)", numer, denom);
    }

    /**
     * Compare this to another key, based on the ratio
     * @param o other Key to test
     * @return which Key has larger ratio
     */
    @Override
    public int compareTo(Key o) {
        return Double.compare(this.ratio, o.ratio);
    }
}
