package comm.event;




public interface LiftDataChangedListener {
    public void onConnCreate ();


    public void onDataChanged ( long timestamp, int msg );


    public void onConnLost ();
}
