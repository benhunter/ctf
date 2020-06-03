/*    */ package org.springframework.web.context.support;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class ServletContextAttributeExporter
/*    */   implements ServletContextAware
/*    */ {
/* 50 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private Map<String, Object> attributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAttributes(Map<String, Object> attributes) {
/* 65 */     this.attributes = attributes;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setServletContext(ServletContext servletContext) {
/* 70 */     if (this.attributes != null)
/* 71 */       for (Map.Entry<String, Object> entry : this.attributes.entrySet()) {
/* 72 */         String attributeName = entry.getKey();
/* 73 */         if (this.logger.isDebugEnabled() && 
/* 74 */           servletContext.getAttribute(attributeName) != null) {
/* 75 */           this.logger.debug("Replacing existing ServletContext attribute with name '" + attributeName + "'");
/*    */         }
/*    */         
/* 78 */         servletContext.setAttribute(attributeName, entry.getValue());
/* 79 */         if (this.logger.isTraceEnabled())
/* 80 */           this.logger.trace("Exported ServletContext attribute with name '" + attributeName + "'"); 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/support/ServletContextAttributeExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */