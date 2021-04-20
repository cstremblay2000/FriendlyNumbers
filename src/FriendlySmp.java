/*
 * FriendlySmp.java
 *
 * This is the parallelized version of the friendly number finder.
 * It loops through each number and sums the divisors then
 * calculates the gcd using the Euclidean Algorithm.
 * It divides this work into chunks and sends distributes the work
 * among multiple cores.
 *
 * author: Chris Tremblay (cst1465)
 * Created 3/14/2021, PI Day!
 */

import edu.rit.pj2.LongLoop;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.LongVbl;

import java.util.*;

/**
 * The driver task
 *
 * @author Chris Tremblay (cst1465)
 * @version 1.0
 */
public class FriendlySmp extends Task {

    /** The map that holds the friendly number */
    private SortedMap<Key, SortedSet<Long>> friendlyList;

    /** Keeps track of the longest list */
    private LongVbl longestList = new LongVbl(-1);

    /** keeps track of the totoal number of freindly numbers */
    private LongVbl totalFriendlyNums = new LongVbl(0);

    /** The starting bound */
    private LongVbl start;

    /** the ending bound */
    private LongVbl finish;

    /** List of values between start and end */
    LongVbl[] vals;

    /** associative array with numerator of abundance index of vals[i] */
    LongVbl[] numer;

    /** associative array with denominator of abundance index of vals[i] */
    LongVbl[] denom;

    /** List from 0 to (start-finish+1) */
    LongVbl[] indices;


    /**
     * The pseudo main class for the program invoked
     * by pj2 library. Driver function for finding
     * friendly number
     *
     * @param args the command line arguments
     */
    public void main(String[] args){
        String USAGE_MESSAGE = "Usage: java pj2 FriendlySmp start-integer" +
                " end-integer # 0 < start < end ";

        // Get numbers from command line args
        if(args.length != 2){
            System.err.println(USAGE_MESSAGE);
            System.exit(1);
        }

        // check numbers are ints
        try{
            start = new LongVbl(Long.parseLong(args[0]));
            finish = new LongVbl(Long.parseLong(args[1]));
        } catch (Exception e){
            System.err.println(USAGE_MESSAGE);
            System.exit(1);
        }

        // Check numbers are in the right range
        if( !(0 < start.item && start.item < finish.item) ){
            System.err.println(USAGE_MESSAGE);
            System.exit(1);
        }

        // Start the algorithm
        int last = (int) (finish.item - start.item + 1);
        vals = new LongVbl[last];
        numer = new LongVbl[last];
        denom = new LongVbl[last];
        indices = new LongVbl[last];

        parallelFor(start.item, finish.item).exec(new LongLoop() {
            @Override
            public void run(long val) throws Exception {
                int index = (int) (val - start.item);
                vals[index] = new LongVbl(val);
                indices[index] = new LongVbl(index);
                denom[index] = new LongVbl(val);
            }
        });

        // initialize hashmap of friendly number
        friendlyList = new TreeMap<>();

        parallelFor(0, indices.length-1).exec( new Loop() {

            /**
             * Run the algorithm by splitting up the indices array
             * to do the work
             *
             * @param i the iteration of the list
             * @throws Exception catch any exception that might happen
             */
            @Override
            public void run(int i) throws Exception {
                // get val
                long val = vals[i].item;

                // numbers [1...5] are solitary numbers by definiton
                if (1 <= val && val <= 5)
                    return;

                // Initialize total
                long total = 1 + val;
                for(long j = 2; j <= Math.sqrt(val); j++){
                    if(val % j== 0) {
                        total += j + val / j; // find factor pairs
                        if (j == val / j)
                            total -= j;
                    }
                }

                // get numerator, divisor and scale by GCD
                numer[i] = new LongVbl(total);
                long n2 = total;
                long n1 = val;

                // calculate gcd
                while(n2!=0){
                    long temp = n2;
                    n2 = n1%n2;
                    n1 = temp;
                }

                if( total - val == 1 ){
                    return;
                } else {
                    numer[i] = new LongVbl(total / n1);
                    denom[i] = new LongVbl(val / n1);
                }

                // Create key and add to list
                Key key = new Key(numer[i].item, denom[i].item);
                SortedSet<Long> l;
                synchronized (friendlyList) {
                    if (!friendlyList.containsKey(key)) {
                        l = new TreeSet<>();
                        l.add(vals[i].item);
                        friendlyList.put(key, l);
                    } else {
                        l = friendlyList.get(key);
                        l.add(vals[i].item);
                    }

                    // Keep track of largest size list
                    long tempSize = l.size();
                    if (longestList.item < tempSize)
                        longestList.item = tempSize;}
            }
        });

        // initialize total friendly number count, and sort keys
        for(Key k : friendlyList.keySet() ){
            Set<Long> l = friendlyList.get(k);
            long size = l.size();
            if(size <= 1) continue; // lists of size 1 aren't friendly
            totalFriendlyNums.item += l.size();
            if(size == longestList.item)
                System.out.printf("{%s: %s}\n", k, l.toString());
        }
        System.out.printf("%d friendly numbers.\n", totalFriendlyNums.item);
    }
}
