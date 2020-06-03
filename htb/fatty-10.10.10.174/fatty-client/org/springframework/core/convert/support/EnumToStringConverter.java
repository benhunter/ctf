/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ final class EnumToStringConverter
/*    */   extends AbstractConditionalEnumConverter
/*    */   implements Converter<Enum<?>, String>
/*    */ {
/*    */   public EnumToStringConverter(ConversionService conversionService) {
/* 33 */     super(conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public String convert(Enum<?> source) {
/* 38 */     return source.name();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/support/EnumToStringConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */