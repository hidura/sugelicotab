package sugelico.postabsugelico.General.Definitions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sugelico.postabsugelico.SugelicoMobile;

/**
 * Created by hidura on 9/21/2016.
 */
public class UserProfile implements java.io.Serializable{
    private int code;
    private String name;
    private String email;
    private String avatar;
    private String session;
    private Integer usertype;
    private String mainFolder="sugelicomobile";
    private Integer bill;
    private Integer billtype;
    private Integer ordertype;
    private Integer cashbox;
    private Integer table_sel;
    private HashMap<Integer, Integer> tablebill;
    private HashMap<Integer, Integer> clientbill;
    private List<Object> companies;
    private Integer company;
    private ArrayList<ActiveOrders> activeorders;
    private ClientStruct client;
    private HashMap<String, ClientStruct> clients;
    private RuleList rules;
    private android.app.AlertDialog dialog;
    private Integer billCopies;
    private JSONObject cxcbillpaid;
    private JSONObject cashbox_info;
    private HashMap<Integer, SugelicoMobileBill> sugelicomobilebills;
    public UserProfile(){}

    public void setOrdertype(Integer ordertype) {
        this.ordertype = ordertype;
    }

    public void setSugelicomobilebills(HashMap<Integer, SugelicoMobileBill> sugelicomobilebills) {
        this.sugelicomobilebills = sugelicomobilebills;
    }

    public void setName(String name){this.name=name;}

    public void setRules(RuleList rules) {
        this.rules = rules;
    }
    public void setCashbox_info(JSONObject cashbox_info){this.cashbox_info=cashbox_info;}
    public void setCxcbillpaid(JSONObject cxcbillpaid) {
        this.cxcbillpaid = cxcbillpaid;
    }

    public void setCashbox(Integer cashbox){this.cashbox=cashbox;}
    public void setBilltype(Integer billtype){this.billtype=billtype;}
    public void setClient(ClientStruct client){this.client=client;}
    public void setTablebill(HashMap<Integer, Integer> tablebill){this.tablebill=tablebill;}
    public void setClientbill(HashMap<Integer, Integer> clientbill){this.clientbill=clientbill;}
    public void setClients(HashMap<String, ClientStruct> clients){this.clients=clients;}
    public void setActiveorders(ArrayList<ActiveOrders> activeorders){this.activeorders=activeorders;}
    public void setBill(Integer bill){this.bill=bill;}
    public void setTable_sel(Integer table_sel){this.table_sel=table_sel;}
    public void setUsertype(Integer usertype){this.usertype=usertype;}
    public void setEmail(String email){this.email=email;}
    public void setCode(int code){this.code=code;}

    public void setBillCopies(Integer billCopies) {
        this.billCopies = billCopies;
    }

    public void setAvatar(String avatar){this.avatar=avatar;}
    public void setSession(String session){this.session=session;}
    public void setCompanies(List<Object> companies) {
        this.companies = companies;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }
    public void setDialog(android.app.AlertDialog dialog){this.dialog=dialog;}



    public int getCode(){return this.code;}
    public JSONObject getCxcbillpaid() {
        return cxcbillpaid;
    }
    public Integer getOrdertype() {
        return ordertype;
    }

    public Integer getBillCopies() {
        return billCopies;
    }

    public Integer getCashBox(){return this.cashbox;}
    public ClientStruct getClient(){return this.client;}
    public android.app.AlertDialog getDialog(){return this.dialog;}
    public int getBillType(){return this.billtype;}
    public int getUserType(){return this.usertype;}
    public HashMap<String, ClientStruct> getClients(){return this.clients;}
    public String getEmail(){return this.email;}
    public HashMap<Integer, Integer> getTablebill(){return this.tablebill;}
    public HashMap<Integer, Integer> getClientbill(){return this.clientbill;}
    public ArrayList<ActiveOrders> getActiveorders(){return this.activeorders;}
    public String getAvatar(){return this.avatar;}
    public String getMainFolder(){return this.mainFolder;}

    public JSONObject getCashbox_info() {return cashbox_info;}

    public String getName(){return this.name;}

    public void setMainFolder(String mainFolder) {
        this.mainFolder = mainFolder;
    }

    public HashMap<Integer, SugelicoMobileBill> getSugelicomobilebills() {
        return sugelicomobilebills;
    }

    public List<Object> getCompanies() {
        return companies;
    }

    public RuleList getRules() {
        return rules;
    }

    public Integer getCompany() {
        return company;
    }

    public String getSession(){return this.session;}
    public Integer getBill(){return this.bill;}
    public Integer getTable_sel(){return this.table_sel;}

}
