/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.context.expression.AnnotatedElementKey;
/*    */ import org.springframework.context.expression.BeanFactoryResolver;
/*    */ import org.springframework.context.expression.CachedExpressionEvaluator;
/*    */ import org.springframework.context.expression.MethodBasedEvaluationContext;
/*    */ import org.springframework.expression.BeanResolver;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.Expression;
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
/*    */ class EventExpressionEvaluator
/*    */   extends CachedExpressionEvaluator
/*    */ {
/* 42 */   private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean condition(String conditionExpression, ApplicationEvent event, Method targetMethod, AnnotatedElementKey methodKey, Object[] args, @Nullable BeanFactory beanFactory) {
/* 51 */     EventExpressionRootObject root = new EventExpressionRootObject(event, args);
/*    */     
/* 53 */     MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(root, targetMethod, args, getParameterNameDiscoverer());
/* 54 */     if (beanFactory != null) {
/* 55 */       evaluationContext.setBeanResolver((BeanResolver)new BeanFactoryResolver(beanFactory));
/*    */     }
/*    */     
/* 58 */     return Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression).getValue((EvaluationContext)evaluationContext, Boolean.class));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/EventExpressionEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */