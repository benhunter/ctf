/*     */ package org.springframework.jmx.export;
/*     */ 
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.InvalidAttributeValueException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*     */ import javax.management.modelmbean.ModelMBeanInfo;
/*     */ import javax.management.modelmbean.RequiredModelMBean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpringModelMBean
/*     */   extends RequiredModelMBean
/*     */ {
/*  46 */   private ClassLoader managedResourceClassLoader = Thread.currentThread().getContextClassLoader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpringModelMBean() throws MBeanException, RuntimeOperationsException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpringModelMBean(ModelMBeanInfo mbi) throws MBeanException, RuntimeOperationsException {
/*  62 */     super(mbi);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagedResource(Object managedResource, String managedResourceType) throws MBeanException, InstanceNotFoundException, InvalidTargetObjectTypeException {
/*  73 */     this.managedResourceClassLoader = managedResource.getClass().getClassLoader();
/*  74 */     super.setManagedResource(managedResource, managedResourceType);
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
/*     */   public Object invoke(String opName, Object[] opArgs, String[] sig) throws MBeanException, ReflectionException {
/*  87 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/*  89 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/*  90 */       return super.invoke(opName, opArgs, sig);
/*     */     } finally {
/*     */       
/*  93 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     } 
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
/*     */   public Object getAttribute(String attrName) throws AttributeNotFoundException, MBeanException, ReflectionException {
/* 106 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 108 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/* 109 */       return super.getAttribute(attrName);
/*     */     } finally {
/*     */       
/* 112 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeList getAttributes(String[] attrNames) {
/* 123 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 125 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/* 126 */       return super.getAttributes(attrNames);
/*     */     } finally {
/*     */       
/* 129 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     } 
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
/*     */   public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
/* 142 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 144 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/* 145 */       super.setAttribute(attribute);
/*     */     } finally {
/*     */       
/* 148 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeList setAttributes(AttributeList attributes) {
/* 159 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 161 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/* 162 */       return super.setAttributes(attributes);
/*     */     } finally {
/*     */       
/* 165 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/SpringModelMBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */