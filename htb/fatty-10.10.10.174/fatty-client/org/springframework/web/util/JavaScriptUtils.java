/*    */ package org.springframework.web.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class JavaScriptUtils
/*    */ {
/*    */   public static String javaScriptEscape(String input) {
/* 40 */     StringBuilder filtered = new StringBuilder(input.length());
/* 41 */     char prevChar = Character.MIN_VALUE;
/*    */     
/* 43 */     for (int i = 0; i < input.length(); i++) {
/* 44 */       char c = input.charAt(i);
/* 45 */       if (c == '"') {
/* 46 */         filtered.append("\\\"");
/*    */       }
/* 48 */       else if (c == '\'') {
/* 49 */         filtered.append("\\'");
/*    */       }
/* 51 */       else if (c == '\\') {
/* 52 */         filtered.append("\\\\");
/*    */       }
/* 54 */       else if (c == '/') {
/* 55 */         filtered.append("\\/");
/*    */       }
/* 57 */       else if (c == '\t') {
/* 58 */         filtered.append("\\t");
/*    */       }
/* 60 */       else if (c == '\n') {
/* 61 */         if (prevChar != '\r') {
/* 62 */           filtered.append("\\n");
/*    */         }
/*    */       }
/* 65 */       else if (c == '\r') {
/* 66 */         filtered.append("\\n");
/*    */       }
/* 68 */       else if (c == '\f') {
/* 69 */         filtered.append("\\f");
/*    */       }
/* 71 */       else if (c == '\b') {
/* 72 */         filtered.append("\\b");
/*    */       
/*    */       }
/* 75 */       else if (c == '\013') {
/* 76 */         filtered.append("\\v");
/*    */       }
/* 78 */       else if (c == '<') {
/* 79 */         filtered.append("\\u003C");
/*    */       }
/* 81 */       else if (c == '>') {
/* 82 */         filtered.append("\\u003E");
/*    */       
/*    */       }
/* 85 */       else if (c == ' ') {
/* 86 */         filtered.append("\\u2028");
/*    */       
/*    */       }
/* 89 */       else if (c == ' ') {
/* 90 */         filtered.append("\\u2029");
/*    */       } else {
/*    */         
/* 93 */         filtered.append(c);
/*    */       } 
/* 95 */       prevChar = c;
/*    */     } 
/*    */     
/* 98 */     return filtered.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/util/JavaScriptUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */