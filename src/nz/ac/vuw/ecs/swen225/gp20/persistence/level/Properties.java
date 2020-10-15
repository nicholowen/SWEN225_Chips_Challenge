package nz.ac.vuw.ecs.swen225.gp20.persistence.level;

public class Properties {
    public int chipsInLevel;
    public int chipsRequired;
    public int width;
    public int height;
    public int timeLimit;

    @Override
    public String toString() {
        return "Properties{" +
                "chipsInLevel=" + chipsInLevel +
                ", chipsRequired=" + chipsRequired +
                ", width=" + width +
                ", height=" + height +
                ", timeLimit=" + timeLimit +
                '}';
    }
}

