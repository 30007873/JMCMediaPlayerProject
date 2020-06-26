package utils;

import constants.JMCConstants;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JMCUtils {
	
	private static final Logger log = Logger.getLogger(JMCUtils.class.getName());

	private static KeySpec keySpec;
	private static SecretKeyFactory secretKeyFactory;
	private static Cipher cipher;
	private static byte[] byteArray;
	private static String crypticHashkey;
	private static SecretKey key;
	// singleton bean - static instance
	private static JMCUtils jmcUtilsInstance;

	// singleton bean - private constructor
	private JMCUtils() throws Exception {
		// get salt hash key
		crypticHashkey = JMCConstants.JMC_ENCRYPTION_SALTED_KEY;
		// get salt hash key bytearray
		byteArray = crypticHashkey.getBytes(JMCConstants.ENCODING_UTF);
		// get DESedeKeySpec from bytearray of salted hash key
		keySpec = new DESedeKeySpec(byteArray);
		// get encryption transformation algorithm secret keym factory
		secretKeyFactory = SecretKeyFactory.getInstance(JMCConstants.JMC_ENCRYPTION_TRANSFORMATION_ALGORITHM);
		// get encryption transformation algorithm cipher
		cipher = Cipher.getInstance(JMCConstants.JMC_ENCRYPTION_TRANSFORMATION_ALGORITHM);
		// create secret from keySpec and secret key factory
		key = secretKeyFactory.generateSecret(keySpec);
	}

	/**
	 * Encode a string using Base64 encoding and cipher
	 *
	 * @param string
	 * @return
	 */
	public String encode(String string) {
		String encryptedString = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = string.getBytes(JMCConstants.ENCODING_UTF);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = new String(Base64.getEncoder().encode((encryptedText)));
		} catch (Exception e) {
			log.info(e.getMessage() + " :: " +  Level.SEVERE);
		}
		return encryptedString;
	}

	/**
	 * Decode a string using Base64 encoding and cipher
	 *
	 * @param string
	 * @return
	 */
	public String decode(String string) {
		String decryptedText = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptedText = Base64.getDecoder().decode(string);
			byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText = new String(plainText);
		} catch (Exception e) {
			log.info(e.getMessage() + " :: " +  Level.SEVERE);
		}
		return decryptedText;
	}
	
	// singleton bean - static method logic to return only one bean
	public static JMCUtils getInstance() throws Exception {
		if(jmcUtilsInstance == null) {
			jmcUtilsInstance = new JMCUtils();
		}
		return jmcUtilsInstance;
	}
}
