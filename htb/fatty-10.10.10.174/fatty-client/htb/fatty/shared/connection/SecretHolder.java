/*    */ package htb.fatty.shared.connection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SecretHolder
/*    */ {
/*    */   private String superSecret;
/*    */   
/*    */   public SecretHolder() {
/* 11 */     this.superSecret = "";
/*    */   }
/*    */   
/*    */   public SecretHolder(String secret) {
/* 15 */     this.superSecret = secret;
/*    */   }
/*    */   
/*    */   public String getSecret() {
/* 19 */     return this.superSecret;
/*    */   }
/*    */   
/*    */   public void setSecret(String secret) {
/* 23 */     this.superSecret = secret;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/htb/fatty/shared/connection/SecretHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */