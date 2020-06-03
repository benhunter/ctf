/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.context.expression.MethodBasedEvaluationContext;
/*    */ import org.springframework.core.ParameterNameDiscoverer;
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
/*    */ class CacheEvaluationContext
/*    */   extends MethodBasedEvaluationContext
/*    */ {
/* 47 */   private final Set<String> unavailableVariables = new HashSet<>(1);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   CacheEvaluationContext(Object rootObject, Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer) {
/* 53 */     super(rootObject, method, arguments, parameterNameDiscoverer);
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
/*    */   public void addUnavailableVariable(String name) {
/* 65 */     this.unavailableVariables.add(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object lookupVariable(String name) {
/* 75 */     if (this.unavailableVariables.contains(name)) {
/* 76 */       throw new VariableNotAvailableException(name);
/*    */     }
/* 78 */     return super.lookupVariable(name);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheEvaluationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */