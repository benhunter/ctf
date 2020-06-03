/*    */ package org.springframework.remoting.soap;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import org.springframework.remoting.RemoteInvocationFailureException;
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
/*    */ public abstract class SoapFaultException
/*    */   extends RemoteInvocationFailureException
/*    */ {
/*    */   protected SoapFaultException(String msg, Throwable cause) {
/* 41 */     super(msg, cause);
/*    */   }
/*    */   
/*    */   public abstract String getFaultCode();
/*    */   
/*    */   public abstract QName getFaultCodeAsQName();
/*    */   
/*    */   public abstract String getFaultString();
/*    */   
/*    */   public abstract String getFaultActor();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/soap/SoapFaultException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */