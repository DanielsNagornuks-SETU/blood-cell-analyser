package daniels_nagornuks;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PixelArrayTest {

    PixelArray pixelArray;

    @BeforeEach
    void setUp() {
        pixelArray = new PixelArray(20);
    }

    @AfterEach
    void tearDown() {
        pixelArray = null;
    }

    @Test
    void testSettingColors() {
        pixelArray.setRed(0);
        assertTrue(pixelArray.isRed(0));
        pixelArray.setWhite(1);
        assertTrue(pixelArray.isWhite(1));
        pixelArray.setPurple(2);
        assertTrue(pixelArray.isPurple(2));
    }

    @Test
    void testUnion() {
        pixelArray.union(0,1);
        pixelArray.union(0,2);
        pixelArray.union(3,4);
        assertEquals(pixelArray.find(0), pixelArray.find(1));
        assertEquals(pixelArray.find(1), pixelArray.find(2));
        assertEquals(pixelArray.find(3), pixelArray.find(4));
        assertEquals(3, pixelArray.getSize(0));
        assertEquals(2, pixelArray.getSize(3));
        assertNotEquals(pixelArray.find(0), pixelArray.getSize(3));
        pixelArray.union(0,3);
        assertEquals(pixelArray.find(0), pixelArray.find(3));
        assertEquals(5, pixelArray.getSize(2));
    }

    @Test
    void testFind() {
        pixelArray.union(0,1);
        pixelArray.union(0,2);
        pixelArray.union(3,4);
        int oldRoot = pixelArray.find(0); // This should be the new root after union
        pixelArray.union(0,3);
        assertEquals(oldRoot, pixelArray.find(0));
        pixelArray.union(3,5);
        assertEquals(oldRoot, pixelArray.find(0));
        pixelArray.union(10,11);
        pixelArray.union(10,12);
        pixelArray.union(10,13);
        pixelArray.union(13,14);
        pixelArray.union(14,15);
        pixelArray.union(15,16);
        oldRoot = pixelArray.find(15); // This should be the new root after union
        pixelArray.union(0,16);
        assertEquals(oldRoot, pixelArray.find(0));
    }

}
