package sugelico.postabsugelico.General.Definitions;

/**
 * Created by hidura on 2/11/2016.
 */
public class GPSLocation {
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String latitud;
    private String longitud;
    private String name;

    public GPSLocation(){}


    public void setName(String name){this.name=name;}
    public void setAddress(String address){this.address=address;}
    public void setCity(String city){this.city=city;}
    public void setState(String state){this.state=state;}
    public void setCountry(String country){this.country=country;}
    public void setPostalCode(String postalCode){this.postalCode=postalCode;}
    public void setLatitud(String latitud){this.latitud=latitud;}
    public void setLongitud(String longitud){this.longitud=longitud;}

    public String getAddress(){return this.address;}
    public String getCity(){return this.city;}
    public String getState(){return this.state;}
    public String getCountry(){return this.country;}
    public String getPostalCode(){return this.postalCode;}
    public String getLatitud(){return this.latitud;}
    public String getLongitud(){return this.longitud;}
    public String getName(){return this.name;}

}
