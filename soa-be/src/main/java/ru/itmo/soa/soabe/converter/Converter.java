package ru.itmo.soa.soabe.converter;

import java.util.List;

public interface Converter {
    <T> String listToStr(List<T> list, String name, T[] array);
    <T> String toStr(T object);
    <T> T fromStr(String str, Class<T> tClass);
}
