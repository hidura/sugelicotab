package sugelico.postabsugelico.General;

/**
 * Created by hidura on 7/1/2015.
 */
public class connection {
    private final String NAMESPACE = "http://my.sugelico.com/";
    private final String NAMESPACEWS = "http://my.sugelico.com/";
    private final String ROOTNAMESPACEWS = "http://my.sugelico.com/";
    private final String URL = "http://my.sugelico.com";//I added this because i don't know if some day they going to use SOAP or REST.
    private final String PORT = "80";



    public connection() {
        // TODO Auto-generated constructor stub
    }

    public String NameSPaceWS(){
        return this.NAMESPACEWS;
    }
    public String ROOTNameSpace(){
        return this.ROOTNAMESPACEWS;
    }
    public String NameSpace()
    {
        return this.NAMESPACE;

    }

    public String Url()
    {
        return this.URL;

    }

    public String Port()
    {
        return this.PORT;
    }
}
