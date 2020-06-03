/*     */ package htb.fatty.shared.message;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Message
/*     */ {
/*     */   static boolean DEBUG = false;
/*     */   int messageType;
/*     */   int timestamp;
/*     */   byte[] sessionID;
/*     */   byte[] payload;
/*     */   
/*     */   public Message(int messageType, int timestamp, byte[] sessionID) {
/*  26 */     this.messageType = messageType;
/*  27 */     this.timestamp = timestamp;
/*  28 */     this.sessionID = sessionID;
/*     */   }
/*     */   
/*     */   public byte[] getPayload() {
/*  32 */     return this.payload;
/*     */   }
/*     */   
/*     */   public void setPayload(byte[] payload) {
/*  36 */     this.payload = payload;
/*     */   }
/*     */   
/*     */   public int getMessageType() {
/*  40 */     return this.messageType;
/*     */   }
/*     */   
/*     */   public int getTimestamp() {
/*  44 */     return this.timestamp;
/*     */   }
/*     */   
/*     */   public byte[] getSessionID() {
/*  48 */     return this.sessionID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toBytes() throws MessageBuildException {
/*  61 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/*  62 */     DataOutputStream dataOutput = new DataOutputStream(output);
/*     */     
/*     */     try {
/*  65 */       dataOutput.writeInt(this.messageType);
/*  66 */       dataOutput.writeInt(this.timestamp);
/*  67 */       dataOutput.write(this.sessionID);
/*  68 */       dataOutput.write(this.payload);
/*  69 */     } catch (IOException e) {
/*  70 */       throw new MessageBuildException("Failed to transform message to bytes");
/*     */     } 
/*     */     
/*  73 */     return output.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes() throws MessageBuildException {
/*  82 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/*  83 */     DataOutputStream out = new DataOutputStream(output);
/*     */     
/*  85 */     byte[] signature = sign();
/*     */     
/*  87 */     if (DEBUG) {
/*  88 */       System.out.println("[+] Message Type: " + this.messageType);
/*  89 */       System.out.println("[+] Timestamp: " + this.timestamp);
/*  90 */       System.out.println("[+] SessionID: " + DatatypeConverter.printHexBinary(this.sessionID));
/*  91 */       System.out.println("[+] Signature: " + DatatypeConverter.printHexBinary(signature));
/*  92 */       System.out.println("[+] PayloadLength: " + this.payload.length);
/*  93 */       System.out.println("[+] Payload: " + DatatypeConverter.printHexBinary(this.payload));
/*     */     } 
/*     */     try {
/*  96 */       out.writeInt(this.messageType);
/*  97 */       out.writeInt(this.timestamp);
/*  98 */       out.write(this.sessionID);
/*  99 */       out.write(signature);
/* 100 */       out.writeInt(this.payload.length);
/* 101 */       out.write(this.payload);
/* 102 */     } catch (IOException e) {
/* 103 */       throw new MessageBuildException("Failed to transform message to bytes");
/*     */     } 
/* 105 */     return output.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] sign() throws MessageBuildException {
/* 114 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 115 */     byte[] message = toBytes();
/*     */     
/*     */     try {
/* 118 */       output.write(message);
/* 119 */       output.write("clarabibi2019!".getBytes());
/* 120 */       output.write(this.sessionID);
/* 121 */     } catch (IOException e1) {
/* 122 */       throw new MessageBuildException("Failed to sign the message");
/*     */     } 
/*     */     
/* 125 */     byte[] hashString = output.toByteArray();
/* 126 */     MessageDigest digest = null;
/*     */     try {
/* 128 */       digest = MessageDigest.getInstance("SHA-256");
/* 129 */     } catch (NoSuchAlgorithmException e) {
/* 130 */       e.printStackTrace();
/*     */     } 
/* 132 */     return digest.digest(hashString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validate(byte[] signature) throws MessageBuildException {
/* 142 */     byte[] mySignature = sign();
/* 143 */     return Arrays.equals(signature, mySignature);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Message recv(InputStream input) throws MessageParseException, MessageBuildException {
/* 153 */     int timestamp = 0;
/* 154 */     int messageType = 0;
/*     */     
/* 156 */     byte[] sessionID = new byte[128];
/* 157 */     byte[] signature = new byte[32];
/* 158 */     byte[] payload = null;
/* 159 */     DataInputStream in = new DataInputStream(input);
/*     */     try {
/* 161 */       messageType = in.readInt();
/* 162 */       timestamp = in.readInt();
/*     */       
/* 164 */       if (DEBUG) {
/* 165 */         System.out.println("[+] MessageType: " + messageType);
/* 166 */         System.out.println("[+] Timestamp: " + timestamp);
/*     */       } 
/*     */       
/* 169 */       int session_length = in.read(sessionID);
/* 170 */       if (session_length != 128) {
/* 171 */         throw new MessageParseException("SessionID has wrong Size");
/*     */       }
/* 173 */       if (DEBUG) {
/* 174 */         System.out.println(DatatypeConverter.printHexBinary(sessionID));
/*     */       }
/*     */       
/* 177 */       int signatureLenth = in.read(signature);
/* 178 */       if (signatureLenth != 32) {
/* 179 */         throw new MessageParseException("Signature has wrong Size!");
/*     */       }
/* 181 */       if (DEBUG) {
/* 182 */         System.out.println(DatatypeConverter.printHexBinary(signature));
/*     */       }
/*     */       
/* 185 */       int payloadLength = in.readInt();
/* 186 */       if (DEBUG) {
/* 187 */         System.out.println("[+] Payload Length:" + payloadLength);
/*     */       }
/*     */       
/* 190 */       byte[] buffer = new byte[2048];
/* 191 */       ByteArrayOutputStream aOut = new ByteArrayOutputStream();
/* 192 */       int read = 0;
/*     */       
/* 194 */       while (payloadLength > 2048) {
/* 195 */         read = in.read(buffer);
/* 196 */         aOut.write(buffer, 0, read);
/* 197 */         payloadLength -= read;
/*     */       } 
/*     */       
/* 200 */       read = in.read(buffer, 0, payloadLength);
/* 201 */       aOut.write(buffer, 0, read);
/* 202 */       payloadLength -= read;
/*     */       
/* 204 */       payload = aOut.toByteArray();
/* 205 */       aOut.close();
/*     */       
/* 207 */       if (payloadLength != 0) {
/* 208 */         throw new MessageParseException("Real payloadLength != payloadLength");
/*     */       }
/* 210 */       if (DEBUG) {
/* 211 */         System.out.println("[+] Payload: " + DatatypeConverter.printHexBinary(payload));
/*     */       }
/*     */     }
/* 214 */     catch (IOException e) {
/* 215 */       throw new MessageParseException("Unknown Error");
/*     */     } 
/*     */     
/* 218 */     Message incomingMessage = new Message(messageType, timestamp, sessionID);
/* 219 */     incomingMessage.setPayload(payload);
/* 220 */     if (!incomingMessage.validate(signature)) {
/* 221 */       throw new MessageParseException("Invalid Signature!");
/*     */     }
/* 223 */     return incomingMessage;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/shared/message/Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */