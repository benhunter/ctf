/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.web.HttpMediaTypeException;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
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
/*     */ public final class ProducesRequestCondition
/*     */   extends AbstractRequestCondition<ProducesRequestCondition>
/*     */ {
/*  50 */   private static final ProducesRequestCondition EMPTY_CONDITION = new ProducesRequestCondition(new String[0]);
/*     */ 
/*     */   
/*  53 */   private static final List<ProduceMediaTypeExpression> MEDIA_TYPE_ALL_LIST = Collections.singletonList(new ProduceMediaTypeExpression("*/*"));
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<ProduceMediaTypeExpression> expressions;
/*     */ 
/*     */ 
/*     */   
/*     */   private final ContentNegotiationManager contentNegotiationManager;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProducesRequestCondition(String... produces) {
/*  67 */     this(produces, null, null);
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
/*     */   public ProducesRequestCondition(String[] produces, @Nullable String[] headers) {
/*  79 */     this(produces, headers, null);
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
/*     */   public ProducesRequestCondition(String[] produces, @Nullable String[] headers, @Nullable ContentNegotiationManager manager) {
/*  92 */     this.expressions = new ArrayList<>(parseExpressions(produces, headers));
/*  93 */     Collections.sort(this.expressions);
/*  94 */     this.contentNegotiationManager = (manager != null) ? manager : new ContentNegotiationManager();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProducesRequestCondition(Collection<ProduceMediaTypeExpression> expressions, @Nullable ContentNegotiationManager manager) {
/* 103 */     this.expressions = new ArrayList<>(expressions);
/* 104 */     Collections.sort(this.expressions);
/* 105 */     this.contentNegotiationManager = (manager != null) ? manager : new ContentNegotiationManager();
/*     */   }
/*     */ 
/*     */   
/*     */   private Set<ProduceMediaTypeExpression> parseExpressions(String[] produces, @Nullable String[] headers) {
/* 110 */     Set<ProduceMediaTypeExpression> result = new LinkedHashSet<>();
/* 111 */     if (headers != null) {
/* 112 */       for (String header : headers) {
/* 113 */         HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
/* 114 */         if ("Accept".equalsIgnoreCase(expr.name) && expr.value != null) {
/* 115 */           for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
/* 116 */             result.add(new ProduceMediaTypeExpression(mediaType, expr.isNegated));
/*     */           }
/*     */         }
/*     */       } 
/*     */     }
/* 121 */     for (String produce : produces) {
/* 122 */       result.add(new ProduceMediaTypeExpression(produce));
/*     */     }
/* 124 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<MediaTypeExpression> getExpressions() {
/* 131 */     return new LinkedHashSet<>((Collection)this.expressions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<MediaType> getProducibleMediaTypes() {
/* 138 */     Set<MediaType> result = new LinkedHashSet<>();
/* 139 */     for (ProduceMediaTypeExpression expression : this.expressions) {
/* 140 */       if (!expression.isNegated()) {
/* 141 */         result.add(expression.getMediaType());
/*     */       }
/*     */     } 
/* 144 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 152 */     return this.expressions.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ProduceMediaTypeExpression> getContent() {
/* 157 */     return this.expressions;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getToStringInfix() {
/* 162 */     return " || ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProducesRequestCondition combine(ProducesRequestCondition other) {
/* 172 */     return !other.expressions.isEmpty() ? other : this;
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
/*     */   public ProducesRequestCondition getMatchingCondition(HttpServletRequest request) {
/*     */     List<MediaType> acceptedMediaTypes;
/* 188 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 189 */       return EMPTY_CONDITION;
/*     */     }
/* 191 */     if (isEmpty()) {
/* 192 */       return this;
/*     */     }
/*     */     
/*     */     try {
/* 196 */       acceptedMediaTypes = getAcceptedMediaTypes(request);
/*     */     }
/* 198 */     catch (HttpMediaTypeException ex) {
/* 199 */       return null;
/*     */     } 
/* 201 */     Set<ProduceMediaTypeExpression> result = new LinkedHashSet<>(this.expressions);
/* 202 */     result.removeIf(expression -> !expression.match(acceptedMediaTypes));
/* 203 */     if (!result.isEmpty()) {
/* 204 */       return new ProducesRequestCondition(result, this.contentNegotiationManager);
/*     */     }
/* 206 */     if (MediaType.ALL.isPresentIn(acceptedMediaTypes)) {
/* 207 */       return EMPTY_CONDITION;
/*     */     }
/*     */     
/* 210 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(ProducesRequestCondition other, HttpServletRequest request) {
/*     */     try {
/* 234 */       List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(request);
/* 235 */       for (MediaType acceptedMediaType : acceptedMediaTypes) {
/* 236 */         int thisIndex = indexOfEqualMediaType(acceptedMediaType);
/* 237 */         int otherIndex = other.indexOfEqualMediaType(acceptedMediaType);
/* 238 */         int result = compareMatchingMediaTypes(this, thisIndex, other, otherIndex);
/* 239 */         if (result != 0) {
/* 240 */           return result;
/*     */         }
/* 242 */         thisIndex = indexOfIncludedMediaType(acceptedMediaType);
/* 243 */         otherIndex = other.indexOfIncludedMediaType(acceptedMediaType);
/* 244 */         result = compareMatchingMediaTypes(this, thisIndex, other, otherIndex);
/* 245 */         if (result != 0) {
/* 246 */           return result;
/*     */         }
/*     */       } 
/* 249 */       return 0;
/*     */     }
/* 251 */     catch (HttpMediaTypeNotAcceptableException ex) {
/*     */       
/* 253 */       throw new IllegalStateException("Cannot compare without having any requested media types", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) throws HttpMediaTypeNotAcceptableException {
/* 258 */     return this.contentNegotiationManager.resolveMediaTypes((NativeWebRequest)new ServletWebRequest(request));
/*     */   }
/*     */   
/*     */   private int indexOfEqualMediaType(MediaType mediaType) {
/* 262 */     for (int i = 0; i < getExpressionsToCompare().size(); i++) {
/* 263 */       MediaType currentMediaType = ((ProduceMediaTypeExpression)getExpressionsToCompare().get(i)).getMediaType();
/* 264 */       if (mediaType.getType().equalsIgnoreCase(currentMediaType.getType()) && mediaType
/* 265 */         .getSubtype().equalsIgnoreCase(currentMediaType.getSubtype())) {
/* 266 */         return i;
/*     */       }
/*     */     } 
/* 269 */     return -1;
/*     */   }
/*     */   
/*     */   private int indexOfIncludedMediaType(MediaType mediaType) {
/* 273 */     for (int i = 0; i < getExpressionsToCompare().size(); i++) {
/* 274 */       if (mediaType.includes(((ProduceMediaTypeExpression)getExpressionsToCompare().get(i)).getMediaType())) {
/* 275 */         return i;
/*     */       }
/*     */     } 
/* 278 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int compareMatchingMediaTypes(ProducesRequestCondition condition1, int index1, ProducesRequestCondition condition2, int index2) {
/* 284 */     int result = 0;
/* 285 */     if (index1 != index2) {
/* 286 */       result = index2 - index1;
/*     */     }
/* 288 */     else if (index1 != -1) {
/* 289 */       ProduceMediaTypeExpression expr1 = condition1.getExpressionsToCompare().get(index1);
/* 290 */       ProduceMediaTypeExpression expr2 = condition2.getExpressionsToCompare().get(index2);
/* 291 */       result = expr1.compareTo(expr2);
/* 292 */       result = (result != 0) ? result : expr1.getMediaType().compareTo((MimeType)expr2.getMediaType());
/*     */     } 
/* 294 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<ProduceMediaTypeExpression> getExpressionsToCompare() {
/* 302 */     return this.expressions.isEmpty() ? MEDIA_TYPE_ALL_LIST : this.expressions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ProduceMediaTypeExpression
/*     */     extends AbstractMediaTypeExpression
/*     */   {
/*     */     ProduceMediaTypeExpression(MediaType mediaType, boolean negated) {
/* 312 */       super(mediaType, negated);
/*     */     }
/*     */     
/*     */     ProduceMediaTypeExpression(String expression) {
/* 316 */       super(expression);
/*     */     }
/*     */     
/*     */     public final boolean match(List<MediaType> acceptedMediaTypes) {
/* 320 */       boolean match = matchMediaType(acceptedMediaTypes);
/* 321 */       return !isNegated() ? match : (!match);
/*     */     }
/*     */     
/*     */     private boolean matchMediaType(List<MediaType> acceptedMediaTypes) {
/* 325 */       for (MediaType acceptedMediaType : acceptedMediaTypes) {
/* 326 */         if (getMediaType().isCompatibleWith(acceptedMediaType)) {
/* 327 */           return true;
/*     */         }
/*     */       } 
/* 330 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/ProducesRequestCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */