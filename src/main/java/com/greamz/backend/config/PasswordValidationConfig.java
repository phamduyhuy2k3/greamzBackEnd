package com.greamz.backend.config;

import org.passay.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class PasswordValidationConfig {
    @Bean
    public PasswordValidator passwordValidator(){
        Properties props = new Properties();

        InputStream inputStream = getClass()

                .getClassLoader().getResourceAsStream("passay.properties");

        try {
            props.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MessageResolver resolver = new PropertiesMessageResolver(props);

        PasswordValidator passwordValidator=new PasswordValidator(resolver,
                new LengthRule(8,16),
                new CharacterRule(EnglishCharacterData.UpperCase,1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()
        );
        return passwordValidator;
    }

}
