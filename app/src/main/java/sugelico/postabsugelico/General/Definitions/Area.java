package sugelico.postabsugelico.General.Definitions;

import java.util.HashMap;

/**
 * Created by hidura on 11/6/2016.
 */

public class Area {
    private Integer code;
    private Integer order;
    private String area_name;
    private Integer status;
    private HashMap<Integer, TableItem> tables;

    public Area(){}
    public void setArea_name(String area_name){this.area_name=area_name;}
    public void setStatus(Integer status){this.status=status;}
    public void setCode(int code){this.code=code;}
    public void setOrder(int order){this.order=order;}
    public void setTables(HashMap<Integer, TableItem> tables){this.tables=tables;}


    public String getArea_name(){return this.area_name;}
    public Integer getStatus(){return this.status;}
    public int getCode(){return this.code;}
    public int getOrder(){return this.order;}
    public HashMap<Integer, TableItem> getTables(){return this.tables;}
}
