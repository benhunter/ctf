/*     */ package org.springframework.context.expression;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodBasedEvaluationContext
/*     */   extends StandardEvaluationContext
/*     */ {
/*     */   private final Method method;
/*     */   private final Object[] arguments;
/*     */   private final ParameterNameDiscoverer parameterNameDiscoverer;
/*     */   private boolean argumentsLoaded = false;
/*     */   
/*     */   public MethodBasedEvaluationContext(Object rootObject, Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer) {
/*  56 */     super(rootObject);
/*  57 */     this.method = method;
/*  58 */     this.arguments = arguments;
/*  59 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object lookupVariable(String name) {
/*  66 */     Object variable = super.lookupVariable(name);
/*  67 */     if (variable != null) {
/*  68 */       return variable;
/*     */     }
/*  70 */     if (!this.argumentsLoaded) {
/*  71 */       lazyLoadArguments();
/*  72 */       this.argumentsLoaded = true;
/*  73 */       variable = super.lookupVariable(name);
/*     */     } 
/*  75 */     return variable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void lazyLoadArguments() {
/*  83 */     if (ObjectUtils.isEmpty(this.arguments)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  88 */     String[] paramNames = this.parameterNameDiscoverer.getParameterNames(this.method);
/*  89 */     int paramCount = (paramNames != null) ? paramNames.length : this.method.getParameterCount();
/*  90 */     int argsCount = this.arguments.length;
/*     */     
/*  92 */     for (int i = 0; i < paramCount; i++) {
/*  93 */       Object value = null;
/*  94 */       if (argsCount > paramCount && i == paramCount - 1) {
/*     */         
/*  96 */         value = Arrays.copyOfRange(this.arguments, i, argsCount);
/*     */       }
/*  98 */       else if (argsCount > i) {
/*     */         
/* 100 */         value = this.arguments[i];
/*     */       } 
/* 102 */       setVariable("a" + i, value);
/* 103 */       setVariable("p" + i, value);
/* 104 */       if (paramNames != null && paramNames[i] != null)
/* 105 */         setVariable(paramNames[i], value); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/expression/MethodBasedEvaluationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */