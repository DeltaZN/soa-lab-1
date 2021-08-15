package ru.itmo.soa.soabe.converter;

public interface Converter {
    <T> String convert(T object);
}
