/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.core.BridgeMethodResolver;
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
/*     */ public class ReflectiveMethodInvocation
/*     */   implements ProxyMethodInvocation, Cloneable
/*     */ {
/*     */   protected final Object proxy;
/*     */   @Nullable
/*     */   protected final Object target;
/*     */   protected final Method method;
/*  71 */   protected Object[] arguments = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final Class<?> targetClass;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Map<String, Object> userAttributes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final List<?> interceptorsAndDynamicMethodMatchers;
/*     */ 
/*     */ 
/*     */   
/*  92 */   private int currentInterceptorIndex = -1;
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
/*     */   protected ReflectiveMethodInvocation(Object proxy, @Nullable Object target, Method method, @Nullable Object[] arguments, @Nullable Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
/* 112 */     this.proxy = proxy;
/* 113 */     this.target = target;
/* 114 */     this.targetClass = targetClass;
/* 115 */     this.method = BridgeMethodResolver.findBridgedMethod(method);
/* 116 */     this.arguments = AopProxyUtils.adaptArgumentsIfNecessary(method, arguments);
/* 117 */     this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getProxy() {
/* 123 */     return this.proxy;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Object getThis() {
/* 129 */     return this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public final AccessibleObject getStaticPart() {
/* 134 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method getMethod() {
/* 144 */     return this.method;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object[] getArguments() {
/* 149 */     return this.arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setArguments(Object... arguments) {
/* 154 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object proceed() throws Throwable {
/* 162 */     if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
/* 163 */       return invokeJoinpoint();
/*     */     }
/*     */ 
/*     */     
/* 167 */     Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
/* 168 */     if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
/*     */ 
/*     */       
/* 171 */       InterceptorAndDynamicMethodMatcher dm = (InterceptorAndDynamicMethodMatcher)interceptorOrInterceptionAdvice;
/*     */       
/* 173 */       Class<?> targetClass = (this.targetClass != null) ? this.targetClass : this.method.getDeclaringClass();
/* 174 */       if (dm.methodMatcher.matches(this.method, targetClass, this.arguments)) {
/* 175 */         return dm.interceptor.invoke((MethodInvocation)this);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 180 */       return proceed();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     return ((MethodInterceptor)interceptorOrInterceptionAdvice).invoke((MethodInvocation)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object invokeJoinpoint() throws Throwable {
/* 198 */     return AopUtils.invokeJoinpointUsingReflection(this.target, this.method, this.arguments);
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
/*     */   
/*     */   public MethodInvocation invocableClone() {
/* 212 */     Object[] cloneArguments = this.arguments;
/* 213 */     if (this.arguments.length > 0) {
/*     */       
/* 215 */       cloneArguments = new Object[this.arguments.length];
/* 216 */       System.arraycopy(this.arguments, 0, cloneArguments, 0, this.arguments.length);
/*     */     } 
/* 218 */     return invocableClone(cloneArguments);
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
/*     */ 
/*     */   
/*     */   public MethodInvocation invocableClone(Object... arguments) {
/* 233 */     if (this.userAttributes == null) {
/* 234 */       this.userAttributes = new HashMap<>();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 239 */       ReflectiveMethodInvocation clone = (ReflectiveMethodInvocation)clone();
/* 240 */       clone.arguments = arguments;
/* 241 */       return (MethodInvocation)clone;
/*     */     }
/* 243 */     catch (CloneNotSupportedException ex) {
/* 244 */       throw new IllegalStateException("Should be able to clone object of type [" + 
/* 245 */           getClass() + "]: " + ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserAttribute(String key, @Nullable Object value) {
/* 252 */     if (value != null) {
/* 253 */       if (this.userAttributes == null) {
/* 254 */         this.userAttributes = new HashMap<>();
/*     */       }
/* 256 */       this.userAttributes.put(key, value);
/*     */     
/*     */     }
/* 259 */     else if (this.userAttributes != null) {
/* 260 */       this.userAttributes.remove(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getUserAttribute(String key) {
/* 268 */     return (this.userAttributes != null) ? this.userAttributes.get(key) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getUserAttributes() {
/* 279 */     if (this.userAttributes == null) {
/* 280 */       this.userAttributes = new HashMap<>();
/*     */     }
/* 282 */     return this.userAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 289 */     StringBuilder sb = new StringBuilder("ReflectiveMethodInvocation: ");
/* 290 */     sb.append(this.method).append("; ");
/* 291 */     if (this.target == null) {
/* 292 */       sb.append("target is null");
/*     */     } else {
/*     */       
/* 295 */       sb.append("target is of class [").append(this.target.getClass().getName()).append(']');
/*     */     } 
/* 297 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/framework/ReflectiveMethodInvocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */