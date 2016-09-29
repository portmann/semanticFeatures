package ch.lgt.ming.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ming Deng on 9/27/2016.
 */
public class DataTime {
    public static void main(String[] args) {

        /**
         * This part of code converts the date information stored in fullMetaForMing.csv to DataTime.ser
        * */

//		File file = new File("data/corpus4/fullMetaForMing.csv");
//		Scanner input = new Scanner(file);
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		Map<Integer,Date> map = new HashMap<>();
//		input.nextLine();
//		for (int i = 1; i < 503859; i++){
//			String line = input.next();
//			String[] strings = line.split(",");
//			System.out.println(strings[0]);
//			Integer index = Integer.valueOf(strings[0]);
//			System.out.println(strings[1]);
//			Date date = format.parse(strings[1]);
//			System.out.println(index);
//			System.out.println(date);
//			map.put(index,date);
//			System.out.println("ture");
//		}
//		ObjectOutputStream objectOutputStream;
//		try {
//			objectOutputStream = new ObjectOutputStream(new FileOutputStream("data/corpus4/DataTime.ser"));
//			objectOutputStream.writeObject(map);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

        /**
         * This part of code reads the date information from DataTime.ser to Map<Integer, Date> map
         * */

        Map<Integer,Date> map = new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("data/corpus4/DataTime.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            map = (Map<Integer, Date>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(map);
    }
}
