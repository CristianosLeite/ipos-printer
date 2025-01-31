package com.conecsa.iposprinter.Utils;


public class BytesUtil {
    /**
     * Create a line of data for printing.
     * @param size  Line size
     * @param width Line width
     * @return Line data
     */
    protected static int[] createLineData(int size, int width) {
        int[] pixels = new int[width * (size + 6)];
        int k = 0;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < width; i++) {
                pixels[k++] = 0xffffffff;
            }
        }

        for (int j = 0; j < size; j++) {
            for (int i = 0; i < width; i++) {
                pixels[k++] = 0xff000000;
            }
        }

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < width; i++) {
                pixels[k++] = 0xffffffff;
            }
        }
        return pixels;
    }

    /**
     * Create a line of data for printing
     *
     * @param w    Line width
     * @param type Line type
     * @return Line data
     */
    public static byte[] initLine1(int w, int type) {
        byte[][] kk = new byte[][]{{0x00, 0x00, 0x7c, 0x7c, 0x7c, 0x00, 0x00}, {0x00, 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, 0x00}, {0x00, 0x44, 0x44, (byte) 0xff, 0x44, 0x44, 0x00}, {0x00, 0x22, 0x55, (byte) 0x88, 0x55, 0x22, 0x00}, {0x08, 0x08, 0x1c, 0x7f, 0x1c, 0x08, 0x08}, {0x08, 0x14, 0x22, 0x41, 0x22, 0x14, 0x08}, {0x08, 0x14, 0x2a, 0x55, 0x2a, 0x14, 0x08}, {0x08, 0x1c, 0x3e, 0x7f, 0x3e, 0x1c, 0x08}, {0x49, 0x22, 0x14, 0x49, 0x14, 0x22, 0x49}, {0x63, 0x77, 0x3e, 0x1c, 0x3e, 0x77, 0x63}, {0x70, 0x20, (byte) 0xaf, (byte) 0xaa, (byte) 0xfa, 0x02, 0x07}, {(byte) 0xef, 0x28, (byte) 0xee, (byte) 0xaa, (byte) 0xee, (byte) 0x82, (byte) 0xfe},};

        int ww = w / 8;

        byte[] data = new byte[13 * ww];

        int k = 0;
        for (int i = 0; i < 3 * ww; i++) {
            data[k++] = 0;
        }
        for (int i = 0; i < ww; i++) {
            data[k++] = kk[type][0];
        }
        for (int i = 0; i < ww; i++) {
            data[k++] = kk[type][1];
        }
        for (int i = 0; i < ww; i++) {
            data[k++] = kk[type][2];
        }
        for (int i = 0; i < ww; i++) {
            data[k++] = kk[type][3];
        }
        for (int i = 0; i < ww; i++) {
            data[k++] = kk[type][4];
        }
        for (int i = 0; i < ww; i++) {
            data[k++] = kk[type][5];
        }
        for (int i = 0; i < ww; i++) {
            data[k++] = kk[type][6];
        }
        for (int i = 0; i < 3 * ww; i++) {
            data[k++] = 0;
        }
        return data;
    }
}