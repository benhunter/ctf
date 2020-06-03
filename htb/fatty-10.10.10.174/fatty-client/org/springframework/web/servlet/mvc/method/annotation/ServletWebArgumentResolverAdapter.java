/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.bind.support.WebArgumentResolver;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.context.request.RequestAttributes;
/*    */ import org.springframework.web.context.request.RequestContextHolder;
/*    */ import org.springframework.web.context.request.ServletRequestAttributes;
/*    */ import org.springframework.web.context.request.ServletWebRequest;
/*    */ import org.springframework.web.method.annotation.AbstractWebArgumentResolverAdapter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServletWebArgumentResolverAdapter
/*    */   extends AbstractWebArgumentResolverAdapter
/*    */ {
/*    */   public ServletWebArgumentResolverAdapter(WebArgumentResolver adaptee) {
/* 44 */     super(adaptee);
/*    */   }
/*    */ 
/*    */   
/*    */   protected NativeWebRequest getWebRequest() {
/* 49 */     RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
/* 50 */     Assert.state(requestAttributes instanceof ServletRequestAttributes, "No ServletRequestAttributes");
/* 51 */     ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)requestAttributes;
/* 52 */     return (NativeWebRequest)new ServletWebRequest(servletRequestAttributes.getRequest());
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ServletWebArgumentResolverAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */