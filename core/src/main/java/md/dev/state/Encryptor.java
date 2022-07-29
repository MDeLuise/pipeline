package md.dev.state;

import md.dev.state.exception.HandleStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

class Encryptor {
    private static final Logger LOG = LoggerFactory.getLogger(Encryptor.class);


    public static String encrypt(String secretKey, String saltValue, String strToEncrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(
                    secretKey.toCharArray(),
                    saltValue.getBytes(),
                    65536,
                    256
            );
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeyObj = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeyObj, ivSpec);
            return Base64.getEncoder().
                    encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidAlgorithmParameterException |
                InvalidKeySpecException |
                InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException |
                NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            LOG.warn("error while encrypting string", e);
            throw new HandleStateException();
        }
    }


    public static String decrypt(String secretKey, String saltValue, String strToDecrypt) {
        try {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(
                    secretKey.toCharArray(),
                    saltValue.getBytes(),
                    65536,
                    256
            );
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeyObj = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeyObj, ivSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (InvalidAlgorithmParameterException |
                InvalidKeyException |
                NoSuchAlgorithmException |
                InvalidKeySpecException |
                BadPaddingException |
                IllegalBlockSizeException |
                NoSuchPaddingException e) {
            LOG.warn("error while decrypting string", e);
            throw new HandleStateException();
        }
    }
}
