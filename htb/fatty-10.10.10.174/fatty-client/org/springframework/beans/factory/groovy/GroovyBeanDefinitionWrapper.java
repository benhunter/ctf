/*     */ package org.springframework.beans.factory.groovy;
/*     */ 
/*     */ import groovy.lang.GroovyObjectSupport;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinitionHolder;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.RuntimeBeanReference;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GroovyBeanDefinitionWrapper
/*     */   extends GroovyObjectSupport
/*     */ {
/*     */   private static final String PARENT = "parent";
/*     */   private static final String AUTOWIRE = "autowire";
/*     */   private static final String CONSTRUCTOR_ARGS = "constructorArgs";
/*     */   private static final String FACTORY_BEAN = "factoryBean";
/*     */   private static final String FACTORY_METHOD = "factoryMethod";
/*     */   private static final String INIT_METHOD = "initMethod";
/*     */   private static final String DESTROY_METHOD = "destroyMethod";
/*     */   private static final String SINGLETON = "singleton";
/*  54 */   private static final List<String> dynamicProperties = new ArrayList<>(8);
/*     */   
/*     */   static {
/*  57 */     dynamicProperties.add("parent");
/*  58 */     dynamicProperties.add("autowire");
/*  59 */     dynamicProperties.add("constructorArgs");
/*  60 */     dynamicProperties.add("factoryBean");
/*  61 */     dynamicProperties.add("factoryMethod");
/*  62 */     dynamicProperties.add("initMethod");
/*  63 */     dynamicProperties.add("destroyMethod");
/*  64 */     dynamicProperties.add("singleton");
/*     */   }
/*     */ 
/*     */   
/*     */   private String beanName;
/*     */   
/*     */   private Class<?> clazz;
/*     */   
/*     */   private Collection<?> constructorArgs;
/*     */   
/*     */   private AbstractBeanDefinition definition;
/*     */   
/*     */   private BeanWrapper definitionWrapper;
/*     */   
/*     */   private String parentName;
/*     */ 
/*     */   
/*     */   public GroovyBeanDefinitionWrapper(String beanName) {
/*  82 */     this.beanName = beanName;
/*     */   }
/*     */   
/*     */   public GroovyBeanDefinitionWrapper(String beanName, Class<?> clazz) {
/*  86 */     this.beanName = beanName;
/*  87 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   public GroovyBeanDefinitionWrapper(String beanName, Class<?> clazz, Collection<?> constructorArgs) {
/*  91 */     this.beanName = beanName;
/*  92 */     this.clazz = clazz;
/*  93 */     this.constructorArgs = constructorArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/*  98 */     return this.beanName;
/*     */   }
/*     */   
/*     */   public void setBeanDefinition(AbstractBeanDefinition definition) {
/* 102 */     this.definition = definition;
/*     */   }
/*     */   
/*     */   public AbstractBeanDefinition getBeanDefinition() {
/* 106 */     if (this.definition == null) {
/* 107 */       this.definition = createBeanDefinition();
/*     */     }
/* 109 */     return this.definition;
/*     */   }
/*     */   
/*     */   protected AbstractBeanDefinition createBeanDefinition() {
/* 113 */     GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
/* 114 */     genericBeanDefinition.setBeanClass(this.clazz);
/* 115 */     if (!CollectionUtils.isEmpty(this.constructorArgs)) {
/* 116 */       ConstructorArgumentValues cav = new ConstructorArgumentValues();
/* 117 */       for (Object constructorArg : this.constructorArgs) {
/* 118 */         cav.addGenericArgumentValue(constructorArg);
/*     */       }
/* 120 */       genericBeanDefinition.setConstructorArgumentValues(cav);
/*     */     } 
/* 122 */     if (this.parentName != null) {
/* 123 */       genericBeanDefinition.setParentName(this.parentName);
/*     */     }
/* 125 */     this.definitionWrapper = (BeanWrapper)new BeanWrapperImpl(genericBeanDefinition);
/* 126 */     return (AbstractBeanDefinition)genericBeanDefinition;
/*     */   }
/*     */   
/*     */   public void setBeanDefinitionHolder(BeanDefinitionHolder holder) {
/* 130 */     this.definition = (AbstractBeanDefinition)holder.getBeanDefinition();
/* 131 */     this.beanName = holder.getBeanName();
/*     */   }
/*     */   
/*     */   public BeanDefinitionHolder getBeanDefinitionHolder() {
/* 135 */     return new BeanDefinitionHolder((BeanDefinition)getBeanDefinition(), getBeanName());
/*     */   }
/*     */   
/*     */   public void setParent(Object obj) {
/* 139 */     if (obj == null) {
/* 140 */       throw new IllegalArgumentException("Parent bean cannot be set to a null runtime bean reference!");
/*     */     }
/* 142 */     if (obj instanceof String) {
/* 143 */       this.parentName = (String)obj;
/*     */     }
/* 145 */     else if (obj instanceof RuntimeBeanReference) {
/* 146 */       this.parentName = ((RuntimeBeanReference)obj).getBeanName();
/*     */     }
/* 148 */     else if (obj instanceof GroovyBeanDefinitionWrapper) {
/* 149 */       this.parentName = ((GroovyBeanDefinitionWrapper)obj).getBeanName();
/*     */     } 
/* 151 */     getBeanDefinition().setParentName(this.parentName);
/* 152 */     getBeanDefinition().setAbstract(false);
/*     */   }
/*     */   
/*     */   public GroovyBeanDefinitionWrapper addProperty(String propertyName, Object propertyValue) {
/* 156 */     if (propertyValue instanceof GroovyBeanDefinitionWrapper) {
/* 157 */       propertyValue = ((GroovyBeanDefinitionWrapper)propertyValue).getBeanDefinition();
/*     */     }
/* 159 */     getBeanDefinition().getPropertyValues().add(propertyName, propertyValue);
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String property) {
/* 166 */     if (this.definitionWrapper.isReadableProperty(property)) {
/* 167 */       return this.definitionWrapper.getPropertyValue(property);
/*     */     }
/* 169 */     if (dynamicProperties.contains(property)) {
/* 170 */       return null;
/*     */     }
/* 172 */     return super.getProperty(property);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setProperty(String property, Object newValue) {
/* 177 */     if ("parent".equals(property)) {
/* 178 */       setParent(newValue);
/*     */     } else {
/*     */       
/* 181 */       AbstractBeanDefinition bd = getBeanDefinition();
/* 182 */       if ("autowire".equals(property)) {
/* 183 */         if ("byName".equals(newValue)) {
/* 184 */           bd.setAutowireMode(1);
/*     */         }
/* 186 */         else if ("byType".equals(newValue)) {
/* 187 */           bd.setAutowireMode(2);
/*     */         }
/* 189 */         else if ("constructor".equals(newValue)) {
/* 190 */           bd.setAutowireMode(3);
/*     */         }
/* 192 */         else if (Boolean.TRUE.equals(newValue)) {
/* 193 */           bd.setAutowireMode(1);
/*     */         }
/*     */       
/*     */       }
/* 197 */       else if ("constructorArgs".equals(property) && newValue instanceof List) {
/* 198 */         ConstructorArgumentValues cav = new ConstructorArgumentValues();
/* 199 */         List args = (List)newValue;
/* 200 */         for (Object arg : args) {
/* 201 */           cav.addGenericArgumentValue(arg);
/*     */         }
/* 203 */         bd.setConstructorArgumentValues(cav);
/*     */       
/*     */       }
/* 206 */       else if ("factoryBean".equals(property)) {
/* 207 */         if (newValue != null) {
/* 208 */           bd.setFactoryBeanName(newValue.toString());
/*     */         
/*     */         }
/*     */       }
/* 212 */       else if ("factoryMethod".equals(property)) {
/* 213 */         if (newValue != null) {
/* 214 */           bd.setFactoryMethodName(newValue.toString());
/*     */         
/*     */         }
/*     */       }
/* 218 */       else if ("initMethod".equals(property)) {
/* 219 */         if (newValue != null) {
/* 220 */           bd.setInitMethodName(newValue.toString());
/*     */         
/*     */         }
/*     */       }
/* 224 */       else if ("destroyMethod".equals(property)) {
/* 225 */         if (newValue != null) {
/* 226 */           bd.setDestroyMethodName(newValue.toString());
/*     */         
/*     */         }
/*     */       }
/* 230 */       else if ("singleton".equals(property)) {
/* 231 */         bd.setScope(Boolean.TRUE.equals(newValue) ? "singleton" : "prototype");
/*     */       
/*     */       }
/* 234 */       else if (this.definitionWrapper.isWritableProperty(property)) {
/* 235 */         this.definitionWrapper.setPropertyValue(property, newValue);
/*     */       } else {
/*     */         
/* 238 */         super.setProperty(property, newValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/groovy/GroovyBeanDefinitionWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */