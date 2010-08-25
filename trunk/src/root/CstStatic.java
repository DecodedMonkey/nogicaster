/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package root;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Letty
 */
public class CstStatic { 

    	public static final long ERINN_HOUR = 90 * 1000;
	public static final long ERINN_MINUTE = 1500;
	public static final long ERINN_DAY = 36 * 60 * 1000; //36 minutes.
        public static final long IRL_HOUR = 3600 * 1000; //3600 sec/hour.
        public static final long IRL_MINUTE = 60 * 1000; //60 sec/minute.
        public static final long IRL_SECOND = 1000;
        public static final int HOURS_IN_DAY = 24;

        public static final long serverOffset = -8 * 60 * 60 * 1000; //GMT -8, PST
	public static final long dstOffset = (60 * 1000 * 60); //For PDT

        public static ArrayList<String> parseStrArray(String str)
        {
            String leftoff = str.substring(1);
            String rightoff = leftoff.substring(0, leftoff.length() -1);

            String[] strarray = rightoff.split(",\\s");
            return new ArrayList(Arrays.asList(strarray));
        }

        public static ArrayList<Integer> parseIntArray(String str)
        {
            String leftoff = str.substring(1);
            String rightoff = leftoff.substring(0, leftoff.length() -1);

            String[] strarray = rightoff.split(",\\s");
            ArrayList<Integer> i = new ArrayList<Integer>();
            for(String s : strarray) i.add(Integer.parseInt(s));
            return i;
        }

}
