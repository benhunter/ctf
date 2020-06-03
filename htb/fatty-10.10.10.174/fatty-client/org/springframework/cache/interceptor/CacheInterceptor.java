/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Method;
/*    */ import org.aopalliance.intercept.MethodInterceptor;
/*    */ import org.aopalliance.intercept.MethodInvocation;
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
/*    */ public class CacheInterceptor
/*    */   extends CacheAspectSupport
/*    */   implements MethodInterceptor, Serializable
/*    */ {
/*    */   @Nullable
/*    */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 49 */     Method method = invocation.getMethod();
/*    */     
/* 51 */     CacheOperationInvoker aopAllianceInvoker = () -> {
/*    */         
/*    */         try {
/*    */           return invocation.proceed();
/* 55 */         } catch (Throwable ex) {
/*    */           throw new CacheOperationInvoker.ThrowableWrapper(ex);
/*    */         } 
/*    */       };
/*    */     
/*    */     try {
/* 61 */       return execute(aopAllianceInvoker, invocation.getThis(), method, invocation.getArguments());
/*    */     }
/* 63 */     catch (ThrowableWrapper th) {
/* 64 */       throw th.getOriginal();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */