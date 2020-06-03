/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanIsNotAFactoryException;
/*     */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.beans.factory.SmartFactoryBean;
/*     */ import org.springframework.core.OrderComparator;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class StaticListableBeanFactory
/*     */   implements ListableBeanFactory
/*     */ {
/*     */   private final Map<String, Object> beans;
/*     */   
/*     */   public StaticListableBeanFactory() {
/*  76 */     this.beans = new LinkedHashMap<>();
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
/*     */   public StaticListableBeanFactory(Map<String, Object> beans) {
/*  90 */     Assert.notNull(beans, "Beans Map must not be null");
/*  91 */     this.beans = beans;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBean(String name, Object bean) {
/* 102 */     this.beans.put(name, bean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getBean(String name) throws BeansException {
/* 112 */     String beanName = BeanFactoryUtils.transformedBeanName(name);
/* 113 */     Object bean = this.beans.get(beanName);
/*     */     
/* 115 */     if (bean == null) {
/* 116 */       throw new NoSuchBeanDefinitionException(beanName, "Defined beans are [" + 
/* 117 */           StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 122 */     if (BeanFactoryUtils.isFactoryDereference(name) && !(bean instanceof FactoryBean)) {
/* 123 */       throw new BeanIsNotAFactoryException(beanName, bean.getClass());
/*     */     }
/*     */     
/* 126 */     if (bean instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name)) {
/*     */       try {
/* 128 */         Object exposedObject = ((FactoryBean)bean).getObject();
/* 129 */         if (exposedObject == null) {
/* 130 */           throw new BeanCreationException(beanName, "FactoryBean exposed null object");
/*     */         }
/* 132 */         return exposedObject;
/*     */       }
/* 134 */       catch (Exception ex) {
/* 135 */         throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
/*     */       } 
/*     */     }
/*     */     
/* 139 */     return bean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException {
/* 146 */     Object bean = getBean(name);
/* 147 */     if (requiredType != null && !requiredType.isInstance(bean)) {
/* 148 */       throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
/*     */     }
/* 150 */     return (T)bean;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getBean(String name, Object... args) throws BeansException {
/* 155 */     if (!ObjectUtils.isEmpty(args)) {
/* 156 */       throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */     
/* 159 */     return getBean(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getBean(Class<T> requiredType) throws BeansException {
/* 164 */     String[] beanNames = getBeanNamesForType(requiredType);
/* 165 */     if (beanNames.length == 1) {
/* 166 */       return getBean(beanNames[0], requiredType);
/*     */     }
/* 168 */     if (beanNames.length > 1) {
/* 169 */       throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
/*     */     }
/*     */     
/* 172 */     throw new NoSuchBeanDefinitionException(requiredType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
/* 178 */     if (!ObjectUtils.isEmpty(args)) {
/* 179 */       throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */     
/* 182 */     return getBean(requiredType);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) throws BeansException {
/* 187 */     return getBeanProvider(ResolvableType.forRawClass(requiredType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ObjectProvider<T> getBeanProvider(final ResolvableType requiredType) {
/* 193 */     return new ObjectProvider<T>()
/*     */       {
/*     */         public T getObject() throws BeansException {
/* 196 */           String[] beanNames = StaticListableBeanFactory.this.getBeanNamesForType(requiredType);
/* 197 */           if (beanNames.length == 1) {
/* 198 */             return (T)StaticListableBeanFactory.this.getBean(beanNames[0], new Object[] { this.val$requiredType });
/*     */           }
/* 200 */           if (beanNames.length > 1) {
/* 201 */             throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
/*     */           }
/*     */           
/* 204 */           throw new NoSuchBeanDefinitionException(requiredType);
/*     */         }
/*     */ 
/*     */         
/*     */         public T getObject(Object... args) throws BeansException {
/* 209 */           String[] beanNames = StaticListableBeanFactory.this.getBeanNamesForType(requiredType);
/* 210 */           if (beanNames.length == 1) {
/* 211 */             return (T)StaticListableBeanFactory.this.getBean(beanNames[0], args);
/*     */           }
/* 213 */           if (beanNames.length > 1) {
/* 214 */             throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
/*     */           }
/*     */           
/* 217 */           throw new NoSuchBeanDefinitionException(requiredType);
/*     */         }
/*     */ 
/*     */         
/*     */         @Nullable
/*     */         public T getIfAvailable() throws BeansException {
/* 223 */           String[] beanNames = StaticListableBeanFactory.this.getBeanNamesForType(requiredType);
/* 224 */           if (beanNames.length == 1) {
/* 225 */             return (T)StaticListableBeanFactory.this.getBean(beanNames[0]);
/*     */           }
/* 227 */           if (beanNames.length > 1) {
/* 228 */             throw new NoUniqueBeanDefinitionException(requiredType, beanNames);
/*     */           }
/*     */           
/* 231 */           return null;
/*     */         }
/*     */ 
/*     */         
/*     */         @Nullable
/*     */         public T getIfUnique() throws BeansException {
/* 237 */           String[] beanNames = StaticListableBeanFactory.this.getBeanNamesForType(requiredType);
/* 238 */           if (beanNames.length == 1) {
/* 239 */             return (T)StaticListableBeanFactory.this.getBean(beanNames[0]);
/*     */           }
/*     */           
/* 242 */           return null;
/*     */         }
/*     */ 
/*     */         
/*     */         public Stream<T> stream() {
/* 247 */           return Arrays.<String>stream(StaticListableBeanFactory.this.getBeanNamesForType(requiredType)).map(name -> StaticListableBeanFactory.this.getBean(name));
/*     */         }
/*     */         
/*     */         public Stream<T> orderedStream() {
/* 251 */           return stream().sorted((Comparator<? super T>)OrderComparator.INSTANCE);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsBean(String name) {
/* 258 */     return this.beans.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
/* 263 */     Object bean = getBean(name);
/*     */     
/* 265 */     return (bean instanceof FactoryBean && ((FactoryBean)bean).isSingleton());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
/* 270 */     Object bean = getBean(name);
/*     */     
/* 272 */     return ((bean instanceof SmartFactoryBean && ((SmartFactoryBean)bean).isPrototype()) || (bean instanceof FactoryBean && 
/* 273 */       !((FactoryBean)bean).isSingleton()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
/* 278 */     Class<?> type = getType(name);
/* 279 */     return (type != null && typeToMatch.isAssignableFrom(type));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTypeMatch(String name, @Nullable Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
/* 284 */     Class<?> type = getType(name);
/* 285 */     return (typeToMatch == null || (type != null && typeToMatch.isAssignableFrom(type)));
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
/* 290 */     String beanName = BeanFactoryUtils.transformedBeanName(name);
/*     */     
/* 292 */     Object bean = this.beans.get(beanName);
/* 293 */     if (bean == null) {
/* 294 */       throw new NoSuchBeanDefinitionException(beanName, "Defined beans are [" + 
/* 295 */           StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
/*     */     }
/*     */     
/* 298 */     if (bean instanceof FactoryBean && !BeanFactoryUtils.isFactoryDereference(name))
/*     */     {
/* 300 */       return ((FactoryBean)bean).getObjectType();
/*     */     }
/* 302 */     return bean.getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAliases(String name) {
/* 307 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsBeanDefinition(String name) {
/* 317 */     return this.beans.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBeanDefinitionCount() {
/* 322 */     return this.beans.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanDefinitionNames() {
/* 327 */     return StringUtils.toStringArray(this.beans.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanNamesForType(@Nullable ResolvableType type) {
/* 332 */     boolean isFactoryType = false;
/* 333 */     if (type != null) {
/* 334 */       Class<?> resolved = type.resolve();
/* 335 */       if (resolved != null && FactoryBean.class.isAssignableFrom(resolved)) {
/* 336 */         isFactoryType = true;
/*     */       }
/*     */     } 
/* 339 */     List<String> matches = new ArrayList<>();
/* 340 */     for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
/* 341 */       String name = entry.getKey();
/* 342 */       Object beanInstance = entry.getValue();
/* 343 */       if (beanInstance instanceof FactoryBean && !isFactoryType) {
/* 344 */         Class<?> objectType = ((FactoryBean)beanInstance).getObjectType();
/* 345 */         if (objectType != null && (type == null || type.isAssignableFrom(objectType))) {
/* 346 */           matches.add(name);
/*     */         }
/*     */         continue;
/*     */       } 
/* 350 */       if (type == null || type.isInstance(beanInstance)) {
/* 351 */         matches.add(name);
/*     */       }
/*     */     } 
/*     */     
/* 355 */     return StringUtils.toStringArray(matches);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanNamesForType(@Nullable Class<?> type) {
/* 360 */     return getBeanNamesForType(ResolvableType.forClass(type));
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
/* 365 */     return getBeanNamesForType(ResolvableType.forClass(type));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
/* 370 */     return getBeansOfType(type, true, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 378 */     boolean isFactoryType = (type != null && FactoryBean.class.isAssignableFrom(type));
/* 379 */     Map<String, T> matches = new LinkedHashMap<>();
/*     */     
/* 381 */     for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
/* 382 */       String beanName = entry.getKey();
/* 383 */       Object beanInstance = entry.getValue();
/*     */       
/* 385 */       if (beanInstance instanceof FactoryBean && !isFactoryType) {
/*     */         
/* 387 */         FactoryBean<?> factory = (FactoryBean)beanInstance;
/* 388 */         Class<?> objectType = factory.getObjectType();
/* 389 */         if ((includeNonSingletons || factory.isSingleton()) && objectType != null && (type == null || type
/* 390 */           .isAssignableFrom(objectType))) {
/* 391 */           matches.put(beanName, getBean(beanName, type));
/*     */         }
/*     */         continue;
/*     */       } 
/* 395 */       if (type == null || type.isInstance(beanInstance)) {
/*     */ 
/*     */         
/* 398 */         if (isFactoryType) {
/* 399 */           beanName = "&" + beanName;
/*     */         }
/* 401 */         matches.put(beanName, (T)beanInstance);
/*     */       } 
/*     */     } 
/*     */     
/* 405 */     return matches;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
/* 410 */     List<String> results = new ArrayList<>();
/* 411 */     for (String beanName : this.beans.keySet()) {
/* 412 */       if (findAnnotationOnBean(beanName, annotationType) != null) {
/* 413 */         results.add(beanName);
/*     */       }
/*     */     } 
/* 416 */     return StringUtils.toStringArray(results);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
/* 423 */     Map<String, Object> results = new LinkedHashMap<>();
/* 424 */     for (String beanName : this.beans.keySet()) {
/* 425 */       if (findAnnotationOnBean(beanName, annotationType) != null) {
/* 426 */         results.put(beanName, getBean(beanName));
/*     */       }
/*     */     } 
/* 429 */     return results;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException {
/* 437 */     Class<?> beanType = getType(beanName);
/* 438 */     return (beanType != null) ? (A)AnnotationUtils.findAnnotation(beanType, annotationType) : null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/StaticListableBeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */