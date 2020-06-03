/*    */ package org.springframework.web.context.request;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.util.Assert;
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
/*    */ public abstract class AbstractRequestAttributes
/*    */   implements RequestAttributes
/*    */ {
/* 36 */   protected final Map<String, Runnable> requestDestructionCallbacks = new LinkedHashMap<>(8);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private volatile boolean requestActive = true;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void requestCompleted() {
/* 47 */     executeRequestDestructionCallbacks();
/* 48 */     updateAccessedSessionAttributes();
/* 49 */     this.requestActive = false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final boolean isRequestActive() {
/* 57 */     return this.requestActive;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void registerRequestDestructionCallback(String name, Runnable callback) {
/* 66 */     Assert.notNull(name, "Name must not be null");
/* 67 */     Assert.notNull(callback, "Callback must not be null");
/* 68 */     synchronized (this.requestDestructionCallbacks) {
/* 69 */       this.requestDestructionCallbacks.put(name, callback);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void removeRequestDestructionCallback(String name) {
/* 78 */     Assert.notNull(name, "Name must not be null");
/* 79 */     synchronized (this.requestDestructionCallbacks) {
/* 80 */       this.requestDestructionCallbacks.remove(name);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void executeRequestDestructionCallbacks() {
/* 89 */     synchronized (this.requestDestructionCallbacks) {
/* 90 */       for (Runnable runnable : this.requestDestructionCallbacks.values()) {
/* 91 */         runnable.run();
/*    */       }
/* 93 */       this.requestDestructionCallbacks.clear();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract void updateAccessedSessionAttributes();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/AbstractRequestAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */