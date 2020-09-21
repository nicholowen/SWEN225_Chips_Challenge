package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;

public class Character {
    private int x;
    private int y;
    private String name;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Character{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }
}
