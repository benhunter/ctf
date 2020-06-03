/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.JMX;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerFactory;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ public abstract class JmxUtils
/*     */ {
/*     */   public static final String IDENTITY_OBJECT_NAME_KEY = "identity";
/*     */   private static final String MBEAN_SUFFIX = "MBean";
/*  65 */   private static final Log logger = LogFactory.getLog(JmxUtils.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MBeanServer locateMBeanServer() throws MBeanServerNotFoundException {
/*  77 */     return locateMBeanServer(null);
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
/*     */   public static MBeanServer locateMBeanServer(@Nullable String agentId) throws MBeanServerNotFoundException {
/*  92 */     MBeanServer server = null;
/*     */ 
/*     */     
/*  95 */     if (!"".equals(agentId)) {
/*  96 */       List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(agentId);
/*  97 */       if (!CollectionUtils.isEmpty(servers)) {
/*     */         
/*  99 */         if (servers.size() > 1 && logger.isInfoEnabled()) {
/* 100 */           logger.info("Found more than one MBeanServer instance" + ((agentId != null) ? (" with agent id [" + agentId + "]") : "") + ". Returning first from list.");
/*     */         }
/*     */ 
/*     */         
/* 104 */         server = servers.get(0);
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     if (server == null && !StringUtils.hasLength(agentId)) {
/*     */       
/*     */       try {
/* 111 */         server = ManagementFactory.getPlatformMBeanServer();
/*     */       }
/* 113 */       catch (SecurityException ex) {
/* 114 */         throw new MBeanServerNotFoundException("No specific MBeanServer found, and not allowed to obtain the Java platform MBeanServer", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 119 */     if (server == null) {
/* 120 */       throw new MBeanServerNotFoundException("Unable to locate an MBeanServer instance" + ((agentId != null) ? (" with agent id [" + agentId + "]") : ""));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 125 */     if (logger.isDebugEnabled()) {
/* 126 */       logger.debug("Found MBeanServer: " + server);
/*     */     }
/* 128 */     return server;
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
/*     */   public static Class<?>[] parameterInfoToTypes(@Nullable MBeanParameterInfo[] paramInfo) throws ClassNotFoundException {
/* 142 */     return parameterInfoToTypes(paramInfo, ClassUtils.getDefaultClassLoader());
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
/*     */   public static Class<?>[] parameterInfoToTypes(@Nullable MBeanParameterInfo[] paramInfo, @Nullable ClassLoader classLoader) throws ClassNotFoundException {
/* 158 */     Class<?>[] types = null;
/* 159 */     if (paramInfo != null && paramInfo.length > 0) {
/* 160 */       types = new Class[paramInfo.length];
/* 161 */       for (int x = 0; x < paramInfo.length; x++) {
/* 162 */         types[x] = ClassUtils.forName(paramInfo[x].getType(), classLoader);
/*     */       }
/*     */     } 
/* 165 */     return types;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getMethodSignature(Method method) {
/* 176 */     Class<?>[] types = method.getParameterTypes();
/* 177 */     String[] signature = new String[types.length];
/* 178 */     for (int x = 0; x < types.length; x++) {
/* 179 */       signature[x] = types[x].getName();
/*     */     }
/* 181 */     return signature;
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
/*     */   public static String getAttributeName(PropertyDescriptor property, boolean useStrictCasing) {
/* 195 */     if (useStrictCasing) {
/* 196 */       return StringUtils.capitalize(property.getName());
/*     */     }
/*     */     
/* 199 */     return property.getName();
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
/*     */   
/*     */   public static ObjectName appendIdentityToObjectName(ObjectName objectName, Object managedResource) throws MalformedObjectNameException {
/* 220 */     Hashtable<String, String> keyProperties = objectName.getKeyPropertyList();
/* 221 */     keyProperties.put("identity", ObjectUtils.getIdentityHexString(managedResource));
/* 222 */     return ObjectNameManager.getInstance(objectName.getDomain(), keyProperties);
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
/*     */   public static Class<?> getClassToExpose(Object managedBean) {
/* 236 */     return ClassUtils.getUserClass(managedBean);
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
/*     */   public static Class<?> getClassToExpose(Class<?> clazz) {
/* 250 */     return ClassUtils.getUserClass(clazz);
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
/*     */   public static boolean isMBean(@Nullable Class<?> clazz) {
/* 263 */     return (clazz != null && (DynamicMBean.class
/* 264 */       .isAssignableFrom(clazz) || 
/* 265 */       getMBeanInterface(clazz) != null || getMXBeanInterface(clazz) != null));
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
/*     */   public static Class<?> getMBeanInterface(@Nullable Class<?> clazz) {
/* 277 */     if (clazz == null || clazz.getSuperclass() == null) {
/* 278 */       return null;
/*     */     }
/* 280 */     String mbeanInterfaceName = clazz.getName() + "MBean";
/* 281 */     Class<?>[] implementedInterfaces = clazz.getInterfaces();
/* 282 */     for (Class<?> iface : implementedInterfaces) {
/* 283 */       if (iface.getName().equals(mbeanInterfaceName)) {
/* 284 */         return iface;
/*     */       }
/*     */     } 
/* 287 */     return getMBeanInterface(clazz.getSuperclass());
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
/*     */   public static Class<?> getMXBeanInterface(@Nullable Class<?> clazz) {
/* 299 */     if (clazz == null || clazz.getSuperclass() == null) {
/* 300 */       return null;
/*     */     }
/* 302 */     Class<?>[] implementedInterfaces = clazz.getInterfaces();
/* 303 */     for (Class<?> iface : implementedInterfaces) {
/* 304 */       if (JMX.isMXBeanInterface(iface)) {
/* 305 */         return iface;
/*     */       }
/*     */     } 
/* 308 */     return getMXBeanInterface(clazz.getSuperclass());
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/support/JmxUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */