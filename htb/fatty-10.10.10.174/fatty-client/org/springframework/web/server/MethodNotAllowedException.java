/*    */ package org.springframework.web.server;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.http.HttpMethod;
/*    */ import org.springframework.http.HttpStatus;
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
/*    */ public class MethodNotAllowedException
/*    */   extends ResponseStatusException
/*    */ {
/*    */   private final String method;
/*    */   private final Set<HttpMethod> supportedMethods;
/*    */   
/*    */   public MethodNotAllowedException(HttpMethod method, Collection<HttpMethod> supportedMethods) {
/* 44 */     this(method.name(), supportedMethods);
/*    */   }
/*    */   
/*    */   public MethodNotAllowedException(String method, @Nullable Collection<HttpMethod> supportedMethods) {
/* 48 */     super(HttpStatus.METHOD_NOT_ALLOWED, "Request method '" + method + "' not supported");
/* 49 */     Assert.notNull(method, "'method' is required");
/* 50 */     if (supportedMethods == null) {
/* 51 */       supportedMethods = Collections.emptySet();
/*    */     }
/* 53 */     this.method = method;
/* 54 */     this.supportedMethods = Collections.unmodifiableSet(new HashSet<>(supportedMethods));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getHttpMethod() {
/* 62 */     return this.method;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<HttpMethod> getSupportedMethods() {
/* 69 */     return this.supportedMethods;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/MethodNotAllowedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */