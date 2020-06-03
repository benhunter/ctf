/*    */ package org.springframework.http.client.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.springframework.http.HttpRequest;
/*    */ import org.springframework.http.client.ClientHttpRequestExecution;
/*    */ import org.springframework.http.client.ClientHttpRequestInterceptor;
/*    */ import org.springframework.http.client.ClientHttpResponse;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.Base64Utils;
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
/*    */ @Deprecated
/*    */ public class BasicAuthorizationInterceptor
/*    */   implements ClientHttpRequestInterceptor
/*    */ {
/*    */   private final String username;
/*    */   private final String password;
/*    */   
/*    */   public BasicAuthorizationInterceptor(@Nullable String username, @Nullable String password) {
/* 54 */     Assert.doesNotContain(username, ":", "Username must not contain a colon");
/* 55 */     this.username = (username != null) ? username : "";
/* 56 */     this.password = (password != null) ? password : "";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
/* 64 */     String token = Base64Utils.encodeToString((this.username + ":" + this.password)
/* 65 */         .getBytes(StandardCharsets.UTF_8));
/* 66 */     request.getHeaders().add("Authorization", "Basic " + token);
/* 67 */     return execution.execute(request, body);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/support/BasicAuthorizationInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */