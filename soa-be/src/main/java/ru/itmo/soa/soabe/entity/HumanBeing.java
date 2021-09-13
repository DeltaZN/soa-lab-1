package ru.itmo.soa.soabe.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itmo.soa.soabe.converter.ZonedDateTimeAdapter;
import ru.itmo.soa.soabe.entity.data.HumanData;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "human_being")
@XmlRootElement(name = "human_being")
public class HumanBeing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    @Getter
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @Column(columnDefinition = "TEXT NOT NULL CHECK (char_length(human_being.name) > 0)")
    @XmlElement
    private String name; //Поле не может быть null, Строка не может быть пустой
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id")
    @XmlElement
    private Coordinates coordinates; //Поле не может быть null
    @Column(nullable = false, name = "creation_date")
    @XmlElement
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    @Getter
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @Column(nullable = false, name = "real_hero")
    @XmlElement
    private Boolean realHero; //Поле не может быть null
    @Column(nullable = false, name = "has_toothpick")
    @XmlElement
    private boolean hasToothpick;
    @Column(name = "impact_speed", columnDefinition = "BIGINT CHECK (human_being.impact_speed < 333)")
    @XmlElement(required = true)
    private long impactSpeed; //Максимальное значение поля: 333
    @Column(nullable = false, name = "soundtrack_name")
    @XmlElement
    private String soundtrackName; //Поле не может быть null
    @XmlElement
    @Column(nullable = false, name = "minutes_of_waiting")
    private long minutesOfWaiting;
    @Column(nullable = false, name = "weapon_type")
    @XmlElement
    private WeaponType weaponType; //Поле не может быть null
    @OneToOne(cascade = CascadeType.ALL)
    @Getter
    @JoinColumn(name = "car_id")
    @XmlElement
    private Car car; //Поле не может быть null

    public void update(HumanData data) {
        this.name = data.getName();
        this.car.update(data.getCar());
        this.coordinates.update(data.getCoordinates());
        this.hasToothpick = data.getHasToothpick();
        this.impactSpeed = data.getImpactSpeed();
        this.minutesOfWaiting = data.getMinutesOfWaiting();
        this.realHero = data.getRealHero();
        this.soundtrackName = data.getSoundtrackName();
        this.weaponType = data.getWeaponType();
    }
}
