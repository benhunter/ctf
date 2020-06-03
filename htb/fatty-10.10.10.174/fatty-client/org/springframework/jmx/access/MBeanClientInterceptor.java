/*     */ package org.springframework.jmx.access;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.IntrospectionException;
/*     */ import javax.management.JMException;
/*     */ import javax.management.JMX;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.MBeanServerConnection;
/*     */ import javax.management.MBeanServerInvocationHandler;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.OperationsException;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeErrorException;
/*     */ import javax.management.RuntimeMBeanException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.TabularData;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.jmx.support.JmxUtils;
/*     */ import org.springframework.jmx.support.ObjectNameManager;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class MBeanClientInterceptor
/*     */   implements MethodInterceptor, BeanClassLoaderAware, InitializingBean, DisposableBean
/*     */ {
/*  96 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   @Nullable
/*     */   private MBeanServerConnection server;
/*     */   
/*     */   @Nullable
/*     */   private JMXServiceURL serviceUrl;
/*     */   
/*     */   @Nullable
/*     */   private Map<String, ?> environment;
/*     */   
/*     */   @Nullable
/*     */   private String agentId;
/*     */   
/*     */   private boolean connectOnStartup = true;
/*     */   
/*     */   private boolean refreshOnConnectFailure = false;
/*     */   
/*     */   @Nullable
/*     */   private ObjectName objectName;
/*     */   
/*     */   private boolean useStrictCasing = true;
/*     */   
/*     */   @Nullable
/*     */   private Class<?> managementInterface;
/*     */   
/*     */   @Nullable
/* 123 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/* 125 */   private final ConnectorDelegate connector = new ConnectorDelegate();
/*     */   
/*     */   @Nullable
/*     */   private MBeanServerConnection serverToUse;
/*     */   
/*     */   @Nullable
/*     */   private MBeanServerInvocationHandler invocationHandler;
/*     */   
/* 133 */   private Map<String, MBeanAttributeInfo> allowedAttributes = Collections.emptyMap();
/*     */   
/* 135 */   private Map<MethodCacheKey, MBeanOperationInfo> allowedOperations = Collections.emptyMap();
/*     */   
/* 137 */   private final Map<Method, String[]> signatureCache = (Map)new HashMap<>();
/*     */   
/* 139 */   private final Object preparationMonitor = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServer(MBeanServerConnection server) {
/* 147 */     this.server = server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceUrl(String url) throws MalformedURLException {
/* 154 */     this.serviceUrl = new JMXServiceURL(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(@Nullable Map<String, ?> environment) {
/* 162 */     this.environment = environment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map<String, ?> getEnvironment() {
/* 174 */     return this.environment;
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
/*     */   public void setAgentId(String agentId) {
/* 186 */     this.agentId = agentId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectOnStartup(boolean connectOnStartup) {
/* 195 */     this.connectOnStartup = connectOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefreshOnConnectFailure(boolean refreshOnConnectFailure) {
/* 205 */     this.refreshOnConnectFailure = refreshOnConnectFailure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectName(Object objectName) throws MalformedObjectNameException {
/* 213 */     this.objectName = ObjectNameManager.getInstance(objectName);
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
/* 224 */     this.useStrictCasing = useStrictCasing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagementInterface(@Nullable Class<?> managementInterface) {
/* 233 */     this.managementInterface = managementInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected final Class<?> getManagementInterface() {
/* 242 */     return this.managementInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 247 */     this.beanClassLoader = beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 257 */     if (this.server != null && this.refreshOnConnectFailure) {
/* 258 */       throw new IllegalArgumentException("'refreshOnConnectFailure' does not work when setting a 'server' reference. Prefer 'serviceUrl' etc instead.");
/*     */     }
/*     */     
/* 261 */     if (this.connectOnStartup) {
/* 262 */       prepare();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 271 */     synchronized (this.preparationMonitor) {
/* 272 */       if (this.server != null) {
/* 273 */         this.serverToUse = this.server;
/*     */       } else {
/*     */         
/* 276 */         this.serverToUse = null;
/* 277 */         this.serverToUse = this.connector.connect(this.serviceUrl, this.environment, this.agentId);
/*     */       } 
/* 279 */       this.invocationHandler = null;
/* 280 */       if (this.useStrictCasing) {
/* 281 */         Assert.state((this.objectName != null), "No ObjectName set");
/*     */         
/* 283 */         this
/* 284 */           .invocationHandler = new MBeanServerInvocationHandler(this.serverToUse, this.objectName, (this.managementInterface != null && JMX.isMXBeanInterface(this.managementInterface)));
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 289 */         retrieveMBeanInfo(this.serverToUse);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void retrieveMBeanInfo(MBeanServerConnection server) throws MBeanInfoRetrievalException {
/*     */     try {
/* 300 */       MBeanInfo info = server.getMBeanInfo(this.objectName);
/*     */       
/* 302 */       MBeanAttributeInfo[] attributeInfo = info.getAttributes();
/* 303 */       this.allowedAttributes = new HashMap<>(attributeInfo.length);
/* 304 */       for (MBeanAttributeInfo infoEle : attributeInfo) {
/* 305 */         this.allowedAttributes.put(infoEle.getName(), infoEle);
/*     */       }
/*     */       
/* 308 */       MBeanOperationInfo[] operationInfo = info.getOperations();
/* 309 */       this.allowedOperations = new HashMap<>(operationInfo.length);
/* 310 */       for (MBeanOperationInfo infoEle : operationInfo) {
/* 311 */         Class<?>[] paramTypes = JmxUtils.parameterInfoToTypes(infoEle.getSignature(), this.beanClassLoader);
/* 312 */         this.allowedOperations.put(new MethodCacheKey(infoEle.getName(), paramTypes), infoEle);
/*     */       }
/*     */     
/* 315 */     } catch (ClassNotFoundException ex) {
/* 316 */       throw new MBeanInfoRetrievalException("Unable to locate class specified in method signature", ex);
/*     */     }
/* 318 */     catch (IntrospectionException ex) {
/* 319 */       throw new MBeanInfoRetrievalException("Unable to obtain MBean info for bean [" + this.objectName + "]", ex);
/*     */     }
/* 321 */     catch (InstanceNotFoundException ex) {
/*     */       
/* 323 */       throw new MBeanInfoRetrievalException("Unable to obtain MBean info for bean [" + this.objectName + "]: it is likely that this bean was unregistered during the proxy creation process", ex);
/*     */ 
/*     */     
/*     */     }
/* 327 */     catch (ReflectionException ex) {
/* 328 */       throw new MBeanInfoRetrievalException("Unable to read MBean info for bean [ " + this.objectName + "]", ex);
/*     */     }
/* 330 */     catch (IOException ex) {
/* 331 */       throw new MBeanInfoRetrievalException("An IOException occurred when communicating with the MBeanServer. It is likely that you are communicating with a remote MBeanServer. Check the inner exception for exact details.", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPrepared() {
/* 342 */     synchronized (this.preparationMonitor) {
/* 343 */       return (this.serverToUse != null);
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
/*     */   @Nullable
/*     */   public Object invoke(MethodInvocation invocation) throws Throwable {
/* 360 */     synchronized (this.preparationMonitor) {
/* 361 */       if (!isPrepared()) {
/* 362 */         prepare();
/*     */       }
/*     */     } 
/*     */     try {
/* 366 */       return doInvoke(invocation);
/*     */     }
/* 368 */     catch (MBeanConnectFailureException|IOException ex) {
/* 369 */       return handleConnectFailure(invocation, ex);
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
/*     */   
/*     */   @Nullable
/*     */   protected Object handleConnectFailure(MethodInvocation invocation, Exception ex) throws Throwable {
/* 387 */     if (this.refreshOnConnectFailure) {
/* 388 */       String msg = "Could not connect to JMX server - retrying";
/* 389 */       if (this.logger.isDebugEnabled()) {
/* 390 */         this.logger.warn(msg, ex);
/*     */       }
/* 392 */       else if (this.logger.isWarnEnabled()) {
/* 393 */         this.logger.warn(msg);
/*     */       } 
/* 395 */       prepare();
/* 396 */       return doInvoke(invocation);
/*     */     } 
/*     */     
/* 399 */     throw ex;
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
/*     */   protected Object doInvoke(MethodInvocation invocation) throws Throwable {
/* 413 */     Method method = invocation.getMethod();
/*     */     try {
/*     */       Object result;
/* 416 */       if (this.invocationHandler != null) {
/* 417 */         result = this.invocationHandler.invoke(invocation.getThis(), method, invocation.getArguments());
/*     */       } else {
/*     */         
/* 420 */         PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
/* 421 */         if (pd != null) {
/* 422 */           result = invokeAttribute(pd, invocation);
/*     */         } else {
/*     */           
/* 425 */           result = invokeOperation(method, invocation.getArguments());
/*     */         } 
/*     */       } 
/* 428 */       return convertResultValueIfNecessary(result, new MethodParameter(method, -1));
/*     */     }
/* 430 */     catch (MBeanException ex) {
/* 431 */       throw ex.getTargetException();
/*     */     }
/* 433 */     catch (RuntimeMBeanException ex) {
/* 434 */       throw ex.getTargetException();
/*     */     }
/* 436 */     catch (RuntimeErrorException ex) {
/* 437 */       throw ex.getTargetError();
/*     */     }
/* 439 */     catch (RuntimeOperationsException ex) {
/*     */       
/* 441 */       RuntimeException rex = ex.getTargetException();
/* 442 */       if (rex instanceof RuntimeMBeanException) {
/* 443 */         throw ((RuntimeMBeanException)rex).getTargetException();
/*     */       }
/* 445 */       if (rex instanceof RuntimeErrorException) {
/* 446 */         throw ((RuntimeErrorException)rex).getTargetError();
/*     */       }
/*     */       
/* 449 */       throw rex;
/*     */     
/*     */     }
/* 452 */     catch (OperationsException ex) {
/* 453 */       if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 454 */         throw ex;
/*     */       }
/*     */       
/* 457 */       throw new InvalidInvocationException(ex.getMessage());
/*     */     
/*     */     }
/* 460 */     catch (JMException ex) {
/* 461 */       if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 462 */         throw ex;
/*     */       }
/*     */       
/* 465 */       throw new InvocationFailureException("JMX access failed", ex);
/*     */     
/*     */     }
/* 468 */     catch (IOException ex) {
/* 469 */       if (ReflectionUtils.declaresException(method, ex.getClass())) {
/* 470 */         throw ex;
/*     */       }
/*     */       
/* 473 */       throw new MBeanConnectFailureException("I/O failure during JMX access", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object invokeAttribute(PropertyDescriptor pd, MethodInvocation invocation) throws JMException, IOException {
/* 482 */     Assert.state((this.serverToUse != null), "No MBeanServerConnection available");
/*     */     
/* 484 */     String attributeName = JmxUtils.getAttributeName(pd, this.useStrictCasing);
/* 485 */     MBeanAttributeInfo inf = this.allowedAttributes.get(attributeName);
/*     */ 
/*     */     
/* 488 */     if (inf == null) {
/* 489 */       throw new InvalidInvocationException("Attribute '" + pd
/* 490 */           .getName() + "' is not exposed on the management interface");
/*     */     }
/*     */     
/* 493 */     if (invocation.getMethod().equals(pd.getReadMethod())) {
/* 494 */       if (inf.isReadable()) {
/* 495 */         return this.serverToUse.getAttribute(this.objectName, attributeName);
/*     */       }
/*     */       
/* 498 */       throw new InvalidInvocationException("Attribute '" + attributeName + "' is not readable");
/*     */     } 
/*     */     
/* 501 */     if (invocation.getMethod().equals(pd.getWriteMethod())) {
/* 502 */       if (inf.isWritable()) {
/* 503 */         this.serverToUse.setAttribute(this.objectName, new Attribute(attributeName, invocation.getArguments()[0]));
/* 504 */         return null;
/*     */       } 
/*     */       
/* 507 */       throw new InvalidInvocationException("Attribute '" + attributeName + "' is not writable");
/*     */     } 
/*     */ 
/*     */     
/* 511 */     throw new IllegalStateException("Method [" + invocation
/* 512 */         .getMethod() + "] is neither a bean property getter nor a setter");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object invokeOperation(Method method, Object[] args) throws JMException, IOException {
/*     */     String[] signature;
/* 524 */     Assert.state((this.serverToUse != null), "No MBeanServerConnection available");
/*     */     
/* 526 */     MethodCacheKey key = new MethodCacheKey(method.getName(), method.getParameterTypes());
/* 527 */     MBeanOperationInfo info = this.allowedOperations.get(key);
/* 528 */     if (info == null) {
/* 529 */       throw new InvalidInvocationException("Operation '" + method.getName() + "' is not exposed on the management interface");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 534 */     synchronized (this.signatureCache) {
/* 535 */       signature = this.signatureCache.get(method);
/* 536 */       if (signature == null) {
/* 537 */         signature = JmxUtils.getMethodSignature(method);
/* 538 */         this.signatureCache.put(method, signature);
/*     */       } 
/*     */     } 
/*     */     
/* 542 */     return this.serverToUse.invoke(this.objectName, method.getName(), args, signature);
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
/*     */   protected Object convertResultValueIfNecessary(@Nullable Object result, MethodParameter parameter) {
/* 555 */     Class<?> targetClass = parameter.getParameterType();
/*     */     try {
/* 557 */       if (result == null) {
/* 558 */         return null;
/*     */       }
/* 560 */       if (ClassUtils.isAssignableValue(targetClass, result)) {
/* 561 */         return result;
/*     */       }
/* 563 */       if (result instanceof CompositeData) {
/* 564 */         Method fromMethod = targetClass.getMethod("from", new Class[] { CompositeData.class });
/* 565 */         return ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { result });
/*     */       } 
/* 567 */       if (result instanceof CompositeData[]) {
/* 568 */         CompositeData[] array = (CompositeData[])result;
/* 569 */         if (targetClass.isArray()) {
/* 570 */           return convertDataArrayToTargetArray((Object[])array, targetClass);
/*     */         }
/* 572 */         if (Collection.class.isAssignableFrom(targetClass)) {
/*     */           
/* 574 */           Class<?> elementType = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0]);
/* 575 */           if (elementType != null) {
/* 576 */             return convertDataArrayToTargetCollection((Object[])array, targetClass, elementType);
/*     */           }
/*     */         } 
/*     */       } else {
/* 580 */         if (result instanceof TabularData) {
/* 581 */           Method fromMethod = targetClass.getMethod("from", new Class[] { TabularData.class });
/* 582 */           return ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { result });
/*     */         } 
/* 584 */         if (result instanceof TabularData[]) {
/* 585 */           TabularData[] array = (TabularData[])result;
/* 586 */           if (targetClass.isArray()) {
/* 587 */             return convertDataArrayToTargetArray((Object[])array, targetClass);
/*     */           }
/* 589 */           if (Collection.class.isAssignableFrom(targetClass)) {
/*     */             
/* 591 */             Class<?> elementType = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0]);
/* 592 */             if (elementType != null)
/* 593 */               return convertDataArrayToTargetCollection((Object[])array, targetClass, elementType); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 597 */       throw new InvocationFailureException("Incompatible result value [" + result + "] for target type [" + targetClass
/* 598 */           .getName() + "]");
/*     */     }
/* 600 */     catch (NoSuchMethodException ex) {
/* 601 */       throw new InvocationFailureException("Could not obtain 'from(CompositeData)' / 'from(TabularData)' method on target type [" + targetClass
/*     */           
/* 603 */           .getName() + "] for conversion of MXBean data structure [" + result + "]");
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object convertDataArrayToTargetArray(Object[] array, Class<?> targetClass) throws NoSuchMethodException {
/* 608 */     Class<?> targetType = targetClass.getComponentType();
/* 609 */     Method fromMethod = targetType.getMethod("from", new Class[] { array.getClass().getComponentType() });
/* 610 */     Object resultArray = Array.newInstance(targetType, array.length);
/* 611 */     for (int i = 0; i < array.length; i++) {
/* 612 */       Array.set(resultArray, i, ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { array[i] }));
/*     */     } 
/* 614 */     return resultArray;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<?> convertDataArrayToTargetCollection(Object[] array, Class<?> collectionType, Class<?> elementType) throws NoSuchMethodException {
/* 620 */     Method fromMethod = elementType.getMethod("from", new Class[] { array.getClass().getComponentType() });
/* 621 */     Collection<Object> resultColl = CollectionFactory.createCollection(collectionType, Array.getLength(array));
/* 622 */     for (int i = 0; i < array.length; i++) {
/* 623 */       resultColl.add(ReflectionUtils.invokeMethod(fromMethod, null, new Object[] { array[i] }));
/*     */     } 
/* 625 */     return resultColl;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 631 */     this.connector.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class MethodCacheKey
/*     */     implements Comparable<MethodCacheKey>
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<?>[] parameterTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MethodCacheKey(String name, @Nullable Class<?>[] parameterTypes) {
/* 652 */       this.name = name;
/* 653 */       this.parameterTypes = (parameterTypes != null) ? parameterTypes : new Class[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 658 */       if (this == other) {
/* 659 */         return true;
/*     */       }
/* 661 */       if (!(other instanceof MethodCacheKey)) {
/* 662 */         return false;
/*     */       }
/* 664 */       MethodCacheKey otherKey = (MethodCacheKey)other;
/* 665 */       return (this.name.equals(otherKey.name) && Arrays.equals((Object[])this.parameterTypes, (Object[])otherKey.parameterTypes));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 670 */       return this.name.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 675 */       return this.name + "(" + StringUtils.arrayToCommaDelimitedString((Object[])this.parameterTypes) + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(MethodCacheKey other) {
/* 680 */       int result = this.name.compareTo(other.name);
/* 681 */       if (result != 0) {
/* 682 */         return result;
/*     */       }
/* 684 */       if (this.parameterTypes.length < other.parameterTypes.length) {
/* 685 */         return -1;
/*     */       }
/* 687 */       if (this.parameterTypes.length > other.parameterTypes.length) {
/* 688 */         return 1;
/*     */       }
/* 690 */       for (int i = 0; i < this.parameterTypes.length; i++) {
/* 691 */         result = this.parameterTypes[i].getName().compareTo(other.parameterTypes[i].getName());
/* 692 */         if (result != 0) {
/* 693 */           return result;
/*     */         }
/*     */       } 
/* 696 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/access/MBeanClientInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */