package ru.itmo.soa.soabe.entity.data.validator;

import ru.itmo.soa.soabe.entity.data.CoordinatesData;
import ru.itmo.soa.soabe.entity.data.HumanData;

import javax.xml.bind.ValidationException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CoordinatesValidator  implements Validator<CoordinatesData> {
    public List<String> validate(CoordinatesData coordinates) throws IllegalAccessException {
        List<String> errorList = new ArrayList<>();
        if (coordinates == null) {
            return errorList;
        }
        for (Field f : CoordinatesData.class.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.get(coordinates) == null) {
                errorList.add(String.format("Coordinates %s isn't specified", f.getName()));
            }
        }
        if (coordinates.getY() != null && coordinates.getY() <= -399.0) {
            errorList.add("Coordinates y should be bigger than -399");
        }
        return errorList;
    }
}
