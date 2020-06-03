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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletContextAttributeFactoryBean
/*    */   implements FactoryBean<Object>, ServletContextAware
/*    */ {
/*    */   @Nullable
/*    */   private String attributeName;
/*    */   @Nullable
/*    */   private Object attribute;
/*    */   
/*    */   public void setAttributeName(String attributeName) {
/* 58 */     this.attributeName = attributeName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setServletContext(ServletContext servletContext) {
/* 63 */     if (this.attributeName == null) {
/* 64 */       throw new IllegalArgumentException("Property 'attributeName' is required");
/*    */     }
/* 66 */     this.attribute = servletContext.getAttribute(this.attributeName);
/* 67 */     if (this.attribute == null) {
/* 68 */       throw new IllegalStateException("No ServletContext attribute '" + this.attributeName + "' found");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getObject() throws Exception {
/* 76 */     return this.attribute;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 81 */     return (this.attribute != null) ? this.attribute.getClass() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 86 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/ServletContextAttributeFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */