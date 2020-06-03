/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DisposableBeanAdapter
/*     */   implements DisposableBean, Runnable, Serializable
/*     */ {
/*     */   private static final String CLOSE_METHOD_NAME = "close";
/*     */   private static final String SHUTDOWN_METHOD_NAME = "shutdown";
/*  69 */   private static final Log logger = LogFactory.getLog(DisposableBeanAdapter.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object bean;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String beanName;
/*     */ 
/*     */   
/*     */   private final boolean invokeDisposableBean;
/*     */ 
/*     */   
/*     */   private final boolean nonPublicAccessAllowed;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final AccessControlContext acc;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String destroyMethodName;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private transient Method destroyMethod;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private List<DestructionAwareBeanPostProcessor> beanPostProcessors;
/*     */ 
/*     */ 
/*     */   
/*     */   public DisposableBeanAdapter(Object bean, String beanName, RootBeanDefinition beanDefinition, List<BeanPostProcessor> postProcessors, @Nullable AccessControlContext acc) {
/* 104 */     Assert.notNull(bean, "Disposable bean must not be null");
/* 105 */     this.bean = bean;
/* 106 */     this.beanName = beanName;
/* 107 */     this
/* 108 */       .invokeDisposableBean = (this.bean instanceof DisposableBean && !beanDefinition.isExternallyManagedDestroyMethod("destroy"));
/* 109 */     this.nonPublicAccessAllowed = beanDefinition.isNonPublicAccessAllowed();
/* 110 */     this.acc = acc;
/* 111 */     String destroyMethodName = inferDestroyMethodIfNecessary(bean, beanDefinition);
/* 112 */     if (destroyMethodName != null && (!this.invokeDisposableBean || !"destroy".equals(destroyMethodName)) && 
/* 113 */       !beanDefinition.isExternallyManagedDestroyMethod(destroyMethodName)) {
/* 114 */       this.destroyMethodName = destroyMethodName;
/* 115 */       Method destroyMethod = determineDestroyMethod(destroyMethodName);
/* 116 */       if (destroyMethod == null) {
/* 117 */         if (beanDefinition.isEnforceDestroyMethod()) {
/* 118 */           throw new BeanDefinitionValidationException("Could not find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 123 */         Class<?>[] paramTypes = destroyMethod.getParameterTypes();
/* 124 */         if (paramTypes.length > 1) {
/* 125 */           throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" + beanName + "' has more than one parameter - not supported as destroy method");
/*     */         }
/*     */         
/* 128 */         if (paramTypes.length == 1 && boolean.class != paramTypes[0]) {
/* 129 */           throw new BeanDefinitionValidationException("Method '" + destroyMethodName + "' of bean '" + beanName + "' has a non-boolean parameter - not supported as destroy method");
/*     */         }
/*     */         
/* 132 */         destroyMethod = ClassUtils.getInterfaceMethodIfPossible(destroyMethod);
/*     */       } 
/* 134 */       this.destroyMethod = destroyMethod;
/*     */     } 
/* 136 */     this.beanPostProcessors = filterPostProcessors(postProcessors, bean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DisposableBeanAdapter(Object bean, List<BeanPostProcessor> postProcessors, AccessControlContext acc) {
/* 146 */     Assert.notNull(bean, "Disposable bean must not be null");
/* 147 */     this.bean = bean;
/* 148 */     this.beanName = bean.getClass().getName();
/* 149 */     this.invokeDisposableBean = this.bean instanceof DisposableBean;
/* 150 */     this.nonPublicAccessAllowed = true;
/* 151 */     this.acc = acc;
/* 152 */     this.beanPostProcessors = filterPostProcessors(postProcessors, bean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DisposableBeanAdapter(Object bean, String beanName, boolean invokeDisposableBean, boolean nonPublicAccessAllowed, @Nullable String destroyMethodName, @Nullable List<DestructionAwareBeanPostProcessor> postProcessors) {
/* 162 */     this.bean = bean;
/* 163 */     this.beanName = beanName;
/* 164 */     this.invokeDisposableBean = invokeDisposableBean;
/* 165 */     this.nonPublicAccessAllowed = nonPublicAccessAllowed;
/* 166 */     this.acc = null;
/* 167 */     this.destroyMethodName = destroyMethodName;
/* 168 */     this.beanPostProcessors = postProcessors;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String inferDestroyMethodIfNecessary(Object bean, RootBeanDefinition beanDefinition) {
/* 187 */     String destroyMethodName = beanDefinition.getDestroyMethodName();
/* 188 */     if ("(inferred)".equals(destroyMethodName) || (destroyMethodName == null && bean instanceof AutoCloseable)) {
/*     */ 
/*     */ 
/*     */       
/* 192 */       if (!(bean instanceof DisposableBean)) {
/*     */         try {
/* 194 */           return bean.getClass().getMethod("close", new Class[0]).getName();
/*     */         }
/* 196 */         catch (NoSuchMethodException ex) {
/*     */           try {
/* 198 */             return bean.getClass().getMethod("shutdown", new Class[0]).getName();
/*     */           }
/* 200 */           catch (NoSuchMethodException noSuchMethodException) {}
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 205 */       return null;
/*     */     } 
/* 207 */     return StringUtils.hasLength(destroyMethodName) ? destroyMethodName : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private List<DestructionAwareBeanPostProcessor> filterPostProcessors(List<BeanPostProcessor> processors, Object bean) {
/* 217 */     List<DestructionAwareBeanPostProcessor> filteredPostProcessors = null;
/* 218 */     if (!CollectionUtils.isEmpty(processors)) {
/* 219 */       filteredPostProcessors = new ArrayList<>(processors.size());
/* 220 */       for (BeanPostProcessor processor : processors) {
/* 221 */         if (processor instanceof DestructionAwareBeanPostProcessor) {
/* 222 */           DestructionAwareBeanPostProcessor dabpp = (DestructionAwareBeanPostProcessor)processor;
/* 223 */           if (dabpp.requiresDestruction(bean)) {
/* 224 */             filteredPostProcessors.add(dabpp);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 229 */     return filteredPostProcessors;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 235 */     destroy();
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 240 */     if (!CollectionUtils.isEmpty(this.beanPostProcessors)) {
/* 241 */       for (DestructionAwareBeanPostProcessor processor : this.beanPostProcessors) {
/* 242 */         processor.postProcessBeforeDestruction(this.bean, this.beanName);
/*     */       }
/*     */     }
/*     */     
/* 246 */     if (this.invokeDisposableBean) {
/* 247 */       if (logger.isTraceEnabled()) {
/* 248 */         logger.trace("Invoking destroy() on bean with name '" + this.beanName + "'");
/*     */       }
/*     */       try {
/* 251 */         if (System.getSecurityManager() != null) {
/* 252 */           AccessController.doPrivileged(() -> { ((DisposableBean)this.bean).destroy(); return null; }this.acc);
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 258 */           ((DisposableBean)this.bean).destroy();
/*     */         }
/*     */       
/* 261 */       } catch (Throwable ex) {
/* 262 */         String msg = "Invocation of destroy method failed on bean with name '" + this.beanName + "'";
/* 263 */         if (logger.isDebugEnabled()) {
/* 264 */           logger.warn(msg, ex);
/*     */         } else {
/*     */           
/* 267 */           logger.warn(msg + ": " + ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 272 */     if (this.destroyMethod != null) {
/* 273 */       invokeCustomDestroyMethod(this.destroyMethod);
/*     */     }
/* 275 */     else if (this.destroyMethodName != null) {
/* 276 */       Method methodToInvoke = determineDestroyMethod(this.destroyMethodName);
/* 277 */       if (methodToInvoke != null) {
/* 278 */         invokeCustomDestroyMethod(ClassUtils.getInterfaceMethodIfPossible(methodToInvoke));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Method determineDestroyMethod(String name) {
/*     */     try {
/* 287 */       if (System.getSecurityManager() != null) {
/* 288 */         return AccessController.<Method>doPrivileged(() -> findDestroyMethod(name));
/*     */       }
/*     */       
/* 291 */       return findDestroyMethod(name);
/*     */     
/*     */     }
/* 294 */     catch (IllegalArgumentException ex) {
/* 295 */       throw new BeanDefinitionValidationException("Could not find unique destroy method on bean with name '" + this.beanName + ": " + ex
/* 296 */           .getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Method findDestroyMethod(String name) {
/* 302 */     return this.nonPublicAccessAllowed ? 
/* 303 */       BeanUtils.findMethodWithMinimalParameters(this.bean.getClass(), name) : 
/* 304 */       BeanUtils.findMethodWithMinimalParameters(this.bean.getClass().getMethods(), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void invokeCustomDestroyMethod(Method destroyMethod) {
/* 314 */     Class<?>[] paramTypes = destroyMethod.getParameterTypes();
/* 315 */     Object[] args = new Object[paramTypes.length];
/* 316 */     if (paramTypes.length == 1) {
/* 317 */       args[0] = Boolean.TRUE;
/*     */     }
/* 319 */     if (logger.isTraceEnabled()) {
/* 320 */       logger.trace("Invoking destroy method '" + this.destroyMethodName + "' on bean with name '" + this.beanName + "'");
/*     */     }
/*     */     
/*     */     try {
/* 324 */       if (System.getSecurityManager() != null) {
/* 325 */         AccessController.doPrivileged(() -> {
/*     */               ReflectionUtils.makeAccessible(destroyMethod);
/*     */               return null;
/*     */             });
/*     */         try {
/* 330 */           AccessController.doPrivileged(() -> destroyMethod.invoke(this.bean, args), this.acc);
/*     */         
/*     */         }
/* 333 */         catch (PrivilegedActionException pax) {
/* 334 */           throw (InvocationTargetException)pax.getException();
/*     */         } 
/*     */       } else {
/*     */         
/* 338 */         ReflectionUtils.makeAccessible(destroyMethod);
/* 339 */         destroyMethod.invoke(this.bean, args);
/*     */       }
/*     */     
/* 342 */     } catch (InvocationTargetException ex) {
/* 343 */       String msg = "Destroy method '" + this.destroyMethodName + "' on bean with name '" + this.beanName + "' threw an exception";
/*     */       
/* 345 */       if (logger.isDebugEnabled()) {
/* 346 */         logger.warn(msg, ex.getTargetException());
/*     */       } else {
/*     */         
/* 349 */         logger.warn(msg + ": " + ex.getTargetException());
/*     */       }
/*     */     
/* 352 */     } catch (Throwable ex) {
/* 353 */       logger.warn("Failed to invoke destroy method '" + this.destroyMethodName + "' on bean with name '" + this.beanName + "'", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object writeReplace() {
/* 364 */     List<DestructionAwareBeanPostProcessor> serializablePostProcessors = null;
/* 365 */     if (this.beanPostProcessors != null) {
/* 366 */       serializablePostProcessors = new ArrayList<>();
/* 367 */       for (DestructionAwareBeanPostProcessor postProcessor : this.beanPostProcessors) {
/* 368 */         if (postProcessor instanceof Serializable) {
/* 369 */           serializablePostProcessors.add(postProcessor);
/*     */         }
/*     */       } 
/*     */     } 
/* 373 */     return new DisposableBeanAdapter(this.bean, this.beanName, this.invokeDisposableBean, this.nonPublicAccessAllowed, this.destroyMethodName, serializablePostProcessors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasDestroyMethod(Object bean, RootBeanDefinition beanDefinition) {
/* 384 */     if (bean instanceof DisposableBean || bean instanceof AutoCloseable) {
/* 385 */       return true;
/*     */     }
/* 387 */     String destroyMethodName = beanDefinition.getDestroyMethodName();
/* 388 */     if ("(inferred)".equals(destroyMethodName)) {
/* 389 */       return (ClassUtils.hasMethod(bean.getClass(), "close", new Class[0]) || 
/* 390 */         ClassUtils.hasMethod(bean.getClass(), "shutdown", new Class[0]));
/*     */     }
/* 392 */     return StringUtils.hasLength(destroyMethodName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasApplicableProcessors(Object bean, List<BeanPostProcessor> postProcessors) {
/* 401 */     if (!CollectionUtils.isEmpty(postProcessors)) {
/* 402 */       for (BeanPostProcessor processor : postProcessors) {
/* 403 */         if (processor instanceof DestructionAwareBeanPostProcessor) {
/* 404 */           DestructionAwareBeanPostProcessor dabpp = (DestructionAwareBeanPostProcessor)processor;
/* 405 */           if (dabpp.requiresDestruction(bean)) {
/* 406 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 411 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/DisposableBeanAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */