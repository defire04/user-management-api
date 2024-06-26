package com.example.util.handler;

import com.example.util.annotation.AdultAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class AdultAgeValidator implements ConstraintValidator<AdultAge, Date> {

    @Value("${user.min.age}")
    private int minUserAge;

    @Override
    public void initialize(AdultAge constraintAnnotation) {
    }

    @Override
    public boolean isValid(Date birthDate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null) {
            return true;
        }

        LocalDate today = LocalDate.now();
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(birthLocalDate, today);
        return period.getYears() >= minUserAge;
    }
}