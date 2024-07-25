import java.io.Serializable;

public class Segment implements Serializable{
    public int src_port; // currently not used
    public int dest_port; // currently not used
    public int seq_num; // currently not used
    public int length; // currently not used
    public int type; // segment type
    public int rcv_win; // currently not used
    public int checksum; // currently not used
    public char[] data;
    public int MAXCHARS;
    
    public Segment(int s, int d, int ss, int len, int t, int rcv, int check, int MAXCHARS) {
        this.src_port = s;
        this.dest_port = d;
        this.seq_num = ss;
        this.length = len;
        this.type = t;
        this.rcv_win = rcv;
        this.checksum = check;
        this.MAXCHARS = MAXCHARS;
        data = new char[MAXCHARS];
    }
}