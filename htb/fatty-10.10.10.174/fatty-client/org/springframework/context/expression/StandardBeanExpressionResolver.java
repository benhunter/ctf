/*     */ package org.springframework.context.expression;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanExpressionException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*     */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.ExpressionParser;
/*     */ import org.springframework.expression.ParserContext;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypeLocator;
/*     */ import org.springframework.expression.spel.SpelParserConfiguration;
/*     */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*     */ import org.springframework.expression.spel.support.StandardTypeConverter;
/*     */ import org.springframework.expression.spel.support.StandardTypeLocator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class StandardBeanExpressionResolver
/*     */   implements BeanExpressionResolver
/*     */ {
/*     */   public static final String DEFAULT_EXPRESSION_PREFIX = "#{";
/*     */   public static final String DEFAULT_EXPRESSION_SUFFIX = "}";
/*  59 */   private String expressionPrefix = "#{";
/*     */   
/*  61 */   private String expressionSuffix = "}";
/*     */   
/*     */   private ExpressionParser expressionParser;
/*     */   
/*  65 */   private final Map<String, Expression> expressionCache = new ConcurrentHashMap<>(256);
/*     */   
/*  67 */   private final Map<BeanExpressionContext, StandardEvaluationContext> evaluationCache = new ConcurrentHashMap<>(8);
/*     */   
/*  69 */   private final ParserContext beanExpressionParserContext = new ParserContext()
/*     */     {
/*     */       public boolean isTemplate() {
/*  72 */         return true;
/*     */       }
/*     */       
/*     */       public String getExpressionPrefix() {
/*  76 */         return StandardBeanExpressionResolver.this.expressionPrefix;
/*     */       }
/*     */       
/*     */       public String getExpressionSuffix() {
/*  80 */         return StandardBeanExpressionResolver.this.expressionSuffix;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardBeanExpressionResolver() {
/*  89 */     this.expressionParser = (ExpressionParser)new SpelExpressionParser();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardBeanExpressionResolver(@Nullable ClassLoader beanClassLoader) {
/*  98 */     this.expressionParser = (ExpressionParser)new SpelExpressionParser(new SpelParserConfiguration(null, beanClassLoader));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpressionPrefix(String expressionPrefix) {
/* 108 */     Assert.hasText(expressionPrefix, "Expression prefix must not be empty");
/* 109 */     this.expressionPrefix = expressionPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpressionSuffix(String expressionSuffix) {
/* 118 */     Assert.hasText(expressionSuffix, "Expression suffix must not be empty");
/* 119 */     this.expressionSuffix = expressionSuffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpressionParser(ExpressionParser expressionParser) {
/* 128 */     Assert.notNull(expressionParser, "ExpressionParser must not be null");
/* 129 */     this.expressionParser = expressionParser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object evaluate(@Nullable String value, BeanExpressionContext evalContext) throws BeansException {
/* 136 */     if (!StringUtils.hasLength(value)) {
/* 137 */       return value;
/*     */     }
/*     */     try {
/* 140 */       Expression expr = this.expressionCache.get(value);
/* 141 */       if (expr == null) {
/* 142 */         expr = this.expressionParser.parseExpression(value, this.beanExpressionParserContext);
/* 143 */         this.expressionCache.put(value, expr);
/*     */       } 
/* 145 */       StandardEvaluationContext sec = this.evaluationCache.get(evalContext);
/* 146 */       if (sec == null) {
/* 147 */         sec = new StandardEvaluationContext(evalContext);
/* 148 */         sec.addPropertyAccessor(new BeanExpressionContextAccessor());
/* 149 */         sec.addPropertyAccessor(new BeanFactoryAccessor());
/* 150 */         sec.addPropertyAccessor((PropertyAccessor)new MapAccessor());
/* 151 */         sec.addPropertyAccessor(new EnvironmentAccessor());
/* 152 */         sec.setBeanResolver(new BeanFactoryResolver((BeanFactory)evalContext.getBeanFactory()));
/* 153 */         sec.setTypeLocator((TypeLocator)new StandardTypeLocator(evalContext.getBeanFactory().getBeanClassLoader()));
/* 154 */         ConversionService conversionService = evalContext.getBeanFactory().getConversionService();
/* 155 */         if (conversionService != null) {
/* 156 */           sec.setTypeConverter((TypeConverter)new StandardTypeConverter(conversionService));
/*     */         }
/* 158 */         customizeEvaluationContext(sec);
/* 159 */         this.evaluationCache.put(evalContext, sec);
/*     */       } 
/* 161 */       return expr.getValue((EvaluationContext)sec);
/*     */     }
/* 163 */     catch (Throwable ex) {
/* 164 */       throw new BeanExpressionException("Expression parsing failed", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void customizeEvaluationContext(StandardEvaluationContext evalContext) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/expression/StandardBeanExpressionResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */