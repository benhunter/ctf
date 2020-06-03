/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMonitoringInterceptor
/*     */   extends AbstractTraceInterceptor
/*     */ {
/*  44 */   private String prefix = "";
/*     */   
/*  46 */   private String suffix = "";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean logTargetClassInvocation = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(@Nullable String prefix) {
/*  56 */     this.prefix = (prefix != null) ? prefix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPrefix() {
/*  63 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(@Nullable String suffix) {
/*  71 */     this.suffix = (suffix != null) ? suffix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSuffix() {
/*  78 */     return this.suffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogTargetClassInvocation(boolean logTargetClassInvocation) {
/*  88 */     this.logTargetClassInvocation = logTargetClassInvocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String createInvocationTraceName(MethodInvocation invocation) {
/* 101 */     StringBuilder sb = new StringBuilder(getPrefix());
/* 102 */     Method method = invocation.getMethod();
/* 103 */     Class<?> clazz = method.getDeclaringClass();
/* 104 */     if (this.logTargetClassInvocation && clazz.isInstance(invocation.getThis())) {
/* 105 */       clazz = invocation.getThis().getClass();
/*     */     }
/* 107 */     sb.append(clazz.getName());
/* 108 */     sb.append('.').append(method.getName());
/* 109 */     sb.append(getSuffix());
/* 110 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/AbstractMonitoringInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */