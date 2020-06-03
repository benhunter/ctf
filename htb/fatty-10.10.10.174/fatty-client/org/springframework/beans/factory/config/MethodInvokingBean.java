/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodInvokingBean
/*     */   extends ArgumentConvertingMethodInvoker
/*     */   implements BeanClassLoaderAware, BeanFactoryAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*  71 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ConfigurableBeanFactory beanFactory;
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/*  79 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> resolveClassName(String className) throws ClassNotFoundException {
/*  84 */     return ClassUtils.forName(className, this.beanClassLoader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) {
/*  89 */     if (beanFactory instanceof ConfigurableBeanFactory) {
/*  90 */       this.beanFactory = (ConfigurableBeanFactory)beanFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeConverter getDefaultTypeConverter() {
/* 101 */     if (this.beanFactory != null) {
/* 102 */       return this.beanFactory.getTypeConverter();
/*     */     }
/*     */     
/* 105 */     return super.getDefaultTypeConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws Exception {
/* 112 */     prepare();
/* 113 */     invokeWithTargetException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object invokeWithTargetException() throws Exception {
/*     */     try {
/* 123 */       return invoke();
/*     */     }
/* 125 */     catch (InvocationTargetException ex) {
/* 126 */       if (ex.getTargetException() instanceof Exception) {
/* 127 */         throw (Exception)ex.getTargetException();
/*     */       }
/* 129 */       if (ex.getTargetException() instanceof Error) {
/* 130 */         throw (Error)ex.getTargetException();
/*     */       }
/* 132 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/MethodInvokingBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */