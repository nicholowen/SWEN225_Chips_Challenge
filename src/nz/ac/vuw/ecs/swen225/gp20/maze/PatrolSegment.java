package nz.ac.vuw.ecs.swen225.gp20.maze;

import nz.ac.vuw.ecs.swen225.gp20.persistence.level.Coordinate;

import java.awt.*;

public class PatrolSegment {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    /**
     * Stores two coordinates - a simplistic "line".
     * @param x1 X point of first coordinate
     * @param y1 Y point of first coordinate
     * @param x2 X point of second coordinate
     * @param y2 Y point of second coordinate
     */
    public PatrolSegment(int x1, int y1, int x2, int y2){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
    }

    public PatrolSegment(Coordinate first, Coordinate second){
        this.x1=first.x;
        this.x2=second.x;
        this.y1=first.y;
        this.y2=second.y;
    }


    public int getX1() {
        return x1;
    }
    public int getX2() {
        return x2;
    }
    public int getY1() {
        return y1;
    }
    public int getY2() {
        return y2;
    }
    public Point get1stPoint(){
        return new Point(x1,y1);
    }
    public Point get2ndPoint(){
        return new Point(x2,y2);
    }

    /**
     * Return the direction which the NPC must move in next to continue along this segment.
     * @param isClockwise Is the NPC moving in a clockwise circuit?
     * @return The direction which must be moved in next to traverse the segment.
     */
    public Direction getNextMove(boolean isClockwise){
        Point startPos;//Point we're moving from
        Point targetPos;//The position we're attempting to move to!
        if(isClockwise){
            targetPos=get2ndPoint();
            startPos=get1stPoint();
        } else{
            targetPos=get1stPoint();
            startPos=get2ndPoint();
        }
        if(startPos.getX()<targetPos.getX())
            return Direction.RIGHT;
        if(startPos.getX()>targetPos.getX())
            return Direction.LEFT;
        if(startPos.getY()<targetPos.getY())
            return Direction.DOWN;
        if(startPos.getY()>targetPos.getY())
            return Direction.UP;
        System.out.println("CRITICAL ERROR IN getNextMove - SEGMENT IS ACTUALLY A POINT.");
        return null;
    }

}
