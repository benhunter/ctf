/*    */ package org.springframework.jmx.export;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnableToRegisterMBeanException
/*    */   extends MBeanExportException
/*    */ {
/*    */   public UnableToRegisterMBeanException(String msg) {
/* 35 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UnableToRegisterMBeanException(String msg, Throwable cause) {
/* 45 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/UnableToRegisterMBeanException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */