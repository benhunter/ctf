/*    */ package org.springframework.scripting.support;
/*    */ 
/*    */ import org.springframework.aop.target.dynamic.BeanFactoryRefreshableTargetSource;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.scripting.ScriptFactory;
/*    */ import org.springframework.scripting.ScriptSource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefreshableScriptTargetSource
/*    */   extends BeanFactoryRefreshableTargetSource
/*    */ {
/*    */   private final ScriptFactory scriptFactory;
/*    */   private final ScriptSource scriptSource;
/*    */   private final boolean isFactoryBean;
/*    */   
/*    */   public RefreshableScriptTargetSource(BeanFactory beanFactory, String beanName, ScriptFactory scriptFactory, ScriptSource scriptSource, boolean isFactoryBean) {
/* 55 */     super(beanFactory, beanName);
/* 56 */     Assert.notNull(scriptFactory, "ScriptFactory must not be null");
/* 57 */     Assert.notNull(scriptSource, "ScriptSource must not be null");
/* 58 */     this.scriptFactory = scriptFactory;
/* 59 */     this.scriptSource = scriptSource;
/* 60 */     this.isFactoryBean = isFactoryBean;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean requiresRefresh() {
/* 71 */     return this.scriptFactory.requiresScriptedObjectRefresh(this.scriptSource);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Object obtainFreshBean(BeanFactory beanFactory, String beanName) {
/* 79 */     return super.obtainFreshBean(beanFactory, this.isFactoryBean ? ("&" + beanName) : beanName);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/scripting/support/RefreshableScriptTargetSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */