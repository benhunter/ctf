/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.PayloadApplicationEvent;
/*     */ import org.springframework.context.expression.AnnotatedElementKey;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class ApplicationListenerMethodAdapter
/*     */   implements GenericApplicationListener
/*     */ {
/*  63 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final String beanName;
/*     */   
/*     */   private final Method method;
/*     */   
/*     */   private final Method targetMethod;
/*     */   
/*     */   private final AnnotatedElementKey methodKey;
/*     */   
/*     */   private final List<ResolvableType> declaredEventTypes;
/*     */   
/*     */   @Nullable
/*     */   private final String condition;
/*     */   
/*     */   private final int order;
/*     */   
/*     */   @Nullable
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */   @Nullable
/*     */   private EventExpressionEvaluator evaluator;
/*     */ 
/*     */   
/*     */   public ApplicationListenerMethodAdapter(String beanName, Class<?> targetClass, Method method) {
/*  88 */     this.beanName = beanName;
/*  89 */     this.method = BridgeMethodResolver.findBridgedMethod(method);
/*  90 */     this
/*  91 */       .targetMethod = !Proxy.isProxyClass(targetClass) ? AopUtils.getMostSpecificMethod(method, targetClass) : this.method;
/*  92 */     this.methodKey = new AnnotatedElementKey(this.targetMethod, targetClass);
/*     */     
/*  94 */     EventListener ann = (EventListener)AnnotatedElementUtils.findMergedAnnotation(this.targetMethod, EventListener.class);
/*  95 */     this.declaredEventTypes = resolveDeclaredEventTypes(method, ann);
/*  96 */     this.condition = (ann != null) ? ann.condition() : null;
/*  97 */     this.order = resolveOrder(this.targetMethod);
/*     */   }
/*     */   
/*     */   private static List<ResolvableType> resolveDeclaredEventTypes(Method method, @Nullable EventListener ann) {
/* 101 */     int count = method.getParameterCount();
/* 102 */     if (count > 1) {
/* 103 */       throw new IllegalStateException("Maximum one parameter is allowed for event listener method: " + method);
/*     */     }
/*     */ 
/*     */     
/* 107 */     if (ann != null) {
/* 108 */       Class<?>[] classes = ann.classes();
/* 109 */       if (classes.length > 0) {
/* 110 */         List<ResolvableType> types = new ArrayList<>(classes.length);
/* 111 */         for (Class<?> eventType : classes) {
/* 112 */           types.add(ResolvableType.forClass(eventType));
/*     */         }
/* 114 */         return types;
/*     */       } 
/*     */     } 
/*     */     
/* 118 */     if (count == 0) {
/* 119 */       throw new IllegalStateException("Event parameter is mandatory for event listener method: " + method);
/*     */     }
/*     */     
/* 122 */     return Collections.singletonList(ResolvableType.forMethodParameter(method, 0));
/*     */   }
/*     */   
/*     */   private static int resolveOrder(Method method) {
/* 126 */     Order ann = (Order)AnnotatedElementUtils.findMergedAnnotation(method, Order.class);
/* 127 */     return (ann != null) ? ann.value() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void init(ApplicationContext applicationContext, EventExpressionEvaluator evaluator) {
/* 135 */     this.applicationContext = applicationContext;
/* 136 */     this.evaluator = evaluator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ApplicationEvent event) {
/* 142 */     processEvent(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsEventType(ResolvableType eventType) {
/* 147 */     for (ResolvableType declaredEventType : this.declaredEventTypes) {
/* 148 */       if (declaredEventType.isAssignableFrom(eventType)) {
/* 149 */         return true;
/*     */       }
/* 151 */       if (PayloadApplicationEvent.class.isAssignableFrom(eventType.toClass())) {
/* 152 */         ResolvableType payloadType = eventType.as(PayloadApplicationEvent.class).getGeneric(new int[0]);
/* 153 */         if (declaredEventType.isAssignableFrom(payloadType)) {
/* 154 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 158 */     return eventType.hasUnresolvableGenerics();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSourceType(@Nullable Class<?> sourceType) {
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 168 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processEvent(ApplicationEvent event) {
/* 177 */     Object[] args = resolveArguments(event);
/* 178 */     if (shouldHandle(event, args)) {
/* 179 */       Object result = doInvoke(args);
/* 180 */       if (result != null) {
/* 181 */         handleResult(result);
/*     */       } else {
/*     */         
/* 184 */         this.logger.trace("No result object given - no result to handle");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object[] resolveArguments(ApplicationEvent event) {
/* 197 */     ResolvableType declaredEventType = getResolvableType(event);
/* 198 */     if (declaredEventType == null) {
/* 199 */       return null;
/*     */     }
/* 201 */     if (this.method.getParameterCount() == 0) {
/* 202 */       return new Object[0];
/*     */     }
/* 204 */     Class<?> declaredEventClass = declaredEventType.toClass();
/* 205 */     if (!ApplicationEvent.class.isAssignableFrom(declaredEventClass) && event instanceof PayloadApplicationEvent) {
/*     */       
/* 207 */       Object payload = ((PayloadApplicationEvent)event).getPayload();
/* 208 */       if (declaredEventClass.isInstance(payload)) {
/* 209 */         return new Object[] { payload };
/*     */       }
/*     */     } 
/* 212 */     return new Object[] { event };
/*     */   }
/*     */   
/*     */   protected void handleResult(Object result) {
/* 216 */     if (result.getClass().isArray()) {
/* 217 */       Object[] events = ObjectUtils.toObjectArray(result);
/* 218 */       for (Object event : events) {
/* 219 */         publishEvent(event);
/*     */       }
/*     */     }
/* 222 */     else if (result instanceof Collection) {
/* 223 */       Collection<?> events = (Collection)result;
/* 224 */       for (Object event : events) {
/* 225 */         publishEvent(event);
/*     */       }
/*     */     } else {
/*     */       
/* 229 */       publishEvent(result);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void publishEvent(@Nullable Object event) {
/* 234 */     if (event != null) {
/* 235 */       Assert.notNull(this.applicationContext, "ApplicationContext must not be null");
/* 236 */       this.applicationContext.publishEvent(event);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean shouldHandle(ApplicationEvent event, @Nullable Object[] args) {
/* 241 */     if (args == null) {
/* 242 */       return false;
/*     */     }
/* 244 */     String condition = getCondition();
/* 245 */     if (StringUtils.hasText(condition)) {
/* 246 */       Assert.notNull(this.evaluator, "EventExpressionEvaluator must not be null");
/* 247 */       return this.evaluator.condition(condition, event, this.targetMethod, this.methodKey, args, (BeanFactory)this.applicationContext);
/*     */     } 
/*     */     
/* 250 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object doInvoke(Object... args) {
/* 258 */     Object bean = getTargetBean();
/* 259 */     ReflectionUtils.makeAccessible(this.method);
/*     */     try {
/* 261 */       return this.method.invoke(bean, args);
/*     */     }
/* 263 */     catch (IllegalArgumentException ex) {
/* 264 */       assertTargetBean(this.method, bean, args);
/* 265 */       throw new IllegalStateException(getInvocationErrorMessage(bean, ex.getMessage(), args), ex);
/*     */     }
/* 267 */     catch (IllegalAccessException ex) {
/* 268 */       throw new IllegalStateException(getInvocationErrorMessage(bean, ex.getMessage(), args), ex);
/*     */     }
/* 270 */     catch (InvocationTargetException ex) {
/*     */       
/* 272 */       Throwable targetException = ex.getTargetException();
/* 273 */       if (targetException instanceof RuntimeException) {
/* 274 */         throw (RuntimeException)targetException;
/*     */       }
/*     */       
/* 277 */       String msg = getInvocationErrorMessage(bean, "Failed to invoke event listener method", args);
/* 278 */       throw new UndeclaredThrowableException(targetException, msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getTargetBean() {
/* 287 */     Assert.notNull(this.applicationContext, "ApplicationContext must no be null");
/* 288 */     return this.applicationContext.getBean(this.beanName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getCondition() {
/* 299 */     return this.condition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDetailedErrorMessage(Object bean, String message) {
/* 308 */     StringBuilder sb = (new StringBuilder(message)).append("\n");
/* 309 */     sb.append("HandlerMethod details: \n");
/* 310 */     sb.append("Bean [").append(bean.getClass().getName()).append("]\n");
/* 311 */     sb.append("Method [").append(this.method.toGenericString()).append("]\n");
/* 312 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertTargetBean(Method method, Object targetBean, Object[] args) {
/* 323 */     Class<?> methodDeclaringClass = method.getDeclaringClass();
/* 324 */     Class<?> targetBeanClass = targetBean.getClass();
/* 325 */     if (!methodDeclaringClass.isAssignableFrom(targetBeanClass)) {
/*     */ 
/*     */       
/* 328 */       String msg = "The event listener method class '" + methodDeclaringClass.getName() + "' is not an instance of the actual bean class '" + targetBeanClass.getName() + "'. If the bean requires proxying (e.g. due to @Transactional), please use class-based proxying.";
/*     */       
/* 330 */       throw new IllegalStateException(getInvocationErrorMessage(targetBean, msg, args));
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getInvocationErrorMessage(Object bean, String message, Object[] resolvedArgs) {
/* 335 */     StringBuilder sb = new StringBuilder(getDetailedErrorMessage(bean, message));
/* 336 */     sb.append("Resolved arguments: \n");
/* 337 */     for (int i = 0; i < resolvedArgs.length; i++) {
/* 338 */       sb.append("[").append(i).append("] ");
/* 339 */       if (resolvedArgs[i] == null) {
/* 340 */         sb.append("[null] \n");
/*     */       } else {
/*     */         
/* 343 */         sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
/* 344 */         sb.append("[value=").append(resolvedArgs[i]).append("]\n");
/*     */       } 
/*     */     } 
/* 347 */     return sb.toString();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private ResolvableType getResolvableType(ApplicationEvent event) {
/* 352 */     ResolvableType payloadType = null;
/* 353 */     if (event instanceof PayloadApplicationEvent) {
/* 354 */       PayloadApplicationEvent<?> payloadEvent = (PayloadApplicationEvent)event;
/* 355 */       ResolvableType eventType = payloadEvent.getResolvableType();
/* 356 */       if (eventType != null) {
/* 357 */         payloadType = eventType.as(PayloadApplicationEvent.class).getGeneric(new int[0]);
/*     */       }
/*     */     } 
/* 360 */     for (ResolvableType declaredEventType : this.declaredEventTypes) {
/* 361 */       Class<?> eventClass = declaredEventType.toClass();
/* 362 */       if (!ApplicationEvent.class.isAssignableFrom(eventClass) && payloadType != null && declaredEventType
/* 363 */         .isAssignableFrom(payloadType)) {
/* 364 */         return declaredEventType;
/*     */       }
/* 366 */       if (eventClass.isInstance(event)) {
/* 367 */         return declaredEventType;
/*     */       }
/*     */     } 
/* 370 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 376 */     return this.method.toGenericString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/ApplicationListenerMethodAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */