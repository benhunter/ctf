/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*    */ import org.springframework.core.convert.converter.GenericConverter;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ final class CollectionToStringConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private static final String DELIMITER = ",";
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public CollectionToStringConverter(ConversionService conversionService) {
/* 42 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 48 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, String.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 53 */     return ConversionUtils.canConvertElements(sourceType
/* 54 */         .getElementTypeDescriptor(), targetType, this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 60 */     if (source == null) {
/* 61 */       return null;
/*    */     }
/* 63 */     Collection<?> sourceCollection = (Collection)source;
/* 64 */     if (sourceCollection.isEmpty()) {
/* 65 */       return "";
/*    */     }
/* 67 */     StringBuilder sb = new StringBuilder();
/* 68 */     int i = 0;
/* 69 */     for (Object sourceElement : sourceCollection) {
/* 70 */       if (i > 0) {
/* 71 */         sb.append(",");
/*    */       }
/* 73 */       Object targetElement = this.conversionService.convert(sourceElement, sourceType
/* 74 */           .elementTypeDescriptor(sourceElement), targetType);
/* 75 */       sb.append(targetElement);
/* 76 */       i++;
/*    */     } 
/* 78 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/support/CollectionToStringConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */