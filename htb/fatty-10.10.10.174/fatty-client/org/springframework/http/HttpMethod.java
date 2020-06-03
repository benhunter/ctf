/*    */ package org.springframework.http;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public enum HttpMethod
/*    */ {
/* 35 */   GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
/*    */   
/*    */   static {
/* 38 */     mappings = new HashMap<>(16);
/*    */ 
/*    */     
/* 41 */     for (HttpMethod httpMethod : values()) {
/* 42 */       mappings.put(httpMethod.name(), httpMethod);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final Map<String, HttpMethod> mappings;
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static HttpMethod resolve(@Nullable String method) {
/* 55 */     return (method != null) ? mappings.get(method) : null;
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
/*    */   public boolean matches(String method) {
/* 67 */     return (this == resolve(method));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/HttpMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */