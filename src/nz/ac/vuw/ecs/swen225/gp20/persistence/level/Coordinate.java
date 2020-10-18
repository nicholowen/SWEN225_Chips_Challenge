package nz.ac.vuw.ecs.swen225.gp20.persistence.level;

public class Coordinate {
    public int x;
    public int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{x=" + x + ", y=" + y + '}';
    }
}
