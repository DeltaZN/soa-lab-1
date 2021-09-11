package ru.itmo.soa.soabe.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@XmlRootElement
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    private int id; // в модели отсутствует
    @XmlElement
    @Column(nullable = false)
    private String name; //Поле может быть null
    @XmlElement
    private boolean cool;
}
