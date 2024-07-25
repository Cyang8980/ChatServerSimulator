public class SendBufferNode{
    Segment seg;
    int timesegsent;

    public SendBufferNode(Segment segg, int timeseg){
        this.seg = segg;
        this.timesegsent = timeseg;
    }
}