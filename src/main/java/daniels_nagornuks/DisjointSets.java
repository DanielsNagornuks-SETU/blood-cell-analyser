package daniels_nagornuks;

public class DisjointSets {

    public static int[] elements;

    public static void resetPixelArray(int size) {
        elements = new int[size];
        for (int i = 0; i < size; i++) {
            elements[i] = elements[i] | 0x80010001;
        }
    }

    public static int find(int id) {
        while(!isRoot(id)) {
            id = elements[id];
        }
        return id;
    }

    public static void quickUnion(int id1, int id2) {
        int newSize = getSize(id1) + getSize(id2);
        elements[find(id1)] = id2;
        setSize(id1, newSize);
    }

    public static boolean isRoot(int id) {
        return (elements[id] >>> 31) == 1;
    }

    public static int getSize(int id) {
        return elements[find(id)] & 0xFFFF;
    }

    public static int getHeight(int id) {
        return (elements[find(id)] << 1) >>> 17;
    }

    public static void setSize(int id, int size) {
        elements[find(id)] = (elements[find(id)] & 0xFFFF0000) | size;
    }

    public static void setHeight(int id, int height) {
        elements[find(id)] = (elements[find(id)] & 0x8000FFFF) | (height << 16);
    }

}
