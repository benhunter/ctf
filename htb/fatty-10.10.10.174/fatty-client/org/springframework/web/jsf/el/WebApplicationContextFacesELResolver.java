/*     */ package org.springframework.web.jsf.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.util.Iterator;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ELResolver;
/*     */ import javax.faces.context.FacesContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.jsf.FacesContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebApplicationContextFacesELResolver
/*     */   extends ELResolver
/*     */ {
/*     */   public static final String WEB_APPLICATION_CONTEXT_VARIABLE_NAME = "webApplicationContext";
/*  66 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue(ELContext elContext, @Nullable Object base, Object property) throws ELException {
/*  72 */     if (base != null) {
/*  73 */       if (base instanceof WebApplicationContext) {
/*  74 */         WebApplicationContext wac = (WebApplicationContext)base;
/*  75 */         String beanName = property.toString();
/*  76 */         if (this.logger.isTraceEnabled()) {
/*  77 */           this.logger.trace("Attempting to resolve property '" + beanName + "' in root WebApplicationContext");
/*     */         }
/*  79 */         if (wac.containsBean(beanName)) {
/*  80 */           if (this.logger.isDebugEnabled()) {
/*  81 */             this.logger.debug("Successfully resolved property '" + beanName + "' in root WebApplicationContext");
/*     */           }
/*  83 */           elContext.setPropertyResolved(true);
/*     */           try {
/*  85 */             return wac.getBean(beanName);
/*     */           }
/*  87 */           catch (BeansException ex) {
/*  88 */             throw new ELException(ex);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/*  93 */         return null;
/*     */       
/*     */       }
/*     */     
/*     */     }
/*  98 */     else if ("webApplicationContext".equals(property)) {
/*  99 */       elContext.setPropertyResolved(true);
/* 100 */       return getWebApplicationContext(elContext);
/*     */     } 
/*     */ 
/*     */     
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getType(ELContext elContext, @Nullable Object base, Object property) throws ELException {
/* 110 */     if (base != null) {
/* 111 */       if (base instanceof WebApplicationContext) {
/* 112 */         WebApplicationContext wac = (WebApplicationContext)base;
/* 113 */         String beanName = property.toString();
/* 114 */         if (this.logger.isDebugEnabled()) {
/* 115 */           this.logger.debug("Attempting to resolve property '" + beanName + "' in root WebApplicationContext");
/*     */         }
/* 117 */         if (wac.containsBean(beanName)) {
/* 118 */           if (this.logger.isDebugEnabled()) {
/* 119 */             this.logger.debug("Successfully resolved property '" + beanName + "' in root WebApplicationContext");
/*     */           }
/* 121 */           elContext.setPropertyResolved(true);
/*     */           try {
/* 123 */             return wac.getType(beanName);
/*     */           }
/* 125 */           catch (BeansException ex) {
/* 126 */             throw new ELException(ex);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 131 */         return null;
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 136 */     else if ("webApplicationContext".equals(property)) {
/* 137 */       elContext.setPropertyResolved(true);
/* 138 */       return WebApplicationContext.class;
/*     */     } 
/*     */ 
/*     */     
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(ELContext elContext, Object base, Object property, Object value) throws ELException {}
/*     */ 
/*     */   
/*     */   public boolean isReadOnly(ELContext elContext, Object base, Object property) throws ELException {
/* 151 */     if (base instanceof WebApplicationContext) {
/* 152 */       elContext.setPropertyResolved(true);
/* 153 */       return true;
/*     */     } 
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext, Object base) {
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext elContext, Object base) {
/* 166 */     return Object.class;
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
/*     */   @Nullable
/*     */   protected WebApplicationContext getWebApplicationContext(ELContext elContext) {
/* 180 */     FacesContext facesContext = FacesContext.getCurrentInstance();
/* 181 */     return FacesContextUtils.getRequiredWebApplicationContext(facesContext);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/jsf/el/WebApplicationContextFacesELResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */