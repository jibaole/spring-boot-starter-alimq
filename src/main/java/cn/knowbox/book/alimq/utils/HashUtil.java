package cn.knowbox.book.alimq.utils;

import java.util.zip.CRC32;

public class HashUtil {
    public static long crc32Code(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return crc32.getValue();
    }
}
