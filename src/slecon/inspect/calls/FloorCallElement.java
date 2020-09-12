package slecon.inspect.calls;
public class FloorCallElement implements Comparable<FloorCallElement>{
    private final int     floor;
    private final boolean front_car;
    private final boolean rear_car;
    private final boolean disabled_car;
    private final boolean front_hall_up;
    private final boolean front_hall_down;
    private final boolean rear_hall_up;
    private final boolean rear_hall_down;
    private final boolean dis_front_hall_up;
    private final boolean dis_front_hall_down;
    private final boolean dis_rear_hall_up;
    private final boolean dis_rear_hall_down;



    public FloorCallElement ( int floor, boolean front_car, boolean rear_car, boolean disabled_car, boolean front_hall_up, boolean front_hall_down,
    						  boolean rear_hall_up, boolean rear_hall_down, boolean dis_front_hall_up, boolean dis_front_hall_down, boolean dis_rear_hall_up, boolean dis_rear_hall_down) {
        super();
        this.floor = floor;
        this.front_car = front_car;
        this.rear_car = rear_car;
        this.disabled_car = disabled_car;
        this.front_hall_up = front_hall_up;
        this.front_hall_down = front_hall_down;
        this.rear_hall_up = rear_hall_up;
        this.rear_hall_down = rear_hall_down;
        this.dis_front_hall_up = dis_front_hall_up;
        this.dis_front_hall_down = dis_front_hall_down;
        this.dis_rear_hall_up = dis_rear_hall_up;
        this.dis_rear_hall_down = dis_rear_hall_down;
    }

    public boolean isFrontCarPresent () {
    	return front_car;
    }
    
    public boolean isRearCarPresent () {
    	return rear_car;
    }
    
    public boolean isDisabledCarPresent () {
    	return disabled_car;
    }
    
    public boolean isFrontUpPresent () {
        return front_hall_up;
    }

    public boolean isFrontDownPresent () {
        return front_hall_down;
    }
    
    public boolean isRearUpPresent () {
        return rear_hall_up;
    }

    public boolean isRearDownPresent () {
        return rear_hall_down;
    }
    
    public boolean isDisFrontUpPresent () {
        return dis_front_hall_up;
    }

    public boolean isDisFrontDownPresent () {
        return dis_front_hall_down;
    }
    
    public boolean isDisRearUpPresent () {
        return dis_rear_hall_up;
    }

    public boolean isDisRearDownPresent () {
        return dis_rear_hall_down;
    }
    
    public int getFloor () {
        return floor;
    }
    
    @Override
    public int compareTo ( FloorCallElement o ) {
        return new Integer( floor ).compareTo( o.floor );
    }
}
