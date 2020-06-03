/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.Charset;
/*    */ import org.springframework.http.HttpHeaders;
/*    */ import org.springframework.http.HttpRequest;
/*    */ import org.springframework.http.client.ClientHttpRequestExecution;
/*    */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*    */ import org.springframework.http.client.ClientHttpResponse;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class BasicAuthenticationInterceptor
/*    */   implements ClientHttpRequestInterceptor
/*    */ {
/*    */   private final String username;
/*    */   private final String password;
/*    */   @Nullable
/*    */   private final Charset charset;
/*    */   
/*    */   public BasicAuthenticationInterceptor(String username, String password) {
/* 57 */     this(username, password, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BasicAuthenticationInterceptor(String username, String password, @Nullable Charset charset) {
/* 69 */     Assert.doesNotContain(username, ":", "Username must not contain a colon");
/* 70 */     this.username = username;
/* 71 */     this.password = password;
/* 72 */     this.charset = charset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
/* 80 */     HttpHeaders headers = request.getHeaders();
/* 81 */     if (!headers.containsKey("Authorization")) {
/* 82 */       headers.setBasicAuth(this.username, this.password, this.charset);
/*    */     }
/* 84 */     return execution.execute(request, body);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/support/BasicAuthenticationInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */