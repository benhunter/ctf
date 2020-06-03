/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.core.convert.support.ConversionServiceFactory;
/*     */ import org.springframework.core.convert.support.DefaultConversionService;
/*     */ import org.springframework.core.convert.support.GenericConversionService;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConversionServiceFactoryBean
/*     */   implements FactoryBean<ConversionService>, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private Set<?> converters;
/*     */   @Nullable
/*     */   private GenericConversionService conversionService;
/*     */   
/*     */   public void setConverters(Set<?> converters) {
/*  67 */     this.converters = converters;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  72 */     this.conversionService = createConversionService();
/*  73 */     ConversionServiceFactory.registerConverters(this.converters, (ConverterRegistry)this.conversionService);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected GenericConversionService createConversionService() {
/*  83 */     return (GenericConversionService)new DefaultConversionService();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ConversionService getObject() {
/*  92 */     return (ConversionService)this.conversionService;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends ConversionService> getObjectType() {
/*  97 */     return (Class)GenericConversionService.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 102 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/support/ConversionServiceFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */