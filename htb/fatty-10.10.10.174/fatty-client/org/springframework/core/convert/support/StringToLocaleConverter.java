/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ final class StringToLocaleConverter
/*    */   implements Converter<String, Locale>
/*    */ {
/*    */   @Nullable
/*    */   public Locale convert(String source) {
/* 41 */     return StringUtils.parseLocale(source);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/support/StringToLocaleConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */