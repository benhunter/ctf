/*     */ package org.springframework.jndi.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.jndi.JndiLocatorSupport;
/*     */ import org.springframework.jndi.TypeMismatchNamingException;
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
/*     */ public class SimpleJndiBeanFactory
/*     */   extends JndiLocatorSupport
/*     */   implements BeanFactory
/*     */ {
/*  66 */   private final Set<String> shareableResources = new HashSet<>();
/*     */ 
/*     */   
/*  69 */   private final Map<String, Object> singletonObjects = new HashMap<>();
/*     */ 
/*     */   
/*  72 */   private final Map<String, Class<?>> resourceTypes = new HashMap<>();
/*     */ 
/*     */   
/*     */   public SimpleJndiBeanFactory() {
/*  76 */     setResourceRef(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addShareableResource(String shareableResource) {
/*  87 */     this.shareableResources.add(shareableResource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShareableResources(String... shareableResources) {
/*  97 */     Collections.addAll(this.shareableResources, shareableResources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getBean(String name) throws BeansException {
/* 108 */     return getBean(name, Object.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
/*     */     try {
/* 114 */       if (isSingleton(name)) {
/* 115 */         return doGetSingleton(name, requiredType);
/*     */       }
/*     */       
/* 118 */       return (T)lookup(name, requiredType);
/*     */     
/*     */     }
/* 121 */     catch (NameNotFoundException ex) {
/* 122 */       throw new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
/*     */     }
/* 124 */     catch (TypeMismatchNamingException ex) {
/* 125 */       throw new BeanNotOfRequiredTypeException(name, ex.getRequiredType(), ex.getActualType());
/*     */     }
/* 127 */     catch (NamingException ex) {
/* 128 */       throw new BeanDefinitionStoreException("JNDI environment", name, "JNDI lookup failed", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getBean(String name, @Nullable Object... args) throws BeansException {
/* 134 */     if (args != null) {
/* 135 */       throw new UnsupportedOperationException("SimpleJndiBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */     
/* 138 */     return getBean(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getBean(Class<T> requiredType) throws BeansException {
/* 143 */     return getBean(requiredType.getSimpleName(), requiredType);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getBean(Class<T> requiredType, @Nullable Object... args) throws BeansException {
/* 148 */     if (args != null) {
/* 149 */       throw new UnsupportedOperationException("SimpleJndiBeanFactory does not support explicit bean creation arguments");
/*     */     }
/*     */     
/* 152 */     return getBean(requiredType);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ObjectProvider<T> getBeanProvider(final Class<T> requiredType) {
/* 157 */     return new ObjectProvider<T>()
/*     */       {
/*     */         public T getObject() throws BeansException {
/* 160 */           return SimpleJndiBeanFactory.this.getBean(requiredType);
/*     */         }
/*     */         
/*     */         public T getObject(Object... args) throws BeansException {
/* 164 */           return SimpleJndiBeanFactory.this.getBean(requiredType, args);
/*     */         }
/*     */         
/*     */         @Nullable
/*     */         public T getIfAvailable() throws BeansException {
/*     */           try {
/* 170 */             return SimpleJndiBeanFactory.this.getBean(requiredType);
/*     */           }
/* 172 */           catch (NoUniqueBeanDefinitionException ex) {
/* 173 */             throw ex;
/*     */           }
/* 175 */           catch (NoSuchBeanDefinitionException ex) {
/* 176 */             return null;
/*     */           } 
/*     */         }
/*     */         
/*     */         @Nullable
/*     */         public T getIfUnique() throws BeansException {
/*     */           try {
/* 183 */             return SimpleJndiBeanFactory.this.getBean(requiredType);
/*     */           }
/* 185 */           catch (NoSuchBeanDefinitionException ex) {
/* 186 */             return null;
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
/* 194 */     throw new UnsupportedOperationException("SimpleJndiBeanFactory does not support resolution by ResolvableType");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsBean(String name) {
/* 200 */     if (this.singletonObjects.containsKey(name) || this.resourceTypes.containsKey(name)) {
/* 201 */       return true;
/*     */     }
/*     */     try {
/* 204 */       doGetType(name);
/* 205 */       return true;
/*     */     }
/* 207 */     catch (NamingException ex) {
/* 208 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
/* 214 */     return this.shareableResources.contains(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
/* 219 */     return !this.shareableResources.contains(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
/* 224 */     Class<?> type = getType(name);
/* 225 */     return (type != null && typeToMatch.isAssignableFrom(type));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTypeMatch(String name, @Nullable Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
/* 230 */     Class<?> type = getType(name);
/* 231 */     return (typeToMatch == null || (type != null && typeToMatch.isAssignableFrom(type)));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
/*     */     try {
/* 238 */       return doGetType(name);
/*     */     }
/* 240 */     catch (NameNotFoundException ex) {
/* 241 */       throw new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
/*     */     }
/* 243 */     catch (NamingException ex) {
/* 244 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAliases(String name) {
/* 250 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> T doGetSingleton(String name, @Nullable Class<T> requiredType) throws NamingException {
/* 256 */     synchronized (this.singletonObjects) {
/* 257 */       Object singleton = this.singletonObjects.get(name);
/* 258 */       if (singleton != null) {
/* 259 */         if (requiredType != null && !requiredType.isInstance(singleton)) {
/* 260 */           throw new TypeMismatchNamingException(convertJndiName(name), requiredType, singleton.getClass());
/*     */         }
/* 262 */         return (T)singleton;
/*     */       } 
/* 264 */       T jndiObject = (T)lookup(name, requiredType);
/* 265 */       this.singletonObjects.put(name, jndiObject);
/* 266 */       return jndiObject;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Class<?> doGetType(String name) throws NamingException {
/* 271 */     if (isSingleton(name)) {
/* 272 */       return doGetSingleton(name, (Class<T>)null).getClass();
/*     */     }
/*     */     
/* 275 */     synchronized (this.resourceTypes) {
/* 276 */       Class<?> type = this.resourceTypes.get(name);
/* 277 */       if (type == null) {
/* 278 */         type = lookup(name, null).getClass();
/* 279 */         this.resourceTypes.put(name, type);
/*     */       } 
/* 281 */       return type;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/support/SimpleJndiBeanFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */