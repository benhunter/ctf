/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CachedIntrospectionResults
/*     */ {
/*     */   public static final String IGNORE_BEANINFO_PROPERTY_NAME = "spring.beaninfo.ignore";
/*  97 */   private static final boolean shouldIntrospectorIgnoreBeaninfoClasses = SpringProperties.getFlag("spring.beaninfo.ignore");
/*     */ 
/*     */   
/* 100 */   private static List<BeanInfoFactory> beanInfoFactories = SpringFactoriesLoader.loadFactories(BeanInfoFactory.class, CachedIntrospectionResults.class
/* 101 */       .getClassLoader());
/*     */   
/* 103 */   private static final Log logger = LogFactory.getLog(CachedIntrospectionResults.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   static final Set<ClassLoader> acceptedClassLoaders = Collections.newSetFromMap(new ConcurrentHashMap<>(16));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   static final ConcurrentMap<Class<?>, CachedIntrospectionResults> strongClassCache = new ConcurrentHashMap<>(64);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   static final ConcurrentMap<Class<?>, CachedIntrospectionResults> softClassCache = (ConcurrentMap<Class<?>, CachedIntrospectionResults>)new ConcurrentReferenceHashMap(64);
/*     */ 
/*     */ 
/*     */   
/*     */   private final BeanInfo beanInfo;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, PropertyDescriptor> propertyDescriptorCache;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ConcurrentMap<PropertyDescriptor, TypeDescriptor> typeDescriptorCache;
/*     */ 
/*     */ 
/*     */   
/*     */   public static void acceptClassLoader(@Nullable ClassLoader classLoader) {
/* 140 */     if (classLoader != null) {
/* 141 */       acceptedClassLoaders.add(classLoader);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearClassLoader(@Nullable ClassLoader classLoader) {
/* 152 */     acceptedClassLoaders.removeIf(registeredLoader -> isUnderneathClassLoader(registeredLoader, classLoader));
/*     */     
/* 154 */     strongClassCache.keySet().removeIf(beanClass -> isUnderneathClassLoader(beanClass.getClassLoader(), classLoader));
/*     */     
/* 156 */     softClassCache.keySet().removeIf(beanClass -> isUnderneathClassLoader(beanClass.getClassLoader(), classLoader));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static CachedIntrospectionResults forClass(Class<?> beanClass) throws BeansException {
/*     */     ConcurrentMap<Class<?>, CachedIntrospectionResults> classCacheToUse;
/* 168 */     CachedIntrospectionResults results = strongClassCache.get(beanClass);
/* 169 */     if (results != null) {
/* 170 */       return results;
/*     */     }
/* 172 */     results = softClassCache.get(beanClass);
/* 173 */     if (results != null) {
/* 174 */       return results;
/*     */     }
/*     */     
/* 177 */     results = new CachedIntrospectionResults(beanClass);
/*     */ 
/*     */     
/* 180 */     if (ClassUtils.isCacheSafe(beanClass, CachedIntrospectionResults.class.getClassLoader()) || 
/* 181 */       isClassLoaderAccepted(beanClass.getClassLoader())) {
/* 182 */       classCacheToUse = strongClassCache;
/*     */     } else {
/*     */       
/* 185 */       if (logger.isDebugEnabled()) {
/* 186 */         logger.debug("Not strongly caching class [" + beanClass.getName() + "] because it is not cache-safe");
/*     */       }
/* 188 */       classCacheToUse = softClassCache;
/*     */     } 
/*     */     
/* 191 */     CachedIntrospectionResults existing = classCacheToUse.putIfAbsent(beanClass, results);
/* 192 */     return (existing != null) ? existing : results;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isClassLoaderAccepted(ClassLoader classLoader) {
/* 203 */     for (ClassLoader acceptedLoader : acceptedClassLoaders) {
/* 204 */       if (isUnderneathClassLoader(classLoader, acceptedLoader)) {
/* 205 */         return true;
/*     */       }
/*     */     } 
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isUnderneathClassLoader(@Nullable ClassLoader candidate, @Nullable ClassLoader parent) {
/* 218 */     if (candidate == parent) {
/* 219 */       return true;
/*     */     }
/* 221 */     if (candidate == null) {
/* 222 */       return false;
/*     */     }
/* 224 */     ClassLoader classLoaderToCheck = candidate;
/* 225 */     while (classLoaderToCheck != null) {
/* 226 */       classLoaderToCheck = classLoaderToCheck.getParent();
/* 227 */       if (classLoaderToCheck == parent) {
/* 228 */         return true;
/*     */       }
/*     */     } 
/* 231 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException {
/* 241 */     for (BeanInfoFactory beanInfoFactory : beanInfoFactories) {
/* 242 */       BeanInfo beanInfo = beanInfoFactory.getBeanInfo(beanClass);
/* 243 */       if (beanInfo != null) {
/* 244 */         return beanInfo;
/*     */       }
/*     */     } 
/* 247 */     return shouldIntrospectorIgnoreBeaninfoClasses ? 
/* 248 */       Introspector.getBeanInfo(beanClass, 3) : 
/* 249 */       Introspector.getBeanInfo(beanClass);
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
/*     */   private CachedIntrospectionResults(Class<?> beanClass) throws BeansException {
/*     */     try {
/* 270 */       if (logger.isTraceEnabled()) {
/* 271 */         logger.trace("Getting BeanInfo for class [" + beanClass.getName() + "]");
/*     */       }
/* 273 */       this.beanInfo = getBeanInfo(beanClass);
/*     */       
/* 275 */       if (logger.isTraceEnabled()) {
/* 276 */         logger.trace("Caching PropertyDescriptors for class [" + beanClass.getName() + "]");
/*     */       }
/* 278 */       this.propertyDescriptorCache = new LinkedHashMap<>();
/*     */ 
/*     */       
/* 281 */       PropertyDescriptor[] pds = this.beanInfo.getPropertyDescriptors();
/* 282 */       for (PropertyDescriptor pd : pds) {
/* 283 */         if (Class.class != beanClass || (
/* 284 */           !"classLoader".equals(pd.getName()) && !"protectionDomain".equals(pd.getName()))) {
/*     */ 
/*     */ 
/*     */           
/* 288 */           if (logger.isTraceEnabled()) {
/* 289 */             logger.trace("Found bean property '" + pd.getName() + "'" + (
/* 290 */                 (pd.getPropertyType() != null) ? (" of type [" + pd.getPropertyType().getName() + "]") : "") + (
/* 291 */                 (pd.getPropertyEditorClass() != null) ? ("; editor [" + pd
/* 292 */                 .getPropertyEditorClass().getName() + "]") : ""));
/*     */           }
/* 294 */           pd = buildGenericTypeAwarePropertyDescriptor(beanClass, pd);
/* 295 */           this.propertyDescriptorCache.put(pd.getName(), pd);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 300 */       Class<?> currClass = beanClass;
/* 301 */       while (currClass != null && currClass != Object.class) {
/* 302 */         introspectInterfaces(beanClass, currClass);
/* 303 */         currClass = currClass.getSuperclass();
/*     */       } 
/*     */       
/* 306 */       this.typeDescriptorCache = (ConcurrentMap<PropertyDescriptor, TypeDescriptor>)new ConcurrentReferenceHashMap();
/*     */     }
/* 308 */     catch (IntrospectionException ex) {
/* 309 */       throw new FatalBeanException("Failed to obtain BeanInfo for class [" + beanClass.getName() + "]", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void introspectInterfaces(Class<?> beanClass, Class<?> currClass) throws IntrospectionException {
/* 314 */     for (Class<?> ifc : currClass.getInterfaces()) {
/* 315 */       if (!ClassUtils.isJavaLanguageInterface(ifc)) {
/* 316 */         for (PropertyDescriptor pd : getBeanInfo(ifc).getPropertyDescriptors()) {
/* 317 */           PropertyDescriptor existingPd = this.propertyDescriptorCache.get(pd.getName());
/* 318 */           if (existingPd == null || (existingPd
/* 319 */             .getReadMethod() == null && pd.getReadMethod() != null)) {
/*     */ 
/*     */             
/* 322 */             pd = buildGenericTypeAwarePropertyDescriptor(beanClass, pd);
/* 323 */             this.propertyDescriptorCache.put(pd.getName(), pd);
/*     */           } 
/*     */         } 
/* 326 */         introspectInterfaces(ifc, ifc);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   BeanInfo getBeanInfo() {
/* 333 */     return this.beanInfo;
/*     */   }
/*     */   
/*     */   Class<?> getBeanClass() {
/* 337 */     return this.beanInfo.getBeanDescriptor().getBeanClass();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   PropertyDescriptor getPropertyDescriptor(String name) {
/* 342 */     PropertyDescriptor pd = this.propertyDescriptorCache.get(name);
/* 343 */     if (pd == null && StringUtils.hasLength(name)) {
/*     */       
/* 345 */       pd = this.propertyDescriptorCache.get(StringUtils.uncapitalize(name));
/* 346 */       if (pd == null) {
/* 347 */         pd = this.propertyDescriptorCache.get(StringUtils.capitalize(name));
/*     */       }
/*     */     } 
/* 350 */     return (pd == null || pd instanceof GenericTypeAwarePropertyDescriptor) ? pd : 
/* 351 */       buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd);
/*     */   }
/*     */   
/*     */   PropertyDescriptor[] getPropertyDescriptors() {
/* 355 */     PropertyDescriptor[] pds = new PropertyDescriptor[this.propertyDescriptorCache.size()];
/* 356 */     int i = 0;
/* 357 */     for (PropertyDescriptor pd : this.propertyDescriptorCache.values()) {
/* 358 */       pds[i] = (pd instanceof GenericTypeAwarePropertyDescriptor) ? pd : 
/* 359 */         buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd);
/* 360 */       i++;
/*     */     } 
/* 362 */     return pds;
/*     */   }
/*     */   
/*     */   private PropertyDescriptor buildGenericTypeAwarePropertyDescriptor(Class<?> beanClass, PropertyDescriptor pd) {
/*     */     try {
/* 367 */       return new GenericTypeAwarePropertyDescriptor(beanClass, pd.getName(), pd.getReadMethod(), pd
/* 368 */           .getWriteMethod(), pd.getPropertyEditorClass());
/*     */     }
/* 370 */     catch (IntrospectionException ex) {
/* 371 */       throw new FatalBeanException("Failed to re-introspect class [" + beanClass.getName() + "]", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   TypeDescriptor addTypeDescriptor(PropertyDescriptor pd, TypeDescriptor td) {
/* 376 */     TypeDescriptor existing = this.typeDescriptorCache.putIfAbsent(pd, td);
/* 377 */     return (existing != null) ? existing : td;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   TypeDescriptor getTypeDescriptor(PropertyDescriptor pd) {
/* 382 */     return this.typeDescriptorCache.get(pd);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/CachedIntrospectionResults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */