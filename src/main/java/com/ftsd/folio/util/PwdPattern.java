package com.ftsd.folio.util;
import java.util.regex.Pattern;
import lombok.Data;

@Data
public class PwdPattern {
    
    private static final String PASSWORD_REGEX =
    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    //emailAddress = "username@domain.com";
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    // private static final String COMPLEX_PASSWORD_REGEX =
    // "^(?:(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])|" +
    // "(?=.*\\d)(?=.*[^A-Za-z0-9])(?=.*[a-z])|" +
    // "(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|" +
    // "(?=.*\\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))(?!.*(.)\\1{2,})" +
    // "[A-Za-z0-9!~<>,;:_=?*+#.\"&§%°()\\|\\[\\]\\-\\$\\^\\@\\/]" +
    // "{8,32}$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
                                    

    public static  boolean pwdValidate(String password){

        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static  boolean emailValidate(String email){

        return EMAIL_PATTERN.matcher(email).matches();
    }

}
