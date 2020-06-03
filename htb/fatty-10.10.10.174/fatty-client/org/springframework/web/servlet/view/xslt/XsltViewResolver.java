/*     */ package org.springframework.web.servlet.view.xslt;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*     */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XsltViewResolver
/*     */   extends UrlBasedViewResolver
/*     */ {
/*     */   @Nullable
/*     */   private String sourceKey;
/*     */   @Nullable
/*     */   private URIResolver uriResolver;
/*     */   @Nullable
/*     */   private ErrorListener errorListener;
/*     */   private boolean indent = true;
/*     */   @Nullable
/*     */   private Properties outputProperties;
/*     */   private boolean cacheTemplates = true;
/*     */   
/*     */   public XsltViewResolver() {
/*  56 */     setViewClass(requiredViewClass());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> requiredViewClass() {
/*  62 */     return XsltView.class;
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
/*     */   public void setSourceKey(String sourceKey) {
/*  74 */     this.sourceKey = sourceKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUriResolver(URIResolver uriResolver) {
/*  82 */     this.uriResolver = uriResolver;
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
/*     */   public void setErrorListener(ErrorListener errorListener) {
/*  95 */     this.errorListener = errorListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndent(boolean indent) {
/* 106 */     this.indent = indent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputProperties(Properties outputProperties) {
/* 116 */     this.outputProperties = outputProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheTemplates(boolean cacheTemplates) {
/* 125 */     this.cacheTemplates = cacheTemplates;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractUrlBasedView buildView(String viewName) throws Exception {
/* 131 */     XsltView view = (XsltView)super.buildView(viewName);
/* 132 */     if (this.sourceKey != null) {
/* 133 */       view.setSourceKey(this.sourceKey);
/*     */     }
/* 135 */     if (this.uriResolver != null) {
/* 136 */       view.setUriResolver(this.uriResolver);
/*     */     }
/* 138 */     if (this.errorListener != null) {
/* 139 */       view.setErrorListener(this.errorListener);
/*     */     }
/* 141 */     view.setIndent(this.indent);
/* 142 */     if (this.outputProperties != null) {
/* 143 */       view.setOutputProperties(this.outputProperties);
/*     */     }
/* 145 */     view.setCacheTemplates(this.cacheTemplates);
/* 146 */     return view;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/xslt/XsltViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */