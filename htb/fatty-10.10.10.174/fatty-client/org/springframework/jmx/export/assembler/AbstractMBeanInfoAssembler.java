/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.JMException;
/*     */ import javax.management.modelmbean.ModelMBeanAttributeInfo;
/*     */ import javax.management.modelmbean.ModelMBeanConstructorInfo;
/*     */ import javax.management.modelmbean.ModelMBeanInfo;
/*     */ import javax.management.modelmbean.ModelMBeanInfoSupport;
/*     */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/*     */ import javax.management.modelmbean.ModelMBeanOperationInfo;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.jmx.support.JmxUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMBeanInfoAssembler
/*     */   implements MBeanInfoAssembler
/*     */ {
/*     */   public ModelMBeanInfo getMBeanInfo(Object managedBean, String beanKey) throws JMException {
/*  64 */     checkManagedBean(managedBean);
/*     */ 
/*     */ 
/*     */     
/*  68 */     ModelMBeanInfo info = new ModelMBeanInfoSupport(getClassName(managedBean, beanKey), getDescription(managedBean, beanKey), getAttributeInfo(managedBean, beanKey), getConstructorInfo(managedBean, beanKey), getOperationInfo(managedBean, beanKey), getNotificationInfo(managedBean, beanKey));
/*  69 */     Descriptor desc = info.getMBeanDescriptor();
/*  70 */     populateMBeanDescriptor(desc, managedBean, beanKey);
/*  71 */     info.setMBeanDescriptor(desc);
/*  72 */     return info;
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
/*     */   protected void checkManagedBean(Object managedBean) throws IllegalArgumentException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getTargetClass(Object managedBean) {
/*  95 */     return AopUtils.getTargetClass(managedBean);
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
/*     */   protected Class<?> getClassToExpose(Object managedBean) {
/* 107 */     return JmxUtils.getClassToExpose(managedBean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> getClassToExpose(Class<?> beanClass) {
/* 118 */     return JmxUtils.getClassToExpose(beanClass);
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
/*     */   protected String getClassName(Object managedBean, String beanKey) throws JMException {
/* 132 */     return getTargetClass(managedBean).getName();
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
/*     */   protected String getDescription(Object managedBean, String beanKey) throws JMException {
/* 145 */     String targetClassName = getTargetClass(managedBean).getName();
/* 146 */     if (AopUtils.isAopProxy(managedBean)) {
/* 147 */       return "Proxy for " + targetClassName;
/*     */     }
/* 149 */     return targetClassName;
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
/*     */   protected void populateMBeanDescriptor(Descriptor descriptor, Object managedBean, String beanKey) throws JMException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ModelMBeanConstructorInfo[] getConstructorInfo(Object managedBean, String beanKey) throws JMException {
/* 180 */     return new ModelMBeanConstructorInfo[0];
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
/*     */   protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey) throws JMException {
/* 196 */     return new ModelMBeanNotificationInfo[0];
/*     */   }
/*     */   
/*     */   protected abstract ModelMBeanAttributeInfo[] getAttributeInfo(Object paramObject, String paramString) throws JMException;
/*     */   
/*     */   protected abstract ModelMBeanOperationInfo[] getOperationInfo(Object paramObject, String paramString) throws JMException;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/assembler/AbstractMBeanInfoAssembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */