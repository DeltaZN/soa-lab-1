package ru.itmo.soa.soabe.converter;

import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@AllArgsConstructor
public class TestEntity {
    @XmlElement
    private int id;
}
