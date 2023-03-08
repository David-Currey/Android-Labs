package algonquin.cst2335.curr0263;



public class ChatMessage
{
   String message;
   String timeSent;
   boolean isSend;

   ChatMessage(String m, String t, boolean sent)
   {
       message = m;
       timeSent = t;
       isSend = sent;
   }

   public String getMessage()
   {
       return message;
   }

   public String getTimeSent()
   {
       return timeSent;
   }

   public boolean getIsSent()
   {
       return isSend;
   }



}
