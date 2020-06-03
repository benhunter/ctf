/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.util.ConcurrentReferenceHashMap;
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
/*    */ abstract class BeanAnnotationHelper
/*    */ {
/* 35 */   private static final Map<Method, String> beanNameCache = (Map<Method, String>)new ConcurrentReferenceHashMap();
/*    */   
/* 37 */   private static final Map<Method, Boolean> scopedProxyCache = (Map<Method, Boolean>)new ConcurrentReferenceHashMap();
/*    */ 
/*    */   
/*    */   public static boolean isBeanAnnotated(Method method) {
/* 41 */     return AnnotatedElementUtils.hasAnnotation(method, Bean.class);
/*    */   }
/*    */   
/*    */   public static String determineBeanNameFor(Method beanMethod) {
/* 45 */     String beanName = beanNameCache.get(beanMethod);
/* 46 */     if (beanName == null) {
/*    */       
/* 48 */       beanName = beanMethod.getName();
/*    */ 
/*    */       
/* 51 */       AnnotationAttributes bean = AnnotatedElementUtils.findMergedAnnotationAttributes(beanMethod, Bean.class, false, false);
/* 52 */       if (bean != null) {
/* 53 */         String[] names = bean.getStringArray("name");
/* 54 */         if (names.length > 0) {
/* 55 */           beanName = names[0];
/*    */         }
/*    */       } 
/* 58 */       beanNameCache.put(beanMethod, beanName);
/*    */     } 
/* 60 */     return beanName;
/*    */   }
/*    */   
/*    */   public static boolean isScopedProxy(Method beanMethod) {
/* 64 */     Boolean scopedProxy = scopedProxyCache.get(beanMethod);
/* 65 */     if (scopedProxy == null) {
/*    */       
/* 67 */       AnnotationAttributes scope = AnnotatedElementUtils.findMergedAnnotationAttributes(beanMethod, Scope.class, false, false);
/* 68 */       scopedProxy = Boolean.valueOf((scope != null && scope.getEnum("proxyMode") != ScopedProxyMode.NO));
/* 69 */       scopedProxyCache.put(beanMethod, scopedProxy);
/*    */     } 
/* 71 */     return scopedProxy.booleanValue();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/BeanAnnotationHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */