package sugelico.postabsugelico.General.Definitions;

import java.util.ArrayList;

public class  ClientStruct implements java.io.Serializable, Find{

    @Override
    public Boolean contains(Object value) {
        if (value.equals(name)){
            return true;
        }else if (value.equals(code)){
            return true;
        }else if (value.equals(rnc)){
            return true;
        }else if (value.equals(extra)){
            return true;
        }

        return false;
    }

    private String name;
    private Integer code;
    private String rnc;
    private Integer ncf_type;
    private String extra;
    private ArrayList<Integer> ordercodes;
    private Double curcredit;
    private Double max_credit;
    private Boolean credit;

    public ClientStruct(){}


    public void setCode(Integer code){this.code=code;}
    public void setName(String name){this.name=name;}
    public void setRnc(String rnc){this.rnc=rnc;}
    public void setExtra(String extra){this.extra=extra;}

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    public void setCurcredit(Double curcredit) {
        this.curcredit = curcredit;
    }

    public void setMax_credit(Double max_credit) {
        this.max_credit = max_credit;
    }

    public void setNcf_type(Integer ncf_type) {
        this.ncf_type = ncf_type;
    }

    public void setOrdercodes(ArrayList<Integer> ordercodes){this.ordercodes=ordercodes;}

    public Integer getCode(){return (( this.code== null) ? 1 : this.code);}
    public String getName(){return this.name;}
    public String getRNC(){return this.rnc;}
    public String getExtra(){return this.extra;}
    public Integer getNCF_type() {
        return ncf_type;
    }

    public Boolean getCredit() {
        return credit;
    }

    public Double getMax_credit() {
        return max_credit;
    }

    public Double getCurcredit() {
        return curcredit;
    }

    public ArrayList<Integer> getOrdercodes(){return this.ordercodes;}


}