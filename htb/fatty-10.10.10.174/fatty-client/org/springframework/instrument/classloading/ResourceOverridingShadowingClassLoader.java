/*     */ package org.springframework.instrument.classloading;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceOverridingShadowingClassLoader
/*     */   extends ShadowingClassLoader
/*     */ {
/*  39 */   private static final Enumeration<URL> EMPTY_URL_ENUMERATION = new Enumeration<URL>()
/*     */     {
/*     */       public boolean hasMoreElements() {
/*  42 */         return false;
/*     */       }
/*     */       
/*     */       public URL nextElement() {
/*  46 */         throw new UnsupportedOperationException("Should not be called. I am empty.");
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   private Map<String, String> overrides = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceOverridingShadowingClassLoader(ClassLoader enclosingClassLoader) {
/*  63 */     super(enclosingClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void override(String oldPath, String newPath) {
/*  74 */     this.overrides.put(oldPath, newPath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void suppress(String oldPath) {
/*  83 */     this.overrides.put(oldPath, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyOverrides(ResourceOverridingShadowingClassLoader other) {
/*  91 */     Assert.notNull(other, "Other ClassLoader must not be null");
/*  92 */     this.overrides.putAll(other.overrides);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getResource(String requestedPath) {
/*  98 */     if (this.overrides.containsKey(requestedPath)) {
/*  99 */       String overriddenPath = this.overrides.get(requestedPath);
/* 100 */       return (overriddenPath != null) ? super.getResource(overriddenPath) : null;
/*     */     } 
/*     */     
/* 103 */     return super.getResource(requestedPath);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public InputStream getResourceAsStream(String requestedPath) {
/* 110 */     if (this.overrides.containsKey(requestedPath)) {
/* 111 */       String overriddenPath = this.overrides.get(requestedPath);
/* 112 */       return (overriddenPath != null) ? super.getResourceAsStream(overriddenPath) : null;
/*     */     } 
/*     */     
/* 115 */     return super.getResourceAsStream(requestedPath);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<URL> getResources(String requestedPath) throws IOException {
/* 121 */     if (this.overrides.containsKey(requestedPath)) {
/* 122 */       String overriddenLocation = this.overrides.get(requestedPath);
/* 123 */       return (overriddenLocation != null) ? super
/* 124 */         .getResources(overriddenLocation) : EMPTY_URL_ENUMERATION;
/*     */     } 
/*     */     
/* 127 */     return super.getResources(requestedPath);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/instrument/classloading/ResourceOverridingShadowingClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */