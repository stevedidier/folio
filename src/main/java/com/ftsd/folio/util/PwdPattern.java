package com.ftsd.folio.util;
import java.util.regex.Pattern;
import lombok.Data;

@Data
public class PwdPattern {
    
    private static final String PASSWORD_REGEX =
    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    private static final String COMPLEX_PASSWORD_REGEX =
    "^(?:(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])|" +
    "(?=.*\\d)(?=.*[^A-Za-z0-9])(?=.*[a-z])|" +
    "(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|" +
    "(?=.*\\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))(?!.*(.)\\1{2,})" +
    "[A-Za-z0-9!~<>,;:_=?*+#.\"&§%°()\\|\\[\\]\\-\\$\\^\\@\\/]" +
    "{8,32}$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
                                    

    public static  boolean pwdValidate(String password){

        return PASSWORD_PATTERN.matcher(password).matches();
    }

}
