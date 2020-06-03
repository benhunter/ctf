/*    */ package org.springframework.web.servlet.tags;
/*    */ 
/*    */ import javax.servlet.jsp.JspException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HtmlEscapeTag
/*    */   extends RequestContextAwareTag
/*    */ {
/*    */   private boolean defaultHtmlEscape;
/*    */   
/*    */   public void setDefaultHtmlEscape(boolean defaultHtmlEscape) {
/* 65 */     this.defaultHtmlEscape = defaultHtmlEscape;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int doStartTagInternal() throws JspException {
/* 71 */     getRequestContext().setDefaultHtmlEscape(this.defaultHtmlEscape);
/* 72 */     return 1;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/HtmlEscapeTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */