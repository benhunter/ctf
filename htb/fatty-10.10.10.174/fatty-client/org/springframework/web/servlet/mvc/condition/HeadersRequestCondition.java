/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HeadersRequestCondition
/*     */   extends AbstractRequestCondition<HeadersRequestCondition>
/*     */ {
/*  44 */   private static final HeadersRequestCondition PRE_FLIGHT_MATCH = new HeadersRequestCondition(new String[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Set<HeaderExpression> expressions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeadersRequestCondition(String... headers) {
/*  58 */     this(parseExpressions(headers));
/*     */   }
/*     */   
/*     */   private HeadersRequestCondition(Collection<HeaderExpression> conditions) {
/*  62 */     this.expressions = Collections.unmodifiableSet(new LinkedHashSet<>(conditions));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Collection<HeaderExpression> parseExpressions(String... headers) {
/*  67 */     Set<HeaderExpression> expressions = new LinkedHashSet<>();
/*  68 */     for (String header : headers) {
/*  69 */       HeaderExpression expr = new HeaderExpression(header);
/*  70 */       if (!"Accept".equalsIgnoreCase(expr.name) && !"Content-Type".equalsIgnoreCase(expr.name))
/*     */       {
/*     */         
/*  73 */         expressions.add(expr); } 
/*     */     } 
/*  75 */     return expressions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<NameValueExpression<String>> getExpressions() {
/*  82 */     return new LinkedHashSet<>((Collection)this.expressions);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Collection<HeaderExpression> getContent() {
/*  87 */     return this.expressions;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getToStringInfix() {
/*  92 */     return " && ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeadersRequestCondition combine(HeadersRequestCondition other) {
/* 101 */     Set<HeaderExpression> set = new LinkedHashSet<>(this.expressions);
/* 102 */     set.addAll(other.expressions);
/* 103 */     return new HeadersRequestCondition(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public HeadersRequestCondition getMatchingCondition(HttpServletRequest request) {
/* 113 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 114 */       return PRE_FLIGHT_MATCH;
/*     */     }
/* 116 */     for (HeaderExpression expression : this.expressions) {
/* 117 */       if (!expression.match(request)) {
/* 118 */         return null;
/*     */       }
/*     */     } 
/* 121 */     return this;
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
/*     */   public int compareTo(HeadersRequestCondition other, HttpServletRequest request) {
/* 137 */     int result = other.expressions.size() - this.expressions.size();
/* 138 */     if (result != 0) {
/* 139 */       return result;
/*     */     }
/* 141 */     return (int)(getValueMatchCount(other.expressions) - getValueMatchCount(this.expressions));
/*     */   }
/*     */   
/*     */   private long getValueMatchCount(Set<HeaderExpression> expressions) {
/* 145 */     long count = 0L;
/* 146 */     for (HeaderExpression e : expressions) {
/* 147 */       if (e.getValue() != null && !e.isNegated()) {
/* 148 */         count++;
/*     */       }
/*     */     } 
/* 151 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class HeaderExpression
/*     */     extends AbstractNameValueExpression<String>
/*     */   {
/*     */     public HeaderExpression(String expression) {
/* 161 */       super(expression);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isCaseSensitiveName() {
/* 166 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String parseValue(String valueExpression) {
/* 171 */       return valueExpression;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean matchName(HttpServletRequest request) {
/* 176 */       return (request.getHeader(this.name) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean matchValue(HttpServletRequest request) {
/* 181 */       return ObjectUtils.nullSafeEquals(this.value, request.getHeader(this.name));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/HeadersRequestCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */