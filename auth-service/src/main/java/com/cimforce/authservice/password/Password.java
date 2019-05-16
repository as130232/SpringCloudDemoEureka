package com.cimforce.authservice.password;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Password {
	public static PasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final int workload = 12;
    
    /**
	 * 將密碼(明碼)加密
	 * @author Charles
	 * @date 2017年9月2日 下午2:49:04
	 * @param plaintextPassword
	 * @return String
	 */
    public static String encrypt(String plaintextPassword) {
    	//BCrypt內部已用SecureRandom隨機產生內部salt，所以每次加密結果都不一樣
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(plaintextPassword, salt);
        return(hashed_password);
    }
    
    /**
	 * 驗證密碼(明碼)與加密過的密碼是否一樣
	 * @author Charles
	 * @date 2017年9月2日 下午2:49:54
	 * @param param1 type 描述
	 * @return boolean
	 */
    public static boolean isValidatePassword(String password, String hashPassword) {
        return BCrypt.checkpw(password, hashPassword);
    }
    
    public static String createRandomPassword() {
    	StringBuilder result = new StringBuilder(); 
    	final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    final int N = alphabet.length();

	    Random r = new Random();
	    int length = 10;
	    
	    for (int i = 0; i < length; i++) {
	    	result.append(alphabet.charAt(r.nextInt(N)));
	    }
    	return result.toString();
    }
    
    /**
	 * 測試
	 * @author charles.chen
	 * @date 2018年1月25日 下午4:36:53
	 */
	public static void main(String[] args) {
		String tmp = "0000";	//$2a$12$dQevIWsWVAVrkzFDk6Z2LOPJWpsNGEEbkFqbltCrQ3kYXuLn2HEpC
		String encryptor  = encrypt(tmp);
		System.out.println("tmp:" + tmp + ", encryptor:" + encryptor );
		boolean isValidatePassword = isValidatePassword( tmp, encryptor);
		System.out.println("isValidatePassword:" + isValidatePassword);
	}
}
