/*    */ package htb.fatty.shared.message;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResponseMessage
/*    */   extends Message
/*    */ {
/* 17 */   public static int messageType = 65290;
/*    */   protected boolean error;
/*    */   protected byte[] content;
/*    */   
/*    */   public ResponseMessage(byte[] sessionID, boolean error, byte[] content) {
/* 22 */     super(messageType, (int)(new Date()).getTime(), sessionID);
/* 23 */     this.error = error;
/* 24 */     this.content = content;
/*    */   }
/*    */   
/*    */   public ResponseMessage(byte[] sessionID, boolean error, String content) {
/* 28 */     super(messageType, (int)(new Date()).getTime(), sessionID);
/* 29 */     this.error = error;
/* 30 */     this.content = content.getBytes();
/*    */   }
/*    */   
/*    */   public boolean hasError() {
/* 34 */     return this.error;
/*    */   }
/*    */   
/*    */   public String getContentAsString() {
/* 38 */     return new String(this.content);
/*    */   }
/*    */   
/*    */   public byte[] getContent() {
/* 42 */     return this.content;
/*    */   }
/*    */   
/*    */   public ResponseMessage(Message message) throws MessageParseException {
/* 46 */     super(message.messageType, message.timestamp, message.sessionID);
/* 47 */     ByteArrayInputStream input = new ByteArrayInputStream(message.payload);
/* 48 */     DataInputStream dataInput = new DataInputStream(input);
/*    */     try {
/* 50 */       this.error = dataInput.readBoolean();
/*    */       
/* 52 */       int contentLength = dataInput.readInt();
/* 53 */       byte[] bContent = new byte[contentLength];
/*    */       
/* 55 */       if (contentLength != dataInput.read(bContent)) {
/* 56 */         throw new MessageParseException("Unable to reconstruct response Message");
/*    */       }
/*    */       
/* 59 */       this.content = bContent;
/* 60 */       dataInput.close();
/* 61 */       input.close();
/* 62 */     } catch (IOException e) {
/* 63 */       throw new MessageParseException("Unable to reconstruct response Message");
/*    */     } 
/*    */   }
/*    */   
/*    */   public void send(OutputStream output) throws MessageBuildException {
/* 68 */     ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
/* 69 */     DataOutputStream dataOutput = new DataOutputStream(byteOutput);
/*    */     
/*    */     try {
/* 72 */       dataOutput.writeBoolean(this.error);
/* 73 */       dataOutput.writeInt(this.content.length);
/* 74 */       dataOutput.write(this.content);
/* 75 */     } catch (IOException e1) {
/* 76 */       throw new MessageBuildException("Failed to build response message");
/*    */     } 
/*    */     
/* 79 */     setPayload(byteOutput.toByteArray());
/* 80 */     byte[] message = getBytes();
/*    */     
/*    */     try {
/* 83 */       int messageLength = message.length;
/* 84 */       int remainder = message.length % 2048;
/* 85 */       int count = 0;
/*    */       
/* 87 */       while (messageLength != remainder) {
/* 88 */         output.write(message, count * 2048, 2048);
/* 89 */         messageLength -= 2048;
/* 90 */         count++;
/*    */       } 
/*    */       
/* 93 */       output.write(message, count * 2048, remainder);
/*    */       
/* 95 */       dataOutput.close();
/* 96 */       byteOutput.close();
/* 97 */     } catch (IOException e) {
/* 98 */       throw new MessageBuildException("Failed to build response message");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/shared/message/ResponseMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */