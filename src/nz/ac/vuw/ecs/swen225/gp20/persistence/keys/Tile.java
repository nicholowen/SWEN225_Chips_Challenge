package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;

public class Tile {
    private int x;
    private int y;
    private String name;
    private String color;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public String getColor(){
        return color;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
