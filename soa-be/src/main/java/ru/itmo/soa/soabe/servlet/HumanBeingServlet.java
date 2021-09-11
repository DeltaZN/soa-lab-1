package ru.itmo.soa.soabe.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itmo.soa.soabe.converter.Converter;
import ru.itmo.soa.soabe.converter.XMLConverter;
import ru.itmo.soa.soabe.service.HumanBeingService;

import java.io.IOException;

@WebServlet(name = "humanBeing", value = "/human-being/*")
public class HumanBeingServlet extends HttpServlet {

    private static final String SOUNDTRACK_NAME_LESS_PARAM = "soundtrackNameLess";
    private static final String MINUTES_OF_WAITING_LESS_PARAM = "minutesOfWaitingLess";

    private static final String NAME_PARAM = "name";
    private static final String MINUTES_OF_WAITING_PARAM = "minutesOfWaiting";
    private static final String REAL_HERO_PARAM = "realHero";
    private static final String HAS_TOOTHPICK_PARAM = "hasToothpick";
    private static final String IMPACT_SPEED_PARAM = "impactSpeed";
    private static final String SOUNDTRACK_NAME_PARAM = "soundtrackName";
    private static final String WEAPON_TYPE_PARAM = "weaponType";
    private static final String CAR_NAME_PARAM = "carName";
    private static final String CAR_COOL_PARAM = "carCool";
    private static final String COORDINATES_X_PARAM = "coordinatesX";
    private static final String COORDINATES_Y_PARAM = "coordinatesY";
    private static final String CREATION_DATE_PARAM = "creationDate";

    private HumanBeingService service;

    @Override
    public void init() throws ServletException {
        super.init();
        Converter converter = new XMLConverter();
        service = new HumanBeingService(converter);
    }

    private HumanBeingFilterParams getFilterParams(HttpServletRequest request) {
       return new HumanBeingFilterParams(
                request.getParameter(NAME_PARAM),
                request.getParameter(MINUTES_OF_WAITING_PARAM),
                request.getParameter(REAL_HERO_PARAM),
                request.getParameter(HAS_TOOTHPICK_PARAM),
                request.getParameter(IMPACT_SPEED_PARAM),
                request.getParameter(SOUNDTRACK_NAME_PARAM),
                request.getParameter(WEAPON_TYPE_PARAM),
                request.getParameter(CAR_NAME_PARAM),
                request.getParameter(CAR_COOL_PARAM),
                request.getParameter(COORDINATES_X_PARAM),
                request.getParameter(COORDINATES_Y_PARAM),
                request.getParameter(CREATION_DATE_PARAM)
        );
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            HumanBeingFilterParams filterParams = getFilterParams(request);
            String minutesOfWaitingLess = request.getParameter(MINUTES_OF_WAITING_LESS_PARAM);
            if (minutesOfWaitingLess != null) {
                service.findMinutesOfWaitingLess(response, Long.parseLong(minutesOfWaitingLess));
            } else {
                service.getAllHumans(response, filterParams);
            }
        } else {
            String[] servletPath = pathInfo.split("/");
            if (servletPath.length > 1) {
                String id = servletPath[1];
                service.getHuman(response, Integer.parseInt(id));
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            service.createHuman(request, response);
        } else {
            String[] servletPath = pathInfo.split("/");
            if (servletPath.length > 1) {
                String soundTrackName = request.getParameter(SOUNDTRACK_NAME_LESS_PARAM);
                service.countSoundtrackNameLess(response, soundTrackName);
            }
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        service.updateHuman(request, response);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            String minutesOfWaiting = request.getParameter(MINUTES_OF_WAITING_PARAM);
            if (minutesOfWaiting != null) {
                service.deleteAnyMinutesOfWaitingEqual(response, Integer.parseInt(minutesOfWaiting));
            }
        } else {
            String[] servletPath = pathInfo.split("/");
            if (servletPath.length > 1) {
                String id = servletPath[1];
                service.deleteHuman(response, Integer.parseInt(id));
            }
        }
    }
}
