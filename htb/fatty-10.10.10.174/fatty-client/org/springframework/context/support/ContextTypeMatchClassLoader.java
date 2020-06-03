/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.core.DecoratingClassLoader;
/*     */ import org.springframework.core.OverridingClassLoader;
/*     */ import org.springframework.core.SmartClassLoader;
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
/*     */ class ContextTypeMatchClassLoader
/*     */   extends DecoratingClassLoader
/*     */   implements SmartClassLoader
/*     */ {
/*     */   private static Method findLoadedClassMethod;
/*     */   
/*     */   static {
/*  43 */     ClassLoader.registerAsParallelCapable();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  51 */       findLoadedClassMethod = ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[] { String.class });
/*     */     }
/*  53 */     catch (NoSuchMethodException ex) {
/*  54 */       throw new IllegalStateException("Invalid [java.lang.ClassLoader] class: no 'findLoadedClass' method defined!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  60 */   private final Map<String, byte[]> bytesCache = (Map)new ConcurrentHashMap<>(256);
/*     */ 
/*     */   
/*     */   public ContextTypeMatchClassLoader(@Nullable ClassLoader parent) {
/*  64 */     super(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> loadClass(String name) throws ClassNotFoundException {
/*  69 */     return (new ContextOverridingClassLoader(getParent())).loadClass(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isClassReloadable(Class<?> clazz) {
/*  74 */     return clazz.getClassLoader() instanceof ContextOverridingClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ContextOverridingClassLoader
/*     */     extends OverridingClassLoader
/*     */   {
/*     */     public ContextOverridingClassLoader(ClassLoader parent) {
/*  85 */       super(parent);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isEligibleForOverriding(String className) {
/*  90 */       if (isExcluded(className) || ContextTypeMatchClassLoader.this.isExcluded(className)) {
/*  91 */         return false;
/*     */       }
/*  93 */       ReflectionUtils.makeAccessible(ContextTypeMatchClassLoader.findLoadedClassMethod);
/*  94 */       ClassLoader parent = getParent();
/*  95 */       while (parent != null) {
/*  96 */         if (ReflectionUtils.invokeMethod(ContextTypeMatchClassLoader.findLoadedClassMethod, parent, new Object[] { className }) != null) {
/*  97 */           return false;
/*     */         }
/*  99 */         parent = parent.getParent();
/*     */       } 
/* 101 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Class<?> loadClassForOverriding(String name) throws ClassNotFoundException {
/* 106 */       byte[] bytes = (byte[])ContextTypeMatchClassLoader.this.bytesCache.get(name);
/* 107 */       if (bytes == null) {
/* 108 */         bytes = loadBytesForClass(name);
/* 109 */         if (bytes != null) {
/* 110 */           ContextTypeMatchClassLoader.this.bytesCache.put(name, bytes);
/*     */         } else {
/*     */           
/* 113 */           return null;
/*     */         } 
/*     */       } 
/* 116 */       return defineClass(name, bytes, 0, bytes.length);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/ContextTypeMatchClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */