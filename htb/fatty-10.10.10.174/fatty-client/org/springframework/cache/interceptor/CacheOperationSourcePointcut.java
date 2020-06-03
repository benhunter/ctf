/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.aop.support.StaticMethodMatcherPointcut;
/*    */ import org.springframework.cache.CacheManager;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ abstract class CacheOperationSourcePointcut
/*    */   extends StaticMethodMatcherPointcut
/*    */   implements Serializable
/*    */ {
/*    */   public boolean matches(Method method, Class<?> targetClass) {
/* 41 */     if (CacheManager.class.isAssignableFrom(targetClass)) {
/* 42 */       return false;
/*    */     }
/* 44 */     CacheOperationSource cas = getCacheOperationSource();
/* 45 */     return (cas != null && !CollectionUtils.isEmpty(cas.getCacheOperations(method, targetClass)));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 50 */     if (this == other) {
/* 51 */       return true;
/*    */     }
/* 53 */     if (!(other instanceof CacheOperationSourcePointcut)) {
/* 54 */       return false;
/*    */     }
/* 56 */     CacheOperationSourcePointcut otherPc = (CacheOperationSourcePointcut)other;
/* 57 */     return ObjectUtils.nullSafeEquals(getCacheOperationSource(), otherPc.getCacheOperationSource());
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     return CacheOperationSourcePointcut.class.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return getClass().getName() + ": " + getCacheOperationSource();
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   protected abstract CacheOperationSource getCacheOperationSource();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheOperationSourcePointcut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */