package sugelico.postabsugelico.General.Definitions;

/**
 * Created by diegohidalgo on 2/14/17.
 */

public class ActiveOrders implements java.io.Serializable{

    private Integer code;
    private Integer order=0;
    private Boolean status;
    private String tblname;

    public ActiveOrders(){}
    public void setStatus(Boolean status){this.status=status;}
    public void setCode(int code){this.code=code;}
    public void setOrder(int order){this.order=order;}
    public void setName(String tblname){this.tblname=tblname;}

    public Boolean getStatus(){return this.status;}
    public int getCode(){return this.code;}
    public int getOrder(){return this.order;}
    public String getName(){return this.tblname;}
}
