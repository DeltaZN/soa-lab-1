package ru.itmo.soa.soabe.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.itmo.soa.soabe.entity.data.CoordinatesData;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@XmlRootElement
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // в модели отсутствует
    @XmlElement
    private float x;
    @Column(columnDefinition = "REAL NOT NULL CHECK (Coordinates.y > -399)")
    @XmlElement
    private Float y; //Значение поля должно быть больше -399, Поле не может быть null

    public void update(CoordinatesData data) {
        this.x = data.getX();
        this.y = data.getY();
    }
}
