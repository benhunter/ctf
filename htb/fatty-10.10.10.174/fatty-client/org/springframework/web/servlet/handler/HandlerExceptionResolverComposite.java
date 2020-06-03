/*    */ package org.springframework.web.servlet.handler;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.servlet.HandlerExceptionResolver;
/*    */ import org.springframework.web.servlet.ModelAndView;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HandlerExceptionResolverComposite
/*    */   implements HandlerExceptionResolver, Ordered
/*    */ {
/*    */   @Nullable
/*    */   private List<HandlerExceptionResolver> resolvers;
/* 41 */   private int order = Integer.MAX_VALUE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
/* 48 */     this.resolvers = exceptionResolvers;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<HandlerExceptionResolver> getExceptionResolvers() {
/* 55 */     return (this.resolvers != null) ? Collections.<HandlerExceptionResolver>unmodifiableList(this.resolvers) : Collections.<HandlerExceptionResolver>emptyList();
/*    */   }
/*    */   
/*    */   public void setOrder(int order) {
/* 59 */     this.order = order;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOrder() {
/* 64 */     return this.order;
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
/*    */   @Nullable
/*    */   public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
/* 77 */     if (this.resolvers != null) {
/* 78 */       for (HandlerExceptionResolver handlerExceptionResolver : this.resolvers) {
/* 79 */         ModelAndView mav = handlerExceptionResolver.resolveException(request, response, handler, ex);
/* 80 */         if (mav != null) {
/* 81 */           return mav;
/*    */         }
/*    */       } 
/*    */     }
/* 85 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/HandlerExceptionResolverComposite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */