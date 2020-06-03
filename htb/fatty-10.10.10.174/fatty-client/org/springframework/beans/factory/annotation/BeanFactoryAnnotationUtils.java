/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Predicate;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.AutowireCandidateQualifier;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BeanFactoryAnnotationUtils
/*     */ {
/*     */   public static <T> Map<String, T> qualifiedBeansOfType(ListableBeanFactory beanFactory, Class<T> beanType, String qualifier) throws BeansException {
/*  65 */     String[] candidateBeans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, beanType);
/*  66 */     Map<String, T> result = new LinkedHashMap<>(4);
/*  67 */     for (String beanName : candidateBeans) {
/*  68 */       if (isQualifierMatch(qualifier::equals, beanName, (BeanFactory)beanFactory)) {
/*  69 */         result.put(beanName, (T)beanFactory.getBean(beanName, beanType));
/*     */       }
/*     */     } 
/*  72 */     return result;
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
/*     */   public static <T> T qualifiedBeanOfType(BeanFactory beanFactory, Class<T> beanType, String qualifier) throws BeansException {
/*  91 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/*     */     
/*  93 */     if (beanFactory instanceof ListableBeanFactory)
/*     */     {
/*  95 */       return qualifiedBeanOfType((ListableBeanFactory)beanFactory, beanType, qualifier);
/*     */     }
/*  97 */     if (beanFactory.containsBean(qualifier))
/*     */     {
/*  99 */       return (T)beanFactory.getBean(qualifier, beanType);
/*     */     }
/*     */     
/* 102 */     throw new NoSuchBeanDefinitionException(qualifier, "No matching " + beanType.getSimpleName() + " bean found for bean name '" + qualifier + "'! (Note: Qualifier matching not supported because given BeanFactory does not implement ConfigurableListableBeanFactory.)");
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
/*     */   private static <T> T qualifiedBeanOfType(ListableBeanFactory bf, Class<T> beanType, String qualifier) {
/* 118 */     String[] candidateBeans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(bf, beanType);
/* 119 */     String matchingBean = null;
/* 120 */     for (String beanName : candidateBeans) {
/* 121 */       if (isQualifierMatch(qualifier::equals, beanName, (BeanFactory)bf)) {
/* 122 */         if (matchingBean != null) {
/* 123 */           throw new NoUniqueBeanDefinitionException(beanType, new String[] { matchingBean, beanName });
/*     */         }
/* 125 */         matchingBean = beanName;
/*     */       } 
/*     */     } 
/* 128 */     if (matchingBean != null) {
/* 129 */       return (T)bf.getBean(matchingBean, beanType);
/*     */     }
/* 131 */     if (bf.containsBean(qualifier))
/*     */     {
/* 133 */       return (T)bf.getBean(qualifier, beanType);
/*     */     }
/*     */     
/* 136 */     throw new NoSuchBeanDefinitionException(qualifier, "No matching " + beanType.getSimpleName() + " bean found for qualifier '" + qualifier + "' - neither qualifier match nor bean name match!");
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
/*     */   public static boolean isQualifierMatch(Predicate<String> qualifier, String beanName, @Nullable BeanFactory beanFactory) {
/* 155 */     if (qualifier.test(beanName)) {
/* 156 */       return true;
/*     */     }
/* 158 */     if (beanFactory != null) {
/* 159 */       for (String alias : beanFactory.getAliases(beanName)) {
/* 160 */         if (qualifier.test(alias)) {
/* 161 */           return true;
/*     */         }
/*     */       } 
/*     */       try {
/* 165 */         Class<?> beanType = beanFactory.getType(beanName);
/* 166 */         if (beanFactory instanceof ConfigurableBeanFactory) {
/* 167 */           BeanDefinition bd = ((ConfigurableBeanFactory)beanFactory).getMergedBeanDefinition(beanName);
/*     */           
/* 169 */           if (bd instanceof AbstractBeanDefinition) {
/* 170 */             AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/* 171 */             AutowireCandidateQualifier candidate = abd.getQualifier(Qualifier.class.getName());
/* 172 */             if (candidate != null) {
/* 173 */               Object value = candidate.getAttribute("value");
/* 174 */               if (value != null && qualifier.test(value.toString())) {
/* 175 */                 return true;
/*     */               }
/*     */             } 
/*     */           } 
/*     */           
/* 180 */           if (bd instanceof RootBeanDefinition) {
/* 181 */             Method factoryMethod = ((RootBeanDefinition)bd).getResolvedFactoryMethod();
/* 182 */             if (factoryMethod != null) {
/* 183 */               Qualifier targetAnnotation = (Qualifier)AnnotationUtils.getAnnotation(factoryMethod, Qualifier.class);
/* 184 */               if (targetAnnotation != null) {
/* 185 */                 return qualifier.test(targetAnnotation.value());
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 191 */         if (beanType != null) {
/* 192 */           Qualifier targetAnnotation = (Qualifier)AnnotationUtils.getAnnotation(beanType, Qualifier.class);
/* 193 */           if (targetAnnotation != null) {
/* 194 */             return qualifier.test(targetAnnotation.value());
/*     */           }
/*     */         }
/*     */       
/* 198 */       } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {}
/*     */     } 
/*     */ 
/*     */     
/* 202 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/BeanFactoryAnnotationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */