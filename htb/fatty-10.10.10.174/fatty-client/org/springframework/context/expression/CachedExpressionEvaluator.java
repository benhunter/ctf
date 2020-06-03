/*     */ package org.springframework.context.expression;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.spel.standard.SpelExpressionParser;
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
/*     */ public abstract class CachedExpressionEvaluator
/*     */ {
/*     */   private final SpelExpressionParser parser;
/*  40 */   private final ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CachedExpressionEvaluator(SpelExpressionParser parser) {
/*  47 */     Assert.notNull(parser, "SpelExpressionParser must not be null");
/*  48 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CachedExpressionEvaluator() {
/*  55 */     this(new SpelExpressionParser());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SpelExpressionParser getParser() {
/*  63 */     return this.parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ParameterNameDiscoverer getParameterNameDiscoverer() {
/*  71 */     return this.parameterNameDiscoverer;
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
/*     */   protected Expression getExpression(Map<ExpressionKey, Expression> cache, AnnotatedElementKey elementKey, String expression) {
/*  85 */     ExpressionKey expressionKey = createKey(elementKey, expression);
/*  86 */     Expression expr = cache.get(expressionKey);
/*  87 */     if (expr == null) {
/*  88 */       expr = getParser().parseExpression(expression);
/*  89 */       cache.put(expressionKey, expr);
/*     */     } 
/*  91 */     return expr;
/*     */   }
/*     */   
/*     */   private ExpressionKey createKey(AnnotatedElementKey elementKey, String expression) {
/*  95 */     return new ExpressionKey(elementKey, expression);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class ExpressionKey
/*     */     implements Comparable<ExpressionKey>
/*     */   {
/*     */     private final AnnotatedElementKey element;
/*     */     
/*     */     private final String expression;
/*     */ 
/*     */     
/*     */     protected ExpressionKey(AnnotatedElementKey element, String expression) {
/* 109 */       Assert.notNull(element, "AnnotatedElementKey must not be null");
/* 110 */       Assert.notNull(expression, "Expression must not be null");
/* 111 */       this.element = element;
/* 112 */       this.expression = expression;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 117 */       if (this == other) {
/* 118 */         return true;
/*     */       }
/* 120 */       if (!(other instanceof ExpressionKey)) {
/* 121 */         return false;
/*     */       }
/* 123 */       ExpressionKey otherKey = (ExpressionKey)other;
/* 124 */       return (this.element.equals(otherKey.element) && 
/* 125 */         ObjectUtils.nullSafeEquals(this.expression, otherKey.expression));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 130 */       return this.element.hashCode() * 29 + this.expression.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 135 */       return this.element + " with expression \"" + this.expression + "\"";
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(ExpressionKey other) {
/* 140 */       int result = this.element.toString().compareTo(other.element.toString());
/* 141 */       if (result == 0) {
/* 142 */         result = this.expression.compareTo(other.expression);
/*     */       }
/* 144 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/expression/CachedExpressionEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */