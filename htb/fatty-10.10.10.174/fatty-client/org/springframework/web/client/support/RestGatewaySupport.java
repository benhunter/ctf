/*    */ package org.springframework.web.client.support;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.http.client.ClientHttpRequestFactory;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.client.RestTemplate;
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
/*    */ public class RestGatewaySupport
/*    */ {
/* 39 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */   
/*    */   private RestTemplate restTemplate;
/*    */ 
/*    */ 
/*    */   
/*    */   public RestGatewaySupport() {
/* 48 */     this.restTemplate = new RestTemplate();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RestGatewaySupport(ClientHttpRequestFactory requestFactory) {
/* 56 */     Assert.notNull(requestFactory, "'requestFactory' must not be null");
/* 57 */     this.restTemplate = new RestTemplate(requestFactory);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRestTemplate(RestTemplate restTemplate) {
/* 65 */     Assert.notNull(restTemplate, "'restTemplate' must not be null");
/* 66 */     this.restTemplate = restTemplate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RestTemplate getRestTemplate() {
/* 73 */     return this.restTemplate;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/client/support/RestGatewaySupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */