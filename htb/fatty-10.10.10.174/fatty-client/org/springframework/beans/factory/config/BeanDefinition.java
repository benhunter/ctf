/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.core.AttributeAccessor;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface BeanDefinition
/*     */   extends AttributeAccessor, BeanMetadataElement
/*     */ {
/*     */   public static final String SCOPE_SINGLETON = "singleton";
/*     */   public static final String SCOPE_PROTOTYPE = "prototype";
/*     */   public static final int ROLE_APPLICATION = 0;
/*     */   public static final int ROLE_SUPPORT = 1;
/*     */   public static final int ROLE_INFRASTRUCTURE = 2;
/*     */   
/*     */   void setParentName(@Nullable String paramString);
/*     */   
/*     */   @Nullable
/*     */   String getParentName();
/*     */   
/*     */   void setBeanClassName(@Nullable String paramString);
/*     */   
/*     */   @Nullable
/*     */   String getBeanClassName();
/*     */   
/*     */   void setScope(@Nullable String paramString);
/*     */   
/*     */   @Nullable
/*     */   String getScope();
/*     */   
/*     */   void setLazyInit(boolean paramBoolean);
/*     */   
/*     */   boolean isLazyInit();
/*     */   
/*     */   void setDependsOn(@Nullable String... paramVarArgs);
/*     */   
/*     */   @Nullable
/*     */   String[] getDependsOn();
/*     */   
/*     */   void setAutowireCandidate(boolean paramBoolean);
/*     */   
/*     */   boolean isAutowireCandidate();
/*     */   
/*     */   void setPrimary(boolean paramBoolean);
/*     */   
/*     */   boolean isPrimary();
/*     */   
/*     */   void setFactoryBeanName(@Nullable String paramString);
/*     */   
/*     */   @Nullable
/*     */   String getFactoryBeanName();
/*     */   
/*     */   void setFactoryMethodName(@Nullable String paramString);
/*     */   
/*     */   @Nullable
/*     */   String getFactoryMethodName();
/*     */   
/*     */   ConstructorArgumentValues getConstructorArgumentValues();
/*     */   
/*     */   default boolean hasConstructorArgumentValues() {
/* 227 */     return !getConstructorArgumentValues().isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MutablePropertyValues getPropertyValues();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean hasPropertyValues() {
/* 242 */     return !getPropertyValues().isEmpty();
/*     */   }
/*     */   
/*     */   void setInitMethodName(@Nullable String paramString);
/*     */   
/*     */   @Nullable
/*     */   String getInitMethodName();
/*     */   
/*     */   void setDestroyMethodName(@Nullable String paramString);
/*     */   
/*     */   @Nullable
/*     */   String getDestroyMethodName();
/*     */   
/*     */   void setRole(int paramInt);
/*     */   
/*     */   int getRole();
/*     */   
/*     */   void setDescription(@Nullable String paramString);
/*     */   
/*     */   @Nullable
/*     */   String getDescription();
/*     */   
/*     */   boolean isSingleton();
/*     */   
/*     */   boolean isPrototype();
/*     */   
/*     */   boolean isAbstract();
/*     */   
/*     */   @Nullable
/*     */   String getResourceDescription();
/*     */   
/*     */   @Nullable
/*     */   BeanDefinition getOriginatingBeanDefinition();
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/config/BeanDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */