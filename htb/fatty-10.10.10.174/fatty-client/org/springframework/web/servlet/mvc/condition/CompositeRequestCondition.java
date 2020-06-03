/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class CompositeRequestCondition
/*     */   extends AbstractRequestCondition<CompositeRequestCondition>
/*     */ {
/*     */   private final RequestConditionHolder[] requestConditions;
/*     */   
/*     */   public CompositeRequestCondition(RequestCondition<?>... requestConditions) {
/*  54 */     this.requestConditions = wrap(requestConditions);
/*     */   }
/*     */   
/*     */   private CompositeRequestCondition(RequestConditionHolder[] requestConditions) {
/*  58 */     this.requestConditions = requestConditions;
/*     */   }
/*     */ 
/*     */   
/*     */   private RequestConditionHolder[] wrap(RequestCondition<?>... rawConditions) {
/*  63 */     RequestConditionHolder[] wrappedConditions = new RequestConditionHolder[rawConditions.length];
/*  64 */     for (int i = 0; i < rawConditions.length; i++) {
/*  65 */       wrappedConditions[i] = new RequestConditionHolder(rawConditions[i]);
/*     */     }
/*  67 */     return wrappedConditions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  75 */     return ObjectUtils.isEmpty((Object[])this.requestConditions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<RequestCondition<?>> getConditions() {
/*  82 */     return unwrap();
/*     */   }
/*     */   
/*     */   private List<RequestCondition<?>> unwrap() {
/*  86 */     List<RequestCondition<?>> result = new ArrayList<>();
/*  87 */     for (RequestConditionHolder holder : this.requestConditions) {
/*  88 */       result.add(holder.getCondition());
/*     */     }
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Collection<?> getContent() {
/*  95 */     return !isEmpty() ? getConditions() : Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getToStringInfix() {
/* 100 */     return " && ";
/*     */   }
/*     */   
/*     */   private int getLength() {
/* 104 */     return this.requestConditions.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeRequestCondition combine(CompositeRequestCondition other) {
/* 114 */     if (isEmpty() && other.isEmpty()) {
/* 115 */       return this;
/*     */     }
/* 117 */     if (other.isEmpty()) {
/* 118 */       return this;
/*     */     }
/* 120 */     if (isEmpty()) {
/* 121 */       return other;
/*     */     }
/*     */     
/* 124 */     assertNumberOfConditions(other);
/* 125 */     RequestConditionHolder[] combinedConditions = new RequestConditionHolder[getLength()];
/* 126 */     for (int i = 0; i < getLength(); i++) {
/* 127 */       combinedConditions[i] = this.requestConditions[i].combine(other.requestConditions[i]);
/*     */     }
/* 129 */     return new CompositeRequestCondition(combinedConditions);
/*     */   }
/*     */ 
/*     */   
/*     */   private void assertNumberOfConditions(CompositeRequestCondition other) {
/* 134 */     Assert.isTrue((getLength() == other.getLength()), "Cannot combine CompositeRequestConditions with a different number of conditions. " + 
/*     */         
/* 136 */         ObjectUtils.nullSafeToString((Object[])this.requestConditions) + " and  " + 
/* 137 */         ObjectUtils.nullSafeToString((Object[])other.requestConditions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public CompositeRequestCondition getMatchingCondition(HttpServletRequest request) {
/* 148 */     if (isEmpty()) {
/* 149 */       return this;
/*     */     }
/* 151 */     RequestConditionHolder[] matchingConditions = new RequestConditionHolder[getLength()];
/* 152 */     for (int i = 0; i < getLength(); i++) {
/* 153 */       matchingConditions[i] = this.requestConditions[i].getMatchingCondition(request);
/* 154 */       if (matchingConditions[i] == null) {
/* 155 */         return null;
/*     */       }
/*     */     } 
/* 158 */     return new CompositeRequestCondition(matchingConditions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(CompositeRequestCondition other, HttpServletRequest request) {
/* 167 */     if (isEmpty() && other.isEmpty()) {
/* 168 */       return 0;
/*     */     }
/* 170 */     if (isEmpty()) {
/* 171 */       return 1;
/*     */     }
/* 173 */     if (other.isEmpty()) {
/* 174 */       return -1;
/*     */     }
/*     */     
/* 177 */     assertNumberOfConditions(other);
/* 178 */     for (int i = 0; i < getLength(); i++) {
/* 179 */       int result = this.requestConditions[i].compareTo(other.requestConditions[i], request);
/* 180 */       if (result != 0) {
/* 181 */         return result;
/*     */       }
/*     */     } 
/* 184 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/CompositeRequestCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */