/*     */ package org.springframework.beans.factory.annotation;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValues;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InjectionMetadata
/*     */ {
/*  50 */   private static final Log logger = LogFactory.getLog(InjectionMetadata.class);
/*     */   
/*     */   private final Class<?> targetClass;
/*     */   
/*     */   private final Collection<InjectedElement> injectedElements;
/*     */   
/*     */   @Nullable
/*     */   private volatile Set<InjectedElement> checkedElements;
/*     */ 
/*     */   
/*     */   public InjectionMetadata(Class<?> targetClass, Collection<InjectedElement> elements) {
/*  61 */     this.targetClass = targetClass;
/*  62 */     this.injectedElements = elements;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkConfigMembers(RootBeanDefinition beanDefinition) {
/*  67 */     Set<InjectedElement> checkedElements = new LinkedHashSet<>(this.injectedElements.size());
/*  68 */     for (InjectedElement element : this.injectedElements) {
/*  69 */       Member member = element.getMember();
/*  70 */       if (!beanDefinition.isExternallyManagedConfigMember(member)) {
/*  71 */         beanDefinition.registerExternallyManagedConfigMember(member);
/*  72 */         checkedElements.add(element);
/*  73 */         if (logger.isTraceEnabled()) {
/*  74 */           logger.trace("Registered injected element on class [" + this.targetClass.getName() + "]: " + element);
/*     */         }
/*     */       } 
/*     */     } 
/*  78 */     this.checkedElements = checkedElements;
/*     */   }
/*     */   
/*     */   public void inject(Object target, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
/*  82 */     Collection<InjectedElement> checkedElements = this.checkedElements;
/*  83 */     Collection<InjectedElement> elementsToIterate = (checkedElements != null) ? checkedElements : this.injectedElements;
/*     */     
/*  85 */     if (!elementsToIterate.isEmpty()) {
/*  86 */       for (InjectedElement element : elementsToIterate) {
/*  87 */         if (logger.isTraceEnabled()) {
/*  88 */           logger.trace("Processing injected element of bean '" + beanName + "': " + element);
/*     */         }
/*  90 */         element.inject(target, beanName, pvs);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear(@Nullable PropertyValues pvs) {
/* 100 */     Collection<InjectedElement> checkedElements = this.checkedElements;
/* 101 */     Collection<InjectedElement> elementsToIterate = (checkedElements != null) ? checkedElements : this.injectedElements;
/*     */     
/* 103 */     if (!elementsToIterate.isEmpty()) {
/* 104 */       for (InjectedElement element : elementsToIterate) {
/* 105 */         element.clearPropertySkipping(pvs);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean needsRefresh(@Nullable InjectionMetadata metadata, Class<?> clazz) {
/* 112 */     return (metadata == null || metadata.targetClass != clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class InjectedElement
/*     */   {
/*     */     protected final Member member;
/*     */ 
/*     */     
/*     */     protected final boolean isField;
/*     */     
/*     */     @Nullable
/*     */     protected final PropertyDescriptor pd;
/*     */     
/*     */     @Nullable
/*     */     protected volatile Boolean skip;
/*     */ 
/*     */     
/*     */     protected InjectedElement(Member member, @Nullable PropertyDescriptor pd) {
/* 132 */       this.member = member;
/* 133 */       this.isField = member instanceof Field;
/* 134 */       this.pd = pd;
/*     */     }
/*     */     
/*     */     public final Member getMember() {
/* 138 */       return this.member;
/*     */     }
/*     */     
/*     */     protected final Class<?> getResourceType() {
/* 142 */       if (this.isField) {
/* 143 */         return ((Field)this.member).getType();
/*     */       }
/* 145 */       if (this.pd != null) {
/* 146 */         return this.pd.getPropertyType();
/*     */       }
/*     */       
/* 149 */       return ((Method)this.member).getParameterTypes()[0];
/*     */     }
/*     */ 
/*     */     
/*     */     protected final void checkResourceType(Class<?> resourceType) {
/* 154 */       if (this.isField) {
/* 155 */         Class<?> fieldType = ((Field)this.member).getType();
/* 156 */         if (!resourceType.isAssignableFrom(fieldType) && !fieldType.isAssignableFrom(resourceType)) {
/* 157 */           throw new IllegalStateException("Specified field type [" + fieldType + "] is incompatible with resource type [" + resourceType
/* 158 */               .getName() + "]");
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 163 */         Class<?> paramType = (this.pd != null) ? this.pd.getPropertyType() : ((Method)this.member).getParameterTypes()[0];
/* 164 */         if (!resourceType.isAssignableFrom(paramType) && !paramType.isAssignableFrom(resourceType)) {
/* 165 */           throw new IllegalStateException("Specified parameter type [" + paramType + "] is incompatible with resource type [" + resourceType
/* 166 */               .getName() + "]");
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void inject(Object target, @Nullable String requestingBeanName, @Nullable PropertyValues pvs) throws Throwable {
/* 177 */       if (this.isField) {
/* 178 */         Field field = (Field)this.member;
/* 179 */         ReflectionUtils.makeAccessible(field);
/* 180 */         field.set(target, getResourceToInject(target, requestingBeanName));
/*     */       } else {
/*     */         
/* 183 */         if (checkPropertySkipping(pvs)) {
/*     */           return;
/*     */         }
/*     */         try {
/* 187 */           Method method = (Method)this.member;
/* 188 */           ReflectionUtils.makeAccessible(method);
/* 189 */           method.invoke(target, new Object[] { getResourceToInject(target, requestingBeanName) });
/*     */         }
/* 191 */         catch (InvocationTargetException ex) {
/* 192 */           throw ex.getTargetException();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean checkPropertySkipping(@Nullable PropertyValues pvs) {
/* 203 */       Boolean skip = this.skip;
/* 204 */       if (skip != null) {
/* 205 */         return skip.booleanValue();
/*     */       }
/* 207 */       if (pvs == null) {
/* 208 */         this.skip = Boolean.valueOf(false);
/* 209 */         return false;
/*     */       } 
/* 211 */       synchronized (pvs) {
/* 212 */         skip = this.skip;
/* 213 */         if (skip != null) {
/* 214 */           return skip.booleanValue();
/*     */         }
/* 216 */         if (this.pd != null) {
/* 217 */           if (pvs.contains(this.pd.getName())) {
/*     */             
/* 219 */             this.skip = Boolean.valueOf(true);
/* 220 */             return true;
/*     */           } 
/* 222 */           if (pvs instanceof MutablePropertyValues) {
/* 223 */             ((MutablePropertyValues)pvs).registerProcessedProperty(this.pd.getName());
/*     */           }
/*     */         } 
/* 226 */         this.skip = Boolean.valueOf(false);
/* 227 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void clearPropertySkipping(@Nullable PropertyValues pvs) {
/* 236 */       if (pvs == null) {
/*     */         return;
/*     */       }
/* 239 */       synchronized (pvs) {
/* 240 */         if (Boolean.FALSE.equals(this.skip) && this.pd != null && pvs instanceof MutablePropertyValues) {
/* 241 */           ((MutablePropertyValues)pvs).clearProcessedProperty(this.pd.getName());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     protected Object getResourceToInject(Object target, @Nullable String requestingBeanName) {
/* 251 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 256 */       if (this == other) {
/* 257 */         return true;
/*     */       }
/* 259 */       if (!(other instanceof InjectedElement)) {
/* 260 */         return false;
/*     */       }
/* 262 */       InjectedElement otherElement = (InjectedElement)other;
/* 263 */       return this.member.equals(otherElement.member);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 268 */       return this.member.getClass().hashCode() * 29 + this.member.getName().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 273 */       return getClass().getSimpleName() + " for " + this.member;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/InjectionMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */