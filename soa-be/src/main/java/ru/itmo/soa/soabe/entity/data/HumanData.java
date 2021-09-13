package ru.itmo.soa.soabe.entity.data;

import lombok.*;
import ru.itmo.soa.soabe.entity.Car;
import ru.itmo.soa.soabe.entity.Coordinates;
import ru.itmo.soa.soabe.entity.HumanBeing;
import ru.itmo.soa.soabe.entity.WeaponType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@XmlRootElement(name = "human_being")
public class HumanData {
    @XmlElement
    private Integer id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @XmlElement
    private String name; //Поле не может быть null, Строка не может быть пустой
    @XmlElement
    private CoordinatesData coordinates; //Поле не может быть null
    @XmlElement
    private Boolean realHero; //Поле не может быть null
    @XmlElement
    private Boolean hasToothpick;
    @XmlElement
    private Long impactSpeed; //Максимальное значение поля: 333
    @XmlElement
    private String soundtrackName; //Поле не может быть null
    @XmlElement
    private Long minutesOfWaiting;
    @XmlElement
    private WeaponType weaponType; //Поле не может быть null
    @XmlElement
    private CarData car; //Поле не может быть null

    public HumanBeing toHumanBeing() {
        return new HumanBeing(
                id,
                name,
                coordinates.toCoordinates(),
                ZonedDateTime.now(),
                realHero,
                hasToothpick,
                impactSpeed,
                soundtrackName,
                minutesOfWaiting,
                weaponType,
                car.toCar()
        );
    }
}
