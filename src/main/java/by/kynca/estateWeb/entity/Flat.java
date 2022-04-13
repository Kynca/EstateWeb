package by.kynca.estateWeb.entity;

import by.kynca.estateWeb.entity.validatorMarks.FlatValidate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity
public class Flat extends AbstractBean {
    @Id
    @SequenceGenerator(name = "flat_sequence", sequenceName = "flat_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flat_sequence")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flat flat = (Flat) o;
        return number == flat.number && rooms == flat.rooms && floor == flat.floor && isRepaired == flat.isRepaired && flatId.equals(flat.flatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatId, number, rooms, floor, isRepaired);
    }

    @Override
    public String toString() {
        return "Flat{" +
                "flatId=" + flatId +
                ", number=" + number +
                ", rooms=" + rooms +
                ", floor=" + floor +
                ", isRepaired=" + isRepaired +
                '}';
    }
}
