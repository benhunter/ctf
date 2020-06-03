/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionException;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.support.DefaultConversionService;
/*    */ import org.springframework.expression.TypeConverter;
/*    */ import org.springframework.expression.spel.SpelEvaluationException;
/*    */ import org.springframework.expression.spel.SpelMessage;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StandardTypeConverter
/*    */   implements TypeConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public StandardTypeConverter() {
/* 48 */     this.conversionService = DefaultConversionService.getSharedInstance();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StandardTypeConverter(ConversionService conversionService) {
/* 56 */     Assert.notNull(conversionService, "ConversionService must not be null");
/* 57 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 63 */     return this.conversionService.canConvert(sourceType, targetType);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convertValue(@Nullable Object value, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/*    */     try {
/* 70 */       return this.conversionService.convert(value, sourceType, targetType);
/*    */     }
/* 72 */     catch (ConversionException ex) {
/* 73 */       throw new SpelEvaluationException(ex, SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { (sourceType != null) ? sourceType
/* 74 */             .toString() : ((value != null) ? value.getClass().getName() : "null"), targetType
/* 75 */             .toString() });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/expression/spel/support/StandardTypeConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */