package ru.itmo.soa.soabe.service;

import jakarta.servlet.http.HttpServletResponse;
import ru.itmo.soa.soabe.converter.Converter;
import ru.itmo.soa.soabe.dao.HumanBeingDao;
import ru.itmo.soa.soabe.entity.HumanBeing;

import java.util.List;

public class HumanBeingService {

    private final HumanBeingDao dao;

    public HumanBeingService(Converter converter) {
        dao = new HumanBeingDao(converter);
    }

    public void getAllHumans(HttpServletResponse response) {
        try {
            List<HumanBeing> humans = dao.getAllHumans();
        } catch (Exception e) {
            response.setStatus(500);
        }
    }
}
