package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.entity.Car;
import domain.entity.CarAdvert;
import domain.entity.Owner;
import domain.enums.Condition;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class JSONHandler {

    public static Map<String, Object> getJsonFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();

        Map<String, Object> jsonMap = null;
        if (!data.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            jsonMap = mapper.readValue(data, Map.class);
        }
        return jsonMap;
    }

    public static CarAdvert getCarAdvertFromJSON(Map<String,Object> jsonObject) {
        Car car = new Car();
        Owner owner = new Owner();

        car.setMake((String) jsonObject.get("make"));
        car.setModel((String) jsonObject.get("model"));
        car.setYear((Integer) jsonObject.get("year"));
        car.setEngineCapacity((Integer) jsonObject.get("engineCapacity"));
        car.setHorsePower((Integer) jsonObject.get("horsePower"));
        car.setCondition(Condition.valueOf((String) jsonObject.get("condition")));

        owner.setOwnerName((String) jsonObject.get("ownerName"));
        owner.setNumbers((List<String>) jsonObject.get("numbers"));

        return new CarAdvert(car, owner);
    }

    public static String getJSONFromCartAdvert(CarAdvert carAdvert) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = null;
        try {
            jsonResponse = mapper.writeValueAsString(carAdvert);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public static String getJSONFromCartAdvertList(List<CarAdvert> carAdvertList) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = null;
        try(StringWriter sw = new StringWriter()) {
            mapper.writeValue(sw, carAdvertList);
            jsonResponse = sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }
}
