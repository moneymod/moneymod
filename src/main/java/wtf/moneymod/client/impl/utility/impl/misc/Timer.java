package wtf.moneymod.client.impl.utility.impl.misc;

public class Timer {
    private long time;
    long startTime = System.currentTimeMillis();
    long delay = 0L;
    boolean paused = false;
    public boolean isPassed() {
        return !this.paused && System.currentTimeMillis() - this.startTime >= this.delay;
    }
    public long getTimePassed() { return System.currentTimeMillis() - this.time; }
    public Timer( ) {
        this.time = -1L;
    }
    public final boolean passed( final long delay ) {
        return passed( delay, false );
    }
    public boolean passed( final long delay, final boolean reset ) {
        if ( reset ) this.reset( );
        return System.currentTimeMillis( ) - this.time >= delay;
    }
    public final void reset( ) {
        this.time = System.currentTimeMillis( );
        this.startTime = System.currentTimeMillis( );
    }
}