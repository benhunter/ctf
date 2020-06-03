/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ final class StringToBooleanConverter
/*    */   implements Converter<String, Boolean>
/*    */ {
/* 33 */   private static final Set<String> trueValues = new HashSet<>(4);
/*    */   
/* 35 */   private static final Set<String> falseValues = new HashSet<>(4);
/*    */   
/*    */   static {
/* 38 */     trueValues.add("true");
/* 39 */     trueValues.add("on");
/* 40 */     trueValues.add("yes");
/* 41 */     trueValues.add("1");
/*    */     
/* 43 */     falseValues.add("false");
/* 44 */     falseValues.add("off");
/* 45 */     falseValues.add("no");
/* 46 */     falseValues.add("0");
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean convert(String source) {
/* 51 */     String value = source.trim();
/* 52 */     if (value.isEmpty()) {
/* 53 */       return null;
/*    */     }
/* 55 */     value = value.toLowerCase();
/* 56 */     if (trueValues.contains(value)) {
/* 57 */       return Boolean.TRUE;
/*    */     }
/* 59 */     if (falseValues.contains(value)) {
/* 60 */       return Boolean.FALSE;
/*    */     }
/*    */     
/* 63 */     throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/convert/support/StringToBooleanConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */