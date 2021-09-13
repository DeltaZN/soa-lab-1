package ru.itmo.soa.soabe.entity.data.validator;

import ru.itmo.soa.soabe.entity.data.CarData;
import ru.itmo.soa.soabe.entity.data.HumanData;

import javax.xml.bind.ValidationException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CarValidator implements Validator<CarData> {
    public List<String> validate(CarData car) throws ValidationException, IllegalAccessException {
        List<String> errorList = new ArrayList<>();
        if (car == null) {
            return errorList;
        }
        for (Field f : CarData.class.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.get(car) == null) {
                errorList.add(String.format("Car %s isn't specified", f.getName()));
            }
        }
        return errorList;
    }
}
