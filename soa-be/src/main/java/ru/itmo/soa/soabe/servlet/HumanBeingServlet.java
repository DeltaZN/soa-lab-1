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
import java.io.PrintWriter;

@WebServlet(name = "humanBeing", value = "/human-being/")
public class HumanBeingServlet extends HttpServlet {

    private HumanBeingService service;

    @Override
    public void init() throws ServletException {
        super.init();
        Converter converter = new XMLConverter();
        service = new HumanBeingService(converter);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        service.getAllHumans(response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        service.createHuman(request, response);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        try {
        } catch (Exception e) {
            response.sendError(500, e.getMessage());
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        try {

        } catch (Exception e) {
            response.sendError(500, e.getMessage());
        }
    }
}
