/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Collection;
/*    */ import org.springframework.cache.Cache;
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
/*    */ class CacheExpressionRootObject
/*    */ {
/*    */   private final Collection<? extends Cache> caches;
/*    */   private final Method method;
/*    */   private final Object[] args;
/*    */   private final Object target;
/*    */   private final Class<?> targetClass;
/*    */   
/*    */   public CacheExpressionRootObject(Collection<? extends Cache> caches, Method method, Object[] args, Object target, Class<?> targetClass) {
/* 47 */     this.method = method;
/* 48 */     this.target = target;
/* 49 */     this.targetClass = targetClass;
/* 50 */     this.args = args;
/* 51 */     this.caches = caches;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<? extends Cache> getCaches() {
/* 56 */     return this.caches;
/*    */   }
/*    */   
/*    */   public Method getMethod() {
/* 60 */     return this.method;
/*    */   }
/*    */   
/*    */   public String getMethodName() {
/* 64 */     return this.method.getName();
/*    */   }
/*    */   
/*    */   public Object[] getArgs() {
/* 68 */     return this.args;
/*    */   }
/*    */   
/*    */   public Object getTarget() {
/* 72 */     return this.target;
/*    */   }
/*    */   
/*    */   public Class<?> getTargetClass() {
/* 76 */     return this.targetClass;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheExpressionRootObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */