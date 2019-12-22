package sugelico.postabsugelico.General.Definitions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hidura on 6/18/2016.
 */
public class Products implements java.io.Serializable{
    private String title;
    private String icon;
    private Integer code;
    private Integer currency;
    private Double price;
    private Double subtotal;
    private Double tax;
    private String client;
    private String notes;
    private ArrayList<Products>companion;
    private ArrayList<String> terms;
    // boolean to set visiblity of the counter
    private String description;
    private ArrayList<Products> compound;
    private ArrayList<Products> optional;
    private ArrayList<Products> suggested;
    private String images;
    private boolean sponsored;
    private String cat_name;
    private Integer category;
    private Integer cycle;
    public Products(){}


    public String getImages(){return this.images;}
    public String getTitle(){
        return this.title;
    }
    public String getNotes(){
        return this.notes;
    }
    public String getClient(){return this.client;}
    public ArrayList<Products> getCompound(){return this.compound;}

    public ArrayList<Products> getSuggested() {
        return suggested;
    }

    public String getIcon(){
        return this.icon;
    }
    public String getCat_name(){return this.cat_name;}

    public Double getPrice(){
        return this.price;
    }
    public Double getSubtotal(){
        return this.subtotal;
    }
    public Double getTax(){
        return this.tax;
    }
    public Integer getCode(){
        return this.code;
    }
    public Integer getCategory(){return this.category;}
    public ArrayList<String> getTerms(){return this.terms;}
    public Integer getCycle(){return this.cycle;}
    public ArrayList<Products> getCompanion(){
        return this.companion;
    }
    public ArrayList<Products> getOptinoal(){
        return this.optional;
    }
    public Integer getCurrency(){
        return this.currency;
    }
    public String getDescription(){return this.description;}
    public boolean getSponsored(){
        return this.sponsored;
    }


    public void setImages(String images){this.images=images;}
    public void setTerms(ArrayList<String> terms){this.terms=terms;}
    public void setCycle(Integer cycle){this.cycle=cycle;}
    public void setCat_name(String cat_name){this.cat_name=cat_name;}
    public void setDescription(String description){this.description=description;}
    public void setTitle(String title){
        this.title = title;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public void setCompound(ArrayList<Products> compound) {
        this.compound = compound;
    }

    public void setSuggested(ArrayList<Products> suggested) {
        this.suggested = suggested;
    }

    public boolean isSponsored() {
        return sponsored;
    }

    public ArrayList<Products> getOptional() {
        return optional;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public void setIcon(String icon){
        this.icon = icon;
    }
    public void setClient(String client){this.client=client;}
    public void setPrice(Double price){this.price=price;}
    public void setSubtotal(Double subtotal){this.subtotal=subtotal;}
    public void setTax(Double tax){this.tax=tax;}
    public void setCurrency(int currency){this.currency=currency;}
    public void setNotes(String notes){this.notes=notes;}

    public void setCode(Integer code){this.code = code;}
    public void setCategory(Integer category){
        this.category= category;
    }
    public void setCompanion(ArrayList<Products> companion){
        this.companion = companion;
    }
    public void setOptional(ArrayList<Products> optional){
        this.optional= optional;
    }
    public void setSponsored(boolean sponsored){
        this.sponsored = sponsored;
    }
}
