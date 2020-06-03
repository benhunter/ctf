/*    */ package org.springframework.remoting.jaxws;
/*    */ 
/*    */ import javax.jws.WebService;
/*    */ import javax.xml.ws.Endpoint;
/*    */ import javax.xml.ws.WebServiceProvider;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleJaxWsServiceExporter
/*    */   extends AbstractJaxWsServiceExporter
/*    */ {
/*    */   public static final String DEFAULT_BASE_ADDRESS = "http://localhost:8080/";
/* 47 */   private String baseAddress = "http://localhost:8080/";
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
/*    */   public void setBaseAddress(String baseAddress) {
/* 60 */     this.baseAddress = baseAddress;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void publishEndpoint(Endpoint endpoint, WebService annotation) {
/* 66 */     endpoint.publish(calculateEndpointAddress(endpoint, annotation.serviceName()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void publishEndpoint(Endpoint endpoint, WebServiceProvider annotation) {
/* 71 */     endpoint.publish(calculateEndpointAddress(endpoint, annotation.serviceName()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String calculateEndpointAddress(Endpoint endpoint, String serviceName) {
/* 81 */     String fullAddress = this.baseAddress + serviceName;
/* 82 */     if (endpoint.getClass().getName().startsWith("weblogic."))
/*    */     {
/* 84 */       fullAddress = fullAddress + "/";
/*    */     }
/* 86 */     return fullAddress;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/jaxws/SimpleJaxWsServiceExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */