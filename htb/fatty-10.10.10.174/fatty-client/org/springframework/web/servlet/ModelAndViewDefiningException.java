/*    */ package org.springframework.web.servlet;
/*    */ 
/*    */ import javax.servlet.ServletException;
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
/*    */ public class ModelAndViewDefiningException
/*    */   extends ServletException
/*    */ {
/*    */   private final ModelAndView modelAndView;
/*    */   
/*    */   public ModelAndViewDefiningException(ModelAndView modelAndView) {
/* 47 */     Assert.notNull(modelAndView, "ModelAndView must not be null in ModelAndViewDefiningException");
/* 48 */     this.modelAndView = modelAndView;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ModelAndView getModelAndView() {
/* 55 */     return this.modelAndView;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/ModelAndViewDefiningException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */