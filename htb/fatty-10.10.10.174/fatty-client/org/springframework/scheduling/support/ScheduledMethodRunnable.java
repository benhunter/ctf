/*    */ package org.springframework.scheduling.support;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.UndeclaredThrowableException;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ public class ScheduledMethodRunnable
/*    */   implements Runnable
/*    */ {
/*    */   private final Object target;
/*    */   private final Method method;
/*    */   
/*    */   public ScheduledMethodRunnable(Object target, Method method) {
/* 48 */     this.target = target;
/* 49 */     this.method = method;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScheduledMethodRunnable(Object target, String methodName) throws NoSuchMethodException {
/* 60 */     this.target = target;
/* 61 */     this.method = target.getClass().getMethod(methodName, new Class[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getTarget() {
/* 69 */     return this.target;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Method getMethod() {
/* 76 */     return this.method;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/* 83 */       ReflectionUtils.makeAccessible(this.method);
/* 84 */       this.method.invoke(this.target, new Object[0]);
/*    */     }
/* 86 */     catch (InvocationTargetException ex) {
/* 87 */       ReflectionUtils.rethrowRuntimeException(ex.getTargetException());
/*    */     }
/* 89 */     catch (IllegalAccessException ex) {
/* 90 */       throw new UndeclaredThrowableException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 96 */     return this.method.getDeclaringClass().getName() + "." + this.method.getName();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scheduling/support/ScheduledMethodRunnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */