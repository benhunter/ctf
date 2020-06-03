/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ abstract class AbstractMediaTypeExpression
/*     */   implements MediaTypeExpression, Comparable<AbstractMediaTypeExpression>
/*     */ {
/*  36 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final MediaType mediaType;
/*     */   
/*     */   private final boolean isNegated;
/*     */ 
/*     */   
/*     */   AbstractMediaTypeExpression(String expression) {
/*  44 */     if (expression.startsWith("!")) {
/*  45 */       this.isNegated = true;
/*  46 */       expression = expression.substring(1);
/*     */     } else {
/*     */       
/*  49 */       this.isNegated = false;
/*     */     } 
/*  51 */     this.mediaType = MediaType.parseMediaType(expression);
/*     */   }
/*     */   
/*     */   AbstractMediaTypeExpression(MediaType mediaType, boolean negated) {
/*  55 */     this.mediaType = mediaType;
/*  56 */     this.isNegated = negated;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType getMediaType() {
/*  62 */     return this.mediaType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNegated() {
/*  67 */     return this.isNegated;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(AbstractMediaTypeExpression other) {
/*  73 */     return MediaType.SPECIFICITY_COMPARATOR.compare(getMediaType(), other.getMediaType());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/*  78 */     if (this == other) {
/*  79 */       return true;
/*     */     }
/*  81 */     if (other == null || getClass() != other.getClass()) {
/*  82 */       return false;
/*     */     }
/*  84 */     AbstractMediaTypeExpression otherExpr = (AbstractMediaTypeExpression)other;
/*  85 */     return (this.mediaType.equals(otherExpr.mediaType) && this.isNegated == otherExpr.isNegated);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  90 */     return this.mediaType.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     StringBuilder builder = new StringBuilder();
/*  96 */     if (this.isNegated) {
/*  97 */       builder.append('!');
/*     */     }
/*  99 */     builder.append(this.mediaType.toString());
/* 100 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/AbstractMediaTypeExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */