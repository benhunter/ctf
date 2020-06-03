/*    */ package htb.fatty.shared.message;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class LogoffMessage
/*    */   extends Message {
/*  9 */   public static int messageType = 4919;
/*    */   
/*    */   public LogoffMessage(byte[] sessionID) {
/* 12 */     super(messageType, (int)(new Date()).getTime(), sessionID);
/*    */   }
/*    */   
/*    */   public void send(OutputStream output) throws MessageBuildException, IOException {
/* 16 */     setPayload("hundekuchen".getBytes());
/* 17 */     byte[] message = getBytes();
/* 18 */     output.write(message);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/shared/message/LogoffMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */