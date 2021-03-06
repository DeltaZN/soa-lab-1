package ru.itmo.soa.soabe.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.itmo.soa.soabe.converter.Converter;
import ru.itmo.soa.soabe.converter.XMLConverter;
import ru.itmo.soa.soabe.service.HumanBeingService;

@WebServlet(name = "humanBeing", value = "/human-being/*")
public class HumanBeingServlet extends HttpServlet {

    public static final String SORTING_PARAM = "sort";
    public static final String PAGE_IDX_PARAM = "pageIdx";
    public static final String PAGE_SIZE_PARAM = "pageSize";

    public static final String SOUNDTRACK_NAME_LESS_PARAM = "soundtrackNameLess";
    public static final String MINUTES_OF_WAITING_LESS_PARAM = "minutesOfWaitingLess";

    public static final String NAME_PARAM = "name";
    public static final String MINUTES_OF_WAITING_PARAM = "minutesOfWaiting";
    public static final String REAL_HERO_PARAM = "realHero";
    public static final String HAS_TOOTHPICK_PARAM = "hasToothpick";
    public static final String IMPACT_SPEED_PARAM = "impactSpeed";
    public static final String SOUNDTRACK_NAME_PARAM = "soundtrackName";
    public static final String WEAPON_TYPE_PARAM = "weaponType";
    public static final String CAR_NAME_PARAM = "carName";
    public static final String CAR_COOL_PARAM = "carCool";
    public static final String COORDINATES_X_PARAM = "coordinatesX";
    public static final String COORDINATES_Y_PARAM = "coordinatesY";
    public static final String CREATION_DATE_PARAM = "creationDate";

    private HumanBeingService service;

    @Override
    public void init() throws ServletException {
        super.init();
        Converter converter = new XMLConverter();
        service = new HumanBeingService(converter);
    }

    private HumanBeingRequestParams getFilterParams(HttpServletRequest request) {
       return new HumanBeingRequestParams(
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
               request.getParameter(CREATION_DATE_PARAM),
               request.getParameter(SORTING_PARAM),
               request.getParameter(PAGE_IDX_PARAM),
               request.getParameter(PAGE_SIZE_PARAM)
        );
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            HumanBeingRequestParams filterParams = getFilterParams(request);
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
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

    @SneakyThrows
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
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
