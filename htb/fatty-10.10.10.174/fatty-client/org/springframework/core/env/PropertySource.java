/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertySource<T>
/*     */ {
/*  62 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   protected final String name;
/*     */ 
/*     */   
/*     */   protected final T source;
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySource(String name, T source) {
/*  73 */     Assert.hasText(name, "Property source name must contain at least one character");
/*  74 */     Assert.notNull(source, "Property source must not be null");
/*  75 */     this.name = name;
/*  76 */     this.source = source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertySource(String name) {
/*  87 */     this(name, (T)new Object());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  95 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getSource() {
/* 102 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProperty(String name) {
/* 113 */     return (getProperty(name) != null);
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
/*     */   @Nullable
/*     */   public abstract Object getProperty(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 136 */     return (this == other || (other instanceof PropertySource && 
/* 137 */       ObjectUtils.nullSafeEquals(this.name, ((PropertySource)other).name)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 146 */     return ObjectUtils.nullSafeHashCode(this.name);
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
/*     */   public String toString() {
/* 160 */     if (this.logger.isDebugEnabled()) {
/* 161 */       return getClass().getSimpleName() + "@" + System.identityHashCode(this) + " {name='" + this.name + "', properties=" + this.source + "}";
/*     */     }
/*     */ 
/*     */     
/* 165 */     return getClass().getSimpleName() + " {name='" + this.name + "'}";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertySource<?> named(String name) {
/* 188 */     return new ComparisonPropertySource(name);
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
/*     */   
/*     */   public static class StubPropertySource
/*     */     extends PropertySource<Object>
/*     */   {
/*     */     public StubPropertySource(String name) {
/* 207 */       super(name, new Object());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getProperty(String name) {
/* 216 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ComparisonPropertySource
/*     */     extends StubPropertySource
/*     */   {
/*     */     private static final String USAGE_ERROR = "ComparisonPropertySource instances are for use with collection comparison only";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ComparisonPropertySource(String name) {
/* 233 */       super(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 238 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsProperty(String name) {
/* 243 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getProperty(String name) {
/* 249 */       throw new UnsupportedOperationException("ComparisonPropertySource instances are for use with collection comparison only");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/env/PropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */