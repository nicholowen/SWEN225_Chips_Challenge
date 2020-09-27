package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;

import java.util.Objects;

public class Tile {
    private int x;
    private int y;
    private String name;
    private String color;
    private String help;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name.toLowerCase();
    }

    public String getColor() {
        return color.toLowerCase();
    }

    public String getHelp() {
        return help;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tile tile = (Tile) o;

        if (x != tile.x) return false;
        if (y != tile.y) return false;
        if (!Objects.equals(name, tile.name)) return false;
        return Objects.equals(color, tile.color);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", help='" + help + '\'' +
                '}';
    }
}
