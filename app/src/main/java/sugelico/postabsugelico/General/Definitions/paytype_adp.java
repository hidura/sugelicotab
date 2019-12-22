package sugelico.postabsugelico.General.Definitions;

/**
 * Created by hidura on 3/18/2018.
 */

public class paytype_adp {
    public int paytype;
    public String paytype_name;

    public paytype_adp( int paytype, String paytype_name)
    {
        this.paytype=paytype;
        this.paytype_name=paytype_name;
    }
    public String toString()
    {
        return( paytype_name );
    }
}
