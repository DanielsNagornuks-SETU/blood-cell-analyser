package daniels_nagornuks;

public class DisjointSets {

    public static int[] elements;

    public static void resetElements(int size) {
        elements = new int[size];
        for (int i = 0; i < size; i++) {
            elements[i] = 0x80010001;
        }
    }

    public static int find(int id) {
        while(!isRoot(id)) {
            int childId = id;
            id = elements[id];
            if(!isRoot(id)) {
                elements[childId] = elements[id];
            }
        }
        return id;
    }

    public static void smartUnion(int id1, int id2) {
        int root1 = find(id1);
        int root2 = find(id2);
        if (root1 == root2) {
            return;
        }
        int size1 = getSize(id1);
        int size2 = getSize(id2);
        int height1 = getHeight(id1);
        int height2 = getHeight(id2);
        boolean joinId1ToId2 = size1 == size2 ? height1 <= height2 : size1 <= size2;
        int newSize = size1 + size2;
        int newHeight;
        if (joinId1ToId2) {
            newHeight = height1 < height2 ? height2 : height1 + 1;
            elements[root1] = root2;
        } else {
            newHeight = height1 > height2 ? height1 : height2 + 1;
            elements[root2] = root1;
        }
        setSize(id1, newSize);
        setHeight(id1, newHeight);
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
