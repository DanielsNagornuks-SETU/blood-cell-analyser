package daniels_nagornuks;

public class PixelArray {

    private final int[] pixels;

    public PixelArray(int size) {
        pixels = new int[size];
        for (int i = 0; i < size; i++) {
            pixels[i] = 0x80000001;
        }
    }

    public void processPixel(int id, int idBelow, int idToRight) {
        if (getColor(id) == getColor(idToRight)) {
            union(id, idToRight);
        }
        if (getColor(id) == getColor(idBelow)) {
            union(id, idBelow);
        }
    }

    public int[] getPixels() {
        return pixels;
    }

    private boolean isRoot(int id) {
        return (pixels[id] >>> 31) == 1;
    }

    private int find(int id) {
        while(!isRoot(id)) {
            int childId = id;
            id = pixels[id];
            if(!isRoot(id)) {
                pixels[childId] = pixels[id];
            }
        }
        return id;
    }

    private void union(int id1, int id2) {
        int root1 = find(id1);
        int root2 = find(id2);
        if (root1 == root2 || getColor(root1) != getColor(root2)) {
            return;
        }
        int size1 = getSize(root1);
        int size2 = getSize(root2);
        if (size1 <= size2) {
            pixels[root1] = root2;
        } else {
            pixels[root2] = root1;
        }
        setSize(root1, size1);
    }

    public boolean isWhite(int id) {
        return getColor(find(id)) == 0;
    }

    public boolean isRed(int id) {
        return getColor(find(id)) == 1;
    }

    public boolean isPurple(int id) {
        return getColor(find(id)) == 2;
    }

    private int getColor(int id) {
        int root = find(id);
        return (root << 1) >>> 30;
    }

    public void setWhite(int id) {
        setColor(id, 0);
    }

    public void setRed(int id) {
        setColor(id, 1);
    }

    public void setPurple(int id) {
        setColor(id, 2);
    }

    private void setColor(int id, int color) {
        int root = find(id);
        pixels[root] = (pixels[root] & 0x90000000) | (color << 29);
    }

    private void setSize(int id, int size) {
        int root = find(id);
        pixels[root] = (pixels[root] & 0xE0000000) | size;
    }

    private int getSize(int id) {
        return pixels[find(id)] & 0x1FFFFFFF;
    }

}
