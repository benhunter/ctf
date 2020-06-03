/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectInstance;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MBeanRegistrationSupport
/*     */ {
/*  74 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected MBeanServer server;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   private final Set<ObjectName> registeredBeans = new LinkedHashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   private RegistrationPolicy registrationPolicy = RegistrationPolicy.FAIL_ON_EXISTING;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServer(@Nullable MBeanServer server) {
/* 100 */     this.server = server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final MBeanServer getServer() {
/* 108 */     return this.server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegistrationPolicy(RegistrationPolicy registrationPolicy) {
/* 118 */     Assert.notNull(registrationPolicy, "RegistrationPolicy must not be null");
/* 119 */     this.registrationPolicy = registrationPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doRegister(Object mbean, ObjectName objectName) throws JMException {
/*     */     ObjectName actualObjectName;
/* 131 */     Assert.state((this.server != null), "No MBeanServer set");
/*     */ 
/*     */     
/* 134 */     synchronized (this.registeredBeans) {
/* 135 */       ObjectInstance registeredBean = null;
/*     */       try {
/* 137 */         registeredBean = this.server.registerMBean(mbean, objectName);
/*     */       }
/* 139 */       catch (InstanceAlreadyExistsException ex) {
/* 140 */         if (this.registrationPolicy == RegistrationPolicy.IGNORE_EXISTING) {
/* 141 */           if (this.logger.isDebugEnabled()) {
/* 142 */             this.logger.debug("Ignoring existing MBean at [" + objectName + "]");
/*     */           }
/*     */         }
/* 145 */         else if (this.registrationPolicy == RegistrationPolicy.REPLACE_EXISTING) {
/*     */           try {
/* 147 */             if (this.logger.isDebugEnabled()) {
/* 148 */               this.logger.debug("Replacing existing MBean at [" + objectName + "]");
/*     */             }
/* 150 */             this.server.unregisterMBean(objectName);
/* 151 */             registeredBean = this.server.registerMBean(mbean, objectName);
/*     */           }
/* 153 */           catch (InstanceNotFoundException ex2) {
/* 154 */             if (this.logger.isInfoEnabled()) {
/* 155 */               this.logger.info("Unable to replace existing MBean at [" + objectName + "]", ex2);
/*     */             }
/* 157 */             throw ex;
/*     */           } 
/*     */         } else {
/*     */           
/* 161 */           throw ex;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 166 */       actualObjectName = (registeredBean != null) ? registeredBean.getObjectName() : null;
/* 167 */       if (actualObjectName == null) {
/* 168 */         actualObjectName = objectName;
/*     */       }
/* 170 */       this.registeredBeans.add(actualObjectName);
/*     */     } 
/*     */     
/* 173 */     onRegister(actualObjectName, mbean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void unregisterBeans() {
/*     */     Set<ObjectName> snapshot;
/* 181 */     synchronized (this.registeredBeans) {
/* 182 */       snapshot = new LinkedHashSet<>(this.registeredBeans);
/*     */     } 
/* 184 */     if (!snapshot.isEmpty()) {
/* 185 */       this.logger.debug("Unregistering JMX-exposed beans");
/* 186 */       for (ObjectName objectName : snapshot) {
/* 187 */         doUnregister(objectName);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doUnregister(ObjectName objectName) {
/* 197 */     Assert.state((this.server != null), "No MBeanServer set");
/* 198 */     boolean actuallyUnregistered = false;
/*     */     
/* 200 */     synchronized (this.registeredBeans) {
/* 201 */       if (this.registeredBeans.remove(objectName)) {
/*     */         
/*     */         try {
/* 204 */           if (this.server.isRegistered(objectName)) {
/* 205 */             this.server.unregisterMBean(objectName);
/* 206 */             actuallyUnregistered = true;
/*     */           
/*     */           }
/* 209 */           else if (this.logger.isInfoEnabled()) {
/* 210 */             this.logger.info("Could not unregister MBean [" + objectName + "] as said MBean is not registered (perhaps already unregistered by an external process)");
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 215 */         catch (JMException ex) {
/* 216 */           if (this.logger.isInfoEnabled()) {
/* 217 */             this.logger.info("Could not unregister MBean [" + objectName + "]", ex);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 223 */     if (actuallyUnregistered) {
/* 224 */       onUnregister(objectName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ObjectName[] getRegisteredObjectNames() {
/* 232 */     synchronized (this.registeredBeans) {
/* 233 */       return this.registeredBeans.<ObjectName>toArray(new ObjectName[0]);
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
/*     */   protected void onRegister(ObjectName objectName, Object mbean) {
/* 246 */     onRegister(objectName);
/*     */   }
/*     */   
/*     */   protected void onRegister(ObjectName objectName) {}
/*     */   
/*     */   protected void onUnregister(ObjectName objectName) {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/support/MBeanRegistrationSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */