/**
 * Copyright 2016 smartbetter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.smartbetter.utilslibrary.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA加密 不可逆
 * Created by gc on 2017/1/4.
 */
public class SHA512Utils {

    private SHA512Utils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * SHA-512 加密
     * StringBuilder不支持并发操作，线性不安全的，不适合多线程中使用。但其在单线程中的性能比StringBuffer高。
     *
     * @param str
     * @return
     */
    public static String encryptSHAForBuilder(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(str.getBytes());
            byte[] cipher = digest.digest();

            for (byte b : cipher) {
                String hexStr = Integer.toHexString(b & 0xff);
                builder.append(hexStr.length() == 1 ? "0" + hexStr : hexStr);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * SHA-512 加密
     * StringBuffer支持并发操作，线性安全的，适合多线程中使用。
     *
     * @param str
     * @return
     */
    public static String encryptSHAForBuffer(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(str.getBytes());
            byte[] cipher = digest.digest();

            for (byte b : cipher) {
                String hexStr = Integer.toHexString(b & 0xff);
                buffer.append(hexStr.length() == 1 ? "0" + hexStr : hexStr);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

}