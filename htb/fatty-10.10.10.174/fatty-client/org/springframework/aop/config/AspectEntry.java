/*    */ package org.springframework.aop.config;
/*    */ 
/*    */ import org.springframework.beans.factory.parsing.ParseState;
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
/*    */ 
/*    */ public class AspectEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private final String id;
/*    */   private final String ref;
/*    */   
/*    */   public AspectEntry(String id, String ref) {
/* 42 */     this.id = id;
/* 43 */     this.ref = ref;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 48 */     return "Aspect: " + (StringUtils.hasLength(this.id) ? ("id='" + this.id + "'") : ("ref='" + this.ref + "'"));
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/config/AspectEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */