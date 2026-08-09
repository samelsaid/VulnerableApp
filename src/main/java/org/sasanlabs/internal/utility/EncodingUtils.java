package org.sasanlabs.internal.utility;

public class EncodingUtils {
    public static String bytesToHex(byte[] data) {
        StringBuilder builder = new StringBuilder(data.length * 2);
        for (byte value : data) {
            builder.append(String.format("%02x", value));
        }
        return builder.toString();
    }

    // encodeBase64 used to live here. Base64 is a transport encoding with no key and no secret,
    // and the only thing a helper named like this encourages is treating it as though it were
    // encryption. Nothing calls it any more, so it is gone rather than merely unused.
}
