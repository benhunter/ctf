/*     */ package htb.fatty.client.connection;
/*     */ 
/*     */ import htb.fatty.shared.connection.ConnectionContext;
/*     */ import htb.fatty.shared.connection.SecretHolder;
/*     */ import htb.fatty.shared.connection.TrustedFatty;
/*     */ import htb.fatty.shared.logging.FattyLogger;
/*     */ import htb.fatty.shared.message.LoginMessage;
/*     */ import htb.fatty.shared.message.LogoffMessage;
/*     */ import htb.fatty.shared.message.Message;
/*     */ import htb.fatty.shared.message.MessageBuildException;
/*     */ import htb.fatty.shared.message.ResponseMessage;
/*     */ import htb.fatty.shared.resources.User;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import org.springframework.context.support.ClassPathXmlApplicationContext;
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
/*     */ public class Connection
/*     */ {
/*     */   protected SSLSocket clientSocket;
/*     */   protected ConnectionContext connectionContext;
/*     */   protected TrustedFatty sslContext;
/*     */   protected SecretHolder secretHolder;
/*     */   protected byte[] sessionID;
/*     */   protected InputStream serverInput;
/*     */   protected OutputStream serverOutput;
/*  41 */   private static FattyLogger logger = new FattyLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection(ConnectionContext connectionContext, TrustedFatty sslContext, SecretHolder secretHolder) {
/*  51 */     this.connectionContext = connectionContext;
/*  52 */     this.sslContext = sslContext;
/*  53 */     this.secretHolder = secretHolder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect() throws ConnectionException {
/*  61 */     SSLContext realContext = null;
/*     */     try {
/*  63 */       realContext = this.sslContext.getSSLContext();
/*  64 */     } catch (UnrecoverableKeyException|java.security.KeyManagementException|java.security.KeyStoreException|java.security.NoSuchAlgorithmException|java.security.cert.CertificateException|IOException e) {
/*     */       
/*  66 */       throw new ConnectionException("Failed to generate SSLContext!");
/*     */     } 
/*  68 */     SSLSocketFactory ssf = realContext.getSocketFactory();
/*  69 */     SSLSocket s = null;
/*     */     try {
/*  71 */       s = (SSLSocket)ssf.createSocket(this.connectionContext.hostname, this.connectionContext.port);
/*  72 */       s.startHandshake();
/*  73 */     } catch (IOException e) {
/*  74 */       logger.logError("[-] SSL Handshake failure.");
/*  75 */       throw new ConnectionException("SSL Handshake failure!");
/*     */     } 
/*     */     
/*  78 */     int bytesRead = 0;
/*  79 */     byte[] buffer = new byte[128];
/*     */     try {
/*  81 */       this.serverInput = s.getInputStream();
/*  82 */       this.serverOutput = s.getOutputStream();
/*  83 */       bytesRead = this.serverInput.read(buffer, 0, 128);
/*  84 */     } catch (IOException e) {
/*  85 */       logger.logError("[-] SessionID could not be obtained becuase of an IOException.");
/*  86 */       throw new ConnectionException("Failed to obtain SessionID from the server!");
/*     */     } 
/*     */     
/*  89 */     if (bytesRead != 128) {
/*  90 */       logger.logError("[-] Obtianed SessionID is to short.");
/*  91 */       throw new ConnectionException("SessionID to short.");
/*     */     } 
/*     */     
/*  94 */     this.sessionID = buffer;
/*  95 */     logger.logInfo("[+] Obtained SessionID: '" + DatatypeConverter.printHexBinary(this.sessionID) + "'");
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
/*     */   public String getRoleName() {
/*     */     ResponseMessage rMessage;
/*     */     try {
/* 110 */       Message response = Message.recv(this.serverInput);
/* 111 */       rMessage = new ResponseMessage(response);
/*     */     
/*     */     }
/* 114 */     catch (Exception e) {
/*     */       
/* 116 */       logger.logError("[-] Failed read response message from server!");
/* 117 */       logger.logError("[-] User role is set to the default role: anonymous.");
/* 118 */       return "anonymous";
/*     */     } 
/*     */ 
/*     */     
/* 122 */     return rMessage.getContentAsString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean login(User user) {
/*     */     ResponseMessage rMessage;
/* 132 */     LoginMessage lmessage = new LoginMessage(this.sessionID, user);
/*     */ 
/*     */     
/*     */     try {
/* 136 */       lmessage.send(this.serverOutput);
/*     */     }
/* 138 */     catch (MessageBuildException e) {
/*     */ 
/*     */       
/* 141 */       logger.logError("[-] Failed to construct login message!");
/* 142 */       return false;
/*     */     }
/* 144 */     catch (IOException e1) {
/*     */ 
/*     */       
/* 147 */       logger.logError("[-] Failure while sending the login message.");
/* 148 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 158 */       Message response = Message.recv(this.serverInput);
/* 159 */       rMessage = new ResponseMessage(response);
/*     */     }
/* 161 */     catch (Exception e) {
/*     */ 
/*     */       
/* 164 */       logger.logError("[-] Unable to read response message from the server.");
/* 165 */       logger.logError("[-] Login failed.");
/* 166 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 171 */     if (rMessage.hasError()) {
/*     */ 
/*     */       
/* 174 */       logger.logError("[-] Login failed. Server answer was: '" + rMessage.getContent() + "'.");
/* 175 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 180 */     logger.logInfo("[+] Login successful!");
/* 181 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean logoff() {
/* 191 */     LogoffMessage logoff = new LogoffMessage(this.sessionID);
/* 192 */     logger.logInfo("[+] Sending logoff message to the server");
/*     */     try {
/* 194 */       logoff.send(this.serverOutput);
/* 195 */     } catch (Exception e) {
/* 196 */       logger.logError("[-] Failure while sending the logoff message");
/* 197 */       return false;
/*     */     } 
/* 199 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*     */     try {
/* 209 */       logger.logInfo("[+] Closing server input stream.");
/* 210 */       this.serverInput.close();
/*     */     }
/* 212 */     catch (IOException e) {
/*     */       
/* 214 */       logger.logInfo("[+] Server input stream was already closed.");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 219 */       logger.logInfo("[+] Closing server output stream.");
/* 220 */       this.serverOutput.close();
/*     */     }
/* 222 */     catch (IOException e) {
/*     */       
/* 224 */       logger.logInfo("[+] Server output stream was already closed.");
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 229 */       logger.logInfo("[+] Closing client socket.");
/* 230 */       this.clientSocket.close();
/*     */     }
/* 232 */     catch (Exception e) {
/*     */       
/* 234 */       logger.logInfo("[+] Client socket was already closed.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Connection getConnection() throws ConnectionException {
/* 245 */     ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("beans.xml");
/* 246 */     Connection obj = (Connection)classPathXmlApplicationContext.getBean("connection");
/* 247 */     logger.logInfo("[+] Starting connection to: '" + obj.getHostname() + ":" + obj.getPort() + "'.");
/* 248 */     obj.connect();
/* 249 */     logger.logInfo("[+] Connection process finished.");
/* 250 */     return obj;
/*     */   }
/*     */   
/*     */   public String getHostname() {
/* 254 */     return this.connectionContext.hostname;
/*     */   }
/*     */   
/*     */   public String getPort() {
/* 258 */     return Integer.toString(this.connectionContext.port);
/*     */   }
/*     */   
/*     */   public byte[] getSessionID() {
/* 262 */     return this.sessionID;
/*     */   }
/*     */   
/*     */   public OutputStream getServerOutputStream() {
/* 266 */     return this.serverOutput;
/*     */   }
/*     */   
/*     */   public InputStream getServerInputStream() {
/* 270 */     return this.serverInput;
/*     */   }
/*     */   
/*     */   public class ConnectionException
/*     */     extends Exception {
/*     */     public ConnectionException(String s) {
/* 276 */       super(s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/client/connection/Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */