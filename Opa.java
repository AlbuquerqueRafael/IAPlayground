package teste;
import java.util.HashMap;
import java.io.Serializable;

public class Opa implements Serializable {
	private static final long serialVersionUID = -5930904419691645745L;

  private HashMap<String, Object> qTable;

  public Opa(HashMap<String, Object> qTable) {
    this.qTable = qTable;
  }

  public HashMap<String, Object> getQTable() {
    return qTable;
  }

}
