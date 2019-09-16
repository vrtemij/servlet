package domain.entity;

import java.util.List;

public class Owner {
    private int id;
    private String ownerName;
    private List<String> numbers;

    public Owner() {}

    public Owner(String ownerName, List<String> numbers) {
        this.ownerName = ownerName;
        this.numbers = numbers;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
