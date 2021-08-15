package ru.itmo.soa.soabe.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itmo.soa.soabe.converter.Converter;
import ru.itmo.soa.soabe.converter.XMLConverter;
import ru.itmo.soa.soabe.dao.TestDao;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    private TestDao dao;

    @Override
    public void init() throws ServletException {
        super.init();
        Converter converter = new XMLConverter();
        dao = new TestDao(converter);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println(dao.test().orElse("[Error]"));
    }

}