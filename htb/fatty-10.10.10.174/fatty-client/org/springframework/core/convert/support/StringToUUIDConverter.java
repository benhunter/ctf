/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ final class StringToUUIDConverter
/*    */   implements Converter<String, UUID>
/*    */ {
/*    */   public UUID convert(String source) {
/* 35 */     return StringUtils.hasLength(source) ? UUID.fromString(source.trim()) : null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/support/StringToUUIDConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */