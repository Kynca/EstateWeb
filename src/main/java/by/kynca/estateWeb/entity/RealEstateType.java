package by.kynca.estateWeb.entity;

public enum RealEstateType {
    FLAT("FLAT"),
    HOUSE("HOUSE"),
    LAND("LAND");

    private final String value;

    RealEstateType(String value){this.value = value;}

    public String getValue(){
        return value;
    }

    public static RealEstateType getByCode(String realEstateType){
        for(RealEstateType type: RealEstateType.values()){
            if(type.value.equals(realEstateType)){
                return type;
            }
        }
        return null;
    }
}
