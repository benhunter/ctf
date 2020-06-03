/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import javax.servlet.http.HttpServletResponseWrapper;
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.util.WebUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class RelativeRedirectResponseWrapper
/*    */   extends HttpServletResponseWrapper
/*    */ {
/*    */   private final HttpStatus redirectStatus;
/*    */   
/*    */   private RelativeRedirectResponseWrapper(HttpServletResponse response, HttpStatus redirectStatus) {
/* 39 */     super(response);
/* 40 */     Assert.notNull(redirectStatus, "'redirectStatus' is required");
/* 41 */     this.redirectStatus = redirectStatus;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void sendRedirect(String location) {
/* 47 */     setStatus(this.redirectStatus.value());
/* 48 */     setHeader("Location", location);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpServletResponse wrapIfNecessary(HttpServletResponse response, HttpStatus redirectStatus) {
/* 56 */     RelativeRedirectResponseWrapper wrapper = (RelativeRedirectResponseWrapper)WebUtils.getNativeResponse((ServletResponse)response, RelativeRedirectResponseWrapper.class);
/*    */     
/* 58 */     return (wrapper != null) ? response : (HttpServletResponse)new RelativeRedirectResponseWrapper(response, redirectStatus);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/RelativeRedirectResponseWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */