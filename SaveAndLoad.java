package teste;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import robocode.*;


public class SaveAndLoad {

  public static HashMap<String, ArrayList<Double>> load(String filename) {
    HashMap<String, ArrayList<Double>> auxQTable;

    try {
      FileInputStream fos = new FileInputStream(filename);
      ObjectInputStream ois = new ObjectInputStream(fos);
      auxQTable = (HashMap<String, ArrayList<Double>>) ois.readObject();
      ois.close();
    } catch(Exception e) {
      System.out.println("vem");
      System.out.println(e.getMessage());
      auxQTable = new HashMap<String, ArrayList<Double>>();
    }

    return auxQTable;
  }

  public static void save(HashMap<String, ArrayList<Double>> qTable, String filename) {
    try {
      RobocodeFileOutputStream fos = new RobocodeFileOutputStream(filename);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(qTable);
      oos.close();
    } catch(Exception e) {
      System.out.println("vem");
      System.out.println(e.getMessage());
    }
  }
}
