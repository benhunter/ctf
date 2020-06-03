/*    */ package org.springframework.web.servlet.mvc.annotation;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.ui.ExtendedModelMap;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
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
/*    */ public interface ModelAndViewResolver
/*    */ {
/* 53 */   public static final ModelAndView UNRESOLVED = new ModelAndView();
/*    */   
/*    */   ModelAndView resolveModelAndView(Method paramMethod, Class<?> paramClass, @Nullable Object paramObject, ExtendedModelMap paramExtendedModelMap, NativeWebRequest paramNativeWebRequest);
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/annotation/ModelAndViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */