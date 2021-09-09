package ru.itmo.soa.soabe.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Coordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id; // в модели отсутствует
    private float x;
    @Column(columnDefinition = "REAL NOT NULL CHECK (y > -399)")
    private Float y; //Значение поля должно быть больше -399, Поле не может быть null
}
