/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.GroovySystem;
/*     */ import groovy.lang.MetaClass;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ClassPathResource;
/*     */ import org.springframework.core.io.Resource;
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
/*     */ public class GenericGroovyApplicationContext
/*     */   extends GenericApplicationContext
/*     */   implements GroovyObject
/*     */ {
/* 121 */   private final GroovyBeanDefinitionReader reader = new GroovyBeanDefinitionReader(this);
/*     */   
/* 123 */   private final BeanWrapper contextWrapper = (BeanWrapper)new BeanWrapperImpl(this);
/*     */   
/* 125 */   private MetaClass metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericGroovyApplicationContext() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericGroovyApplicationContext(Resource... resources) {
/* 141 */     load(resources);
/* 142 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericGroovyApplicationContext(String... resourceLocations) {
/* 151 */     load(resourceLocations);
/* 152 */     refresh();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericGroovyApplicationContext(Class<?> relativeClass, String... resourceNames) {
/* 163 */     load(relativeClass, resourceNames);
/* 164 */     refresh();
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
/*     */   public final GroovyBeanDefinitionReader getReader() {
/* 176 */     return this.reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(ConfigurableEnvironment environment) {
/* 185 */     super.setEnvironment(environment);
/* 186 */     this.reader.setEnvironment((Environment)getEnvironment());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(Resource... resources) {
/* 196 */     this.reader.loadBeanDefinitions(resources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(String... resourceLocations) {
/* 206 */     this.reader.loadBeanDefinitions(resourceLocations);
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
/*     */   public void load(Class<?> relativeClass, String... resourceNames) {
/* 218 */     Resource[] resources = new Resource[resourceNames.length];
/* 219 */     for (int i = 0; i < resourceNames.length; i++) {
/* 220 */       resources[i] = (Resource)new ClassPathResource(resourceNames[i], relativeClass);
/*     */     }
/* 222 */     load(resources);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetaClass(MetaClass metaClass) {
/* 229 */     this.metaClass = metaClass;
/*     */   }
/*     */   
/*     */   public MetaClass getMetaClass() {
/* 233 */     return this.metaClass;
/*     */   }
/*     */   
/*     */   public Object invokeMethod(String name, Object args) {
/* 237 */     return this.metaClass.invokeMethod(this, name, args);
/*     */   }
/*     */   
/*     */   public void setProperty(String property, Object newValue) {
/* 241 */     if (newValue instanceof BeanDefinition) {
/* 242 */       registerBeanDefinition(property, (BeanDefinition)newValue);
/*     */     } else {
/*     */       
/* 245 */       this.metaClass.setProperty(this, property, newValue);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Object getProperty(String property) {
/* 251 */     if (containsBean(property)) {
/* 252 */       return getBean(property);
/*     */     }
/* 254 */     if (this.contextWrapper.isReadableProperty(property)) {
/* 255 */       return this.contextWrapper.getPropertyValue(property);
/*     */     }
/* 257 */     throw new NoSuchBeanDefinitionException(property);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/GenericGroovyApplicationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */