/*    */ package org.springframework.web.server;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.time.Instant;
/*    */ import java.util.Map;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import reactor.core.publisher.Mono;
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
/*    */ public interface WebSession
/*    */ {
/*    */   @Nullable
/*    */   default <T> T getAttribute(String name) {
/* 61 */     return (T)getAttributes().get(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <T> T getRequiredAttribute(String name) {
/* 73 */     T value = getAttribute(name);
/* 74 */     Assert.notNull(value, () -> "Required attribute '" + name + "' is missing.");
/* 75 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <T> T getAttributeOrDefault(String name, T defaultValue) {
/* 87 */     return (T)getAttributes().getOrDefault(name, defaultValue);
/*    */   }
/*    */   
/*    */   String getId();
/*    */   
/*    */   Map<String, Object> getAttributes();
/*    */   
/*    */   void start();
/*    */   
/*    */   boolean isStarted();
/*    */   
/*    */   Mono<Void> changeSessionId();
/*    */   
/*    */   Mono<Void> invalidate();
/*    */   
/*    */   Mono<Void> save();
/*    */   
/*    */   boolean isExpired();
/*    */   
/*    */   Instant getCreationTime();
/*    */   
/*    */   Instant getLastAccessTime();
/*    */   
/*    */   void setMaxIdleTime(Duration paramDuration);
/*    */   
/*    */   Duration getMaxIdleTime();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/server/WebSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */