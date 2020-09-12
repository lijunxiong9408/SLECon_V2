package slecon.inspect.devices;
public class IoStatusBean {
    public final int ioCount;
    public final int inputPort;
    public final int outputPort;
    public final int sync;
    public final int blink;
    public final int fast;




    public IoStatusBean ( int ioCount, int inputPort, int outputPort, int sync, int blink, int fast ) {
        this.ioCount    = ioCount;
        this.inputPort  = inputPort;
        this.outputPort = outputPort;
        this.sync       = sync;
        this.blink      = blink;
        this.fast       = fast;
    }
}
