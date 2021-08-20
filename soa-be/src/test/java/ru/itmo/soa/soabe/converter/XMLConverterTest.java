package ru.itmo.soa.soabe.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.itmo.soa.soabe.entity.TestEntity;


class XMLConverterTest {

    private final String str = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <test>
                <id>1</id>
            </test>
            """;

    @Test
    void toStr() {
        XMLConverter xmlConverter = new XMLConverter();
        TestEntity testEntity = new TestEntity(1);
        String s = xmlConverter.toStr(testEntity);
        Assertions.assertTrue(s.contains(str));
    }

    @Test
    void fromStr() {
        XMLConverter xmlConverter = new XMLConverter();
        TestEntity testEntity = new TestEntity(1);
        TestEntity parsed = xmlConverter.fromStr(str, TestEntity.class);
        Assertions.assertEquals(parsed, testEntity);
    }
}