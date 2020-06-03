/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class BeanFactoryUtils
/*     */ {
/*     */   public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";
/*  60 */   private static final Map<String, String> transformedBeanNameCache = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFactoryDereference(@Nullable String name) {
/*  71 */     return (name != null && name.startsWith("&"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String transformedBeanName(String name) {
/*  82 */     Assert.notNull(name, "'name' must not be null");
/*  83 */     if (!name.startsWith("&")) {
/*  84 */       return name;
/*     */     }
/*  86 */     return transformedBeanNameCache.computeIfAbsent(name, beanName -> {
/*     */           while (true) {
/*     */             beanName = beanName.substring("&".length());
/*     */             if (!beanName.startsWith("&")) {
/*     */               return beanName;
/*     */             }
/*     */           } 
/*     */         });
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
/*     */   public static boolean isGeneratedBeanName(@Nullable String name) {
/* 105 */     return (name != null && name.contains("#"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String originalBeanName(String name) {
/* 116 */     Assert.notNull(name, "'name' must not be null");
/* 117 */     int separatorIndex = name.indexOf("#");
/* 118 */     return (separatorIndex != -1) ? name.substring(0, separatorIndex) : name;
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
/*     */   public static int countBeansIncludingAncestors(ListableBeanFactory lbf) {
/* 134 */     return (beanNamesIncludingAncestors(lbf)).length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] beanNamesIncludingAncestors(ListableBeanFactory lbf) {
/* 144 */     return beanNamesForTypeIncludingAncestors(lbf, Object.class);
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
/*     */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, ResolvableType type) {
/* 162 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 163 */     String[] result = lbf.getBeanNamesForType(type);
/* 164 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 165 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 166 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 167 */         String[] parentResult = beanNamesForTypeIncludingAncestors((ListableBeanFactory)hbf
/* 168 */             .getParentBeanFactory(), type);
/* 169 */         result = mergeNamesWithParent(result, parentResult, hbf);
/*     */       } 
/*     */     } 
/* 172 */     return result;
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
/*     */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type) {
/* 189 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 190 */     String[] result = lbf.getBeanNamesForType(type);
/* 191 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 192 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 193 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 194 */         String[] parentResult = beanNamesForTypeIncludingAncestors((ListableBeanFactory)hbf
/* 195 */             .getParentBeanFactory(), type);
/* 196 */         result = mergeNamesWithParent(result, parentResult, hbf);
/*     */       } 
/*     */     } 
/* 199 */     return result;
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
/*     */ 
/*     */   
/*     */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
/* 226 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 227 */     String[] result = lbf.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/* 228 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 229 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 230 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 231 */         String[] parentResult = beanNamesForTypeIncludingAncestors((ListableBeanFactory)hbf
/* 232 */             .getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
/* 233 */         result = mergeNamesWithParent(result, parentResult, hbf);
/*     */       } 
/*     */     } 
/* 236 */     return result;
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
/*     */   public static String[] beanNamesForAnnotationIncludingAncestors(ListableBeanFactory lbf, Class<? extends Annotation> annotationType) {
/* 252 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 253 */     String[] result = lbf.getBeanNamesForAnnotation(annotationType);
/* 254 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 255 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 256 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 257 */         String[] parentResult = beanNamesForAnnotationIncludingAncestors((ListableBeanFactory)hbf
/* 258 */             .getParentBeanFactory(), annotationType);
/* 259 */         result = mergeNamesWithParent(result, parentResult, hbf);
/*     */       } 
/*     */     } 
/* 262 */     return result;
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
/*     */ 
/*     */   
/*     */   public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type) throws BeansException {
/* 289 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 290 */     Map<String, T> result = new LinkedHashMap<>(4);
/* 291 */     result.putAll(lbf.getBeansOfType(type));
/* 292 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 293 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 294 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 295 */         Map<String, T> parentResult = beansOfTypeIncludingAncestors((ListableBeanFactory)hbf
/* 296 */             .getParentBeanFactory(), type);
/* 297 */         parentResult.forEach((beanName, beanInstance) -> {
/*     */               if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
/*     */                 result.put(beanName, beanInstance);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/* 304 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 338 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 339 */     Map<String, T> result = new LinkedHashMap<>(4);
/* 340 */     result.putAll(lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit));
/* 341 */     if (lbf instanceof HierarchicalBeanFactory) {
/* 342 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 343 */       if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
/* 344 */         Map<String, T> parentResult = beansOfTypeIncludingAncestors((ListableBeanFactory)hbf
/* 345 */             .getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
/* 346 */         parentResult.forEach((beanName, beanInstance) -> {
/*     */               if (!result.containsKey(beanName) && !hbf.containsLocalBean(beanName)) {
/*     */                 result.put(beanName, beanInstance);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/* 353 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type) throws BeansException {
/* 382 */     Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type);
/* 383 */     return uniqueBean(type, beansOfType);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 420 */     Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type, includeNonSingletons, allowEagerInit);
/* 421 */     return uniqueBean(type, beansOfType);
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
/*     */   public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type) throws BeansException {
/* 442 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 443 */     Map<String, T> beansOfType = lbf.getBeansOfType(type);
/* 444 */     return uniqueBean(type, beansOfType);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
/* 476 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 477 */     Map<String, T> beansOfType = lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit);
/* 478 */     return uniqueBean(type, beansOfType);
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
/*     */   private static String[] mergeNamesWithParent(String[] result, String[] parentResult, HierarchicalBeanFactory hbf) {
/* 491 */     if (parentResult.length == 0) {
/* 492 */       return result;
/*     */     }
/* 494 */     List<String> merged = new ArrayList<>(result.length + parentResult.length);
/* 495 */     merged.addAll(Arrays.asList(result));
/* 496 */     for (String beanName : parentResult) {
/* 497 */       if (!merged.contains(beanName) && !hbf.containsLocalBean(beanName)) {
/* 498 */         merged.add(beanName);
/*     */       }
/*     */     } 
/* 501 */     return StringUtils.toStringArray(merged);
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
/*     */   private static <T> T uniqueBean(Class<T> type, Map<String, T> matchingBeans) {
/* 513 */     int count = matchingBeans.size();
/* 514 */     if (count == 1) {
/* 515 */       return matchingBeans.values().iterator().next();
/*     */     }
/* 517 */     if (count > 1) {
/* 518 */       throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
/*     */     }
/*     */     
/* 521 */     throw new NoSuchBeanDefinitionException(type);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/BeanFactoryUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */