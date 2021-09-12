package ru.itmo.soa.soabe.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.itmo.soa.soabe.converter.Converter;
import ru.itmo.soa.soabe.dao.HumanBeingDao;
import ru.itmo.soa.soabe.entity.HumanBeing;
import ru.itmo.soa.soabe.response.ServerResponse;
import ru.itmo.soa.soabe.servlet.HumanBeingRequestParams;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static ru.itmo.soa.soabe.util.ServletUtil.getBody;

public class HumanBeingService {

    private final HumanBeingDao dao;
    private final Converter converter;

    public HumanBeingService(Converter converter) {
        this.converter = converter;
        dao = new HumanBeingDao();
    }

    @SneakyThrows
    public void countSoundtrackNameLess(HttpServletResponse response, String soundtrack) {
            Long count = dao.countHumansSoundtrackNameLess(soundtrack);
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            ServerResponse<Long> r = new ServerResponse<>(count);
            writer.write(converter.toStr(r));
    }

    @SneakyThrows
    public void findMinutesOfWaitingLess(HttpServletResponse response, long minutesOfWaiting) {
            List<HumanBeing> list = dao.findHumansMinutesOfWaitingLess(minutesOfWaiting);
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.write(converter.listToStr(list, "humans", new HumanBeing[0]));
    }

    @SneakyThrows
    public void deleteAnyMinutesOfWaitingEqual(HttpServletResponse response, long minutesOfWaiting) {
            long id = dao.deleteAnyHumanMinutesOfWaitingEqual(minutesOfWaiting);
            response.setStatus(id >= 0 ? 200 : 404);
            PrintWriter writer = response.getWriter();
            ServerResponse<Long> r = new ServerResponse<>(id);
            writer.write(converter.toStr(r));
    }

    @SneakyThrows
    public void getHuman(HttpServletResponse response, int id) {
            Optional<HumanBeing> human = dao.getHuman(id);
            if (human.isPresent()) {
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(converter.toStr(human.get()));
            } else {
                response.setStatus(404);
            }
    }

    @SneakyThrows
    public void getAllHumans(HttpServletResponse response, HumanBeingRequestParams params) {
            HumanBeingDao.PaginationResult humans = dao.getAllHumans(params);
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.write(converter.toStr(humans));
    }

    @SneakyThrows
    public void createHuman(HttpServletRequest request, HttpServletResponse response) {
            HumanBeing humanBeing = converter.fromStr(getBody(request), HumanBeing.class);
            dao.createHuman(humanBeing);
            response.setStatus(200);
    }

    @SneakyThrows
    public void updateHuman(HttpServletRequest request, HttpServletResponse response)  {
            HumanBeing humanBeing = converter.fromStr(getBody(request), HumanBeing.class);
            dao.updateHuman(humanBeing);
            response.setStatus(200);
    }

    public void deleteHuman(HttpServletResponse response, int id) {
        if (dao.deleteHuman(id)) {
            response.setStatus(200);
        } else {
            response.setStatus(404);
        }
    }
}
