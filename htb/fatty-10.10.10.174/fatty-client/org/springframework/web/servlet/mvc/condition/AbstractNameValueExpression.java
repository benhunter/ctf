/*     */ package org.springframework.web.servlet.mvc.condition;
/*     */ 
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ abstract class AbstractNameValueExpression<T>
/*     */   implements NameValueExpression<T>
/*     */ {
/*     */   protected final String name;
/*     */   @Nullable
/*     */   protected final T value;
/*     */   protected final boolean isNegated;
/*     */   
/*     */   AbstractNameValueExpression(String expression) {
/*  45 */     int separator = expression.indexOf('=');
/*  46 */     if (separator == -1) {
/*  47 */       this.isNegated = expression.startsWith("!");
/*  48 */       this.name = this.isNegated ? expression.substring(1) : expression;
/*  49 */       this.value = null;
/*     */     } else {
/*     */       
/*  52 */       this.isNegated = (separator > 0 && expression.charAt(separator - 1) == '!');
/*  53 */       this.name = this.isNegated ? expression.substring(0, separator - 1) : expression.substring(0, separator);
/*  54 */       this.value = parseValue(expression.substring(separator + 1));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  61 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getValue() {
/*  67 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNegated() {
/*  72 */     return this.isNegated;
/*     */   }
/*     */   
/*     */   public final boolean match(HttpServletRequest request) {
/*     */     boolean isMatch;
/*  77 */     if (this.value != null) {
/*  78 */       isMatch = matchValue(request);
/*     */     } else {
/*     */       
/*  81 */       isMatch = matchName(request);
/*     */     } 
/*  83 */     return this.isNegated ? (!isMatch) : isMatch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract boolean isCaseSensitiveName();
/*     */ 
/*     */   
/*     */   protected abstract T parseValue(String paramString);
/*     */ 
/*     */   
/*     */   protected abstract boolean matchName(HttpServletRequest paramHttpServletRequest);
/*     */   
/*     */   protected abstract boolean matchValue(HttpServletRequest paramHttpServletRequest);
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/*  98 */     if (this == other) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (other == null || getClass() != other.getClass()) {
/* 102 */       return false;
/*     */     }
/* 104 */     AbstractNameValueExpression<?> that = (AbstractNameValueExpression)other;
/* 105 */     return ((isCaseSensitiveName() ? this.name.equals(that.name) : this.name.equalsIgnoreCase(that.name)) && 
/* 106 */       ObjectUtils.nullSafeEquals(this.value, that.value) && this.isNegated == that.isNegated);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 111 */     int result = isCaseSensitiveName() ? this.name.hashCode() : this.name.toLowerCase().hashCode();
/* 112 */     result = 31 * result + ((this.value != null) ? this.value.hashCode() : 0);
/* 113 */     result = 31 * result + (this.isNegated ? 1 : 0);
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     StringBuilder builder = new StringBuilder();
/* 120 */     if (this.value != null) {
/* 121 */       builder.append(this.name);
/* 122 */       if (this.isNegated) {
/* 123 */         builder.append('!');
/*     */       }
/* 125 */       builder.append('=');
/* 126 */       builder.append(this.value);
/*     */     } else {
/*     */       
/* 129 */       if (this.isNegated) {
/* 130 */         builder.append('!');
/*     */       }
/* 132 */       builder.append(this.name);
/*     */     } 
/* 134 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/condition/AbstractNameValueExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */