package daniels_nagornuks;

import java.util.Objects;

public class BloodCellCluster {

    private int startPosX = -1;
    private int startPosY = -1;

    private int endPosX = -1;
    private int endPosY = -1;

    private String type;

    private int numCells = 0;

    private int rootPixel;

    public BloodCellCluster(String type, int rootPixel) {
        this.type = type;
        this.rootPixel = rootPixel;
    }

    public int getRootPixel() {
        return rootPixel;
    }

    public void setRootPixel(int rootPixel) {
        this.rootPixel = rootPixel;
    }

    public int getStartPosX() {
        return startPosX;
    }

    public void setStartPosX(int startPosX) {
        this.startPosX = startPosX;
    }

    public int getStartPosY() {
        return startPosY;
    }

    public void setStartPosY(int startPosY) {
        this.startPosY = startPosY;
    }

    public int getEndPosX() {
        return endPosX;
    }

    public void setEndPosX(int endPosX) {
        this.endPosX = endPosX;
    }

    public int getEndPosY() {
        return endPosY;
    }

    public void setEndPosY(int endPosY) {
        this.endPosY = endPosY;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumCells() {
        return numCells;
    }

    public void setNumCells(int numCells) {
        this.numCells = numCells;
    }

    public int getWidth() {
        return endPosX - startPosX + 1;
    }

    public int getHeight() {
        return endPosY - startPosY + 1;
    }

    @Override
    public String toString() {
        return "BloodCellCluster{" +
                "startPosX=" + startPosX +
                ", startPosY=" + startPosY +
                ", endPosX=" + endPosX +
                ", endPosY=" + endPosY +
                ", type='" + type + '\'' +
                ", numCells=" + numCells +
                ", rootPixel=" + rootPixel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodCellCluster that = (BloodCellCluster) o;
        return startPosX == that.startPosX && startPosY == that.startPosY && endPosX == that.endPosX && endPosY == that.endPosY && numCells == that.numCells && rootPixel == that.rootPixel && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosX, startPosY, endPosX, endPosY, type, numCells, rootPixel);
    }

}
