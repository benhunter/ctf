/*     */ package org.springframework.validation;
/*     */ 
/*     */ import org.springframework.context.support.DefaultMessageSourceResolvable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectError
/*     */   extends DefaultMessageSourceResolvable
/*     */ {
/*     */   private final String objectName;
/*     */   @Nullable
/*     */   private transient Object source;
/*     */   
/*     */   public ObjectError(String objectName, String defaultMessage) {
/*  50 */     this(objectName, null, null, defaultMessage);
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
/*     */   public ObjectError(String objectName, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage) {
/*  63 */     super(codes, arguments, defaultMessage);
/*  64 */     Assert.notNull(objectName, "Object name must not be null");
/*  65 */     this.objectName = objectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjectName() {
/*  73 */     return this.objectName;
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
/*     */   public void wrap(Object source) {
/*  86 */     if (this.source != null) {
/*  87 */       throw new IllegalStateException("Already wrapping " + this.source);
/*     */     }
/*  89 */     this.source = source;
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
/*     */   public <T> T unwrap(Class<T> sourceType) {
/* 105 */     if (sourceType.isInstance(this.source)) {
/* 106 */       return sourceType.cast(this.source);
/*     */     }
/* 108 */     if (this.source instanceof Throwable) {
/* 109 */       Throwable cause = ((Throwable)this.source).getCause();
/* 110 */       if (sourceType.isInstance(cause)) {
/* 111 */         return sourceType.cast(cause);
/*     */       }
/*     */     } 
/* 114 */     throw new IllegalArgumentException("No source object of the given type available: " + sourceType);
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
/*     */   public boolean contains(Class<?> sourceType) {
/* 128 */     return (sourceType.isInstance(this.source) || (this.source instanceof Throwable && sourceType
/* 129 */       .isInstance(((Throwable)this.source).getCause())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 135 */     if (this == other) {
/* 136 */       return true;
/*     */     }
/* 138 */     if (other == null || other.getClass() != getClass() || !super.equals(other)) {
/* 139 */       return false;
/*     */     }
/* 141 */     ObjectError otherError = (ObjectError)other;
/* 142 */     return getObjectName().equals(otherError.getObjectName());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 147 */     return 29 * super.hashCode() + getObjectName().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 152 */     return "Error in object '" + this.objectName + "': " + resolvableToString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/ObjectError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */