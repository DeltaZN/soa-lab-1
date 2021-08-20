package ru.itmo.soa.soabe.converter;

import javax.xml.bind.*;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLConverter implements Converter {
    @Override
    public <T> String toStr(T object) {
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(object, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @Override
    public <T> T fromStr(String str, Class<T> tClass) {
        return JAXB.unmarshal(new StringReader(str), tClass);
    }
}
