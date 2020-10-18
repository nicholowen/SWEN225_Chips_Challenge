package nz.ac.vuw.ecs.swen225.gp20.persistence.level;

import java.util.Objects;

public class Tile extends Coordinate {
    private final String terrain;
    private final String object;
    private final String color;
    private final String help;

    Tile(int x, int y, String terrain, String object, String color, String help) {
        super(x, y);
        this.terrain = terrain;
        this.object = object;
        this.color = color;
        this.help = help;
    }

    private String toLowerCaseSafe(String s) {
        if (s == null) return null;
        return s.toLowerCase();
    }

    public String getTerrain() {
        return toLowerCaseSafe(terrain);
    }

    public String getObject() {
        return toLowerCaseSafe(object);
    }

    public String getColor() {
        return toLowerCaseSafe(color);
    }

    public String getHelp() {
        return toLowerCaseSafe(help);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return Objects.equals(terrain, tile.terrain) &&
                Objects.equals(object, tile.object) &&
                Objects.equals(color, tile.color) &&
                Objects.equals(help, tile.help) &&
                Objects.equals(x, tile.x) &&
                Objects.equals(y, tile.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, terrain, object, color, help);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "terrain='" + terrain + '\'' +
                ", object='" + object + '\'' +
                ", color='" + color + '\'' +
                ", help='" + help + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
