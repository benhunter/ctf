/*    */ package org.springframework.web.servlet.config.annotation;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import org.springframework.core.OrderComparator;
/*    */ import org.springframework.web.context.request.WebRequestInterceptor;
/*    */ import org.springframework.web.servlet.HandlerInterceptor;
/*    */ import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
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
/*    */ public class InterceptorRegistry
/*    */ {
/* 39 */   private final List<InterceptorRegistration> registrations = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/*    */   private static final Comparator<Object> INTERCEPTOR_ORDER_COMPARATOR;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
/* 49 */     InterceptorRegistration registration = new InterceptorRegistration(interceptor);
/* 50 */     this.registrations.add(registration);
/* 51 */     return registration;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InterceptorRegistration addWebRequestInterceptor(WebRequestInterceptor interceptor) {
/* 61 */     WebRequestHandlerInterceptorAdapter adapted = new WebRequestHandlerInterceptorAdapter(interceptor);
/* 62 */     InterceptorRegistration registration = new InterceptorRegistration((HandlerInterceptor)adapted);
/* 63 */     this.registrations.add(registration);
/* 64 */     return registration;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected List<Object> getInterceptors() {
/* 71 */     return (List<Object>)this.registrations.stream()
/* 72 */       .sorted(INTERCEPTOR_ORDER_COMPARATOR)
/* 73 */       .map(InterceptorRegistration::getInterceptor)
/* 74 */       .collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */   
/*    */   static {
/* 79 */     INTERCEPTOR_ORDER_COMPARATOR = OrderComparator.INSTANCE.withSourceProvider(object -> (object instanceof InterceptorRegistration) ? (InterceptorRegistration)object::getOrder : null);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/InterceptorRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */