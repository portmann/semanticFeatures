package ch.lgt.ming.commonfactors;

import ch.lgt.ming.datastore.IdListDouble;

import java.util.*;
import java.util.stream.DoubleStream;

/**
 * Created by Ming Deng on 8/31/2016.
 */
public class Kmeans {

    private static int NumberOfFeatures;
    private static int NumberOfCluster;
    private static int NumberOfDocs;


    public static void main(String[] args) {


        IdListDouble docs = new IdListDouble();

        List<Double> doc1 = new ArrayList<>();
        doc1.add(0,1.0);
        doc1.add(1,1.3);
        docs.putValue(0,doc1);

        List<Double> doc2 = new ArrayList<>();
        doc2.add(0,1.5);
        doc2.add(1,1.3);
        docs.putValue(1,doc2);

        List<Double> doc3 = new ArrayList<>();
        doc3.add(0,1.8);
        doc3.add(1,0.7);
        docs.putValue(2,doc3);

        List<Double> doc4 = new ArrayList<>();
        doc4.add(0,-1.0);
        doc4.add(1,-1.0);
        docs.putValue(3,doc4);

        List<Double> doc5 = new ArrayList<>();
        doc5.add(0,-1.5);
        doc5.add(1,-1.3);
        docs.putValue(4,doc5);

        List<Double> doc6 = new ArrayList<>();
        doc6.add(0,-1.8);
        doc6.add(1,-0.7);
        docs.putValue(5,doc6);

        Kmeans kmeans = new Kmeans(2,2,6);
        IdListDouble Centroids = kmeans.randInitialization(0,2);
        List<Integer> cluster = kmeans.Getcluster(docs, Centroids);
        System.out.println(cluster);
        Centroids = kmeans.GetCentroids(docs,cluster);
        for (int i = 0; i < NumberOfFeatures; i++){
            System.out.printf("%d th Centroid",i);
            System.out.println(Centroids.getValue(i));
        }

    }


    public Kmeans(int NumberOfFeatures, int NumberOfCluster, int NumberOfDocs) {

        this.NumberOfFeatures = NumberOfFeatures;
        this.NumberOfCluster = NumberOfCluster;
        this.NumberOfDocs = NumberOfDocs;
    }


    public IdListDouble randInitialization(int min, int max){

        IdListDouble Centroids = new IdListDouble();
//        Random r = new Random();
//        for(int i = 0; i < NumberOfCluster; i++){
//            List<Double> ini = new ArrayList<>(NumberOfFeatures);
//            for (int j = 0; j < NumberOfFeatures; j++){
//                ini.add(j, min + (max - min) * r.nextDouble());
//            }
//            System.out.println(ini);
//            Centroids.getMap().put(i, ini);
//        }
         List<Double> cen1 = new ArrayList<>();
         cen1.add(0,1.0);
         cen1.add(0,1.0);

         List<Double> cen2 = new ArrayList<>();
         cen2.add(0,-1.0);
         cen2.add(0,-1.0);

         Centroids.getMap().put(0,cen1);
         Centroids.getMap().put(1,cen2);

        return Centroids;

    }

    public List<Integer> Getcluster(IdListDouble Obs, IdListDouble Centroids){
        List<Integer> DocId_Cluster = new ArrayList<>();
        for(int i = 0; i < NumberOfDocs; i++){
            System.out.printf("---------------------%d--------------------\n",i);
            List<Double> disvector = new ArrayList<>();   //To store the distances between a Document with all the other Centroids
            for(int j = 0; j < NumberOfCluster; j++){
                System.out.println(Centroids.getValue(j));
                System.out.println(Obs.getValue(i));
                Double distance = 1 - tfidf.cosineSimilarity(Obs.getValue(i),Centroids.getValue(j));
                disvector.add(j,distance);
                System.out.println("distance: " + distance);
            }
            ArrayIndexComparator comparator = new ArrayIndexComparator(disvector);
            Integer[] indices = comparator.createIndexArray();
            Arrays.sort(indices, comparator);
            int c = indices[indices.length-1];
            System.out.println("c: " + c);
            DocId_Cluster.add(i, c);
        }
        System.out.println("Cluster Finished");
        return DocId_Cluster;
    }


    public IdListDouble GetCentroids(IdListDouble Obs, List<Integer> Cluster){

        IdListDouble Centroids = new IdListDouble();                    //CentroidID_Centroids coordinate
        for (int k = 0; k < NumberOfCluster; k++){
            System.out.printf("------------------Cluster %d -------------------\n",k);
            Double[] Centroid0 = new Double[NumberOfFeatures];
            Arrays.fill(Centroid0, 0.0);
            List<Double> Centroid = Arrays.asList(Centroid0);
            for (int m = 0; m < NumberOfFeatures; m++){
                System.out.printf("------------------Feature %d -------------------\n",m);
                int count = 0;
                Double sum = 0.0;
                for (int l = 0; l < NumberOfDocs; l++){
                    System.out.printf("------------------Doc %d -------------------\n",l);
                    if (Cluster.get(l).equals(k)){   //To decide if doc l is in cluster k
                        count ++;
                        sum += Obs.getValue(l).get(m);
                        System.out.println("sum" + sum);
                        System.out.println("count" + count);
                    }
                }
                Double avg = sum/count;
                Centroid.set(m, avg);
                System.out.println(avg);
            }
            System.out.println(Centroid);
            Centroids.putValue(k,Centroid);
        }
        return  Centroids;
    }

//    public void


}
