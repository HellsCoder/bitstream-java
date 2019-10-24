package com.darkflame.utils.bitstream;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the BitOutputStream class.
 */
public class BitOutputStreamTests {

    // -- Construction Tests

    /**
     * Test that we can actually create the output stream and that the data
     * is correct (i.e., data used is 0)
     */
    @Test
    public void testDefaultConstruction_ValidData() {

        // Create the stream
        BitOutputStream bitOutputStream = new BitOutputStream();
        Assert.assertNotNull(bitOutputStream);

        // Check the size (NOTE: this is not the buffer size, this is how much data we have used)
        Assert.assertEquals(0, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(0, bitOutputStream.getNumBytesUsed());

        // Check the size of the buffer
        Assert.assertEquals(32, bitOutputStream.getBufferLength());
    }

    /**
     * Test that we can actually create the output stream from a byte array and that
     * if the byte array is smaller than the DEFAULT_BYTE_BUFFER_SIZE, the data is correct
     */
    @Test
    public void testByteArrayConstruction_LessThanDefaultSize() {

        // Create the stream
        byte[] data = new byte[3];
        BitOutputStream bitOutputStream = new BitOutputStream(data);
        Assert.assertNotNull(bitOutputStream);

        // Check the size of the data
        Assert.assertEquals(24, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(3, bitOutputStream.getNumBytesUsed());

        // Check the size of the buffer
        Assert.assertEquals(32, bitOutputStream.getBufferLength());
    }

    /**
     * Test that we can actually create the output stream from a byte array and that
     * if the byte array is larger than the DEFAULT_BYTE_BUFFER_SIZE, the data is correct
     */
    @Test
    public void testByteArrayConstruction_MoreThanDefaultSize() {

        // Create the stream
        byte[] data = new byte[40];
        BitOutputStream bitOutputStream = new BitOutputStream(data);
        Assert.assertNotNull(bitOutputStream);

        // Check the size of the data
        Assert.assertEquals(320, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(40, bitOutputStream.getNumBytesUsed());

        // Check the size of the buffer
        Assert.assertEquals(40, bitOutputStream.getBufferLength());
    }

    /**
     * Test that we can actually create the output stream from a byte array and
     * a length of less than the full array.
     */
    @Test
    public void testByteArrayAndLengthConstruction_ValidData() {

        // Create the stream
        byte[] data = {0x00, 0x01, 0x02, 0x03, 0x04};
        BitOutputStream bitOutputStream = new BitOutputStream(data, 3);
        Assert.assertNotNull(bitOutputStream);

        // Check the size of the data
        Assert.assertEquals(24, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(3, bitOutputStream.getNumBytesUsed());

        // Check the size of the buffer
        Assert.assertEquals(32, bitOutputStream.getBufferLength());

        // Verify the data
        Assert.assertEquals((byte) 0x00, bitOutputStream.getDataAtIndex(0));
        Assert.assertEquals((byte) 0x01, bitOutputStream.getDataAtIndex(1));
        Assert.assertEquals((byte) 0x02, bitOutputStream.getDataAtIndex(2));
    }

    /**
     * Tests that the correct exception is thrown when we try to create an output stream
     * from a non-existent byte array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayAndLengthConstruction_NullDataZeroLength() {

        // Create the stream
        BitOutputStream bitOutputStream = new BitOutputStream(null, 0);
    }

    /**
     * Tests that the correct exception is thrown when we try to create an output stream
     * from a non-existent byte array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayAndLengthConstruction_NullDataNonZeroLength() {

        // Create the stream
        BitOutputStream bitOutputStream = new BitOutputStream(null, 10);
    }

    /**
     * Tests that the correct exception is thrown when we try to create an output stream
     * from a non-existent byte array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testByteArrayConstructionLength_ExceedsData() {

        // Create the stream
        byte[] data = {0x00, 0x01, 0x02};
        BitOutputStream bitOutputStream = new BitOutputStream(data, 10);
    }

    /**
     * Tests a valid copy construction of an output stream.
     */
    @Test
    public void testBitStreamCopyConstruction_ValidData() {

        // Create the stream
        byte[] data = {0x00, 0x01, 0x02};
        BitOutputStream bitOutputStream1 = new BitOutputStream(data);
        BitOutputStream bitOutputStream2 = new BitOutputStream(bitOutputStream1);

        // Check our data
        Assert.assertEquals(bitOutputStream2.getNumBitsUsed(), bitOutputStream1.getNumBitsUsed());
        Assert.assertEquals(bitOutputStream2.getNumBytesUsed(), bitOutputStream1.getNumBytesUsed());

        // Validate that the data is correct
        Assert.assertEquals(32, bitOutputStream2.getBufferLength());
        Assert.assertEquals(bitOutputStream2.getDataAtIndex(0), data[0]);
        Assert.assertEquals(bitOutputStream2.getDataAtIndex(1), data[1]);
        Assert.assertEquals(bitOutputStream2.getDataAtIndex(2), data[2]);
    }



    // -- BitOutputStream.writeBits(..) Tests

    /**
     * Tests that if we write a zero byte to the output stream that we will have used exactly
     * 1 byte (8 bits).
     */
    @Test
    public void testWriteBits_WriteZeroByte() {

        // Create the stream
        BitOutputStream bitOutputStream = new BitOutputStream();

        // Write our data
        byte[] data = {0x0};
        bitOutputStream.writeBits(data, 8, false);

        // Check our data
        Assert.assertEquals(8, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(1, bitOutputStream.getNumBytesUsed());

        // Verify that our data is valid
        Assert.assertEquals(32, bitOutputStream.getBufferLength());
        Assert.assertEquals((byte) 0x00, bitOutputStream.getDataAtIndex(0));
    }

    /**
     * Tests that if we write a one byte to the output stream that we will have used exactly
     * 1 byte (8 bits).
     */
    @Test
    public void testWriteBits_WriteOneByte() {

        // Create the stream
        BitOutputStream bitOutputStream = new BitOutputStream();

        // Write our data
        byte[] data = {0x1};
        bitOutputStream.writeBits(data, 8, true);

        // Check our data
        Assert.assertEquals(8, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(1, bitOutputStream.getNumBytesUsed());

        // Verify that our data is valid
        Assert.assertEquals(32, bitOutputStream.getBufferLength());
        Assert.assertEquals((byte) 0x01, bitOutputStream.getDataAtIndex(0));
    }

    /**
     * Tests that if we write a single bit (1) to the BitOutputStream that we will have used exactly
     * 1 byte (1 bit).
     */
    @Test
    public void testWriteBits_WriteOneBitLeftAligned() {

        // Create the stream
        BitOutputStream bitOutputStream = new BitOutputStream();

        // Write our data
        byte[] data = {0x01};
        bitOutputStream.writeBits(data, 1, false);

        // Check our data
        Assert.assertEquals(1, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(1, bitOutputStream.getNumBytesUsed());

        // Verify that our data is valid
        Assert.assertEquals(32, bitOutputStream.getBufferLength());
        Assert.assertEquals((byte) 0x01, bitOutputStream.getDataAtIndex(0));
    }

    /**
     * Tests that if we write a single right-aligned bit (1) to the BitOutputStream
     * that we will have used exactly 1 byte (1 bit).
     */
    @Test
    public void testWriteBits_WriteOneBitRightAligned() {

        // Create the stream
        BitOutputStream bitOutputStream = new BitOutputStream();

        // Write our data
        byte[] data = {0x01};
        bitOutputStream.writeBits(data, 1, true);

        // Check our data
        Assert.assertEquals(1, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(1, bitOutputStream.getNumBytesUsed());

        // Verify that our data is valid
        Assert.assertEquals(32, bitOutputStream.getBufferLength());
        Assert.assertEquals((byte) 0x80, bitOutputStream.getDataAtIndex(0));
    }

    /**
     * Tests that if we write nine left-aligned bits to the BitOutputStream that we will
     * have used exactly 2 bytes (9 bits).
     */
    @Test
    public void testWriteBits_WriteNineBitsLeftAligned() {

        // Create the stream
        BitOutputStream bitOutputStream = new BitOutputStream();

        // Write our data
        byte[] data = {0x01, 0x03};
        bitOutputStream.writeBits(data, 9, false);

        // Check our data
        Assert.assertEquals(9, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(2, bitOutputStream.getNumBytesUsed());

        // Verify that our data is valid
        Assert.assertEquals(32, bitOutputStream.getBufferLength());
        Assert.assertEquals((byte) 0x01, bitOutputStream.getDataAtIndex(0));
        Assert.assertEquals((byte) 0x03, bitOutputStream.getDataAtIndex(1));
    }

    /**
     * Tests that if we write nine right-aligned bits to the BitOutputStream that we will
     * have used exactly 2 bytes (9 bits).
     */
    @Test
    public void testWriteBits_WriteNineBitsRightAligned() {

        // Create the stream
        BitOutputStream bitOutputStream = new BitOutputStream();

        // Write our data
        byte[] data = {0x01, 0x03};
        bitOutputStream.writeBits(data, 9, true);

        // Check our data
        Assert.assertEquals(9, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(2, bitOutputStream.getNumBytesUsed());

        // Verify that our data is valid
        Assert.assertEquals(32, bitOutputStream.getBufferLength());
        Assert.assertEquals((byte) 0x01, bitOutputStream.getDataAtIndex(0));
        Assert.assertEquals((byte) 0x80, bitOutputStream.getDataAtIndex(1));
    }



    // -- BitOutputStream.write(int) Tests

    /**
     * Test that the write method works as intended.
     */
    @Test
    public void testWrite_OneValue() {

        BitOutputStream bitOutputStream = new BitOutputStream();
        bitOutputStream.write(1);

        Assert.assertEquals(8, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(1, bitOutputStream.getNumBytesUsed());
        Assert.assertEquals(1, bitOutputStream.getDataAtIndex(0));
    }



    // -- BitOutputStream.write0() Tests

    /**
     * Tests that the write 0 bit method works properly for a single bit
     */
    @Test
    public void testWrite0_SingleTime() {

        BitOutputStream bitOutputStream = new BitOutputStream();
        bitOutputStream.write0();

        Assert.assertEquals(1, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(1, bitOutputStream.getNumBytesUsed());
        Assert.assertEquals(0, bitOutputStream.getDataAtIndex(0));
    }

    /**
     * Tests that the write 0 bit method works properly for multiple bits
     */
    @Test
    public void testWrite0_MultipleTimes() {

        BitOutputStream bitOutputStream = new BitOutputStream();
        bitOutputStream.write0();
        bitOutputStream.write0();

        Assert.assertEquals(2, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(1, bitOutputStream.getNumBytesUsed());
        Assert.assertEquals(0, bitOutputStream.getDataAtIndex(0));
    }



    // -- BitOutputStream.write1() Tests

    /**
     * Tests the the write 1 bit method works properly for a single bit.
     */
    @Test
    public void testWrite1_SingleTime() {

        BitOutputStream bitOutputStream = new BitOutputStream();
        bitOutputStream.write1();

        Assert.assertEquals(1, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(1, bitOutputStream.getNumBytesUsed());
        Assert.assertEquals((byte) 0x80, bitOutputStream.getDataAtIndex(0));
    }

    /**
     * Tests that the the write 1 bit method works properly for multiple bits.
     */
    @Test
    public void testWrite1_MultipleTimes() {

        BitOutputStream bitOutputStream = new BitOutputStream();
        bitOutputStream.write1();
        bitOutputStream.write1();

        Assert.assertEquals(2, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(1, bitOutputStream.getNumBytesUsed());
        Assert.assertEquals((byte) 0xC0, bitOutputStream.getDataAtIndex(0));
    }

    /**
     * Tests that the write 1 bit method works property for multiple bits an an extra
     * byte.
     */
    @Test
    public void testWrite1_MultipleTimesWithByteAtTheEnd() {

        BitOutputStream bitOutputStream = new BitOutputStream();
        bitOutputStream.write1();
        bitOutputStream.write1();
        bitOutputStream.write(1);

        Assert.assertEquals(10, bitOutputStream.getNumBitsUsed());
        Assert.assertEquals(2, bitOutputStream.getNumBytesUsed());
        Assert.assertEquals((byte) 0xC0, bitOutputStream.getDataAtIndex(0));
        Assert.assertEquals((byte) 0x40, bitOutputStream.getDataAtIndex(1));
    }



    // TODO: Add Remaining Tests (Should Work due to using the underlying methods)
}
