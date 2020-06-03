/*    */ package org.springframework.jmx.export.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.jmx.export.MBeanExporter;
/*    */ import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
/*    */ import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
/*    */ import org.springframework.jmx.export.naming.MetadataNamingStrategy;
/*    */ import org.springframework.jmx.export.naming.ObjectNamingStrategy;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotationMBeanExporter
/*    */   extends MBeanExporter
/*    */ {
/* 38 */   private final AnnotationJmxAttributeSource annotationSource = new AnnotationJmxAttributeSource();
/*    */ 
/*    */   
/* 41 */   private final MetadataNamingStrategy metadataNamingStrategy = new MetadataNamingStrategy(this.annotationSource);
/*    */ 
/*    */   
/* 44 */   private final MetadataMBeanInfoAssembler metadataAssembler = new MetadataMBeanInfoAssembler(this.annotationSource);
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationMBeanExporter() {
/* 49 */     setNamingStrategy((ObjectNamingStrategy)this.metadataNamingStrategy);
/* 50 */     setAssembler((MBeanInfoAssembler)this.metadataAssembler);
/* 51 */     setAutodetectMode(3);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefaultDomain(String defaultDomain) {
/* 64 */     this.metadataNamingStrategy.setDefaultDomain(defaultDomain);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory) {
/* 69 */     super.setBeanFactory(beanFactory);
/* 70 */     this.annotationSource.setBeanFactory(beanFactory);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/annotation/AnnotationMBeanExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */