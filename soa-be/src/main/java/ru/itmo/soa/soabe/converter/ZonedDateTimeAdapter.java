package ru.itmo.soa.soabe.converter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;

public final class ZonedDateTimeAdapter extends
        XmlAdapter<String, ZonedDateTime> {
    @Override
    public ZonedDateTime unmarshal(String s) throws Exception {
        return ZonedDateTime.parse(s);
    }

    @Override
    public String marshal(ZonedDateTime zonedDateTime) throws Exception {
        return zonedDateTime.toString();
    }
}
