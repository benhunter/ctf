/*    */ package org.springframework.jmx.access;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MBeanInfoRetrievalException
/*    */   extends JmxException
/*    */ {
/*    */   public MBeanInfoRetrievalException(String msg) {
/* 40 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MBeanInfoRetrievalException(String msg, Throwable cause) {
/* 50 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/access/MBeanInfoRetrievalException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */