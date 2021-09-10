package ru.itmo.soa.soabe.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itmo.soa.soabe.converter.Converter;
import ru.itmo.soa.soabe.dao.HumanBeingDao;
import ru.itmo.soa.soabe.entity.HumanBeing;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static ru.itmo.soa.soabe.util.ServletUtil.getBody;

public class HumanBeingService {

    private final HumanBeingDao dao;
    private final Converter converter;

    public HumanBeingService(Converter converter) {
        this.converter = converter;
        dao = new HumanBeingDao(converter);
    }

    public void getAllHumans(HttpServletResponse response) {
        try {
            List<HumanBeing> humans = dao.getAllHumans();
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.write(converter.listToStr(humans, "humans", new HumanBeing[0]));
        } catch (Exception e) {
            response.setStatus(500);
        }
    }

    public void createHuman(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HumanBeing humanBeing = converter.fromStr(getBody(request), HumanBeing.class);
            dao.createHuman(humanBeing);
            response.setStatus(200);
        } catch (Exception e) {
            response.sendError(500, e.getMessage());
        }
    }
}
