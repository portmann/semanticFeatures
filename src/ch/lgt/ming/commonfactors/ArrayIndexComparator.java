package ch.lgt.ming.commonfactors;

import edu.stanford.nlp.util.ArrayUtils;
import edu.stanford.nlp.util.IdentityHashSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.DoubleStream;


/**
 * Created by Ming Deng on 8/29/2016.
 */
public class ArrayIndexComparator implements Comparator<Integer> {


    private final List<Double> array;

    public ArrayIndexComparator(List<Double> array){
        this.array = array;
    }

    public Integer[] createIndexArray(){
        Integer[] indices = new Integer[array.size()];
        for (int i = 0; i < array.size(); i++){
            indices[i] = i;
        }
        return indices;
    }

    @Override
    public int compare(Integer index1, Integer index2){

        return -(array.get(index1).compareTo(array.get(index2)));
    }

    public static void main(String[] args) {

        List<Double> doubles = Arrays.asList(0.3,0.1,0.5,0.2,0.0);
        ArrayIndexComparator comparator = new ArrayIndexComparator(doubles);
        Integer[] indices = comparator.createIndexArray();
        Arrays.sort(indices, comparator);
        System.out.println(Arrays.toString(indices));

    }
}
