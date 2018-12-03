package huff;

import java.util.Comparator;

@SuppressWarnings("ALL")
public class data_Comparator implements Comparator {

    public data_Comparator() {
    }

    public int compare(Object o1, Object o2) {
        int prob_diff=((data)o2).get_probability()-((data)o1).get_probability();
        if(prob_diff<0)return -1;
        if(prob_diff>0)return 1;
        return 0;
    }
}