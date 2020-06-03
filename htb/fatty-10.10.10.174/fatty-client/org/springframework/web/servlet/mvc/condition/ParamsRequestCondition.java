/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.util.WebUtils;
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
/*     */ 
/*     */ public final class ParamsRequestCondition
/*     */   extends AbstractRequestCondition<ParamsRequestCondition>
/*     */ {
/*     */   private final Set<ParamExpression> expressions;
/*     */   
/*     */   public ParamsRequestCondition(String... params) {
/*  49 */     this(parseExpressions(params));
/*     */   }
/*     */   
/*     */   private ParamsRequestCondition(Collection<ParamExpression> conditions) {
/*  53 */     this.expressions = Collections.unmodifiableSet(new LinkedHashSet<>(conditions));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Collection<ParamExpression> parseExpressions(String... params) {
/*  58 */     Set<ParamExpression> expressions = new LinkedHashSet<>();
/*  59 */     for (String param : params) {
/*  60 */       expressions.add(new ParamExpression(param));
/*     */     }
/*  62 */     return expressions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<NameValueExpression<String>> getExpressions() {
/*  70 */     return new LinkedHashSet<>((Collection)this.expressions);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Collection<ParamExpression> getContent() {
/*  75 */     return this.expressions;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getToStringInfix() {
/*  80 */     return " && ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParamsRequestCondition combine(ParamsRequestCondition other) {
/*  89 */     Set<ParamExpression> set = new LinkedHashSet<>(this.expressions);
/*  90 */     set.addAll(other.expressions);
/*  91 */     return new ParamsRequestCondition(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ParamsRequestCondition getMatchingCondition(HttpServletRequest request) {
/* 101 */     for (ParamExpression expression : this.expressions) {
/* 102 */       if (!expression.match(request)) {
/* 103 */         return null;
/*     */       }
/*     */     } 
/* 106 */     return this;
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
/*     */   public int compareTo(ParamsRequestCondition other, HttpServletRequest request) {
/* 122 */     int result = other.expressions.size() - this.expressions.size();
/* 123 */     if (result != 0) {
/* 124 */       return result;
/*     */     }
/* 126 */     return (int)(getValueMatchCount(other.expressions) - getValueMatchCount(this.expressions));
/*     */   }
/*     */   
/*     */   private long getValueMatchCount(Set<ParamExpression> expressions) {
/* 130 */     long count = 0L;
/* 131 */     for (ParamExpression e : expressions) {
/* 132 */       if (e.getValue() != null && !e.isNegated()) {
/* 133 */         count++;
/*     */       }
/*     */     } 
/* 136 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ParamExpression
/*     */     extends AbstractNameValueExpression<String>
/*     */   {
/*     */     ParamExpression(String expression) {
/* 146 */       super(expression);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isCaseSensitiveName() {
/* 151 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String parseValue(String valueExpression) {
/* 156 */       return valueExpression;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean matchName(HttpServletRequest request) {
/* 161 */       return (WebUtils.hasSubmitParameter((ServletRequest)request, this.name) || request
/* 162 */         .getParameterMap().containsKey(this.name));
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean matchValue(HttpServletRequest request) {
/* 167 */       return ObjectUtils.nullSafeEquals(this.value, request.getParameter(this.name));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/ParamsRequestCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */