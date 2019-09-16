package controller;

import service.CarAdvertService;
import service.CarAdvertServiceImpl;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;

@WebServlet(name = "PhotoUploadServlet", urlPatterns = "/photo")
@MultipartConfig
public class PhotoUploadServlet extends HttpServlet {

    private CarAdvertService carAdvertService = new CarAdvertServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        Part filePart = request.getPart("file");
        String id = request.getParameter("id");

        carAdvertService.uploadPhoto(filePart, id);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/jpeg");
        String id = request.getParameter("id");
        File photo = carAdvertService.getPhotoById(id);
        BufferedImage bi = ImageIO.read(photo);
        OutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        out.close();
    }
}
