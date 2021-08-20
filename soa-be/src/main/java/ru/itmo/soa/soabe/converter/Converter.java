package ru.itmo.soa.soabe.converter;

public interface Converter {
    <T> String toStr(T object);
    <T> T fromStr(String str, Class<T> tClass);
}
