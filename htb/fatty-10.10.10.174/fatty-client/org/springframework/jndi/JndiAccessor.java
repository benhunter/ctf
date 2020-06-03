/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import java.util.Properties;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class JndiAccessor
/*    */ {
/* 40 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/* 42 */   private JndiTemplate jndiTemplate = new JndiTemplate();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJndiTemplate(@Nullable JndiTemplate jndiTemplate) {
/* 51 */     this.jndiTemplate = (jndiTemplate != null) ? jndiTemplate : new JndiTemplate();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JndiTemplate getJndiTemplate() {
/* 58 */     return this.jndiTemplate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJndiEnvironment(@Nullable Properties jndiEnvironment) {
/* 67 */     this.jndiTemplate = new JndiTemplate(jndiEnvironment);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Properties getJndiEnvironment() {
/* 75 */     return this.jndiTemplate.getEnvironment();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */