package teste;
import java.lang.Math;


public class EnemyPosition {
  private double enemyPositionX;
  private double enemyPositionY;

  public EnemyPosition(double angle, double distance, double x, double y) {
    this.enemyPositionX = (int)(x + Math.sin(angle) * distance);
    this.enemyPositionY = (int)(y + Math.cos(angle) * distance);
  }

  public String getEnemyPositionX() {
    int quantitazedValue = Util.quantitazeValue(this.enemyPositionX);
    return String.valueOf(quantitazedValue);
  }

  public String getEnemyPositionY() {
    int quantitazedValue = Util.quantitazeValue(this.enemyPositionY);
    return String.valueOf(quantitazedValue);
  }
}
