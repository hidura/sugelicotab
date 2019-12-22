package sugelico.postabsugelico.General.Definitions;

/**
 * Created by hidura on 9/19/2015.
 */
public class TableItem {

    private String area_name;
    private Integer area;
    private Integer code;
    private Integer order=0;
    private String tblname;
    private Integer status;

    public TableItem(){}
    public void setArea_name(String area_name){this.area_name=area_name;}
    public void setArea(Integer area){this.area=area;}
    public void setTblname(String tblname){this.tblname=tblname;}
    public void setStatus(Integer status){this.status=status;}
    public void setCode(int code){this.code=code;}
    public void setOrder(int order){this.order=order;}

    public String getArea_name(){return this.area_name;}
    public Integer getArea(){return this.area;}
    public String getTblname(){return this.tblname;}
    public Integer getStatus(){return this.status;}
    public int getCode(){return this.code;}
    public int getOrder(){return this.order;}

}
