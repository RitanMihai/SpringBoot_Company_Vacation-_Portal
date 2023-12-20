package com.lab.model.service;

import com.lab.model.model.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class UserValidatorService implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object userEntity, Errors errors) {
        UserEntity user = (UserEntity) userEntity;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.isUsernameEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.isPasswordEmpty");

        /* Valid email regex pattern - https://owasp.org/www-community/OWASP_Validation_Regex_Repository */
        /* Typical email format: email@domain.com */
        String emailRegexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        /*
           at least 8 digits {8,}
           at least one number (?=.*\d)
           at least one lowercase (?=.*[a-z])
           at least one uppercase (?=.*[A-Z])
           at least one special character (?=.*[@#$%^&+=])
           No space [^\s]
        */
        String passwordRegexPattern = "^(?:(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*)[^\s]{8,}$";

        Boolean isValidUserLength = user.getUsername().length() > 2 && user.getUsername().length() < 32;
        Boolean isValidEmail = user.getEmail().matches(emailRegexPattern);
        Boolean isValidPassword = user.getPassword().matches(passwordRegexPattern);
        Boolean arePasswordsTheSame = user.getPassword().equals(user.getRepeatPassword());

        if(!isValidUserLength)
            errors.rejectValue("username", "user.isValidUserLength");
        if(!isValidEmail)
            errors.rejectValue("email", "user.isValidEmail");
        if(!isValidPassword)
            errors.rejectValue("password", "user.isValidPassword");
        if(!arePasswordsTheSame)
            errors.rejectValue("repeatPassword", "user.isPasswordTheSame");

    }
}
