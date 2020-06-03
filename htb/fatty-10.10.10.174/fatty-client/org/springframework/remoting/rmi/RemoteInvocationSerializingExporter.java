/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.rmi.RemoteException;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
/*     */ import org.springframework.remoting.support.RemoteInvocationBasedExporter;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RemoteInvocationSerializingExporter
/*     */   extends RemoteInvocationBasedExporter
/*     */   implements InitializingBean
/*     */ {
/*     */   public static final String CONTENT_TYPE_SERIALIZED_OBJECT = "application/x-java-serialized-object";
/*  58 */   private String contentType = "application/x-java-serialized-object";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean acceptProxyClasses = true;
/*     */ 
/*     */   
/*     */   private Object proxy;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentType(String contentType) {
/*  70 */     Assert.notNull(contentType, "'contentType' must not be null");
/*  71 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  78 */     return this.contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAcceptProxyClasses(boolean acceptProxyClasses) {
/*  86 */     this.acceptProxyClasses = acceptProxyClasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAcceptProxyClasses() {
/*  93 */     return this.acceptProxyClasses;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/*  99 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 106 */     this.proxy = getProxyForService();
/*     */   }
/*     */   
/*     */   protected final Object getProxy() {
/* 110 */     if (this.proxy == null) {
/* 111 */       throw new IllegalStateException(ClassUtils.getShortName(getClass()) + " has not been initialized");
/*     */     }
/* 113 */     return this.proxy;
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
/*     */   protected ObjectInputStream createObjectInputStream(InputStream is) throws IOException {
/* 125 */     return (ObjectInputStream)new CodebaseAwareObjectInputStream(is, getBeanClassLoader(), isAcceptProxyClasses());
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
/*     */   protected RemoteInvocation doReadRemoteInvocation(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 144 */     Object obj = ois.readObject();
/* 145 */     if (!(obj instanceof RemoteInvocation)) {
/* 146 */       throw new RemoteException("Deserialized object needs to be assignable to type [" + RemoteInvocation.class
/* 147 */           .getName() + "]: " + ClassUtils.getDescriptiveType(obj));
/*     */     }
/* 149 */     return (RemoteInvocation)obj;
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
/*     */   protected ObjectOutputStream createObjectOutputStream(OutputStream os) throws IOException {
/* 161 */     return new ObjectOutputStream(os);
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
/*     */   protected void doWriteRemoteInvocationResult(RemoteInvocationResult result, ObjectOutputStream oos) throws IOException {
/* 178 */     oos.writeObject(result);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/RemoteInvocationSerializingExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */