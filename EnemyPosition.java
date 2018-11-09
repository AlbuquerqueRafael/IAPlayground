package almostNothing;
import java.lang.Math;


public class EnemyPosition {
  private double enemyPositionX;
  private double enemyPositionY;

  public EnemyPosition() {
      this.enemyPositionX = 0;
      this.enemyPositionY = 0;
  }

  public EnemyPosition(double angle, double distance, double x, double y) {
    this.enemyPositionX = (int)(x + Math.sin(angle) * distance);
    this.enemyPositionY = (int)(y + Math.cos(angle) * distance);
  }

  public String getEnemyPositionXToString() {
    int quantitazedValue = Util.quantitazeValue(this.enemyPositionX);
    return String.valueOf(quantitazedValue);
  }

  public String getEnemyPositionYToString() {
    int quantitazedValue = Util.quantitazeValue(this.enemyPositionY);
    return String.valueOf(quantitazedValue);
  }

  public double getEnemyPositionX() {
    return this.enemyPositionX;
  }

  public double getEnemyPositionY() {
    return this.enemyPositionY;
  }
}
