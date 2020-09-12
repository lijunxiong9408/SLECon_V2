package slecon.inspect.iostatus;

public enum OnOffStatus {
    ON, OFF, DISABLED;

    private final String str;

    OnOffStatus () {
        str = IOStatusC01.TEXT.getString("OnOffStatus" + "." + name());
    }


    public String toString () {
        return str;
    }
}