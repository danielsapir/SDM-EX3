package SDM.Exception;


//noy 11/11
public class DuplicateZoneNameException extends Exception
{
    private String zoneName;
    public DuplicateZoneNameException(String name) {
        this.zoneName = name;
    }

    public String getZoneName() {
        return zoneName;
    }

}
