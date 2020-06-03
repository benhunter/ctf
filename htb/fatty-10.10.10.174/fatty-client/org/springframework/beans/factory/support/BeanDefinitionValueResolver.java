/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanNameReference;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.config.TypedStringValue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BeanDefinitionValueResolver
/*     */ {
/*     */   private final AbstractBeanFactory beanFactory;
/*     */   private final String beanName;
/*     */   private final BeanDefinition beanDefinition;
/*     */   private final TypeConverter typeConverter;
/*     */   
/*     */   public BeanDefinitionValueResolver(AbstractBeanFactory beanFactory, String beanName, BeanDefinition beanDefinition, TypeConverter typeConverter) {
/*  79 */     this.beanFactory = beanFactory;
/*  80 */     this.beanName = beanName;
/*  81 */     this.beanDefinition = beanDefinition;
/*  82 */     this.typeConverter = typeConverter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object resolveValueIfNecessary(Object argName, @Nullable Object value) {
/* 108 */     if (value instanceof RuntimeBeanReference) {
/* 109 */       RuntimeBeanReference ref = (RuntimeBeanReference)value;
/* 110 */       return resolveReference(argName, ref);
/*     */     } 
/* 112 */     if (value instanceof RuntimeBeanNameReference) {
/* 113 */       String refName = ((RuntimeBeanNameReference)value).getBeanName();
/* 114 */       refName = String.valueOf(doEvaluate(refName));
/* 115 */       if (!this.beanFactory.containsBean(refName)) {
/* 116 */         throw new BeanDefinitionStoreException("Invalid bean name '" + refName + "' in bean reference for " + argName);
/*     */       }
/*     */       
/* 119 */       return refName;
/*     */     } 
/* 121 */     if (value instanceof BeanDefinitionHolder) {
/*     */       
/* 123 */       BeanDefinitionHolder bdHolder = (BeanDefinitionHolder)value;
/* 124 */       return resolveInnerBean(argName, bdHolder.getBeanName(), bdHolder.getBeanDefinition());
/*     */     } 
/* 126 */     if (value instanceof BeanDefinition) {
/*     */       
/* 128 */       BeanDefinition bd = (BeanDefinition)value;
/*     */       
/* 130 */       String innerBeanName = "(inner bean)#" + ObjectUtils.getIdentityHexString(bd);
/* 131 */       return resolveInnerBean(argName, innerBeanName, bd);
/*     */     } 
/* 133 */     if (value instanceof ManagedArray) {
/*     */       
/* 135 */       ManagedArray array = (ManagedArray)value;
/* 136 */       Class<?> elementType = array.resolvedElementType;
/* 137 */       if (elementType == null) {
/* 138 */         String elementTypeName = array.getElementTypeName();
/* 139 */         if (StringUtils.hasText(elementTypeName)) {
/*     */           try {
/* 141 */             elementType = ClassUtils.forName(elementTypeName, this.beanFactory.getBeanClassLoader());
/* 142 */             array.resolvedElementType = elementType;
/*     */           }
/* 144 */           catch (Throwable ex) {
/*     */             
/* 146 */             throw new BeanCreationException(this.beanDefinition
/* 147 */                 .getResourceDescription(), this.beanName, "Error resolving array type for " + argName, ex);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 152 */           elementType = Object.class;
/*     */         } 
/*     */       } 
/* 155 */       return resolveManagedArray(argName, (List)value, elementType);
/*     */     } 
/* 157 */     if (value instanceof ManagedList)
/*     */     {
/* 159 */       return resolveManagedList(argName, (List)value);
/*     */     }
/* 161 */     if (value instanceof ManagedSet)
/*     */     {
/* 163 */       return resolveManagedSet(argName, (Set)value);
/*     */     }
/* 165 */     if (value instanceof ManagedMap)
/*     */     {
/* 167 */       return resolveManagedMap(argName, (Map<?, ?>)value);
/*     */     }
/* 169 */     if (value instanceof ManagedProperties) {
/* 170 */       Properties original = (Properties)value;
/* 171 */       Properties copy = new Properties();
/* 172 */       original.forEach((propKey, propValue) -> {
/*     */             if (propKey instanceof TypedStringValue) {
/*     */               propKey = evaluate((TypedStringValue)propKey);
/*     */             }
/*     */             
/*     */             if (propValue instanceof TypedStringValue) {
/*     */               propValue = evaluate((TypedStringValue)propValue);
/*     */             }
/*     */             
/*     */             if (propKey == null || propValue == null) {
/*     */               throw new BeanCreationException(this.beanDefinition.getResourceDescription(), this.beanName, "Error converting Properties key/value pair for " + argName + ": resolved to null");
/*     */             }
/*     */             copy.put(propKey, propValue);
/*     */           });
/* 186 */       return copy;
/*     */     } 
/* 188 */     if (value instanceof TypedStringValue) {
/*     */       
/* 190 */       TypedStringValue typedStringValue = (TypedStringValue)value;
/* 191 */       Object valueObject = evaluate(typedStringValue);
/*     */       try {
/* 193 */         Class<?> resolvedTargetType = resolveTargetType(typedStringValue);
/* 194 */         if (resolvedTargetType != null) {
/* 195 */           return this.typeConverter.convertIfNecessary(valueObject, resolvedTargetType);
/*     */         }
/*     */         
/* 198 */         return valueObject;
/*     */       
/*     */       }
/* 201 */       catch (Throwable ex) {
/*     */         
/* 203 */         throw new BeanCreationException(this.beanDefinition
/* 204 */             .getResourceDescription(), this.beanName, "Error converting typed String value for " + argName, ex);
/*     */       } 
/*     */     } 
/*     */     
/* 208 */     if (value instanceof NullBean) {
/* 209 */       return null;
/*     */     }
/*     */     
/* 212 */     return evaluate(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object evaluate(TypedStringValue value) {
/* 223 */     Object result = doEvaluate(value.getValue());
/* 224 */     if (!ObjectUtils.nullSafeEquals(result, value.getValue())) {
/* 225 */       value.setDynamic();
/*     */     }
/* 227 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object evaluate(@Nullable Object value) {
/* 237 */     if (value instanceof String) {
/* 238 */       return doEvaluate((String)value);
/*     */     }
/* 240 */     if (value instanceof String[]) {
/* 241 */       String[] values = (String[])value;
/* 242 */       boolean actuallyResolved = false;
/* 243 */       Object[] resolvedValues = new Object[values.length];
/* 244 */       for (int i = 0; i < values.length; i++) {
/* 245 */         String originalValue = values[i];
/* 246 */         Object resolvedValue = doEvaluate(originalValue);
/* 247 */         if (resolvedValue != originalValue) {
/* 248 */           actuallyResolved = true;
/*     */         }
/* 250 */         resolvedValues[i] = resolvedValue;
/*     */       } 
/* 252 */       return actuallyResolved ? resolvedValues : values;
/*     */     } 
/*     */     
/* 255 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object doEvaluate(@Nullable String value) {
/* 266 */     return this.beanFactory.evaluateBeanDefinitionString(value, this.beanDefinition);
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
/*     */   protected Class<?> resolveTargetType(TypedStringValue value) throws ClassNotFoundException {
/* 278 */     if (value.hasTargetType()) {
/* 279 */       return value.getTargetType();
/*     */     }
/* 281 */     return value.resolveTargetType(this.beanFactory.getBeanClassLoader());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object resolveReference(Object argName, RuntimeBeanReference ref) {
/*     */     try {
/*     */       Object bean;
/* 291 */       String refName = ref.getBeanName();
/* 292 */       refName = String.valueOf(doEvaluate(refName));
/* 293 */       if (ref.isToParent()) {
/* 294 */         if (this.beanFactory.getParentBeanFactory() == null) {
/* 295 */           throw new BeanCreationException(this.beanDefinition
/* 296 */               .getResourceDescription(), this.beanName, "Can't resolve reference to bean '" + refName + "' in parent factory: no parent factory available");
/*     */         }
/*     */ 
/*     */         
/* 300 */         bean = this.beanFactory.getParentBeanFactory().getBean(refName);
/*     */       } else {
/*     */         
/* 303 */         bean = this.beanFactory.getBean(refName);
/* 304 */         this.beanFactory.registerDependentBean(refName, this.beanName);
/*     */       } 
/* 306 */       if (bean instanceof NullBean) {
/* 307 */         bean = null;
/*     */       }
/* 309 */       return bean;
/*     */     }
/* 311 */     catch (BeansException ex) {
/* 312 */       throw new BeanCreationException(this.beanDefinition
/* 313 */           .getResourceDescription(), this.beanName, "Cannot resolve reference to bean '" + ref
/* 314 */           .getBeanName() + "' while setting " + argName, ex);
/*     */     } 
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
/*     */   private Object resolveInnerBean(Object argName, String innerBeanName, BeanDefinition innerBd) {
/* 327 */     RootBeanDefinition mbd = null;
/*     */     try {
/* 329 */       mbd = this.beanFactory.getMergedBeanDefinition(innerBeanName, innerBd, this.beanDefinition);
/*     */ 
/*     */       
/* 332 */       String actualInnerBeanName = innerBeanName;
/* 333 */       if (mbd.isSingleton()) {
/* 334 */         actualInnerBeanName = adaptInnerBeanName(innerBeanName);
/*     */       }
/* 336 */       this.beanFactory.registerContainedBean(actualInnerBeanName, this.beanName);
/*     */       
/* 338 */       String[] dependsOn = mbd.getDependsOn();
/* 339 */       if (dependsOn != null) {
/* 340 */         for (String dependsOnBean : dependsOn) {
/* 341 */           this.beanFactory.registerDependentBean(dependsOnBean, actualInnerBeanName);
/* 342 */           this.beanFactory.getBean(dependsOnBean);
/*     */         } 
/*     */       }
/*     */       
/* 346 */       Object innerBean = this.beanFactory.createBean(actualInnerBeanName, mbd, (Object[])null);
/* 347 */       if (innerBean instanceof FactoryBean) {
/* 348 */         boolean synthetic = mbd.isSynthetic();
/* 349 */         innerBean = this.beanFactory.getObjectFromFactoryBean((FactoryBean)innerBean, actualInnerBeanName, !synthetic);
/*     */       } 
/*     */       
/* 352 */       if (innerBean instanceof NullBean) {
/* 353 */         innerBean = null;
/*     */       }
/* 355 */       return innerBean;
/*     */     }
/* 357 */     catch (BeansException ex) {
/* 358 */       throw new BeanCreationException(this.beanDefinition
/* 359 */           .getResourceDescription(), this.beanName, "Cannot create inner bean '" + innerBeanName + "' " + ((mbd != null && mbd
/*     */           
/* 361 */           .getBeanClassName() != null) ? ("of type [" + mbd.getBeanClassName() + "] ") : "") + "while setting " + argName, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String adaptInnerBeanName(String innerBeanName) {
/* 373 */     String actualInnerBeanName = innerBeanName;
/* 374 */     int counter = 0;
/* 375 */     while (this.beanFactory.isBeanNameInUse(actualInnerBeanName)) {
/* 376 */       counter++;
/* 377 */       actualInnerBeanName = innerBeanName + "#" + counter;
/*     */     } 
/* 379 */     return actualInnerBeanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object resolveManagedArray(Object argName, List<?> ml, Class<?> elementType) {
/* 386 */     Object resolved = Array.newInstance(elementType, ml.size());
/* 387 */     for (int i = 0; i < ml.size(); i++) {
/* 388 */       Array.set(resolved, i, resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), ml.get(i)));
/*     */     }
/* 390 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<?> resolveManagedList(Object argName, List<?> ml) {
/* 397 */     List<Object> resolved = new ArrayList(ml.size());
/* 398 */     for (int i = 0; i < ml.size(); i++) {
/* 399 */       resolved.add(resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), ml.get(i)));
/*     */     }
/* 401 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<?> resolveManagedSet(Object argName, Set<?> ms) {
/* 408 */     Set<Object> resolved = new LinkedHashSet(ms.size());
/* 409 */     int i = 0;
/* 410 */     for (Object m : ms) {
/* 411 */       resolved.add(resolveValueIfNecessary(new KeyedArgName(argName, Integer.valueOf(i)), m));
/* 412 */       i++;
/*     */     } 
/* 414 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<?, ?> resolveManagedMap(Object argName, Map<?, ?> mm) {
/* 421 */     Map<Object, Object> resolved = new LinkedHashMap<>(mm.size());
/* 422 */     mm.forEach((key, value) -> {
/*     */           Object resolvedKey = resolveValueIfNecessary(argName, key);
/*     */           Object resolvedValue = resolveValueIfNecessary(new KeyedArgName(argName, key), value);
/*     */           resolved.put(resolvedKey, resolvedValue);
/*     */         });
/* 427 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class KeyedArgName
/*     */   {
/*     */     private final Object argName;
/*     */ 
/*     */     
/*     */     private final Object key;
/*     */ 
/*     */     
/*     */     public KeyedArgName(Object argName, Object key) {
/* 441 */       this.argName = argName;
/* 442 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 447 */       return this.argName + " with key " + "[" + this.key + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/BeanDefinitionValueResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */