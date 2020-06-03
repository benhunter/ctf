/*     */ package htb.fatty.client.methods;
/*     */ 
/*     */ import htb.fatty.client.connection.Connection;
/*     */ import htb.fatty.shared.logging.FattyLogger;
/*     */ import htb.fatty.shared.message.ActionMessage;
/*     */ import htb.fatty.shared.message.Message;
/*     */ import htb.fatty.shared.message.MessageBuildException;
/*     */ import htb.fatty.shared.message.MessageParseException;
/*     */ import htb.fatty.shared.message.ResponseMessage;
/*     */ import htb.fatty.shared.resources.User;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Base64;
/*     */ 
/*     */ 
/*     */ public class Invoker
/*     */ {
/*     */   private User user;
/*     */   private byte[] sessionID;
/*     */   private InputStream serverInputStream;
/*     */   private OutputStream serverOutputStream;
/*     */   private ActionMessage action;
/*     */   private Message message;
/*     */   private ResponseMessage response;
/*  28 */   private static FattyLogger logger = new FattyLogger();
/*     */   
/*     */   public Invoker(Connection connection, User user) {
/*  31 */     this.user = user;
/*  32 */     this.sessionID = connection.getSessionID();
/*  33 */     this.serverInputStream = connection.getServerInputStream();
/*  34 */     this.serverOutputStream = connection.getServerOutputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String ping() throws MessageParseException, MessageBuildException, IOException {
/*  40 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/*  41 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/*  42 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/*  43 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/*  46 */     this.action = new ActionMessage(this.sessionID, "ping");
/*  47 */     sendAndRecv();
/*  48 */     if (this.response.hasError()) {
/*  49 */       return "Error: Your action caused an error on the application server!";
/*     */     }
/*  51 */     return this.response.getContentAsString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String whoami() throws MessageParseException, MessageBuildException, IOException {
/*  57 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/*  58 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/*  59 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/*  60 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/*  63 */     this.action = new ActionMessage(this.sessionID, "whoami");
/*  64 */     sendAndRecv();
/*  65 */     if (this.response.hasError()) {
/*  66 */       return "Error: Your action caused an error on the application server!";
/*     */     }
/*  68 */     return this.response.getContentAsString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String showFiles(String folder) throws MessageParseException, MessageBuildException, IOException {
/*  73 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/*  74 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/*  75 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/*  76 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/*  79 */     this.action = new ActionMessage(this.sessionID, "files");
/*  80 */     this.action.addArgument(folder);
/*  81 */     sendAndRecv();
/*  82 */     if (this.response.hasError()) {
/*  83 */       return "Error: Your action caused an error on the application server!";
/*     */     }
/*  85 */     return this.response.getContentAsString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String about() {
/*  91 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/*  92 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/*  93 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/*  94 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/*  97 */     String response = "Dear user,\n\nunfortunately we are currently very busy with writing new functionality for this awesome fatclient.This development takes so much time, that nobody of our staff was available for writing a useful help file so far.However, the client is written by our Java GUI experts who have tons of experience in creating user friendly GUIs.So theoretically, everything should be quite self explanatory. If you find bugs or have questions regarding specific features do not hesitate to contact us!\n\nIf you have urgent problems with the client, you can always decompile it and look at the source code directly :)";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     return response;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String contact() {
/* 110 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/* 111 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/* 112 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/* 113 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/* 116 */     String response = "This client was developed with <3 by qtc.";
/* 117 */     return response;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String open(String foldername, String filename) throws MessageParseException, MessageBuildException, IOException {
/* 123 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/* 124 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/* 125 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/* 126 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/* 129 */     this.action = new ActionMessage(this.sessionID, "open");
/* 130 */     this.action.addArgument(foldername);
/* 131 */     this.action.addArgument(filename);
/* 132 */     sendAndRecv();
/* 133 */     if (this.response.hasError()) {
/* 134 */       return "Error: Your action caused an error on the application server!";
/*     */     }
/* 136 */     String response = "";
/*     */     try {
/* 138 */       response = this.response.getContentAsString();
/* 139 */     } catch (Exception e) {
/* 140 */       response = "Unable to convert byte[] to String. Did you read in a binary file?";
/*     */     } 
/* 142 */     return response;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String changePW(String username, String newPassword) throws MessageParseException, MessageBuildException, IOException {
/* 148 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/* 149 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/* 150 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/* 151 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/* 154 */     User user = new User(username, newPassword);
/* 155 */     ByteArrayOutputStream bOut = new ByteArrayOutputStream();
/*     */     
/*     */     try {
/* 158 */       ObjectOutputStream oOut = new ObjectOutputStream(bOut);
/* 159 */       oOut.writeObject(user);
/* 160 */     } catch (IOException e) {
/* 161 */       e.printStackTrace();
/* 162 */       return "Failure while serializing user object";
/*     */     } 
/* 164 */     byte[] serializedUser64 = Base64.getEncoder().encode(bOut.toByteArray());
/* 165 */     this.action = new ActionMessage(this.sessionID, "changePW");
/* 166 */     this.action.addArgument(new String(serializedUser64));
/* 167 */     sendAndRecv();
/* 168 */     if (this.response.hasError()) {
/* 169 */       return "Error: Your action caused an error on the application server!";
/*     */     }
/* 171 */     return this.response.getContentAsString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String uname() throws MessageParseException, MessageBuildException, IOException {
/* 177 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/* 178 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/* 179 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/* 180 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/* 183 */     this.action = new ActionMessage(this.sessionID, "uname");
/* 184 */     sendAndRecv();
/* 185 */     if (this.response.hasError()) {
/* 186 */       return "Error: Your action caused an error on the application server!";
/*     */     }
/* 188 */     return this.response.getContentAsString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String users() throws MessageParseException, MessageBuildException, IOException {
/* 194 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/* 195 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/* 196 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/* 197 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/* 200 */     this.action = new ActionMessage(this.sessionID, "users");
/* 201 */     sendAndRecv();
/* 202 */     if (this.response.hasError()) {
/* 203 */       return "Error: Your action caused an error on the application server!";
/*     */     }
/* 205 */     return this.response.getContentAsString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String netstat() throws MessageParseException, MessageBuildException, IOException {
/* 211 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/* 212 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/* 213 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/* 214 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/* 217 */     this.action = new ActionMessage(this.sessionID, "netstat");
/* 218 */     sendAndRecv();
/* 219 */     if (this.response.hasError()) {
/* 220 */       return "Error: Your action caused an error on the application server!";
/*     */     }
/* 222 */     return this.response.getContentAsString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String ipconfig() throws MessageParseException, MessageBuildException, IOException {
/* 228 */     String methodName = (new Object() {  }).getClass().getEnclosingMethod().getName();
/* 229 */     logger.logInfo("[+] Method '" + methodName + "' was called by user '" + this.user.getUsername() + "'.");
/* 230 */     if (AccessCheck.checkAccess(methodName, this.user)) {
/* 231 */       return "Error: Method '" + methodName + "' is not allowed for this user account";
/*     */     }
/*     */     
/* 234 */     this.action = new ActionMessage(this.sessionID, "ipconfig");
/* 235 */     sendAndRecv();
/* 236 */     if (this.response.hasError()) {
/* 237 */       return "Error: Your action caused an error on the application server!";
/*     */     }
/* 239 */     return this.response.getContentAsString();
/*     */   }
/*     */   
/*     */   public void sendAndRecv() throws MessageParseException, MessageBuildException, IOException {
/* 243 */     this.action.send(this.serverOutputStream);
/* 244 */     this.message = Message.recv(this.serverInputStream);
/* 245 */     this.response = new ResponseMessage(this.message);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/client/methods/Invoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */