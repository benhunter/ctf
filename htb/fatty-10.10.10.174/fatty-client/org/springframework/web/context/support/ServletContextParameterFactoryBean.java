/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.context.ServletContextAware;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletContextParameterFactoryBean
/*    */   implements FactoryBean<String>, ServletContextAware
/*    */ {
/*    */   @Nullable
/*    */   private String initParamName;
/*    */   @Nullable
/*    */   private String paramValue;
/*    */   
/*    */   public void setInitParamName(String initParamName) {
/* 53 */     this.initParamName = initParamName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setServletContext(ServletContext servletContext) {
/* 58 */     if (this.initParamName == null) {
/* 59 */       throw new IllegalArgumentException("initParamName is required");
/*    */     }
/* 61 */     this.paramValue = servletContext.getInitParameter(this.initParamName);
/* 62 */     if (this.paramValue == null) {
/* 63 */       throw new IllegalStateException("No ServletContext init parameter '" + this.initParamName + "' found");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getObject() {
/* 71 */     return this.paramValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<String> getObjectType() {
/* 76 */     return String.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 81 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/ServletContextParameterFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */