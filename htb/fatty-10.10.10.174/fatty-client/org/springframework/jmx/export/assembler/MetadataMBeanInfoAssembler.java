/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jmx.export.metadata.InvalidMetadataException;
/*     */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*     */ import org.springframework.jmx.export.metadata.JmxMetadataUtils;
/*     */ import org.springframework.jmx.export.metadata.ManagedAttribute;
/*     */ import org.springframework.jmx.export.metadata.ManagedMetric;
/*     */ import org.springframework.jmx.export.metadata.ManagedNotification;
/*     */ import org.springframework.jmx.export.metadata.ManagedOperation;
/*     */ import org.springframework.jmx.export.metadata.ManagedOperationParameter;
/*     */ import org.springframework.jmx.export.metadata.ManagedResource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetadataMBeanInfoAssembler
/*     */   extends AbstractReflectiveMBeanInfoAssembler
/*     */   implements AutodetectCapableMBeanInfoAssembler, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private JmxAttributeSource attributeSource;
/*     */   
/*     */   public MetadataMBeanInfoAssembler() {}
/*     */   
/*     */   public MetadataMBeanInfoAssembler(JmxAttributeSource attributeSource) {
/*  78 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  79 */     this.attributeSource = attributeSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributeSource(JmxAttributeSource attributeSource) {
/*  89 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  90 */     this.attributeSource = attributeSource;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  95 */     if (this.attributeSource == null) {
/*  96 */       throw new IllegalArgumentException("Property 'attributeSource' is required");
/*     */     }
/*     */   }
/*     */   
/*     */   private JmxAttributeSource obtainAttributeSource() {
/* 101 */     Assert.state((this.attributeSource != null), "No JmxAttributeSource set");
/* 102 */     return this.attributeSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkManagedBean(Object managedBean) throws IllegalArgumentException {
/* 112 */     if (AopUtils.isJdkDynamicProxy(managedBean)) {
/* 113 */       throw new IllegalArgumentException("MetadataMBeanInfoAssembler does not support JDK dynamic proxies - export the target beans directly or use CGLIB proxies instead");
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
/*     */   
/*     */   public boolean includeBean(Class<?> beanClass, String beanName) {
/* 127 */     return (obtainAttributeSource().getManagedResource(getClassToExpose(beanClass)) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean includeReadAttribute(Method method, String beanKey) {
/* 138 */     return (hasManagedAttribute(method) || hasManagedMetric(method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean includeWriteAttribute(Method method, String beanKey) {
/* 149 */     return hasManagedAttribute(method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean includeOperation(Method method, String beanKey) {
/* 160 */     PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 161 */     return ((pd != null && hasManagedAttribute(method)) || hasManagedOperation(method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasManagedAttribute(Method method) {
/* 168 */     return (obtainAttributeSource().getManagedAttribute(method) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasManagedMetric(Method method) {
/* 175 */     return (obtainAttributeSource().getManagedMetric(method) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasManagedOperation(Method method) {
/* 183 */     return (obtainAttributeSource().getManagedOperation(method) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDescription(Object managedBean, String beanKey) {
/* 193 */     ManagedResource mr = obtainAttributeSource().getManagedResource(getClassToExpose(managedBean));
/* 194 */     return (mr != null) ? mr.getDescription() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey) {
/* 204 */     Method readMethod = propertyDescriptor.getReadMethod();
/* 205 */     Method writeMethod = propertyDescriptor.getWriteMethod();
/*     */ 
/*     */     
/* 208 */     ManagedAttribute getter = (readMethod != null) ? obtainAttributeSource().getManagedAttribute(readMethod) : null;
/*     */     
/* 210 */     ManagedAttribute setter = (writeMethod != null) ? obtainAttributeSource().getManagedAttribute(writeMethod) : null;
/*     */     
/* 212 */     if (getter != null && StringUtils.hasText(getter.getDescription())) {
/* 213 */       return getter.getDescription();
/*     */     }
/* 215 */     if (setter != null && StringUtils.hasText(setter.getDescription())) {
/* 216 */       return setter.getDescription();
/*     */     }
/*     */     
/* 219 */     ManagedMetric metric = (readMethod != null) ? obtainAttributeSource().getManagedMetric(readMethod) : null;
/* 220 */     if (metric != null && StringUtils.hasText(metric.getDescription())) {
/* 221 */       return metric.getDescription();
/*     */     }
/*     */     
/* 224 */     return propertyDescriptor.getDisplayName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getOperationDescription(Method method, String beanKey) {
/* 233 */     PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 234 */     if (pd != null) {
/* 235 */       ManagedAttribute ma = obtainAttributeSource().getManagedAttribute(method);
/* 236 */       if (ma != null && StringUtils.hasText(ma.getDescription())) {
/* 237 */         return ma.getDescription();
/*     */       }
/* 239 */       ManagedMetric metric = obtainAttributeSource().getManagedMetric(method);
/* 240 */       if (metric != null && StringUtils.hasText(metric.getDescription())) {
/* 241 */         return metric.getDescription();
/*     */       }
/* 243 */       return method.getName();
/*     */     } 
/*     */     
/* 246 */     ManagedOperation mo = obtainAttributeSource().getManagedOperation(method);
/* 247 */     if (mo != null && StringUtils.hasText(mo.getDescription())) {
/* 248 */       return mo.getDescription();
/*     */     }
/* 250 */     return method.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey) {
/* 261 */     ManagedOperationParameter[] params = obtainAttributeSource().getManagedOperationParameters(method);
/* 262 */     if (ObjectUtils.isEmpty((Object[])params)) {
/* 263 */       return super.getOperationParameters(method, beanKey);
/*     */     }
/*     */     
/* 266 */     MBeanParameterInfo[] parameterInfo = new MBeanParameterInfo[params.length];
/* 267 */     Class<?>[] methodParameters = method.getParameterTypes();
/* 268 */     for (int i = 0; i < params.length; i++) {
/* 269 */       ManagedOperationParameter param = params[i];
/* 270 */       parameterInfo[i] = new MBeanParameterInfo(param
/* 271 */           .getName(), methodParameters[i].getName(), param.getDescription());
/*     */     } 
/* 273 */     return parameterInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey) {
/* 283 */     ManagedNotification[] notificationAttributes = obtainAttributeSource().getManagedNotifications(getClassToExpose(managedBean));
/* 284 */     ModelMBeanNotificationInfo[] notificationInfos = new ModelMBeanNotificationInfo[notificationAttributes.length];
/*     */ 
/*     */     
/* 287 */     for (int i = 0; i < notificationAttributes.length; i++) {
/* 288 */       ManagedNotification attribute = notificationAttributes[i];
/* 289 */       notificationInfos[i] = JmxMetadataUtils.convertToModelMBeanNotificationInfo(attribute);
/*     */     } 
/*     */     
/* 292 */     return notificationInfos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateMBeanDescriptor(Descriptor desc, Object managedBean, String beanKey) {
/* 303 */     ManagedResource mr = obtainAttributeSource().getManagedResource(getClassToExpose(managedBean));
/* 304 */     if (mr == null) {
/* 305 */       throw new InvalidMetadataException("No ManagedResource attribute found for class: " + 
/* 306 */           getClassToExpose(managedBean));
/*     */     }
/*     */     
/* 309 */     applyCurrencyTimeLimit(desc, mr.getCurrencyTimeLimit());
/*     */     
/* 311 */     if (mr.isLog()) {
/* 312 */       desc.setField("log", "true");
/*     */     }
/* 314 */     if (StringUtils.hasLength(mr.getLogFile())) {
/* 315 */       desc.setField("logFile", mr.getLogFile());
/*     */     }
/*     */     
/* 318 */     if (StringUtils.hasLength(mr.getPersistPolicy())) {
/* 319 */       desc.setField("persistPolicy", mr.getPersistPolicy());
/*     */     }
/* 321 */     if (mr.getPersistPeriod() >= 0) {
/* 322 */       desc.setField("persistPeriod", Integer.toString(mr.getPersistPeriod()));
/*     */     }
/* 324 */     if (StringUtils.hasLength(mr.getPersistName())) {
/* 325 */       desc.setField("persistName", mr.getPersistName());
/*     */     }
/* 327 */     if (StringUtils.hasLength(mr.getPersistLocation())) {
/* 328 */       desc.setField("persistLocation", mr.getPersistLocation());
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
/*     */   protected void populateAttributeDescriptor(Descriptor desc, @Nullable Method getter, @Nullable Method setter, String beanKey) {
/* 340 */     if (getter != null) {
/* 341 */       ManagedMetric metric = obtainAttributeSource().getManagedMetric(getter);
/* 342 */       if (metric != null) {
/* 343 */         populateMetricDescriptor(desc, metric);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 348 */     ManagedAttribute gma = (getter != null) ? obtainAttributeSource().getManagedAttribute(getter) : null;
/* 349 */     ManagedAttribute sma = (setter != null) ? obtainAttributeSource().getManagedAttribute(setter) : null;
/* 350 */     populateAttributeDescriptor(desc, (gma != null) ? gma : ManagedAttribute.EMPTY, (sma != null) ? sma : ManagedAttribute.EMPTY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateAttributeDescriptor(Descriptor desc, ManagedAttribute gma, ManagedAttribute sma) {
/* 356 */     applyCurrencyTimeLimit(desc, resolveIntDescriptor(gma.getCurrencyTimeLimit(), sma.getCurrencyTimeLimit()));
/*     */     
/* 358 */     Object defaultValue = resolveObjectDescriptor(gma.getDefaultValue(), sma.getDefaultValue());
/* 359 */     desc.setField("default", defaultValue);
/*     */     
/* 361 */     String persistPolicy = resolveStringDescriptor(gma.getPersistPolicy(), sma.getPersistPolicy());
/* 362 */     if (StringUtils.hasLength(persistPolicy)) {
/* 363 */       desc.setField("persistPolicy", persistPolicy);
/*     */     }
/* 365 */     int persistPeriod = resolveIntDescriptor(gma.getPersistPeriod(), sma.getPersistPeriod());
/* 366 */     if (persistPeriod >= 0) {
/* 367 */       desc.setField("persistPeriod", Integer.toString(persistPeriod));
/*     */     }
/*     */   }
/*     */   
/*     */   private void populateMetricDescriptor(Descriptor desc, ManagedMetric metric) {
/* 372 */     applyCurrencyTimeLimit(desc, metric.getCurrencyTimeLimit());
/*     */     
/* 374 */     if (StringUtils.hasLength(metric.getPersistPolicy())) {
/* 375 */       desc.setField("persistPolicy", metric.getPersistPolicy());
/*     */     }
/* 377 */     if (metric.getPersistPeriod() >= 0) {
/* 378 */       desc.setField("persistPeriod", Integer.toString(metric.getPersistPeriod()));
/*     */     }
/*     */     
/* 381 */     if (StringUtils.hasLength(metric.getDisplayName())) {
/* 382 */       desc.setField("displayName", metric.getDisplayName());
/*     */     }
/*     */     
/* 385 */     if (StringUtils.hasLength(metric.getUnit())) {
/* 386 */       desc.setField("units", metric.getUnit());
/*     */     }
/*     */     
/* 389 */     if (StringUtils.hasLength(metric.getCategory())) {
/* 390 */       desc.setField("metricCategory", metric.getCategory());
/*     */     }
/*     */     
/* 393 */     desc.setField("metricType", metric.getMetricType().toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey) {
/* 403 */     ManagedOperation mo = obtainAttributeSource().getManagedOperation(method);
/* 404 */     if (mo != null) {
/* 405 */       applyCurrencyTimeLimit(desc, mo.getCurrencyTimeLimit());
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
/*     */   
/*     */   private int resolveIntDescriptor(int getter, int setter) {
/* 419 */     return (getter >= setter) ? getter : setter;
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
/*     */   @Nullable
/*     */   private Object resolveObjectDescriptor(@Nullable Object getter, @Nullable Object setter) {
/* 432 */     return (getter != null) ? getter : setter;
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
/*     */   @Nullable
/*     */   private String resolveStringDescriptor(@Nullable String getter, @Nullable String setter) {
/* 447 */     return StringUtils.hasLength(getter) ? getter : setter;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/assembler/MetadataMBeanInfoAssembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */