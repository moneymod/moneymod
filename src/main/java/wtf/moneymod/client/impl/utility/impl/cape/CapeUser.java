package wtf.moneymod.client.impl.utility.impl.cape;

public class CapeUser
{
    private String uuid;
    private String cape;

    public CapeUser( String uuid, String cape )
    {
        this.uuid = uuid;
        this.cape = cape;
    }

    public String getUUID( )
    {
        return uuid;
    }

    public String getCape( )
    {
        return cape;
    }
}