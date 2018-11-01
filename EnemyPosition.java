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
    return String.valueOf((int) enemyPositionX);
  }

  public String getEnemyPositionY() {
    return String.valueOf((int) enemyPositionY);
  }
}
