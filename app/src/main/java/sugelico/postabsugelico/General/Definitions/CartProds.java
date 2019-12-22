package sugelico.postabsugelico.General.Definitions;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by hidura on 6/19/2016.
 */
public class CartProds implements java.io.Serializable{
    private String title;
    private String companion;
    private Double amount;
    private Double price=0.00;
    private Long ordercode;
    private String compound;
    private String notes;
    private String portion;
    private String terms;
    private Boolean saveit;
    private Boolean companiontp;
    private Boolean optionaltp;
    private Boolean compoundtp;
    private String client;
    private String time_pos;
    private Integer product;
    private Integer order;
    private Double subtotal=0.00;
    private Double discount=0.00;
    private Double tax=0.00;
    // boolean to set visiblity of the counter

    public CartProds(){}

    public String getTitle(){
        return this.title;
    }
    public String getTime_pos(){
        return this.time_pos;
    }
    public Integer getProduct(){return this.product;}

    public String getCompanion(){
        return this.companion;
    }

    public Long getOrderCode(){return this.ordercode;}
    public Double getAmount(){return this.amount;}

    public Double getPrice(){return this.price;}
    public Double getDiscount(){return this.discount;}
    public Double getSubtotal(){return this.subtotal;}
    public Double getTax(){return this.tax;}

    public String getCompound(){return this.compound;}
    public Boolean getCompaniontp(){return this.companiontp;}

    public String getNotes(){return this.notes;}

    public String getPortion(){return this.portion;}

    public Integer getOrder(){return this.order;}

    public String getTerms(){return this.terms;}

    public Boolean getSaveit(){return this.saveit;}

    public String getClient(){return this.client;}

    public JSONObject getSerialize() throws JSONException {
        JSONObject data_col=new JSONObject();
        data_col.put("product", getProduct());
        data_col.put("terms", getTerms());
        data_col.put("portion", getPortion());
        data_col.put("notes", getNotes());
        data_col.put("ordercode", getOrderCode());
        data_col.put("amount", getAmount());
        data_col.put("companion", getCompanion());
        if (getClient() == null){
            data_col.put("client", "Generico");
        }else {
            data_col.put("client", getClient());
        }
        return data_col;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setTime_pos(String time_pos){
        this.time_pos= time_pos;
    }
    public void setProduct(Integer product){this.product=product;}
    public void setCompanion(String companion){this.companion=companion;}
    public void setCompaniontp(Boolean companiontp){this.companiontp=companiontp;}//This means if the product is companion of another one.
    public void setOrdercode(Long ordercode){this.ordercode=ordercode;}

    public void setOrder(Integer order){this.order=order;}

    public void setAmount(Double amount){this.amount=amount;}

    public void setPrice(Double price){this.price=price;}
    public void setDiscount(Double discount){this.discount=discount;}
    public void setSubtotal(Double subtotal){this.subtotal=subtotal;}
    public void setTax(Double tax){this.tax=tax;}

    public void setCompound(String compound){this.compound=compound;}

    public void setNotes(String notes){this.notes=notes;}

    public void setPortion(String portion){this.portion=portion;}

    public void setTerms(String terms){this.terms=terms;}

    public void setSaveit(Boolean saveit){this.saveit=saveit;}

    public void setClient(String client){this.client=client;}
}
