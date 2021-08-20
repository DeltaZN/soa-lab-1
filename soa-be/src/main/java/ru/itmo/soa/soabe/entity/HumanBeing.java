package ru.itmo.soa.soabe.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@XmlRootElement(name = "human_being")
public class HumanBeing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @Column(columnDefinition = "TEXT NOT NULL CHECK (char_length(name) > 0)")
    private String name; //Поле не может быть null, Строка не может быть пустой
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates; //Поле не может быть null
    @Column(nullable = false)
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @Column(nullable = false)
    private Boolean realHero; //Поле не может быть null
    private boolean hasToothpick;
    @Column(columnDefinition = "BIGINT CHECK (impact_speed < 333)")
    private long impactSpeed; //Максимальное значение поля: 333
    @Column(nullable = false)
    private String soundtrackName; //Поле не может быть null
    private long minutesOfWaiting;
    @Column(nullable = false)
    private WeaponType weaponType; //Поле не может быть null
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car; //Поле не может быть null
}
