package OurCode.Helpers;

public class LocationMessageParser {
    public String type = "info";
    public float x;
    public float y;
    public float yaw;

    public LocationMessageParser(){};

    public LocationMessageParser(float x, float y, float yaw){
        this.x = x;
        this.y = y;
        this.yaw = yaw;
    }

    /** Parse info to string from parameters. **/
    public static String info_to_string(float x, float y, float yaw) {
        StringBuilder s = new StringBuilder();
        s.append("info").append(",");
        s.append(x).append(",");
        s.append(y).append(",");
        s.append(yaw);
        return s.toString();
    }

    /** Parse info to string. **/
    public static String info_to_string(LocationMessageParser info) {
        StringBuilder s = new StringBuilder();
        s.append(info.type).append(",");
        s.append(info.x).append(",");
        s.append(info.y).append(",");
        s.append(info.yaw);
        return s.toString();
    }

    /** Parse string to info. **/
    public static LocationMessageParser string_to_info(String s) {
        String[] info_string = s.split(",");
        if (!info_string[0].equals("info")) {
            throw new IllegalArgumentException("String is not an info message");
        }
        LocationMessageParser info = new LocationMessageParser();
        info.x = Float.parseFloat(info_string[1]);
        info.y = Float.parseFloat(info_string[2]);
        info.yaw = Float.parseFloat(info_string[3]);
        return info;
    }
}
