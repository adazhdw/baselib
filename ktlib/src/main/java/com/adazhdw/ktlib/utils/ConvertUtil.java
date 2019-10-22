package com.adazhdw.ktlib.utils;

public class ConvertUtil {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 把byte[]数组转换成十六进制字符串表示形式
     * @param tmp 要转换的byte[]
     * @return 十六进制字符串表示形式
     */
    public static String byteToHexString(byte[] tmp) {
        char[] str = new char[(16 << 1)];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte byte0 = tmp[i];
            str[k++] = HEX_CHAR[byte0 >>> 4 & 0xf];
            str[k++] = HEX_CHAR[byte0 & 0xf];
        }
        return new String(str);
    }


    /**
     * 方法二：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexFun2(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }

    /**
     * 方法三：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", b & 0xff));
        }

        return buf.toString();
    }


    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] hexToBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }


}
