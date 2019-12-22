package sugelico.postabsugelico.General.Definitions;

import java.util.ArrayList;
import java.util.List;

public class SugelicoMobileBill implements java.io.Serializable {

    public SugelicoMobileBill(){

    }

    private Long BillID;
    private Double total;
    private Double subtotal;
    private Double tax;
    private Double sugelico_fee;
    private Double delivery_fee;
    private String _time;
    private String _address;
    private ArrayList<CartProds> products;


    public void setProducts(ArrayList<CartProds> products) {
        this.products = products;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public void setBillID(Long billID) {
        BillID = billID;
    }

    public void setDelivery_fee(Double delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public void setSugelico_fee(Double sugelico_fee) {
        this.sugelico_fee = sugelico_fee;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getDelivery_fee() {
        return delivery_fee;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public Double getSugelico_fee() {
        return sugelico_fee;
    }

    public String get_time() {
        return _time;
    }

    public String get_address() {
        return _address;
    }

    public Long getBillID() {
        return BillID;
    }

    public List<CartProds> getProducts() {
        return products;
    }

    public Double getTotal() {
        return total;
    }

    public Double getTax() {
        return tax;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
