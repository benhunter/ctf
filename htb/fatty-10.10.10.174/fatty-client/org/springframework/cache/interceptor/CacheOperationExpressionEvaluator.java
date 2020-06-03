/*     */ package org.springframework.cache.interceptor;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.cache.Cache;
/*     */ import org.springframework.context.expression.AnnotatedElementKey;
/*     */ import org.springframework.context.expression.BeanFactoryResolver;
/*     */ import org.springframework.context.expression.CachedExpressionEvaluator;
/*     */ import org.springframework.expression.BeanResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.Expression;
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
/*     */ class CacheOperationExpressionEvaluator
/*     */   extends CachedExpressionEvaluator
/*     */ {
/*  51 */   public static final Object NO_RESULT = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static final Object RESULT_UNAVAILABLE = new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String RESULT_VARIABLE = "result";
/*     */ 
/*     */ 
/*     */   
/*  64 */   private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(64);
/*     */   
/*  66 */   private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);
/*     */   
/*  68 */   private final Map<CachedExpressionEvaluator.ExpressionKey, Expression> unlessCache = new ConcurrentHashMap<>(64);
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
/*     */   public EvaluationContext createEvaluationContext(Collection<? extends Cache> caches, Method method, Object[] args, Object target, Class<?> targetClass, Method targetMethod, @Nullable Object result, @Nullable BeanFactory beanFactory) {
/*  86 */     CacheExpressionRootObject rootObject = new CacheExpressionRootObject(caches, method, args, target, targetClass);
/*     */ 
/*     */     
/*  89 */     CacheEvaluationContext evaluationContext = new CacheEvaluationContext(rootObject, targetMethod, args, getParameterNameDiscoverer());
/*  90 */     if (result == RESULT_UNAVAILABLE) {
/*  91 */       evaluationContext.addUnavailableVariable("result");
/*     */     }
/*  93 */     else if (result != NO_RESULT) {
/*  94 */       evaluationContext.setVariable("result", result);
/*     */     } 
/*  96 */     if (beanFactory != null) {
/*  97 */       evaluationContext.setBeanResolver((BeanResolver)new BeanFactoryResolver(beanFactory));
/*     */     }
/*  99 */     return (EvaluationContext)evaluationContext;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
/* 104 */     return getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
/*     */   }
/*     */   
/*     */   public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
/* 108 */     return Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression).getValue(evalContext, Boolean.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean unless(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
/* 113 */     return Boolean.TRUE.equals(getExpression(this.unlessCache, methodKey, unlessExpression).getValue(evalContext, Boolean.class));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clear() {
/* 121 */     this.keyCache.clear();
/* 122 */     this.conditionCache.clear();
/* 123 */     this.unlessCache.clear();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/cache/interceptor/CacheOperationExpressionEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */