package com.gjw.common.utils.img;

/**
 * @author gaojunwei
 * @date 2019/9/17 13:31
 */
public class RGBTriple {
    public final byte[] channels;

    public RGBTriple() {
        channels = new byte[3];
    }

    public RGBTriple(int R, int G, int B) {
        channels = new byte[]{(byte) R, (byte) G, (byte) B};
    }
}