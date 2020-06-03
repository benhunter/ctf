/*    */ package org.springframework.aop.framework;
/*    */ 
/*    */ import org.springframework.core.NamedThreadLocal;
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
/*    */ public final class AopContext
/*    */ {
/* 50 */   private static final ThreadLocal<Object> currentProxy = (ThreadLocal<Object>)new NamedThreadLocal("Current AOP proxy");
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
/*    */   public static Object currentProxy() throws IllegalStateException {
/* 67 */     Object proxy = currentProxy.get();
/* 68 */     if (proxy == null) {
/* 69 */       throw new IllegalStateException("Cannot find current proxy: Set 'exposeProxy' property on Advised to 'true' to make it available.");
/*    */     }
/*    */     
/* 72 */     return proxy;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   static Object setCurrentProxy(@Nullable Object proxy) {
/* 84 */     Object old = currentProxy.get();
/* 85 */     if (proxy != null) {
/* 86 */       currentProxy.set(proxy);
/*    */     } else {
/*    */       
/* 89 */       currentProxy.remove();
/*    */     } 
/* 91 */     return old;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/AopContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */