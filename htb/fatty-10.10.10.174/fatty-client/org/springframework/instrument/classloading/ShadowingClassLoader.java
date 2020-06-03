/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.instrument.IllegalClassFormatException;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.DecoratingClassLoader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.FileCopyUtils;
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
/*     */ public class ShadowingClassLoader
/*     */   extends DecoratingClassLoader
/*     */ {
/*  50 */   public static final String[] DEFAULT_EXCLUDED_PACKAGES = new String[] { "java.", "javax.", "sun.", "oracle.", "com.sun.", "com.ibm.", "COM.ibm.", "org.w3c.", "org.xml.", "org.dom4j.", "org.eclipse", "org.aspectj.", "net.sf.cglib", "org.springframework.cglib", "org.apache.xerces.", "org.apache.commons.logging." };
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClassLoader enclosingClassLoader;
/*     */ 
/*     */ 
/*     */   
/*  58 */   private final List<ClassFileTransformer> classFileTransformers = new LinkedList<>();
/*     */   
/*  60 */   private final Map<String, Class<?>> classCache = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShadowingClassLoader(ClassLoader enclosingClassLoader) {
/*  70 */     this(enclosingClassLoader, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShadowingClassLoader(ClassLoader enclosingClassLoader, boolean defaultExcludes) {
/*  80 */     Assert.notNull(enclosingClassLoader, "Enclosing ClassLoader must not be null");
/*  81 */     this.enclosingClassLoader = enclosingClassLoader;
/*  82 */     if (defaultExcludes) {
/*  83 */       for (String excludedPackage : DEFAULT_EXCLUDED_PACKAGES) {
/*  84 */         excludePackage(excludedPackage);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer) {
/*  96 */     Assert.notNull(transformer, "Transformer must not be null");
/*  97 */     this.classFileTransformers.add(transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyTransformers(ShadowingClassLoader other) {
/* 106 */     Assert.notNull(other, "Other ClassLoader must not be null");
/* 107 */     this.classFileTransformers.addAll(other.classFileTransformers);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> loadClass(String name) throws ClassNotFoundException {
/* 113 */     if (shouldShadow(name)) {
/* 114 */       Class<?> cls = this.classCache.get(name);
/* 115 */       if (cls != null) {
/* 116 */         return cls;
/*     */       }
/* 118 */       return doLoadClass(name);
/*     */     } 
/*     */     
/* 121 */     return this.enclosingClassLoader.loadClass(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldShadow(String className) {
/* 131 */     return (!className.equals(getClass().getName()) && !className.endsWith("ShadowingClassLoader") && 
/* 132 */       isEligibleForShadowing(className));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleForShadowing(String className) {
/* 143 */     return !isExcluded(className);
/*     */   }
/*     */ 
/*     */   
/*     */   private Class<?> doLoadClass(String name) throws ClassNotFoundException {
/* 148 */     String internalName = StringUtils.replace(name, ".", "/") + ".class";
/* 149 */     InputStream is = this.enclosingClassLoader.getResourceAsStream(internalName);
/* 150 */     if (is == null) {
/* 151 */       throw new ClassNotFoundException(name);
/*     */     }
/*     */     try {
/* 154 */       byte[] bytes = FileCopyUtils.copyToByteArray(is);
/* 155 */       bytes = applyTransformers(name, bytes);
/* 156 */       Class<?> cls = defineClass(name, bytes, 0, bytes.length);
/*     */       
/* 158 */       if (cls.getPackage() == null) {
/* 159 */         int packageSeparator = name.lastIndexOf('.');
/* 160 */         if (packageSeparator != -1) {
/* 161 */           String packageName = name.substring(0, packageSeparator);
/* 162 */           definePackage(packageName, null, null, null, null, null, null, null);
/*     */         } 
/*     */       } 
/* 165 */       this.classCache.put(name, cls);
/* 166 */       return cls;
/*     */     }
/* 168 */     catch (IOException ex) {
/* 169 */       throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private byte[] applyTransformers(String name, byte[] bytes) {
/* 174 */     String internalName = StringUtils.replace(name, ".", "/");
/*     */     try {
/* 176 */       for (ClassFileTransformer transformer : this.classFileTransformers) {
/* 177 */         byte[] transformed = transformer.transform((ClassLoader)this, internalName, null, null, bytes);
/* 178 */         bytes = (transformed != null) ? transformed : bytes;
/*     */       } 
/* 180 */       return bytes;
/*     */     }
/* 182 */     catch (IllegalClassFormatException ex) {
/* 183 */       throw new IllegalStateException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getResource(String name) {
/* 190 */     return this.enclosingClassLoader.getResource(name);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public InputStream getResourceAsStream(String name) {
/* 196 */     return this.enclosingClassLoader.getResourceAsStream(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<URL> getResources(String name) throws IOException {
/* 201 */     return this.enclosingClassLoader.getResources(name);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/ShadowingClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */