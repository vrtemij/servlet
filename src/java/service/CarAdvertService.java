package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import domain.entity.CarAdvert;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CarAdvertService {
    void createCarAdvert(Map<String,Object> carAdvert);
    void deleteCarAdvert(String advertId);
    CarAdvert getCarAdvertById(String advertId) throws JsonProcessingException;
    List<CarAdvert> getCarAdvertPage(int page, int pageSize);
    File getPhotoById(String id);
    void uploadPhoto(Part part, String id) throws IOException;
}
