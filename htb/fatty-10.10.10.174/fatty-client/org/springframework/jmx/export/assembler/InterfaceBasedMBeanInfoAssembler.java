/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ public class InterfaceBasedMBeanInfoAssembler
/*     */   extends AbstractConfigurableMBeanInfoAssembler
/*     */   implements BeanClassLoaderAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Class<?>[] managedInterfaces;
/*     */   @Nullable
/*     */   private Properties interfaceMappings;
/*     */   @Nullable
/*  72 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Map<String, Class<?>[]> resolvedInterfaceMappings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagedInterfaces(@Nullable Class<?>... managedInterfaces) {
/*  88 */     if (managedInterfaces != null) {
/*  89 */       for (Class<?> ifc : managedInterfaces) {
/*  90 */         if (!ifc.isInterface()) {
/*  91 */           throw new IllegalArgumentException("Management interface [" + ifc
/*  92 */               .getName() + "] is not an interface");
/*     */         }
/*     */       } 
/*     */     }
/*  96 */     this.managedInterfaces = managedInterfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterfaceMappings(@Nullable Properties mappings) {
/* 107 */     this.interfaceMappings = mappings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
/* 112 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 118 */     if (this.interfaceMappings != null) {
/* 119 */       this.resolvedInterfaceMappings = resolveInterfaceMappings(this.interfaceMappings);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Class<?>[]> resolveInterfaceMappings(Properties mappings) {
/* 129 */     Map<String, Class<?>[]> resolvedMappings = (Map)new HashMap<>(mappings.size());
/* 130 */     for (Enumeration<?> en = mappings.propertyNames(); en.hasMoreElements(); ) {
/* 131 */       String beanKey = (String)en.nextElement();
/* 132 */       String[] classNames = StringUtils.commaDelimitedListToStringArray(mappings.getProperty(beanKey));
/* 133 */       Class<?>[] classes = resolveClassNames(classNames, beanKey);
/* 134 */       resolvedMappings.put(beanKey, classes);
/*     */     } 
/* 136 */     return resolvedMappings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class<?>[] resolveClassNames(String[] classNames, String beanKey) {
/* 146 */     Class<?>[] classes = new Class[classNames.length];
/* 147 */     for (int x = 0; x < classes.length; x++) {
/* 148 */       Class<?> cls = ClassUtils.resolveClassName(classNames[x].trim(), this.beanClassLoader);
/* 149 */       if (!cls.isInterface()) {
/* 150 */         throw new IllegalArgumentException("Class [" + classNames[x] + "] mapped to bean key [" + beanKey + "] is no interface");
/*     */       }
/*     */       
/* 153 */       classes[x] = cls;
/*     */     } 
/* 155 */     return classes;
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
/*     */   protected boolean includeReadAttribute(Method method, String beanKey) {
/* 170 */     return isPublicInInterface(method, beanKey);
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
/*     */   protected boolean includeWriteAttribute(Method method, String beanKey) {
/* 184 */     return isPublicInInterface(method, beanKey);
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
/*     */   protected boolean includeOperation(Method method, String beanKey) {
/* 198 */     return isPublicInInterface(method, beanKey);
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
/*     */   private boolean isPublicInInterface(Method method, String beanKey) {
/* 210 */     return ((method.getModifiers() & 0x1) > 0 && isDeclaredInInterface(method, beanKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDeclaredInInterface(Method method, String beanKey) {
/* 218 */     Class<?>[] ifaces = null;
/*     */     
/* 220 */     if (this.resolvedInterfaceMappings != null) {
/* 221 */       ifaces = this.resolvedInterfaceMappings.get(beanKey);
/*     */     }
/*     */     
/* 224 */     if (ifaces == null) {
/* 225 */       ifaces = this.managedInterfaces;
/* 226 */       if (ifaces == null) {
/* 227 */         ifaces = ClassUtils.getAllInterfacesForClass(method.getDeclaringClass());
/*     */       }
/*     */     } 
/*     */     
/* 231 */     for (Class<?> ifc : ifaces) {
/* 232 */       for (Method ifcMethod : ifc.getMethods()) {
/* 233 */         if (ifcMethod.getName().equals(method.getName()) && 
/* 234 */           Arrays.equals((Object[])ifcMethod.getParameterTypes(), (Object[])method.getParameterTypes())) {
/* 235 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 240 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/assembler/InterfaceBasedMBeanInfoAssembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */