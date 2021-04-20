/*
 * FriendlySeq.java
 *
 * This is the sequential version of the friendly number finder.
 * It loops through each number and sums the divisors then
 * calculates the gcd using the Euclidean Algorithm
 *
 * author: Chris Tremblay (cst1465)
 * Created 3/14/2021, PI Day!
 */


import edu.rit.pj2.Task;
import java.util.*;

/**
 * The driver class
 *
 * @author Chris Tremblay (cst1465)
 * @version 1.0
 */
public class FriendlySeq extends Task {

    /** The map of friendly numbers */
    private SortedMap<Key, SortedSet<Long>> friendlyList;

    /** keep track of the longest list size */
    private long longestList;

    /** Total number of friendly number accumulator */
    private long totalFriendlyNums;


    /**
     * The pseudo main class for the program invoked
     * by pj2 library. Driver function for finding
     * friendly number
     *
     * @param args the command line arguments
     */
    public void main(String[] args){
        String USAGE_MESSAGE = "Usage: java pj2 FriendlySeq start-integer" +
                " end-integer # 0 < start < end ";

        // Get numbers from command line args
        if(args.length != 2){
            System.err.println(USAGE_MESSAGE);
            System.exit(1);
        }

        // check numbers are ints
        long start = -1, finish = -1; // catch the error
        try{
            start = Integer.parseInt(args[0]);
            finish = Integer.parseInt(args[1]);
        } catch (Exception e){
            System.err.println(USAGE_MESSAGE);
            System.exit(1);
        }

        // Check numbers are in the right range
        if( !(0 < start && start < finish) ){
            System.err.println(USAGE_MESSAGE);
            System.exit(1);
        }

        // Start the algorithm
        int last = (int) (finish - start + 1);
        long[] vals = new long[last];
        long[] numer = new long[last];
        long[] denom = new long[last];

        // initialize hashmap of friendly number
        friendlyList = new TreeMap<>();
        for(long val = start; val <= finish; val++) {
            // [1..5] are considered solitary number, so skip
            if(1 <= val && val <= 5)
                continue;

            // get index and generate val array
            int index = (int) (val - start);
            vals[index] = val;
            long total = 1 + val;

            // get sum of divisors
            for(long i = 2; i <= Math.sqrt(val); i++){
                if(val % i == 0) {
                    total += i + val / i;
                    if (i == val / i)
                        total -= i;
                }
            }

            // get numerator, divisor and scale by GCD
            if( total - val == 1 ){
                continue;
            }

            numer[index] = total;
            denom[index] = val;

            // get gcd
            long n1 = total;
            long n2 = val;
            while(n2!=0){
                long temp = n2;
                n2 = n1%n2;
                n1 = temp;
            }

            // reduce
            numer[index] /= n1;
            denom[index] /= n1;

            Key key = new Key(numer[index], denom[index]);
            SortedSet<Long> l;
            if(!friendlyList.containsKey(key)) {
                l = new TreeSet<>();
                l.add(vals[index]);
                friendlyList.put(key, l);
            } else {
                l = friendlyList.get(key);
                l.add(vals[index]);
            }

            // Keep track of largest size list
            long tempSize = l.size();
            if(longestList < tempSize)
                longestList = tempSize;
        }

        // initialize total friendly number count, and sort keys
        for(Key k : friendlyList.keySet() ){
            Set<Long> l = friendlyList.get(k);
            long size = l.size();
            if(size <= 1) continue; // lists of size 1 aren't friendly
            totalFriendlyNums += l.size();
            if(size == longestList)
                System.out.printf("{%s: %s}\n", k, l.toString());
        }
        System.out.printf("%d friendly numbers.\n", totalFriendlyNums);
    }
}
