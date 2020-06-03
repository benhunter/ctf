/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ public final class RequestConditionHolder
/*     */   extends AbstractRequestCondition<RequestConditionHolder>
/*     */ {
/*     */   @Nullable
/*     */   private final RequestCondition<Object> condition;
/*     */   
/*     */   public RequestConditionHolder(@Nullable RequestCondition<?> requestCondition) {
/*  51 */     this.condition = (RequestCondition)requestCondition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RequestCondition<?> getCondition() {
/*  60 */     return this.condition;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Collection<?> getContent() {
/*  65 */     return (this.condition != null) ? Collections.singleton(this.condition) : Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getToStringInfix() {
/*  70 */     return " ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestConditionHolder combine(RequestConditionHolder other) {
/*  80 */     if (this.condition == null && other.condition == null) {
/*  81 */       return this;
/*     */     }
/*  83 */     if (this.condition == null) {
/*  84 */       return other;
/*     */     }
/*  86 */     if (other.condition == null) {
/*  87 */       return this;
/*     */     }
/*     */     
/*  90 */     assertEqualConditionTypes(this.condition, other.condition);
/*  91 */     RequestCondition<?> combined = (RequestCondition)this.condition.combine(other.condition);
/*  92 */     return new RequestConditionHolder(combined);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertEqualConditionTypes(RequestCondition<?> thisCondition, RequestCondition<?> otherCondition) {
/* 100 */     Class<?> clazz = thisCondition.getClass();
/* 101 */     Class<?> otherClazz = otherCondition.getClass();
/* 102 */     if (!clazz.equals(otherClazz)) {
/* 103 */       throw new ClassCastException("Incompatible request conditions: " + clazz + " and " + otherClazz);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public RequestConditionHolder getMatchingCondition(HttpServletRequest request) {
/* 115 */     if (this.condition == null) {
/* 116 */       return this;
/*     */     }
/* 118 */     RequestCondition<?> match = (RequestCondition)this.condition.getMatchingCondition(request);
/* 119 */     return (match != null) ? new RequestConditionHolder(match) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(RequestConditionHolder other, HttpServletRequest request) {
/* 129 */     if (this.condition == null && other.condition == null) {
/* 130 */       return 0;
/*     */     }
/* 132 */     if (this.condition == null) {
/* 133 */       return 1;
/*     */     }
/* 135 */     if (other.condition == null) {
/* 136 */       return -1;
/*     */     }
/*     */     
/* 139 */     assertEqualConditionTypes(this.condition, other.condition);
/* 140 */     return this.condition.compareTo(other.condition, request);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/RequestConditionHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */