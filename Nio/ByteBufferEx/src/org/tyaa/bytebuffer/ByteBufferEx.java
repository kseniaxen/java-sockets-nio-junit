package org.tyaa.bytebuffer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 *
 * @author Юрий
 */
public class ByteBufferEx
{
    /**
     * @param args the command line arguments
     */
    // private static CharBuffer byteBuffer;
    private static ByteBuffer byteBuffer;
    
    public static void main(String[] args) throws UnsupportedEncodingException
    {
        // byteBuffer = CharBuffer.allocate(10);
        byteBuffer = ByteBuffer.allocate(256);
            byteBuffer.put("Hello".getBytes("utf-16"));
            // byteBuffer.put("Hello");
        getByteBufState();
        byteBuffer.flip();
        getByteBufState();
        byteBuffer.getChar();
        byteBuffer.getChar();
        byteBuffer.getChar();
        byteBuffer.getChar();
        getByteBufState();
        byteBuffer.compact();
        // byteBuffer.limit(8);
        getByteBufState();
        byteBuffer.put("abcde".getBytes("utf-16"));
        getByteBufState();
        byteBuffer.flip();
        getByteBufState();
        // byteBuffer.limit(2);
        getByteBufState();
        System.out.println(byteBuffer.getChar() + "" + byteBuffer.getChar());
        getByteBufState();
        byteBuffer.clear();
        getByteBufState();
    }
    
    private static void getByteBufState()
    {
        System.out.println("pos = " + byteBuffer.position()
            + "  lim = " + byteBuffer.limit()
            + "  cap = " + byteBuffer.capacity());
    }
    
}
