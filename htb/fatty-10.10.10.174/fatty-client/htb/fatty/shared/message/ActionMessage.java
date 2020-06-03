/*     */ package htb.fatty.shared.message;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class ActionMessage
/*     */   extends Message {
/*  14 */   public static int messageType = 65433;
/*     */   private String command;
/*     */   private ArrayList<String> arguments;
/*     */   
/*     */   public ActionMessage(byte[] sessionID, String command) {
/*  19 */     super(messageType, (int)(new Date()).getTime(), sessionID);
/*  20 */     this.command = command;
/*  21 */     this.arguments = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public void setArguments(ArrayList<String> args) {
/*  25 */     this.arguments = args;
/*     */   }
/*     */   
/*     */   public ArrayList<String> getArguments() {
/*  29 */     return this.arguments;
/*     */   }
/*     */   
/*     */   public String getCommand() {
/*  33 */     return this.command;
/*     */   }
/*     */   
/*     */   public void addArgument(String arg) {
/*  37 */     this.arguments.add(arg);
/*     */   }
/*     */   
/*     */   public ActionMessage(Message message) throws MessageParseException, MessageLogoffException {
/*  41 */     super(message.messageType, message.timestamp, message.sessionID);
/*     */     
/*  43 */     if (message.messageType == LogoffMessage.messageType) {
/*  44 */       throw new MessageLogoffException("Client wants to disconnect!");
/*     */     }
/*     */     
/*  47 */     if (message.messageType != messageType) {
/*  48 */       throw new MessageParseException("Incoming message is not an action message!");
/*     */     }
/*     */     
/*  51 */     ByteArrayInputStream bIn = new ByteArrayInputStream(message.payload);
/*  52 */     DataInputStream dIn = new DataInputStream(bIn);
/*     */ 
/*     */     
/*     */     try {
/*  56 */       int commandLength = dIn.readInt();
/*  57 */       byte[] bCommand = new byte[commandLength];
/*     */       
/*  59 */       int realCommandLength = dIn.read(bCommand);
/*  60 */       if (realCommandLength != commandLength) {
/*  61 */         throw new MessageParseException("Real Command Length != Command Length");
/*     */       }
/*     */       
/*  64 */       this.command = new String(bCommand);
/*  65 */       this.arguments = new ArrayList<>();
/*     */       
/*  67 */       int argumentSize = dIn.readInt();
/*  68 */       int ctr = 0;
/*     */       
/*  70 */       while (ctr != argumentSize) {
/*  71 */         int argLen = dIn.readInt();
/*  72 */         byte[] arg = new byte[argLen];
/*     */         
/*  74 */         int realArgLen = dIn.read(arg);
/*  75 */         if (realArgLen != argLen) {
/*  76 */           throw new MessageParseException("Real Arg Length != Arg Length");
/*     */         }
/*  78 */         this.arguments.add(new String(arg));
/*  79 */         ctr++;
/*     */       } 
/*  81 */     } catch (IOException e) {
/*  82 */       throw new MessageParseException("Unkown Error!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(OutputStream output) throws MessageParseException, MessageBuildException, IOException {
/*  89 */     ByteArrayOutputStream bOut = new ByteArrayOutputStream();
/*  90 */     DataOutputStream dOut = new DataOutputStream(bOut);
/*     */     
/*     */     try {
/*  93 */       dOut.writeInt(this.command.length());
/*  94 */       dOut.write(this.command.getBytes());
/*     */       
/*  96 */       dOut.writeInt(this.arguments.size());
/*  97 */       for (String arg : this.arguments) {
/*  98 */         dOut.writeInt(arg.length());
/*  99 */         dOut.write(arg.getBytes());
/*     */       } 
/*     */       
/* 102 */       setPayload(bOut.toByteArray());
/* 103 */       dOut.close();
/* 104 */       bOut.close();
/* 105 */     } catch (IOException e1) {
/* 106 */       throw new MessageBuildException("Unknown Error");
/*     */     } 
/*     */     
/* 109 */     byte[] message = getBytes();
/*     */     
/* 111 */     int messageLength = message.length;
/* 112 */     int remainder = message.length % 2048;
/* 113 */     int count = 0;
/*     */     
/* 115 */     while (messageLength != remainder) {
/* 116 */       output.write(message, count * 2048, 2048);
/* 117 */       messageLength -= 2048;
/* 118 */       count++;
/*     */     } 
/*     */     
/* 121 */     output.write(message, count * 2048, remainder);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/shared/message/ActionMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */