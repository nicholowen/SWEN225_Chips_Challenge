package nz.ac.vuw.ecs.swen225.gp20.persistence.keys;

import java.util.Objects;

public class Tile extends Coordinate {
    private String name;
    private String color;
    private String help;

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
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return Objects.equals(name, tile.name) &&
                Objects.equals(color, tile.color) &&
                Objects.equals(help, tile.help) &&
                Objects.equals(x, tile.x) &&
                Objects.equals(y, tile.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name, color, help);
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
