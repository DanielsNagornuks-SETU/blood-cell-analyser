package daniels_nagornuks;

public class BloodCellCluster {

    private int startPosX = -1;
    private int startPosY = -1;

    private int endPosX = -1;
    private int endPosY = -1;

    private String type;

    private int numCells = -1;

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

}
