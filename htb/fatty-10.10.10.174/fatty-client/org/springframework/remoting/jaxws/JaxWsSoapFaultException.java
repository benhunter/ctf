/*    */ package org.springframework.remoting.jaxws;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.SOAPFault;
/*    */ import javax.xml.ws.soap.SOAPFaultException;
/*    */ import org.springframework.remoting.soap.SoapFaultException;
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
/*    */ public class JaxWsSoapFaultException
/*    */   extends SoapFaultException
/*    */ {
/*    */   public JaxWsSoapFaultException(SOAPFaultException original) {
/* 40 */     super(original.getMessage(), (Throwable)original);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final SOAPFault getFault() {
/* 47 */     return ((SOAPFaultException)getCause()).getFault();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getFaultCode() {
/* 53 */     return getFault().getFaultCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public QName getFaultCodeAsQName() {
/* 58 */     return getFault().getFaultCodeAsQName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFaultString() {
/* 63 */     return getFault().getFaultString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFaultActor() {
/* 68 */     return getFault().getFaultActor();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/jaxws/JaxWsSoapFaultException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */