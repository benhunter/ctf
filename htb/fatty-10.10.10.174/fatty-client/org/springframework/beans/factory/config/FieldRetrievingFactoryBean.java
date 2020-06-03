/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldRetrievingFactoryBean
/*     */   implements FactoryBean<Object>, BeanNameAware, BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Class<?> targetClass;
/*     */   @Nullable
/*     */   private Object targetObject;
/*     */   @Nullable
/*     */   private String targetField;
/*     */   @Nullable
/*     */   private String staticField;
/*     */   @Nullable
/*     */   private String beanName;
/*     */   @Nullable
/*  77 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Field fieldObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetClass(@Nullable Class<?> targetClass) {
/*  92 */     this.targetClass = targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getTargetClass() {
/* 100 */     return this.targetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetObject(@Nullable Object targetObject) {
/* 111 */     this.targetObject = targetObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getTargetObject() {
/* 119 */     return this.targetObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetField(@Nullable String targetField) {
/* 130 */     this.targetField = (targetField != null) ? StringUtils.trimAllWhitespace(targetField) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getTargetField() {
/* 138 */     return this.targetField;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStaticField(String staticField) {
/* 149 */     this.staticField = StringUtils.trimAllWhitespace(staticField);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBeanName(String beanName) {
/* 160 */     this.beanName = StringUtils.trimAllWhitespace(BeanFactoryUtils.originalBeanName(beanName));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 165 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws ClassNotFoundException, NoSuchFieldException {
/* 171 */     if (this.targetClass != null && this.targetObject != null) {
/* 172 */       throw new IllegalArgumentException("Specify either targetClass or targetObject, not both");
/*     */     }
/*     */     
/* 175 */     if (this.targetClass == null && this.targetObject == null) {
/* 176 */       if (this.targetField != null) {
/* 177 */         throw new IllegalArgumentException("Specify targetClass or targetObject in combination with targetField");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 182 */       if (this.staticField == null) {
/* 183 */         this.staticField = this.beanName;
/* 184 */         Assert.state((this.staticField != null), "No target field specified");
/*     */       } 
/*     */ 
/*     */       
/* 188 */       int lastDotIndex = this.staticField.lastIndexOf('.');
/* 189 */       if (lastDotIndex == -1 || lastDotIndex == this.staticField.length()) {
/* 190 */         throw new IllegalArgumentException("staticField must be a fully qualified class plus static field name: e.g. 'example.MyExampleClass.MY_EXAMPLE_FIELD'");
/*     */       }
/*     */ 
/*     */       
/* 194 */       String className = this.staticField.substring(0, lastDotIndex);
/* 195 */       String fieldName = this.staticField.substring(lastDotIndex + 1);
/* 196 */       this.targetClass = ClassUtils.forName(className, this.beanClassLoader);
/* 197 */       this.targetField = fieldName;
/*     */     
/*     */     }
/* 200 */     else if (this.targetField == null) {
/*     */       
/* 202 */       throw new IllegalArgumentException("targetField is required");
/*     */     } 
/*     */ 
/*     */     
/* 206 */     Class<?> targetClass = (this.targetObject != null) ? this.targetObject.getClass() : this.targetClass;
/* 207 */     this.fieldObject = targetClass.getField(this.targetField);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getObject() throws IllegalAccessException {
/* 214 */     if (this.fieldObject == null) {
/* 215 */       throw new FactoryBeanNotInitializedException();
/*     */     }
/* 217 */     ReflectionUtils.makeAccessible(this.fieldObject);
/* 218 */     if (this.targetObject != null)
/*     */     {
/* 220 */       return this.fieldObject.get(this.targetObject);
/*     */     }
/*     */ 
/*     */     
/* 224 */     return this.fieldObject.get(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 230 */     return (this.fieldObject != null) ? this.fieldObject.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 235 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/FieldRetrievingFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */