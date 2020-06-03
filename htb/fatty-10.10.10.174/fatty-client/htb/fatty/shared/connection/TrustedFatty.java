/*    */ package htb.fatty.shared.connection;
/*    */ 
/*    */ import htb.fatty.shared.logging.FattyLogger;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.security.KeyManagementException;
/*    */ import java.security.KeyStore;
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.UnrecoverableKeyException;
/*    */ import java.security.cert.CertificateException;
/*    */ import javax.net.ssl.KeyManagerFactory;
/*    */ import javax.net.ssl.SSLContext;
/*    */ import javax.net.ssl.TrustManager;
/*    */ import javax.net.ssl.TrustManagerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TrustedFatty
/*    */ {
/*    */   public String keystorePath;
/*    */   public SSLContext context;
/* 27 */   private FattyLogger logger = new FattyLogger();
/*    */ 
/*    */   
/*    */   public TrustedFatty() {
/* 31 */     this.keystorePath = "";
/*    */   }
/*    */   
/*    */   public TrustedFatty(String keystore) {
/* 35 */     this.keystorePath = keystore;
/*    */   }
/*    */ 
/*    */   
/*    */   public SSLContext getSSLContext() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException {
/* 40 */     KeyStore ks = KeyStore.getInstance("pkcs12");
/* 41 */     this.logger.logInfo("[+] Opening keystore '" + this.keystorePath + "'.");
/* 42 */     ks.load(getClass().getResourceAsStream("/" + this.keystorePath), "secureclarabibi123".toCharArray());
/*    */     
/* 44 */     KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
/* 45 */     kmf.init(ks, "secureclarabibi123".toCharArray());
/*    */     
/* 47 */     TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
/* 48 */     tmf.init(ks);
/*    */     
/* 50 */     SSLContext sc = SSLContext.getInstance("TLS");
/* 51 */     TrustManager[] trustManagers = tmf.getTrustManagers();
/* 52 */     sc.init(kmf.getKeyManagers(), trustManagers, null);
/* 53 */     return sc;
/*    */   }
/*    */   
/*    */   public void setKeystorePath(String path) {
/* 57 */     this.keystorePath = path;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/shared/connection/TrustedFatty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */