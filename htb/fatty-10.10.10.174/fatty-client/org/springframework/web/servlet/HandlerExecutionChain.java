/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HandlerExecutionChain
/*     */ {
/*  41 */   private static final Log logger = LogFactory.getLog(HandlerExecutionChain.class);
/*     */   
/*     */   private final Object handler;
/*     */   
/*     */   @Nullable
/*     */   private HandlerInterceptor[] interceptors;
/*     */   
/*     */   @Nullable
/*     */   private List<HandlerInterceptor> interceptorList;
/*     */   
/*  51 */   private int interceptorIndex = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerExecutionChain(Object handler) {
/*  59 */     this(handler, (HandlerInterceptor[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandlerExecutionChain(Object handler, @Nullable HandlerInterceptor... interceptors) {
/*  69 */     if (handler instanceof HandlerExecutionChain) {
/*  70 */       HandlerExecutionChain originalChain = (HandlerExecutionChain)handler;
/*  71 */       this.handler = originalChain.getHandler();
/*  72 */       this.interceptorList = new ArrayList<>();
/*  73 */       CollectionUtils.mergeArrayIntoCollection(originalChain.getInterceptors(), this.interceptorList);
/*  74 */       CollectionUtils.mergeArrayIntoCollection(interceptors, this.interceptorList);
/*     */     } else {
/*     */       
/*  77 */       this.handler = handler;
/*  78 */       this.interceptors = interceptors;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getHandler() {
/*  87 */     return this.handler;
/*     */   }
/*     */   
/*     */   public void addInterceptor(HandlerInterceptor interceptor) {
/*  91 */     initInterceptorList().add(interceptor);
/*     */   }
/*     */   
/*     */   public void addInterceptors(HandlerInterceptor... interceptors) {
/*  95 */     if (!ObjectUtils.isEmpty((Object[])interceptors)) {
/*  96 */       CollectionUtils.mergeArrayIntoCollection(interceptors, initInterceptorList());
/*     */     }
/*     */   }
/*     */   
/*     */   private List<HandlerInterceptor> initInterceptorList() {
/* 101 */     if (this.interceptorList == null) {
/* 102 */       this.interceptorList = new ArrayList<>();
/* 103 */       if (this.interceptors != null)
/*     */       {
/* 105 */         CollectionUtils.mergeArrayIntoCollection(this.interceptors, this.interceptorList);
/*     */       }
/*     */     } 
/* 108 */     this.interceptors = null;
/* 109 */     return this.interceptorList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HandlerInterceptor[] getInterceptors() {
/* 118 */     if (this.interceptors == null && this.interceptorList != null) {
/* 119 */       this.interceptors = this.interceptorList.<HandlerInterceptor>toArray(new HandlerInterceptor[0]);
/*     */     }
/* 121 */     return this.interceptors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 132 */     HandlerInterceptor[] interceptors = getInterceptors();
/* 133 */     if (!ObjectUtils.isEmpty((Object[])interceptors)) {
/* 134 */       for (int i = 0; i < interceptors.length; i++) {
/* 135 */         HandlerInterceptor interceptor = interceptors[i];
/* 136 */         if (!interceptor.preHandle(request, response, this.handler)) {
/* 137 */           triggerAfterCompletion(request, response, null);
/* 138 */           return false;
/*     */         } 
/* 140 */         this.interceptorIndex = i;
/*     */       } 
/*     */     }
/* 143 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void applyPostHandle(HttpServletRequest request, HttpServletResponse response, @Nullable ModelAndView mv) throws Exception {
/* 152 */     HandlerInterceptor[] interceptors = getInterceptors();
/* 153 */     if (!ObjectUtils.isEmpty((Object[])interceptors)) {
/* 154 */       for (int i = interceptors.length - 1; i >= 0; i--) {
/* 155 */         HandlerInterceptor interceptor = interceptors[i];
/* 156 */         interceptor.postHandle(request, response, this.handler, mv);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, @Nullable Exception ex) throws Exception {
/* 169 */     HandlerInterceptor[] interceptors = getInterceptors();
/* 170 */     if (!ObjectUtils.isEmpty((Object[])interceptors)) {
/* 171 */       for (int i = this.interceptorIndex; i >= 0; i--) {
/* 172 */         HandlerInterceptor interceptor = interceptors[i];
/*     */         try {
/* 174 */           interceptor.afterCompletion(request, response, this.handler, ex);
/*     */         }
/* 176 */         catch (Throwable ex2) {
/* 177 */           logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void applyAfterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response) {
/* 187 */     HandlerInterceptor[] interceptors = getInterceptors();
/* 188 */     if (!ObjectUtils.isEmpty((Object[])interceptors)) {
/* 189 */       for (int i = interceptors.length - 1; i >= 0; i--) {
/* 190 */         if (interceptors[i] instanceof AsyncHandlerInterceptor) {
/*     */           try {
/* 192 */             AsyncHandlerInterceptor asyncInterceptor = (AsyncHandlerInterceptor)interceptors[i];
/* 193 */             asyncInterceptor.afterConcurrentHandlingStarted(request, response, this.handler);
/*     */           }
/* 195 */           catch (Throwable ex) {
/* 196 */             logger.error("Interceptor [" + interceptors[i] + "] failed in afterConcurrentHandlingStarted", ex);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 209 */     Object handler = getHandler();
/* 210 */     StringBuilder sb = new StringBuilder();
/* 211 */     sb.append("HandlerExecutionChain with [").append(handler).append("] and ");
/* 212 */     if (this.interceptorList != null) {
/* 213 */       sb.append(this.interceptorList.size());
/*     */     }
/* 215 */     else if (this.interceptors != null) {
/* 216 */       sb.append(this.interceptors.length);
/*     */     } else {
/*     */       
/* 219 */       sb.append(0);
/*     */     } 
/* 221 */     return sb.append(" interceptors").toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/HandlerExecutionChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */