/*    */ package org.springframework.web.bind.support;
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
/*    */ public class SimpleSessionStatus
/*    */   implements SessionStatus
/*    */ {
/*    */   private boolean complete = false;
/*    */   
/*    */   public void setComplete() {
/* 33 */     this.complete = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isComplete() {
/* 38 */     return this.complete;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/support/SimpleSessionStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */