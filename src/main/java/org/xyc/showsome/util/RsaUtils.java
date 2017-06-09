package org.xyc.showsome.util;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RsaUtils {

    private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    /**
     * 根据输入的seedKey生成公钥和私钥
     *
     * @throws NoSuchAlgorithmException
     */
    public static HashMap<String, Object> getKeys(String seedKey) throws NoSuchAlgorithmException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            if (StringUtils.isNotEmpty(seedKey)) {
                secureRandom.setSeed(seedKey.getBytes(DEFAULT_CHARACTER_ENCODING));
            }
            keyPairGen.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            map.put("public", publicKey);
            map.put("private", privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 生成公钥和私钥
     *
     * @throws NoSuchAlgorithmException
     */
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptByPublicKey(String data, RSAPublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    public static String encryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        // 模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        //如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    public static String decryptByPublicKey(String data, RSAPublicKey publicKe) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKe);
        //模长
        int key_len = publicKe.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        //如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;

        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    public static String generateToke(String account, String password, String modulus, String public_exponent) {
        String token = "";
        try {
            StringBuilder tokenOriginBuilder = new StringBuilder();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            tokenOriginBuilder.append(account);
            tokenOriginBuilder.append(",");
            tokenOriginBuilder.append(simpleDateFormat.format(new Date()));
            tokenOriginBuilder.append(",");
            tokenOriginBuilder.append(password);
            String ming = tokenOriginBuilder.toString();

            RSAPublicKey pubKey = RsaUtils.getPublicKey(modulus, public_exponent);
            //加密后的密文
            token = RsaUtils.encryptByPublicKey(ming, pubKey);
            return token;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return token;
    }

    public static String parserToken(String token, String modulus, String private_exponent) {
        String result = "";
        try {
            RSAPrivateKey priKey = RsaUtils.getPrivateKey(modulus, private_exponent);
            result = decryptByPrivateKey(token, priKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decryptLicense(String token, String modulus, String public_exponent) {
        String result = "";
        try {
            RSAPublicKey pubKey = RsaUtils.getPublicKey(modulus, public_exponent);
            result = decryptByPublicKey(token, pubKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String generateLicense(String token, String modulus, String private_exponent) {
        String result = "";
        try {
            RSAPrivateKey priKey = RsaUtils.getPrivateKey(modulus, private_exponent);
            result = encryptByPrivateKey(token, priKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        HashMap<String, Object> keyMap = getKeys();
        RSAPublicKey publicKey = (RSAPublicKey) keyMap.get("public");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get("private");
        System.out.println(publicKey.getPublicExponent());
        System.out.println(publicKey.getModulus());
        System.out.println(privateKey.getPrivateExponent());
        System.out.println(privateKey.getModulus());
    }
}
