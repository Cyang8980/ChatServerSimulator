import java.io.Serializable;

public class Message implements Serializable {
    String text;
    Integer from;
    String to; 
    

    public Message(String txt, Integer frm, String to) {
        this.text = txt;
        this.from = frm;
        this.to = to;
        
    }
    
    public String getText() {
        return this.text;
    }

    public Integer getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    
}
