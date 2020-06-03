/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import javax.servlet.http.Cookie;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.annotation.AbstractCookieValueMethodArgumentResolver;
/*    */ import org.springframework.web.util.UrlPathHelper;
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
/*    */ public class ServletCookieValueMethodArgumentResolver
/*    */   extends AbstractCookieValueMethodArgumentResolver
/*    */ {
/* 40 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*    */ 
/*    */   
/*    */   public ServletCookieValueMethodArgumentResolver(@Nullable ConfigurableBeanFactory beanFactory) {
/* 44 */     super(beanFactory);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/* 49 */     this.urlPathHelper = urlPathHelper;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Object resolveName(String cookieName, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
/* 58 */     HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 59 */     Assert.state((servletRequest != null), "No HttpServletRequest");
/*    */     
/* 61 */     Cookie cookieValue = WebUtils.getCookie(servletRequest, cookieName);
/* 62 */     if (Cookie.class.isAssignableFrom(parameter.getNestedParameterType())) {
/* 63 */       return cookieValue;
/*    */     }
/* 65 */     if (cookieValue != null) {
/* 66 */       return this.urlPathHelper.decodeRequestString(servletRequest, cookieValue.getValue());
/*    */     }
/*    */     
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ServletCookieValueMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */