/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
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
/*    */ public class PropertyEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public PropertyEntry(String name) {
/* 39 */     if (!StringUtils.hasText(name)) {
/* 40 */       throw new IllegalArgumentException("Invalid property name '" + name + "'.");
/*    */     }
/* 42 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 48 */     return "Property '" + this.name + "'";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/PropertyEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */