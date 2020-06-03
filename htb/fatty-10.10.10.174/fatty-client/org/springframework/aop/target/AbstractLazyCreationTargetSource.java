/*    */ package org.springframework.aop.target;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.aop.TargetSource;
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
/*    */ public abstract class AbstractLazyCreationTargetSource
/*    */   implements TargetSource
/*    */ {
/* 46 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Object lazyTarget;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized boolean isInitialized() {
/* 57 */     return (this.lazyTarget != null);
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
/*    */   
/*    */   @Nullable
/*    */   public synchronized Class<?> getTargetClass() {
/* 71 */     return (this.lazyTarget != null) ? this.lazyTarget.getClass() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStatic() {
/* 76 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized Object getTarget() throws Exception {
/* 86 */     if (this.lazyTarget == null) {
/* 87 */       this.logger.debug("Initializing lazy target object");
/* 88 */       this.lazyTarget = createObject();
/*    */     } 
/* 90 */     return this.lazyTarget;
/*    */   }
/*    */   
/*    */   public void releaseTarget(Object target) throws Exception {}
/*    */   
/*    */   protected abstract Object createObject() throws Exception;
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/target/AbstractLazyCreationTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */