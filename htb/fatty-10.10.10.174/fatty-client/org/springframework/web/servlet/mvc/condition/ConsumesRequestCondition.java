/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ 
/*     */ public final class ConsumesRequestCondition
/*     */   extends AbstractRequestCondition<ConsumesRequestCondition>
/*     */ {
/*  49 */   private static final ConsumesRequestCondition EMPTY_CONDITION = new ConsumesRequestCondition(new String[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<ConsumeMediaTypeExpression> expressions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConsumesRequestCondition(String... consumes) {
/*  61 */     this(consumes, null);
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
/*     */   public ConsumesRequestCondition(String[] consumes, @Nullable String[] headers) {
/*  73 */     this(parseExpressions(consumes, headers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConsumesRequestCondition(Collection<ConsumeMediaTypeExpression> expressions) {
/*  80 */     this.expressions = new ArrayList<>(expressions);
/*  81 */     Collections.sort(this.expressions);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Set<ConsumeMediaTypeExpression> parseExpressions(String[] consumes, @Nullable String[] headers) {
/*  86 */     Set<ConsumeMediaTypeExpression> result = new LinkedHashSet<>();
/*  87 */     if (headers != null) {
/*  88 */       for (String header : headers) {
/*  89 */         HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
/*  90 */         if ("Content-Type".equalsIgnoreCase(expr.name) && expr.value != null) {
/*  91 */           for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
/*  92 */             result.add(new ConsumeMediaTypeExpression(mediaType, expr.isNegated));
/*     */           }
/*     */         }
/*     */       } 
/*     */     }
/*  97 */     for (String consume : consumes) {
/*  98 */       result.add(new ConsumeMediaTypeExpression(consume));
/*     */     }
/* 100 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<MediaTypeExpression> getExpressions() {
/* 108 */     return new LinkedHashSet<>((Collection)this.expressions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<MediaType> getConsumableMediaTypes() {
/* 115 */     Set<MediaType> result = new LinkedHashSet<>();
/* 116 */     for (ConsumeMediaTypeExpression expression : this.expressions) {
/* 117 */       if (!expression.isNegated()) {
/* 118 */         result.add(expression.getMediaType());
/*     */       }
/*     */     } 
/* 121 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 129 */     return this.expressions.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Collection<ConsumeMediaTypeExpression> getContent() {
/* 134 */     return this.expressions;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getToStringInfix() {
/* 139 */     return " || ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConsumesRequestCondition combine(ConsumesRequestCondition other) {
/* 149 */     return !other.expressions.isEmpty() ? other : this;
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
/*     */   public ConsumesRequestCondition getMatchingCondition(HttpServletRequest request) {
/*     */     MediaType contentType;
/* 165 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 166 */       return EMPTY_CONDITION;
/*     */     }
/* 168 */     if (isEmpty()) {
/* 169 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 175 */       contentType = StringUtils.hasLength(request.getContentType()) ? MediaType.parseMediaType(request.getContentType()) : MediaType.APPLICATION_OCTET_STREAM;
/*     */     
/*     */     }
/* 178 */     catch (InvalidMediaTypeException ex) {
/* 179 */       return null;
/*     */     } 
/*     */     
/* 182 */     Set<ConsumeMediaTypeExpression> result = new LinkedHashSet<>(this.expressions);
/* 183 */     result.removeIf(expression -> !expression.match(contentType));
/* 184 */     return !result.isEmpty() ? new ConsumesRequestCondition(result) : null;
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
/*     */   public int compareTo(ConsumesRequestCondition other, HttpServletRequest request) {
/* 200 */     if (this.expressions.isEmpty() && other.expressions.isEmpty()) {
/* 201 */       return 0;
/*     */     }
/* 203 */     if (this.expressions.isEmpty()) {
/* 204 */       return 1;
/*     */     }
/* 206 */     if (other.expressions.isEmpty()) {
/* 207 */       return -1;
/*     */     }
/*     */     
/* 210 */     return ((ConsumeMediaTypeExpression)this.expressions.get(0)).compareTo(other.expressions.get(0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConsumeMediaTypeExpression
/*     */     extends AbstractMediaTypeExpression
/*     */   {
/*     */     ConsumeMediaTypeExpression(String expression) {
/* 221 */       super(expression);
/*     */     }
/*     */     
/*     */     ConsumeMediaTypeExpression(MediaType mediaType, boolean negated) {
/* 225 */       super(mediaType, negated);
/*     */     }
/*     */     
/*     */     public final boolean match(MediaType contentType) {
/* 229 */       boolean match = getMediaType().includes(contentType);
/* 230 */       return !isNegated() ? match : (!match);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/ConsumesRequestCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */