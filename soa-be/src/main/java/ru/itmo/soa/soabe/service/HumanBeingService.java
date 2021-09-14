package ru.itmo.soa.soabe.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.itmo.soa.soabe.converter.Converter;
import ru.itmo.soa.soabe.dao.HumanBeingDao;
import ru.itmo.soa.soabe.entity.HumanBeing;
import ru.itmo.soa.soabe.entity.data.HumanData;
import ru.itmo.soa.soabe.entity.data.validator.HumanValidator;
import ru.itmo.soa.soabe.response.ServerResponse;
import ru.itmo.soa.soabe.servlet.HumanBeingRequestParams;

import javax.persistence.EntityNotFoundException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static ru.itmo.soa.soabe.util.ServletUtil.getBody;

public class HumanBeingService {

    private final HumanBeingDao dao;
    private final Converter converter;
    private final HumanValidator humanValidator;

    public HumanBeingService(Converter converter) {
        this.converter = converter;
        humanValidator = new HumanValidator();
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
            throw new EntityNotFoundException("Cannot find human with id " + id);
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
        HumanData humanData = converter.fromStr(getBody(request), HumanData.class);
        humanValidator.validate(humanData);
        int id = dao.createHuman(humanData.toHumanBeing());
        response.setStatus(200);
        response.getWriter().write(converter.toStr(new ServerResponse<>(id)));
    }

    @SneakyThrows
    public void updateHuman(HttpServletRequest request, HttpServletResponse response) {
        HumanData humanData = converter.fromStr(getBody(request), HumanData.class);
        humanValidator.validate(humanData);
        Optional<HumanBeing> human = dao.getHuman(humanData.getId());
        if (human.isPresent()) {
            HumanBeing humanBeing = human.get();
            humanBeing.update(humanData);
            dao.updateHuman(humanBeing);
            response.setStatus(200);
        } else {
            throw new EntityNotFoundException("Cannot update human");
        }
    }

    public void deleteHuman(HttpServletResponse response, int id) {
        if (dao.deleteHuman(id)) {
            response.setStatus(200);
        } else {
            throw new EntityNotFoundException("Cannot find human with id " + id);
        }
    }
}
