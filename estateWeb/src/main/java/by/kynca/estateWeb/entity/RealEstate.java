package by.kynca.estateWeb.entity;

import by.kynca.estateWeb.entity.validatorMarks.BasicEstateValidate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;


@Entity
@SQLDelete(sql = "UPDATE real_estate SET deleted = true WHERE real_estate_id=?")
@Where(clause = "deleted=false")
public class RealEstate extends AbstractBean {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long realEstateId;

    @Min(value = 10, message = "area should be more than 10", groups = BasicEstateValidate.class)
    @Max(value = 10000, message = "area can't be more than 10000", groups = BasicEstateValidate.class)
    @NotNull(message = "this field can not be empty", groups = BasicEstateValidate.class)
    private Double area;

    @PositiveOrZero(message = "price must be positive", groups = BasicEstateValidate.class)
    @NotNull(message = "this field can not be empty", groups = BasicEstateValidate.class)
    private Double price;

    @NotEmpty(message = "additional information can't be empty", groups = BasicEstateValidate.class)
    @Size(min = 20, max = 2055, message = "add info should be between 20 and 2055 characters", groups = BasicEstateValidate.class)
    private String addInfo;
    private boolean isRented;
    @Enumerated(value = EnumType.STRING)
    @NotNull(groups = BasicEstateValidate.class)
    private RealEstateType realEstateType;

    private boolean deleted;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "address_Id", referencedColumnName = "addressId")
    @Valid
    private Address address;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "clientId")
    private Client client;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "flat_id", referencedColumnName = "flatId")
    @Valid
    private Flat flat;

    public RealEstate() {
        this.deleted = false;
    }

    public RealEstate(Long realEstateId, Double area, Double price, String addInfo, boolean isRented,
                      RealEstateType realEstateType, boolean deleted, Address address, Client client, Flat flat) {
        this.realEstateId = realEstateId;
        this.area = area;
        this.price = price;
        this.addInfo = addInfo;
        this.isRented = isRented;
        this.realEstateType = realEstateType;
        this.deleted = deleted;
        this.address = address;
        this.client = client;
        this.flat = flat;
    }

    public RealEstate(Double area, Double price, String addInfo, boolean isRented, RealEstateType realEstateType,
                      Address address, Client client, Flat flat) {
        this.area = area;
        this.price = price;
        this.addInfo = addInfo;
        this.isRented = isRented;
        this.realEstateType = realEstateType;
        this.deleted = false;
        this.address = address;
        this.client = client;
        this.flat = flat;
    }

    public Long getRealEstateId() {
        return realEstateId;
    }

    public void setRealEstateId(Long id) {
        this.realEstateId = id;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAddInfo() {
        return addInfo;
    }

    public void setAddInfo(String addInfo) {
        this.addInfo = addInfo;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public RealEstateType getRealEstateType() {
        return realEstateType;
    }

    public void setRealEstateType(RealEstateType realEstateType) {
        this.realEstateType = realEstateType;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public void setEstate(RealEstate estate){
        this.client = estate.getClient();
        this.realEstateId = estate.getRealEstateId();
        this.isRented = estate.isRented();
        this.area = estate.getArea();
        this.price = estate.getPrice();
        this.address = estate.getAddress();
        this.flat = estate.getFlat();
        this.addInfo = estate.getAddInfo();
    }

    @Override
    public String toString() {
        return "RealEstate{" +
                "realEstateId=" + realEstateId +
                ", area=" + area +
                ", price=" + price +
                ", addInfo='" + addInfo + '\'' +
                ", isRented=" + isRented +
                ", address=" + address +
                ", client=" + client +
                ", flat=" + flat +
                '}';
    }

}
