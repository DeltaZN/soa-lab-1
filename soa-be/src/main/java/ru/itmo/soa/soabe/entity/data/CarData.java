package ru.itmo.soa.soabe.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.itmo.soa.soabe.entity.Car;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@XmlRootElement(name = "car")
public class CarData {
    @XmlElement
    private String name; //Поле может быть null
    @XmlElement
    private Boolean cool;

    public Car toCar() {
        return new Car(0, name, cool);
    }
}