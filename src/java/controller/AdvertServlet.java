package controller;

import service.CarAdvertService;
import service.CarAdvertServiceImpl;
import utils.JSONHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "CreateAdvertServlet", urlPatterns = "/car")
public class AdvertServlet extends HttpServlet {

    private CarAdvertService carAdvertService = new CarAdvertServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,Object> incomingRequest = JSONHandler.getJsonFromRequest(request);
        carAdvertService.createCarAdvert(incomingRequest);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try(PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            String action = request.getParameter("action");
            switch (action) {
                case "list" : {
                    int pageSize = Integer.parseInt(request.getParameter("pageSize"));
                    int page = Integer.parseInt(request.getParameter("page"));
                    out.write(JSONHandler.getJSONFromCartAdvertList(carAdvertService.getCarAdvertPage(page, pageSize)));
                }
                default: {
                    out.write(JSONHandler.getJSONFromCartAdvert(carAdvertService.getCarAdvertById(
                    request.getParameter("id"))));
                }
            }
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json");
            carAdvertService.deleteCarAdvert(request.getParameter("id"));
        }
    }
}
