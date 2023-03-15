package algonquin.cst2335.curr0263;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage
{
    @ColumnInfo (name = "message")
    public String message;

    @ColumnInfo (name = "timeSent")
    public String timeSent;

    @ColumnInfo (name="isSend")
    public boolean isSend;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="id")
    public long id;

    ChatMessage(String message, String timeSent, boolean isSend)
    {
        this.message = message;
        this.timeSent = timeSent;
        this.isSend = isSend;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean getIsSent() {
        return isSend;
    }


}
