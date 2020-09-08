package Exception;

import java.util.Date;

public class ErrorSpecifics {

    private Date timestamp;
    private String message;
    private String specifics;

    public ErrorSpecifics(Date timestamp, String message, String specifics) {
        this.timestamp = timestamp;
        this.message = message;
        this.specifics = specifics;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSpecifics() {
        return specifics;
    }

    public void setSpecifics(String specifics) {
        this.specifics = specifics;
    }
}
