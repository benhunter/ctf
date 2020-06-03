/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.time.ZoneId;
/*    */ import java.util.TimeZone;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ZoneIdToTimeZoneConverter
/*    */   implements Converter<ZoneId, TimeZone>
/*    */ {
/*    */   public TimeZone convert(ZoneId source) {
/* 41 */     return TimeZone.getTimeZone(source);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/support/ZoneIdToTimeZoneConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */