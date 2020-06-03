/*    */ package org.springframework.web.context.request;
/*    */ 
/*    */ import org.springframework.beans.factory.ObjectFactory;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class SessionScope
/*    */   extends AbstractRequestAttributesScope
/*    */ {
/*    */   protected int getScope() {
/* 45 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConversationId() {
/* 50 */     return RequestContextHolder.currentRequestAttributes().getSessionId();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object get(String name, ObjectFactory<?> objectFactory) {
/* 55 */     Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
/* 56 */     synchronized (mutex) {
/* 57 */       return super.get(name, objectFactory);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object remove(String name) {
/* 64 */     Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
/* 65 */     synchronized (mutex) {
/* 66 */       return super.remove(name);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/SessionScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */