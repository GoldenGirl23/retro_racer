package mp;
import java.io.Serializable;

/**
 * This class is used by both the GameServer and the Maze classes to store the clients data while playing the game.
 * The data stored will be id,score, pos_x and pos_y.
 * Player data can be changed by the set methods and returned if needed by the get methods.
 * @author ziadelharairi
 *
 */
public class PlayerState implements Serializable {
    private int id;
    private int score;
    private int pos_x;
    private int pos_y;

    /**
     * Constructs and initializes a PlayerState to(id,score,x,y)
     * @param id player id
     * @param score player score
     * @param x player position in x-axis
     * @param y player position in y-axis
     */
    public PlayerState(int id, int score, int x, int y) {
        this.id = id;
        this.score = score;
        this.pos_x = x;
        this.pos_y = y;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setPosX(int x) {
        pos_x = x;
    }
    public void setPosY(int y) {
        pos_y = y;
    }

    public int getPosX() {
        return pos_x;
    }

    public int getPoxY() {
        return pos_y;
    }

}
