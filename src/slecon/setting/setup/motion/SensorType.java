package slecon.setting.setup.motion;

public enum SensorType {
    URL$LRL,
    LPS01;
    
    public String toString() {
        return name().replace( "$", "/" );
    }
}
