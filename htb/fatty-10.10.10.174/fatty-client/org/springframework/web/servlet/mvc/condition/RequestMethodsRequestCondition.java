/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.DispatcherType;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.cors.CorsUtils;
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
/*     */ public final class RequestMethodsRequestCondition
/*     */   extends AbstractRequestCondition<RequestMethodsRequestCondition>
/*     */ {
/*  43 */   private static final RequestMethodsRequestCondition GET_CONDITION = new RequestMethodsRequestCondition(new RequestMethod[] { RequestMethod.GET });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Set<RequestMethod> methods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMethodsRequestCondition(RequestMethod... requestMethods) {
/*  55 */     this(Arrays.asList(requestMethods));
/*     */   }
/*     */   
/*     */   private RequestMethodsRequestCondition(Collection<RequestMethod> requestMethods) {
/*  59 */     this.methods = Collections.unmodifiableSet(new LinkedHashSet<>(requestMethods));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<RequestMethod> getMethods() {
/*  67 */     return this.methods;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Collection<RequestMethod> getContent() {
/*  72 */     return this.methods;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getToStringInfix() {
/*  77 */     return " || ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMethodsRequestCondition combine(RequestMethodsRequestCondition other) {
/*  86 */     Set<RequestMethod> set = new LinkedHashSet<>(this.methods);
/*  87 */     set.addAll(other.methods);
/*  88 */     return new RequestMethodsRequestCondition(set);
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
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RequestMethodsRequestCondition getMatchingCondition(HttpServletRequest request) {
/* 103 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 104 */       return matchPreFlight(request);
/*     */     }
/*     */     
/* 107 */     if (getMethods().isEmpty()) {
/* 108 */       if (RequestMethod.OPTIONS.name().equals(request.getMethod()) && 
/* 109 */         !DispatcherType.ERROR.equals(request.getDispatcherType()))
/*     */       {
/* 111 */         return null;
/*     */       }
/* 113 */       return this;
/*     */     } 
/*     */     
/* 116 */     return matchRequestMethod(request.getMethod());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private RequestMethodsRequestCondition matchPreFlight(HttpServletRequest request) {
/* 126 */     if (getMethods().isEmpty()) {
/* 127 */       return this;
/*     */     }
/* 129 */     String expectedMethod = request.getHeader("Access-Control-Request-Method");
/* 130 */     return matchRequestMethod(expectedMethod);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private RequestMethodsRequestCondition matchRequestMethod(String httpMethodValue) {
/* 135 */     HttpMethod httpMethod = HttpMethod.resolve(httpMethodValue);
/* 136 */     if (httpMethod != null) {
/* 137 */       for (RequestMethod method : getMethods()) {
/* 138 */         if (httpMethod.matches(method.name())) {
/* 139 */           return new RequestMethodsRequestCondition(new RequestMethod[] { method });
/*     */         }
/*     */       } 
/* 142 */       if (httpMethod == HttpMethod.HEAD && getMethods().contains(RequestMethod.GET)) {
/* 143 */         return GET_CONDITION;
/*     */       }
/*     */     } 
/* 146 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(RequestMethodsRequestCondition other, HttpServletRequest request) {
/* 162 */     if (other.methods.size() != this.methods.size()) {
/* 163 */       return other.methods.size() - this.methods.size();
/*     */     }
/* 165 */     if (this.methods.size() == 1) {
/* 166 */       if (this.methods.contains(RequestMethod.HEAD) && other.methods.contains(RequestMethod.GET)) {
/* 167 */         return -1;
/*     */       }
/* 169 */       if (this.methods.contains(RequestMethod.GET) && other.methods.contains(RequestMethod.HEAD)) {
/* 170 */         return 1;
/*     */       }
/*     */     } 
/* 173 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/RequestMethodsRequestCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */