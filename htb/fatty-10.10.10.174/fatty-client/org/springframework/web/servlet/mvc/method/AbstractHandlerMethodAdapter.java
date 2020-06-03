/*     */ package org.springframework.web.servlet.mvc.method;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.servlet.HandlerAdapter;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.support.WebContentGenerator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHandlerMethodAdapter
/*     */   extends WebContentGenerator
/*     */   implements HandlerAdapter, Ordered
/*     */ {
/*  38 */   private int order = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractHandlerMethodAdapter() {
/*  43 */     super(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrder(int order) {
/*  53 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/*  58 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean supports(Object handler) {
/*  69 */     return (handler instanceof HandlerMethod && supportsInternal((HandlerMethod)handler));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean supportsInternal(HandlerMethod paramHandlerMethod);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
/*  87 */     return handleInternal(request, response, (HandlerMethod)handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract ModelAndView handleInternal(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, HandlerMethod paramHandlerMethod) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getLastModified(HttpServletRequest request, Object handler) {
/* 109 */     return getLastModifiedInternal(request, (HandlerMethod)handler);
/*     */   }
/*     */   
/*     */   protected abstract long getLastModifiedInternal(HttpServletRequest paramHttpServletRequest, HandlerMethod paramHandlerMethod);
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/AbstractHandlerMethodAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */