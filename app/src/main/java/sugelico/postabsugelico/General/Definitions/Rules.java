package sugelico.postabsugelico.General.Definitions;

public class Rules  implements java.io.Serializable, Find{

    private Integer code;
    private String name;
    public Rules(){

    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Boolean contains(Object value) {
        return null;
    }
}
