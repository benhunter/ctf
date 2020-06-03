/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeMismatchException
/*     */   extends PropertyAccessException
/*     */ {
/*     */   public static final String ERROR_CODE = "typeMismatch";
/*     */   @Nullable
/*     */   private String propertyName;
/*     */   @Nullable
/*     */   private transient Object value;
/*     */   @Nullable
/*     */   private Class<?> requiredType;
/*     */   
/*     */   public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, Class<?> requiredType) {
/*  56 */     this(propertyChangeEvent, requiredType, (Throwable)null);
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
/*     */   public TypeMismatchException(PropertyChangeEvent propertyChangeEvent, @Nullable Class<?> requiredType, @Nullable Throwable cause) {
/*  68 */     super(propertyChangeEvent, "Failed to convert property value of type '" + 
/*     */         
/*  70 */         ClassUtils.getDescriptiveType(propertyChangeEvent.getNewValue()) + "'" + ((requiredType != null) ? (" to required type '" + 
/*     */         
/*  72 */         ClassUtils.getQualifiedName(requiredType) + "'") : "") + (
/*  73 */         (propertyChangeEvent.getPropertyName() != null) ? (" for property '" + propertyChangeEvent
/*  74 */         .getPropertyName() + "'") : ""), cause);
/*     */     
/*  76 */     this.propertyName = propertyChangeEvent.getPropertyName();
/*  77 */     this.value = propertyChangeEvent.getNewValue();
/*  78 */     this.requiredType = requiredType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeMismatchException(@Nullable Object value, @Nullable Class<?> requiredType) {
/*  88 */     this(value, requiredType, (Throwable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeMismatchException(@Nullable Object value, @Nullable Class<?> requiredType, @Nullable Throwable cause) {
/*  99 */     super("Failed to convert value of type '" + ClassUtils.getDescriptiveType(value) + "'" + ((requiredType != null) ? (" to required type '" + 
/* 100 */         ClassUtils.getQualifiedName(requiredType) + "'") : ""), cause);
/*     */     
/* 102 */     this.value = value;
/* 103 */     this.requiredType = requiredType;
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
/*     */   public void initPropertyName(String propertyName) {
/* 116 */     Assert.state((this.propertyName == null), "Property name already initialized");
/* 117 */     this.propertyName = propertyName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPropertyName() {
/* 126 */     return this.propertyName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue() {
/* 135 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getRequiredType() {
/* 143 */     return this.requiredType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getErrorCode() {
/* 148 */     return "typeMismatch";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/TypeMismatchException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */