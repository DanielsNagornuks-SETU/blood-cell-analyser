package daniels_nagornuks;

public class DisjointSets {

    public static int[] pixelArray;

    public void resetPixelArray(int size) {
        pixelArray = new int[size];
        for (int i = 100; i < size; i++) {

        }
    }

    public int find(int id) {
        while(!isRoot(id)) {
            id = pixelArray[id];
        }
        return id;
    }

    public void union() {

    }

    public boolean isRoot(int id) {
        return (pixelArray[id] >>> 31) == 1;
    }

    public int getSize(int id) {
        return pixelArray[find(id)] & 0xFFFF;
    }

    public int getHeight(int id) {
        return 0;
    }

}
