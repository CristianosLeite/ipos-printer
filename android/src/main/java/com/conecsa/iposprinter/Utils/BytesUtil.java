package com.conecsa.iposprinter.Utils;

import android.annotation.SuppressLint;

import java.util.Random;

public class BytesUtil {
    private static final String TAG             = "BytesUtil";
    private static final int    MATRIX_DATA_ROW = 384;
    private static final int    BYTE_BIT        = 8;
    private static final int    BYTE_PER_LINE   = 48;
    // =============================================================================================
    // Fields
    // =============================================================================================
    private static final Random random = new Random();

    // =============================================================================================
    // Methods
    // =============================================================================================

    /**
     * Randomly generate black dot printing data.
     * @param lines Print paper height, unit point.
     * @return Print data
     */
    public static byte[] RandomDotData(int lines)
    {
        byte[] printData = new byte[lines * BYTE_PER_LINE];
        for (int i = 0; i < lines; i++)
        {
            byte[] lineData = new byte[BYTE_PER_LINE];
            int randData = random.nextInt(BYTE_PER_LINE);
            lineData[randData] = 0x01;
            System.arraycopy(lineData, 0, printData, i * 48, BYTE_PER_LINE);
        }

        return printData;
    }

    /**
     * Generate intermittent black block data.
     *
     * @param w : Print paper width, unit point.
     * @return Print data
     */
    public static byte[] initBlackBlock(int w)
    {
        int ww = w / 8;
        int n = ww  / 12;
        int hh = n * 24;
        byte[] data = new byte[hh * ww ];


        int k = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < 24; j++)
            {
                for (int m = 0; m < ww; m++)
                {
                    if (m / 12 == i)
                    {
                        data[k++] = (byte) 0xFF;
                    }
                    else
                    {
                        data[k++] = 0;
                    }
                }
            }
        }

        return data;
    }

    /**
     * Generate a large block of black block data.
     *
     * @param h : Black block height, unit point.
     * @param w : Black block width, unit point, multiple of 8.
     * @return Print data
     */
    public static byte[] initBlackBlock(int h, int w)
    {
        int ww = w  / 8 ;
        byte[] data = new byte[h * ww ];

        int k = 0;
        for (int i = 0; i < h; i++)
        {
            for (int j = 0; j < ww; j++)
            {
                data[k++] = (byte) 0xFF;
            }
        }

        return data;
    }

    /**
     * Generate black block data
     * @param lines Number of black block lines
     * @return Print data
     */
    public static byte[] BlackBlockData(int lines)
    {
        byte[] printData = new byte[lines * BYTE_PER_LINE];
        for (int i = 0; i < lines * BYTE_PER_LINE; i++)
        {
            printData[i] = (byte) 0xff;
        }
        return printData;
    }

    /**
     * Generate gray block data
     *
     * @param h : Height of the gray block, unit point.
     * @param w : Gray block width, unit point, multiple of 8.
     * @return Print data
     */
    public static byte[] initGrayBlock(int h, int w)
    {
        int ww = w/ 8 ;
        byte[] data = new byte[h * ww ];

        int k = 0;
        byte m = (byte) 0xAA;
        for (int i = 0; i < h; i++)
        {
            m = (byte) ~m;
            for (int j = 0; j < ww; j++)
            {
                data[k++] = m;
            }
        }

        return data;
    }

    /**
     * Convert byte array to hex string
     * @param data Byte array
     * @return Hex string
     */
    public static String getHexStringFromBytes(byte[] data)
    {
        if (data == null || data.length <= 0)
        {
            return null;
        }
        String hexString = "0123456789ABCDEF";
        int size = data.length * 2;
        StringBuilder sb = new StringBuilder(size);
        for (byte datum : data) {
            sb.append(hexString.charAt((datum & 0xF0) >> 4));
            sb.append(hexString.charAt((datum & 0x0F)));
        }
        return sb.toString();
    }

    /**
     * Single character to byte
     *
     * @param c Char to be converted
     * @return Converted byte
     */
    private static byte charToByte(char c)
    {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * Convert hexadecimal string to byte array.
     *
     * @param hexString Hexadecimal string
     * @return Byte array
     */
    @SuppressLint("DefaultLocale")
    public static byte[] getBytesFromHexString(String hexString)
    {
        if (hexString == null || hexString.isEmpty())
        {
            return null;
        }
        hexString = hexString.replace(" ", "");
        hexString = hexString.toUpperCase();
        int size = hexString.length() / 2;
        char[] hexagram = hexString.toCharArray();
        byte[] rv = new byte[size];
        for (int i = 0; i < size; i++)
        {
            int pos = i * 2;
            rv[i] = (byte) (charToByte(hexagram[pos]) << 4 | charToByte(hexagram[pos + 1]));
        }
        return rv;
    }

    /**
     * Create a line of data for printing
     * @param size Line size
     * @param width Line width
     * @return Line data
     */
    protected static int[] createLineData(int size, int width)
    {
        int[] pixels = new int[width * (size + 6)];
        int k = 0;
        for (int j = 0; j < 3; j++)
        {
            for (int i = 0; i < width; i++)
            {
                pixels[k++] = 0xffffffff;
            }
        }

        for (int j = 0; j < size; j++)
        {
            for (int i = 0; i < width; i++)
            {
                pixels[k++] = 0xff000000;
            }
        }

        for (int j = 0; j < 3; j++)
        {
            for (int i = 0; i < width; i++)
            {
                pixels[k++] = 0xffffffff;
            }
        }
        return pixels;
    }

    /**
     * Create a line of data for printing
     * @param w Line width
     * @param type Line type
     * @return Line data
     */
    public static byte[] initLine1(int w, int type)
    {
        byte[][] kk = new byte[][]{{0x00, 0x00, 0x7c, 0x7c, 0x7c, 0x00, 0x00}, {0x00, 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, 0x00}, {0x00, 0x44, 0x44, (byte) 0xff, 0x44, 0x44, 0x00}, {0x00, 0x22, 0x55, (byte) 0x88, 0x55, 0x22, 0x00}, {0x08, 0x08, 0x1c, 0x7f, 0x1c, 0x08, 0x08}, {0x08, 0x14, 0x22, 0x41, 0x22, 0x14, 0x08}, {0x08, 0x14, 0x2a, 0x55, 0x2a, 0x14, 0x08}, {0x08, 0x1c, 0x3e, 0x7f, 0x3e, 0x1c, 0x08}, {0x49, 0x22, 0x14, 0x49, 0x14, 0x22, 0x49}, {0x63, 0x77, 0x3e, 0x1c, 0x3e, 0x77, 0x63}, {0x70, 0x20, (byte) 0xaf, (byte) 0xaa, (byte) 0xfa, 0x02, 0x07}, {(byte) 0xef, 0x28, (byte) 0xee, (byte) 0xaa, (byte) 0xee, (byte) 0x82, (byte) 0xfe},};

        int ww = w / 8;

        byte[] data = new byte[13 * ww ];

        int k = 0;
        for (int i = 0; i < 3 * ww; i++)
        {
            data[k++] = 0;
        }
        for (int i = 0; i < ww; i++)
        {
            data[k++] = kk[type][0];
        }
        for (int i = 0; i < ww; i++)
        {
            data[k++] = kk[type][1];
        }
        for (int i = 0; i < ww; i++)
        {
            data[k++] = kk[type][2];
        }
        for (int i = 0; i < ww; i++)
        {
            data[k++] = kk[type][3];
        }
        for (int i = 0; i < ww; i++)
        {
            data[k++] = kk[type][4];
        }
        for (int i = 0; i < ww; i++)
        {
            data[k++] = kk[type][5];
        }
        for (int i = 0; i < ww; i++)
        {
            data[k++] = kk[type][6];
        }
        for (int i = 0; i < 3 * ww; i++)
        {
            data[k++] = 0;
        }
        return data;
    }

    /**
     * Create a line of data for printing
     * @param w Line width
     * @return Line data
     */
    public static byte[] initLine2(int w)
    {
        int ww = (w + 7) / 8;

        byte[] data = new byte[12 * ww + 8];

        data[0] = 0x1D;
        data[1] = 0x76;
        data[2] = 0x30;
        data[3] = 0x00;

        data[4] = (byte) ww;//xL
        data[5] = (byte) (ww >> 8);//xH
        data[6] = 12;  //高度13
        data[7] = 0;

        int k = 8;
        for (int i = 0; i < 5 * ww; i++)
        {
            data[k++] = 0;
        }
        for (int i = 0; i < ww; i++)
        {
            data[k++] = 0x7f;
        }
        for (int i = 0; i < ww; i++)
        {
            data[k++] = 0x7f;
        }
        for (int i = 0; i < 5 * ww; i++)
        {
            data[k++] = 0;
        }
        return data;
    }

    /**
     * Convert byte to hexadecimal.
     */
    public static String byte2hex(byte[] buffer)
    {
        StringBuilder h = new StringBuilder();

        for (byte aBuffer : buffer)
        {
            String temp = Integer.toHexString(aBuffer & 0xFF);
            if (temp.length() == 1)
            {
                temp = "0" + temp;
            }
            h.append(" ").append(temp);
        }
        return h.toString();
    }
}
