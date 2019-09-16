package domain.entity;

public class CarAdvert {

    private int id;
    private Car car;
    private Owner owner;

    public CarAdvert() {}

    public CarAdvert(Car car, Owner owner) {
        this.car = car;
        this.owner = owner;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
