/*     */ package org.springframework.jmx.export.assembler;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.modelmbean.ModelMBeanAttributeInfo;
/*     */ import javax.management.modelmbean.ModelMBeanOperationInfo;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.jmx.support.JmxUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractReflectiveMBeanInfoAssembler
/*     */   extends AbstractMBeanInfoAssembler
/*     */ {
/*     */   protected static final String FIELD_GET_METHOD = "getMethod";
/*     */   protected static final String FIELD_SET_METHOD = "setMethod";
/*     */   protected static final String FIELD_ROLE = "role";
/*     */   protected static final String ROLE_GETTER = "getter";
/*     */   protected static final String ROLE_SETTER = "setter";
/*     */   protected static final String ROLE_OPERATION = "operation";
/*     */   protected static final String FIELD_VISIBILITY = "visibility";
/*     */   protected static final int ATTRIBUTE_OPERATION_VISIBILITY = 4;
/*     */   protected static final String FIELD_CLASS = "class";
/*     */   protected static final String FIELD_LOG = "log";
/*     */   protected static final String FIELD_LOG_FILE = "logFile";
/*     */   protected static final String FIELD_CURRENCY_TIME_LIMIT = "currencyTimeLimit";
/*     */   protected static final String FIELD_DEFAULT = "default";
/*     */   protected static final String FIELD_PERSIST_POLICY = "persistPolicy";
/*     */   protected static final String FIELD_PERSIST_PERIOD = "persistPeriod";
/*     */   protected static final String FIELD_PERSIST_LOCATION = "persistLocation";
/*     */   protected static final String FIELD_PERSIST_NAME = "persistName";
/*     */   protected static final String FIELD_DISPLAY_NAME = "displayName";
/*     */   protected static final String FIELD_UNITS = "units";
/*     */   protected static final String FIELD_METRIC_TYPE = "metricType";
/*     */   protected static final String FIELD_METRIC_CATEGORY = "metricCategory";
/*     */   @Nullable
/*     */   private Integer defaultCurrencyTimeLimit;
/*     */   private boolean useStrictCasing = true;
/*     */   private boolean exposeClassDescriptor = false;
/*     */   @Nullable
/* 185 */   private ParameterNameDiscoverer parameterNameDiscoverer = (ParameterNameDiscoverer)new DefaultParameterNameDiscoverer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCurrencyTimeLimit(@Nullable Integer defaultCurrencyTimeLimit) {
/* 210 */     this.defaultCurrencyTimeLimit = defaultCurrencyTimeLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Integer getDefaultCurrencyTimeLimit() {
/* 218 */     return this.defaultCurrencyTimeLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseStrictCasing(boolean useStrictCasing) {
/* 229 */     this.useStrictCasing = useStrictCasing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isUseStrictCasing() {
/* 236 */     return this.useStrictCasing;
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
/*     */ 
/*     */   
/*     */   public void setExposeClassDescriptor(boolean exposeClassDescriptor) {
/* 256 */     this.exposeClassDescriptor = exposeClassDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isExposeClassDescriptor() {
/* 263 */     return this.exposeClassDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterNameDiscoverer(@Nullable ParameterNameDiscoverer parameterNameDiscoverer) {
/* 272 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ParameterNameDiscoverer getParameterNameDiscoverer() {
/* 281 */     return this.parameterNameDiscoverer;
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
/*     */   protected ModelMBeanAttributeInfo[] getAttributeInfo(Object managedBean, String beanKey) throws JMException {
/* 299 */     PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(getClassToExpose(managedBean));
/* 300 */     List<ModelMBeanAttributeInfo> infos = new ArrayList<>();
/*     */     
/* 302 */     for (PropertyDescriptor prop : props) {
/* 303 */       Method getter = prop.getReadMethod();
/* 304 */       if (getter == null || getter.getDeclaringClass() != Object.class) {
/*     */ 
/*     */         
/* 307 */         if (getter != null && !includeReadAttribute(getter, beanKey)) {
/* 308 */           getter = null;
/*     */         }
/*     */         
/* 311 */         Method setter = prop.getWriteMethod();
/* 312 */         if (setter != null && !includeWriteAttribute(setter, beanKey)) {
/* 313 */           setter = null;
/*     */         }
/*     */         
/* 316 */         if (getter != null || setter != null) {
/*     */           
/* 318 */           String attrName = JmxUtils.getAttributeName(prop, isUseStrictCasing());
/* 319 */           String description = getAttributeDescription(prop, beanKey);
/* 320 */           ModelMBeanAttributeInfo info = new ModelMBeanAttributeInfo(attrName, description, getter, setter);
/*     */           
/* 322 */           Descriptor desc = info.getDescriptor();
/* 323 */           if (getter != null) {
/* 324 */             desc.setField("getMethod", getter.getName());
/*     */           }
/* 326 */           if (setter != null) {
/* 327 */             desc.setField("setMethod", setter.getName());
/*     */           }
/*     */           
/* 330 */           populateAttributeDescriptor(desc, getter, setter, beanKey);
/* 331 */           info.setDescriptor(desc);
/* 332 */           infos.add(info);
/*     */         } 
/*     */       } 
/*     */     } 
/* 336 */     return infos.<ModelMBeanAttributeInfo>toArray(new ModelMBeanAttributeInfo[0]);
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
/*     */   protected ModelMBeanOperationInfo[] getOperationInfo(Object managedBean, String beanKey) {
/* 353 */     Method[] methods = getClassToExpose(managedBean).getMethods();
/* 354 */     List<ModelMBeanOperationInfo> infos = new ArrayList<>();
/*     */     
/* 356 */     for (Method method : methods) {
/* 357 */       if (!method.isSynthetic())
/*     */       {
/*     */         
/* 360 */         if (Object.class != method.getDeclaringClass()) {
/*     */ 
/*     */ 
/*     */           
/* 364 */           ModelMBeanOperationInfo info = null;
/* 365 */           PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 366 */           if (pd != null && ((method.equals(pd.getReadMethod()) && includeReadAttribute(method, beanKey)) || (method
/* 367 */             .equals(pd.getWriteMethod()) && includeWriteAttribute(method, beanKey)))) {
/*     */ 
/*     */             
/* 370 */             info = createModelMBeanOperationInfo(method, pd.getName(), beanKey);
/* 371 */             Descriptor desc = info.getDescriptor();
/* 372 */             if (method.equals(pd.getReadMethod())) {
/* 373 */               desc.setField("role", "getter");
/*     */             } else {
/*     */               
/* 376 */               desc.setField("role", "setter");
/*     */             } 
/* 378 */             desc.setField("visibility", Integer.valueOf(4));
/* 379 */             if (isExposeClassDescriptor()) {
/* 380 */               desc.setField("class", getClassForDescriptor(managedBean).getName());
/*     */             }
/* 382 */             info.setDescriptor(desc);
/*     */           } 
/*     */ 
/*     */           
/* 386 */           if (info == null && includeOperation(method, beanKey)) {
/* 387 */             info = createModelMBeanOperationInfo(method, method.getName(), beanKey);
/* 388 */             Descriptor desc = info.getDescriptor();
/* 389 */             desc.setField("role", "operation");
/* 390 */             if (isExposeClassDescriptor()) {
/* 391 */               desc.setField("class", getClassForDescriptor(managedBean).getName());
/*     */             }
/* 393 */             populateOperationDescriptor(desc, method, beanKey);
/* 394 */             info.setDescriptor(desc);
/*     */           } 
/*     */           
/* 397 */           if (info != null)
/* 398 */             infos.add(info); 
/*     */         } 
/*     */       }
/*     */     } 
/* 402 */     return infos.<ModelMBeanOperationInfo>toArray(new ModelMBeanOperationInfo[0]);
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
/*     */   protected ModelMBeanOperationInfo createModelMBeanOperationInfo(Method method, String name, String beanKey) {
/* 416 */     MBeanParameterInfo[] params = getOperationParameters(method, beanKey);
/* 417 */     if (params.length == 0) {
/* 418 */       return new ModelMBeanOperationInfo(getOperationDescription(method, beanKey), method);
/*     */     }
/*     */     
/* 421 */     return new ModelMBeanOperationInfo(method.getName(), 
/* 422 */         getOperationDescription(method, beanKey), 
/* 423 */         getOperationParameters(method, beanKey), method
/* 424 */         .getReturnType().getName(), 3);
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
/*     */   protected Class<?> getClassForDescriptor(Object managedBean) {
/* 441 */     if (AopUtils.isJdkDynamicProxy(managedBean)) {
/* 442 */       return AopProxyUtils.proxiedUserInterfaces(managedBean)[0];
/*     */     }
/* 444 */     return getClassToExpose(managedBean);
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
/*     */   protected abstract boolean includeReadAttribute(Method paramMethod, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean includeWriteAttribute(Method paramMethod, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean includeOperation(Method paramMethod, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey) {
/* 487 */     return propertyDescriptor.getDisplayName();
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
/*     */   protected String getOperationDescription(Method method, String beanKey) {
/* 500 */     return method.getName();
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
/*     */   protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey) {
/* 512 */     ParameterNameDiscoverer paramNameDiscoverer = getParameterNameDiscoverer();
/* 513 */     String[] paramNames = (paramNameDiscoverer != null) ? paramNameDiscoverer.getParameterNames(method) : null;
/* 514 */     if (paramNames == null) {
/* 515 */       return new MBeanParameterInfo[0];
/*     */     }
/*     */     
/* 518 */     MBeanParameterInfo[] info = new MBeanParameterInfo[paramNames.length];
/* 519 */     Class<?>[] typeParameters = method.getParameterTypes();
/* 520 */     for (int i = 0; i < info.length; i++) {
/* 521 */       info[i] = new MBeanParameterInfo(paramNames[i], typeParameters[i].getName(), paramNames[i]);
/*     */     }
/*     */     
/* 524 */     return info;
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
/*     */   protected void populateMBeanDescriptor(Descriptor descriptor, Object managedBean, String beanKey) {
/* 540 */     applyDefaultCurrencyTimeLimit(descriptor);
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
/*     */   
/*     */   protected void populateAttributeDescriptor(Descriptor desc, @Nullable Method getter, @Nullable Method setter, String beanKey) {
/* 559 */     applyDefaultCurrencyTimeLimit(desc);
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
/*     */   protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey) {
/* 575 */     applyDefaultCurrencyTimeLimit(desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void applyDefaultCurrencyTimeLimit(Descriptor desc) {
/* 585 */     if (getDefaultCurrencyTimeLimit() != null) {
/* 586 */       desc.setField("currencyTimeLimit", getDefaultCurrencyTimeLimit().toString());
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
/*     */ 
/*     */   
/*     */   protected void applyCurrencyTimeLimit(Descriptor desc, int currencyTimeLimit) {
/* 602 */     if (currencyTimeLimit > 0) {
/*     */       
/* 604 */       desc.setField("currencyTimeLimit", Integer.toString(currencyTimeLimit));
/*     */     }
/* 606 */     else if (currencyTimeLimit == 0) {
/*     */       
/* 608 */       desc.setField("currencyTimeLimit", Integer.toString(2147483647));
/*     */     }
/*     */     else {
/*     */       
/* 612 */       applyDefaultCurrencyTimeLimit(desc);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/assembler/AbstractReflectiveMBeanInfoAssembler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */