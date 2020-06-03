/*     */ package org.springframework.validation;
/*     */ 
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
/*     */ public class FieldError
/*     */   extends ObjectError
/*     */ {
/*     */   private final String field;
/*     */   @Nullable
/*     */   private final Object rejectedValue;
/*     */   private final boolean bindingFailure;
/*     */   
/*     */   public FieldError(String objectName, String field, String defaultMessage) {
/*  53 */     this(objectName, field, (Object)null, false, (String[])null, (Object[])null, defaultMessage);
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
/*     */   public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage) {
/*  70 */     super(objectName, codes, arguments, defaultMessage);
/*  71 */     Assert.notNull(field, "Field must not be null");
/*  72 */     this.field = field;
/*  73 */     this.rejectedValue = rejectedValue;
/*  74 */     this.bindingFailure = bindingFailure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getField() {
/*  82 */     return this.field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getRejectedValue() {
/*  90 */     return this.rejectedValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBindingFailure() {
/*  98 */     return this.bindingFailure;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 104 */     if (this == other) {
/* 105 */       return true;
/*     */     }
/* 107 */     if (!super.equals(other)) {
/* 108 */       return false;
/*     */     }
/* 110 */     FieldError otherError = (FieldError)other;
/* 111 */     return (getField().equals(otherError.getField()) && 
/* 112 */       ObjectUtils.nullSafeEquals(getRejectedValue(), otherError.getRejectedValue()) && 
/* 113 */       isBindingFailure() == otherError.isBindingFailure());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 118 */     int hashCode = super.hashCode();
/* 119 */     hashCode = 29 * hashCode + getField().hashCode();
/* 120 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getRejectedValue());
/* 121 */     hashCode = 29 * hashCode + (isBindingFailure() ? 1 : 0);
/* 122 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return "Field error in object '" + getObjectName() + "' on field '" + this.field + "': rejected value [" + 
/* 128 */       ObjectUtils.nullSafeToString(this.rejectedValue) + "]; " + 
/* 129 */       resolvableToString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/FieldError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */