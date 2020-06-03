/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.PageContext;
/*     */ import javax.servlet.jsp.el.VariableResolver;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.context.expression.BeanFactoryResolver;
/*     */ import org.springframework.context.expression.EnvironmentAccessor;
/*     */ import org.springframework.context.expression.MapAccessor;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.BeanResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.ExpressionParser;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.standard.SpelExpressionParser;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*     */ import org.springframework.expression.spel.support.StandardTypeConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.util.JavaScriptUtils;
/*     */ import org.springframework.web.util.TagUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EvalTag
/*     */   extends HtmlEscapingAwareTag
/*     */ {
/*     */   private static final String EVALUATION_CONTEXT_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.EVALUATION_CONTEXT";
/* 111 */   private final ExpressionParser expressionParser = (ExpressionParser)new SpelExpressionParser();
/*     */   
/*     */   @Nullable
/*     */   private Expression expression;
/*     */   
/*     */   @Nullable
/*     */   private String var;
/*     */   
/* 119 */   private int scope = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean javaScriptEscape = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpression(String expression) {
/* 128 */     this.expression = this.expressionParser.parseExpression(expression);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVar(String var) {
/* 136 */     this.var = var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScope(String scope) {
/* 144 */     this.scope = TagUtils.getScope(scope);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavaScriptEscape(boolean javaScriptEscape) throws JspException {
/* 152 */     this.javaScriptEscape = javaScriptEscape;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int doStartTagInternal() throws JspException {
/* 158 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int doEndTag() throws JspException {
/* 164 */     EvaluationContext evaluationContext = (EvaluationContext)this.pageContext.getAttribute("org.springframework.web.servlet.tags.EVALUATION_CONTEXT");
/* 165 */     if (evaluationContext == null) {
/* 166 */       evaluationContext = createEvaluationContext(this.pageContext);
/* 167 */       this.pageContext.setAttribute("org.springframework.web.servlet.tags.EVALUATION_CONTEXT", evaluationContext);
/*     */     } 
/* 169 */     if (this.var != null) {
/* 170 */       Object result = (this.expression != null) ? this.expression.getValue(evaluationContext) : null;
/* 171 */       this.pageContext.setAttribute(this.var, result, this.scope);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/* 176 */         String result = (this.expression != null) ? (String)this.expression.getValue(evaluationContext, String.class) : null;
/* 177 */         result = ObjectUtils.getDisplayString(result);
/* 178 */         result = htmlEscape(result);
/* 179 */         result = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(result) : result;
/* 180 */         this.pageContext.getOut().print(result);
/*     */       }
/* 182 */       catch (IOException ex) {
/* 183 */         throw new JspException(ex);
/*     */       } 
/*     */     } 
/* 186 */     return 6;
/*     */   }
/*     */   
/*     */   private EvaluationContext createEvaluationContext(PageContext pageContext) {
/* 190 */     StandardEvaluationContext context = new StandardEvaluationContext();
/* 191 */     context.addPropertyAccessor(new JspPropertyAccessor(pageContext));
/* 192 */     context.addPropertyAccessor((PropertyAccessor)new MapAccessor());
/* 193 */     context.addPropertyAccessor((PropertyAccessor)new EnvironmentAccessor());
/* 194 */     context.setBeanResolver((BeanResolver)new BeanFactoryResolver((BeanFactory)getRequestContext().getWebApplicationContext()));
/* 195 */     ConversionService conversionService = getConversionService(pageContext);
/* 196 */     if (conversionService != null) {
/* 197 */       context.setTypeConverter((TypeConverter)new StandardTypeConverter(conversionService));
/*     */     }
/* 199 */     return (EvaluationContext)context;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private ConversionService getConversionService(PageContext pageContext) {
/* 204 */     return (ConversionService)pageContext.getRequest().getAttribute(ConversionService.class.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JspPropertyAccessor
/*     */     implements PropertyAccessor
/*     */   {
/*     */     private final PageContext pageContext;
/*     */     
/*     */     @Nullable
/*     */     private final VariableResolver variableResolver;
/*     */     
/*     */     public JspPropertyAccessor(PageContext pageContext) {
/* 217 */       this.pageContext = pageContext;
/* 218 */       this.variableResolver = pageContext.getVariableResolver();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?>[] getSpecificTargetClasses() {
/* 224 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 229 */       return (target == null && (
/* 230 */         resolveImplicitVariable(name) != null || this.pageContext.findAttribute(name) != null));
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 235 */       Object implicitVar = resolveImplicitVariable(name);
/* 236 */       if (implicitVar != null) {
/* 237 */         return new TypedValue(implicitVar);
/*     */       }
/* 239 */       return new TypedValue(this.pageContext.findAttribute(name));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) {
/* 244 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) {
/* 249 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private Object resolveImplicitVariable(String name) throws AccessException {
/* 254 */       if (this.variableResolver == null) {
/* 255 */         return null;
/*     */       }
/*     */       try {
/* 258 */         return this.variableResolver.resolveVariable(name);
/*     */       }
/* 260 */       catch (Exception ex) {
/* 261 */         throw new AccessException("Unexpected exception occurred accessing '" + name + "' as an implicit variable", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/EvalTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */