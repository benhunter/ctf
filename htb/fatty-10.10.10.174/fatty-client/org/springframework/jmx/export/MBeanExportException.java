/*    */ package org.springframework.jmx.export;
/*    */ 
/*    */ import org.springframework.jmx.JmxException;
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
/*    */ public class MBeanExportException
/*    */   extends JmxException
/*    */ {
/*    */   public MBeanExportException(String msg) {
/* 37 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MBeanExportException(String msg, Throwable cause) {
/* 47 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/MBeanExportException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */