/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.core.Ordered;
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
/*    */ 
/*    */ public interface SmartApplicationListener
/*    */   extends ApplicationListener<ApplicationEvent>, Ordered
/*    */ {
/*    */   boolean supportsEventType(Class<? extends ApplicationEvent> paramClass);
/*    */   
/*    */   default boolean supportsSourceType(@Nullable Class<?> sourceType) {
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default int getOrder() {
/* 59 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/SmartApplicationListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */