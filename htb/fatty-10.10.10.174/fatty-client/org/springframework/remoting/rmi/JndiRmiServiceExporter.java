/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.util.Properties;
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.jndi.JndiTemplate;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiRmiServiceExporter
/*     */   extends RmiBasedExporter
/*     */   implements InitializingBean, DisposableBean
/*     */ {
/*     */   @Nullable
/*     */   private static Method exportObject;
/*     */   @Nullable
/*     */   private static Method unexportObject;
/*     */   
/*     */   static {
/*     */     try {
/*  81 */       Class<?> portableRemoteObject = JndiRmiServiceExporter.class.getClassLoader().loadClass("javax.rmi.PortableRemoteObject");
/*  82 */       exportObject = portableRemoteObject.getMethod("exportObject", new Class[] { Remote.class });
/*  83 */       unexportObject = portableRemoteObject.getMethod("unexportObject", new Class[] { Remote.class });
/*     */     }
/*  85 */     catch (Throwable ex) {
/*     */       
/*  87 */       exportObject = null;
/*  88 */       unexportObject = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  93 */   private JndiTemplate jndiTemplate = new JndiTemplate();
/*     */ 
/*     */ 
/*     */   
/*     */   private String jndiName;
/*     */ 
/*     */ 
/*     */   
/*     */   private Remote exportedObject;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiTemplate(JndiTemplate jndiTemplate) {
/* 106 */     this.jndiTemplate = (jndiTemplate != null) ? jndiTemplate : new JndiTemplate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiEnvironment(Properties jndiEnvironment) {
/* 115 */     this.jndiTemplate = new JndiTemplate(jndiEnvironment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJndiName(String jndiName) {
/* 122 */     this.jndiName = jndiName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException, RemoteException {
/* 128 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() throws NamingException, RemoteException {
/* 137 */     if (this.jndiName == null) {
/* 138 */       throw new IllegalArgumentException("Property 'jndiName' is required");
/*     */     }
/*     */ 
/*     */     
/* 142 */     this.exportedObject = getObjectToExport();
/* 143 */     invokePortableRemoteObject(exportObject);
/*     */     
/* 145 */     rebind();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rebind() throws NamingException {
/* 154 */     if (this.logger.isDebugEnabled()) {
/* 155 */       this.logger.debug("Binding RMI service to JNDI location [" + this.jndiName + "]");
/*     */     }
/* 157 */     this.jndiTemplate.rebind(this.jndiName, this.exportedObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() throws NamingException, RemoteException {
/* 165 */     if (this.logger.isDebugEnabled()) {
/* 166 */       this.logger.debug("Unbinding RMI service from JNDI location [" + this.jndiName + "]");
/*     */     }
/* 168 */     this.jndiTemplate.unbind(this.jndiName);
/* 169 */     invokePortableRemoteObject(unexportObject);
/*     */   }
/*     */ 
/*     */   
/*     */   private void invokePortableRemoteObject(@Nullable Method method) throws RemoteException {
/* 174 */     if (method != null)
/*     */       try {
/* 176 */         method.invoke(null, new Object[] { this.exportedObject });
/*     */       }
/* 178 */       catch (InvocationTargetException ex) {
/* 179 */         Throwable targetEx = ex.getTargetException();
/* 180 */         if (targetEx instanceof RemoteException) {
/* 181 */           throw (RemoteException)targetEx;
/*     */         }
/* 183 */         ReflectionUtils.rethrowRuntimeException(targetEx);
/*     */       }
/* 185 */       catch (Throwable ex) {
/* 186 */         throw new IllegalStateException("PortableRemoteObject invocation failed", ex);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/JndiRmiServiceExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */