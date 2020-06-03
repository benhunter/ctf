/*    */ package org.springframework.context.event;
/*    */ 
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.core.ResolvableType;
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
/*    */ public interface GenericApplicationListener
/*    */   extends ApplicationListener<ApplicationEvent>, Ordered
/*    */ {
/*    */   boolean supportsEventType(ResolvableType paramResolvableType);
/*    */   
/*    */   default boolean supportsSourceType(@Nullable Class<?> sourceType) {
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default int getOrder() {
/* 60 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/event/GenericApplicationListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */