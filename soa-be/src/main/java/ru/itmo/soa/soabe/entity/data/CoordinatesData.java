package ru.itmo.soa.soabe.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.itmo.soa.soabe.entity.Coordinates;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@XmlRootElement(name = "coordinates")
public class CoordinatesData {
    @XmlElement
    private Float x;
    @XmlElement
    private Float y; //Значение поля должно быть больше -399, Поле не может быть null

    public Coordinates toCoordinates() {
        return new Coordinates(0, x, y);
    }
}
