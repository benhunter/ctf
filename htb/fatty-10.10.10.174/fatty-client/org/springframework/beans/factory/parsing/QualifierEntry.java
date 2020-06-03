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
/*    */ public class QualifierEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private String typeName;
/*    */   
/*    */   public QualifierEntry(String typeName) {
/* 33 */     if (!StringUtils.hasText(typeName)) {
/* 34 */       throw new IllegalArgumentException("Invalid qualifier type '" + typeName + "'.");
/*    */     }
/* 36 */     this.typeName = typeName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return "Qualifier '" + this.typeName + "'";
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/QualifierEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */