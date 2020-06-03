/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class SimpleKeyGenerator
/*    */   implements KeyGenerator
/*    */ {
/*    */   public Object generate(Object target, Method method, Object... params) {
/* 41 */     return generateKey(params);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object generateKey(Object... params) {
/* 48 */     if (params.length == 0) {
/* 49 */       return SimpleKey.EMPTY;
/*    */     }
/* 51 */     if (params.length == 1) {
/* 52 */       Object param = params[0];
/* 53 */       if (param != null && !param.getClass().isArray()) {
/* 54 */         return param;
/*    */       }
/*    */     } 
/* 57 */     return new SimpleKey(params);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/SimpleKeyGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */