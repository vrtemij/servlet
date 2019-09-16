package service;
;
import domain.entity.CarAdvert;
import repository.CarAdvertRepository;
import repository.CarAdvertRepositoryImpl;
import utils.JSONHandler;

import javax.servlet.http.Part;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarAdvertServiceImpl implements CarAdvertService {

    public static final String PATH = "/home/user/Desktop/CarsShop/src/java/files/images/";

    private CarAdvertRepository carAdvertRepository = new CarAdvertRepositoryImpl();

    @Override
    public void createCarAdvert(Map<String,Object> incomingJson) {
        try {
            carAdvertRepository.insertAdvert(JSONHandler.getCarAdvertFromJSON(incomingJson));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CarAdvert getCarAdvertById(String advertId) {
        CarAdvert carAdvert = new CarAdvert();
        try {
            carAdvert = carAdvertRepository.getAdvertById(advertId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return carAdvert;
    }

    @Override
    public List<CarAdvert> getCarAdvertPage(int page, int pageSize) {
        int limit = pageSize;
        int offset = page * pageSize;
        List<CarAdvert> carAdverts = new ArrayList<>();
        try {
            carAdverts = carAdvertRepository.getAdvertPage(limit, offset);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return carAdverts;
    }

    @Override
    public File getPhotoById(String id) {
        File photo;
        try {
            String photoName = carAdvertRepository.getPhotoById(Integer.parseInt(id));
            photo = new File(PATH + photoName);
            return photo;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void uploadPhoto(Part filePart, String id) throws IOException {
        //TODO: Update and hide path
        String fileName = getFileName(filePart);

        try (OutputStream out = new FileOutputStream(new File(
                PATH + File.separator + fileName));
             InputStream fileContent = filePart.getInputStream()) {
            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = fileContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            carAdvertRepository.addPhotoToAdvert(Integer.parseInt(id), fileName);
        } catch (FileNotFoundException | SQLException | ClassNotFoundException fne) {
            fne.printStackTrace();
        }
    }

    @Override
    public void deleteCarAdvert(String advertId) {
        try {
            carAdvertRepository.deleteAdvert(advertId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }


}
