package com.example.mapper.util;

import com.example.exception.mapper.util.FieldsFailUpdateException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class FieldsUpdaterMapper {
    public <T> T updateFields(T old, T source) {
        Field[] fields = old.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object sourceValue = field.get(source);
                Object targetValue = field.get(old);

                if (sourceValue != null && !sourceValue.equals(targetValue)) {
                    if (sourceValue.equals("")) {
                        field.set(old, targetValue);
                    } else {
                        field.set(old, sourceValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new FieldsFailUpdateException(e.getMessage());
            }
        }
        return old;
    }
}