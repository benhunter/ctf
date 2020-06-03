/*     */ package org.springframework.web.method;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.OrderUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ControllerAdviceBean
/*     */   implements Ordered
/*     */ {
/*     */   private final Object bean;
/*     */   @Nullable
/*     */   private final BeanFactory beanFactory;
/*     */   private final int order;
/*     */   private final HandlerTypePredicate beanTypePredicate;
/*     */   
/*     */   public ControllerAdviceBean(Object bean) {
/*  64 */     this(bean, (BeanFactory)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ControllerAdviceBean(String beanName, @Nullable BeanFactory beanFactory) {
/*  73 */     this(beanName, beanFactory);
/*     */   }
/*     */   private ControllerAdviceBean(Object bean, @Nullable BeanFactory beanFactory) {
/*     */     Class<?> beanType;
/*  77 */     this.bean = bean;
/*  78 */     this.beanFactory = beanFactory;
/*     */ 
/*     */     
/*  81 */     if (bean instanceof String) {
/*  82 */       String beanName = (String)bean;
/*  83 */       Assert.hasText(beanName, "Bean name must not be null");
/*  84 */       Assert.notNull(beanFactory, "BeanFactory must not be null");
/*  85 */       if (!beanFactory.containsBean(beanName)) {
/*  86 */         throw new IllegalArgumentException("BeanFactory [" + beanFactory + "] does not contain specified controller advice bean '" + beanName + "'");
/*     */       }
/*     */       
/*  89 */       beanType = this.beanFactory.getType(beanName);
/*  90 */       this.order = initOrderFromBeanType(beanType);
/*     */     } else {
/*     */       
/*  93 */       Assert.notNull(bean, "Bean must not be null");
/*  94 */       beanType = bean.getClass();
/*  95 */       this.order = initOrderFromBean(bean);
/*     */     } 
/*     */ 
/*     */     
/*  99 */     ControllerAdvice annotation = (beanType != null) ? (ControllerAdvice)AnnotatedElementUtils.findMergedAnnotation(beanType, ControllerAdvice.class) : null;
/*     */     
/* 101 */     if (annotation != null) {
/* 102 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 107 */         .beanTypePredicate = HandlerTypePredicate.builder().basePackage(annotation.basePackages()).basePackageClass(annotation.basePackageClasses()).assignableType(annotation.assignableTypes()).annotation((Class<? extends Annotation>[])annotation.annotations()).build();
/*     */     } else {
/*     */       
/* 110 */       this.beanTypePredicate = HandlerTypePredicate.forAnyHandlerType();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 121 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getBeanType() {
/* 132 */     Class<?> beanType = (this.bean instanceof String) ? obtainBeanFactory().getType((String)this.bean) : this.bean.getClass();
/* 133 */     return (beanType != null) ? ClassUtils.getUserClass(beanType) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object resolveBean() {
/* 140 */     return (this.bean instanceof String) ? obtainBeanFactory().getBean((String)this.bean) : this.bean;
/*     */   }
/*     */   
/*     */   private BeanFactory obtainBeanFactory() {
/* 144 */     Assert.state((this.beanFactory != null), "No BeanFactory set");
/* 145 */     return this.beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isApplicableToBeanType(@Nullable Class<?> beanType) {
/* 156 */     return this.beanTypePredicate.test(beanType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 162 */     if (this == other) {
/* 163 */       return true;
/*     */     }
/* 165 */     if (!(other instanceof ControllerAdviceBean)) {
/* 166 */       return false;
/*     */     }
/* 168 */     ControllerAdviceBean otherAdvice = (ControllerAdviceBean)other;
/* 169 */     return (this.bean.equals(otherAdvice.bean) && this.beanFactory == otherAdvice.beanFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 174 */     return this.bean.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 179 */     return this.bean.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context) {
/* 189 */     return (List<ControllerAdviceBean>)Arrays.<String>stream(BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory)context, Object.class))
/* 190 */       .filter(name -> (context.findAnnotationOnBean(name, ControllerAdvice.class) != null))
/* 191 */       .map(name -> new ControllerAdviceBean(name, (BeanFactory)context))
/* 192 */       .collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   private static int initOrderFromBean(Object bean) {
/* 196 */     return (bean instanceof Ordered) ? ((Ordered)bean).getOrder() : initOrderFromBeanType(bean.getClass());
/*     */   }
/*     */   
/*     */   private static int initOrderFromBeanType(@Nullable Class<?> beanType) {
/* 200 */     Integer order = null;
/* 201 */     if (beanType != null) {
/* 202 */       order = OrderUtils.getOrder(beanType);
/*     */     }
/* 204 */     return (order != null) ? order.intValue() : Integer.MAX_VALUE;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/ControllerAdviceBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */