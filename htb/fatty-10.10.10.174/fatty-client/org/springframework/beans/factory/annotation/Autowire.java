/*    */ package org.springframework.beans.factory.annotation;
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
/*    */ 
/*    */ public enum Autowire
/*    */ {
/* 40 */   NO(0),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   BY_NAME(1),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   BY_TYPE(2);
/*    */ 
/*    */   
/*    */   private final int value;
/*    */ 
/*    */   
/*    */   Autowire(int value) {
/* 57 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int value() {
/* 61 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAutowire() {
/* 70 */     return (this == BY_NAME || this == BY_TYPE);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/annotation/Autowire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */