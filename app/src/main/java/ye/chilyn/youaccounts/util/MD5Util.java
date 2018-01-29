package ye.chilyn.youaccounts.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * Created by Alex on 2017/5/20.
 * MD5校验的工具类
 */

public class MD5Util {

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String getStringMD5(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }
}
