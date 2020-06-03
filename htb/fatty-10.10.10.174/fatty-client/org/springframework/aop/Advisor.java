/*    */ package org.springframework.aop;
/*    */ 
/*    */ import org.aopalliance.aop.Advice;
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
/*    */ 
/*    */ public interface Advisor
/*    */ {
/* 43 */   public static final Advice EMPTY_ADVICE = new Advice() {
/*    */     
/*    */     };
/*    */   
/*    */   Advice getAdvice();
/*    */   
/*    */   boolean isPerInstance();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/Advisor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */