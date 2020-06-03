/*    */ package org.springframework.web.filter;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.FilterChain;
/*    */ import javax.servlet.ServletException;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.http.HttpStatus;
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
/*    */ public class RelativeRedirectFilter
/*    */   extends OncePerRequestFilter
/*    */ {
/* 45 */   private HttpStatus redirectStatus = HttpStatus.SEE_OTHER;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRedirectStatus(HttpStatus status) {
/* 54 */     Assert.notNull(status, "Property 'redirectStatus' is required");
/* 55 */     Assert.isTrue(status.is3xxRedirection(), "Not a redirect status code");
/* 56 */     this.redirectStatus = status;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpStatus getRedirectStatus() {
/* 63 */     return this.redirectStatus;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 71 */     response = RelativeRedirectResponseWrapper.wrapIfNecessary(response, this.redirectStatus);
/* 72 */     filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/RelativeRedirectFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */