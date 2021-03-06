package ru.itmo.soa.soabe.entity.data.validator;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface Validator<T> {
    List<String> validate(T value)  throws IllegalAccessException, ValidationException;
}
