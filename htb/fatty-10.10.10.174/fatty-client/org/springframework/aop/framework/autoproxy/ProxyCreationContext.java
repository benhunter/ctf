/*    */ package org.springframework.aop.framework.autoproxy;
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
/*    */ public final class ProxyCreationContext
/*    */ {
/* 33 */   private static final ThreadLocal<String> currentProxiedBeanName = (ThreadLocal<String>)new NamedThreadLocal("Name of currently proxied bean");
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
/*    */   @Nullable
/*    */   public static String getCurrentProxiedBeanName() {
/* 47 */     return currentProxiedBeanName.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void setCurrentProxiedBeanName(@Nullable String beanName) {
/* 55 */     if (beanName != null) {
/* 56 */       currentProxiedBeanName.set(beanName);
/*    */     } else {
/*    */       
/* 59 */       currentProxiedBeanName.remove();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/autoproxy/ProxyCreationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */