package by.kynca.estateWeb.entity;

import by.kynca.estateWeb.entity.validatorMarks.FlatValidate;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
public class Flat extends AbstractBean {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long flatId;
    @Min(value = 1, message = "number should be positive", groups = FlatValidate.class)
    private int number;
    @Min(value = 1, message = "number should be positive", groups = FlatValidate.class)
    private int rooms;
    @Min(value = 1, message = "number should be positive", groups = FlatValidate.class)
    private int floor;
    private boolean isRepaired;

    public Flat() {
    }

    public Flat(Long flatId, int number, int rooms, int floor, boolean isRepaired) {
        this.flatId = flatId;
        this.number = number;
        this.rooms = rooms;
        this.floor = floor;
        this.isRepaired = isRepaired;
    }

    public Flat(int number, int rooms, int floor, boolean isRepaired) {
        this.number = number;
        this.rooms = rooms;
        this.floor = floor;
        this.isRepaired = isRepaired;
    }

    public Long getFlatId() {
        return flatId;
    }

    public void setFlatId(Long id) {
        this.flatId = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean isRepaired() {
        return isRepaired;
    }

    public void setRepaired(boolean repaired) {
        isRepaired = repaired;
    }

//    public RealEstate getRealEstate() {
//        return realEstate;
//    }
//
//    public void setRealEstate(RealEstate realEstate) {
//        this.realEstate = realEstate;
//    }
}
