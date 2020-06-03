/*     */ package org.springframework.format.support;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.core.convert.support.ConversionServiceFactory;
/*     */ import org.springframework.format.AnnotationFormatterFactory;
/*     */ import org.springframework.format.Formatter;
/*     */ import org.springframework.format.FormatterRegistrar;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormattingConversionServiceFactoryBean
/*     */   implements FactoryBean<FormattingConversionService>, EmbeddedValueResolverAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Set<?> converters;
/*     */   @Nullable
/*     */   private Set<?> formatters;
/*     */   @Nullable
/*     */   private Set<FormatterRegistrar> formatterRegistrars;
/*     */   private boolean registerDefaultFormatters = true;
/*     */   @Nullable
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   @Nullable
/*     */   private FormattingConversionService conversionService;
/*     */   
/*     */   public void setConverters(Set<?> converters) {
/*  93 */     this.converters = converters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormatters(Set<?> formatters) {
/* 101 */     this.formatters = formatters;
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
/*     */   public void setFormatterRegistrars(Set<FormatterRegistrar> formatterRegistrars) {
/* 119 */     this.formatterRegistrars = formatterRegistrars;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegisterDefaultFormatters(boolean registerDefaultFormatters) {
/* 130 */     this.registerDefaultFormatters = registerDefaultFormatters;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver embeddedValueResolver) {
/* 135 */     this.embeddedValueResolver = embeddedValueResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 141 */     this.conversionService = new DefaultFormattingConversionService(this.embeddedValueResolver, this.registerDefaultFormatters);
/* 142 */     ConversionServiceFactory.registerConverters(this.converters, (ConverterRegistry)this.conversionService);
/* 143 */     registerFormatters(this.conversionService);
/*     */   }
/*     */   
/*     */   private void registerFormatters(FormattingConversionService conversionService) {
/* 147 */     if (this.formatters != null) {
/* 148 */       for (Object formatter : this.formatters) {
/* 149 */         if (formatter instanceof Formatter) {
/* 150 */           conversionService.addFormatter((Formatter)formatter); continue;
/*     */         } 
/* 152 */         if (formatter instanceof AnnotationFormatterFactory) {
/* 153 */           conversionService.addFormatterForFieldAnnotation((AnnotationFormatterFactory<? extends Annotation>)formatter);
/*     */           continue;
/*     */         } 
/* 156 */         throw new IllegalArgumentException("Custom formatters must be implementations of Formatter or AnnotationFormatterFactory");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 161 */     if (this.formatterRegistrars != null) {
/* 162 */       for (FormatterRegistrar registrar : this.formatterRegistrars) {
/* 163 */         registrar.registerFormatters(conversionService);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public FormattingConversionService getObject() {
/* 172 */     return this.conversionService;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends FormattingConversionService> getObjectType() {
/* 177 */     return FormattingConversionService.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 182 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/format/support/FormattingConversionServiceFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */