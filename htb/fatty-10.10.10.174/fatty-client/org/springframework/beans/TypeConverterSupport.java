/*    */ package org.springframework.beans;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.core.convert.ConversionException;
/*    */ import org.springframework.core.convert.ConverterNotFoundException;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TypeConverterSupport
/*    */   extends PropertyEditorRegistrySupport
/*    */   implements TypeConverter
/*    */ {
/*    */   @Nullable
/*    */   TypeConverterDelegate typeConverterDelegate;
/*    */   
/*    */   @Nullable
/*    */   public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType) throws TypeMismatchException {
/* 45 */     return convertIfNecessary(value, requiredType, TypeDescriptor.valueOf(requiredType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable MethodParameter methodParam) throws TypeMismatchException {
/* 53 */     return convertIfNecessary(value, requiredType, (methodParam != null) ? new TypeDescriptor(methodParam) : 
/* 54 */         TypeDescriptor.valueOf(requiredType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable Field field) throws TypeMismatchException {
/* 62 */     return convertIfNecessary(value, requiredType, (field != null) ? new TypeDescriptor(field) : 
/* 63 */         TypeDescriptor.valueOf(requiredType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable TypeDescriptor typeDescriptor) throws TypeMismatchException {
/* 71 */     Assert.state((this.typeConverterDelegate != null), "No TypeConverterDelegate");
/*    */     try {
/* 73 */       return this.typeConverterDelegate.convertIfNecessary(null, null, value, requiredType, typeDescriptor);
/*    */     }
/* 75 */     catch (ConverterNotFoundException|IllegalStateException ex) {
/* 76 */       throw new ConversionNotSupportedException(value, requiredType, ex);
/*    */     }
/* 78 */     catch (ConversionException|IllegalArgumentException ex) {
/* 79 */       throw new TypeMismatchException(value, requiredType, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/TypeConverterSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */