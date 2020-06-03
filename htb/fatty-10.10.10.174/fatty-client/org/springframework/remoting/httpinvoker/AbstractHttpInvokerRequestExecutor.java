/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.rmi.RemoteException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.rmi.CodebaseAwareObjectInputStream;
/*     */ import org.springframework.remoting.support.RemoteInvocation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHttpInvokerRequestExecutor
/*     */   implements HttpInvokerRequestExecutor, BeanClassLoaderAware
/*     */ {
/*     */   public static final String CONTENT_TYPE_SERIALIZED_OBJECT = "application/x-java-serialized-object";
/*     */   private static final int SERIALIZED_INVOCATION_BYTE_ARRAY_INITIAL_SIZE = 1024;
/*     */   protected static final String HTTP_METHOD_POST = "POST";
/*     */   protected static final String HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Language";
/*     */   protected static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
/*     */   protected static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";
/*     */   protected static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
/*     */   protected static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
/*     */   protected static final String ENCODING_GZIP = "gzip";
/*  73 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  75 */   private String contentType = "application/x-java-serialized-object";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean acceptGzipEncoding = true;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ClassLoader beanClassLoader;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentType(String contentType) {
/*  88 */     Assert.notNull(contentType, "'contentType' must not be null");
/*  89 */     this.contentType = contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  96 */     return this.contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAcceptGzipEncoding(boolean acceptGzipEncoding) {
/* 106 */     this.acceptGzipEncoding = acceptGzipEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAcceptGzipEncoding() {
/* 114 */     return this.acceptGzipEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader) {
/* 119 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected ClassLoader getBeanClassLoader() {
/* 127 */     return this.beanClassLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final RemoteInvocationResult executeRequest(HttpInvokerClientConfiguration config, RemoteInvocation invocation) throws Exception {
/* 135 */     ByteArrayOutputStream baos = getByteArrayOutputStream(invocation);
/* 136 */     if (this.logger.isDebugEnabled()) {
/* 137 */       this.logger.debug("Sending HTTP invoker request for service at [" + config.getServiceUrl() + "], with size " + baos
/* 138 */           .size());
/*     */     }
/* 140 */     return doExecuteRequest(config, baos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteArrayOutputStream getByteArrayOutputStream(RemoteInvocation invocation) throws IOException {
/* 150 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/* 151 */     writeRemoteInvocation(invocation, baos);
/* 152 */     return baos;
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
/*     */   protected void writeRemoteInvocation(RemoteInvocation invocation, OutputStream os) throws IOException {
/* 169 */     ObjectOutputStream oos = new ObjectOutputStream(decorateOutputStream(os));
/*     */     try {
/* 171 */       doWriteRemoteInvocation(invocation, oos);
/*     */     } finally {
/*     */       
/* 174 */       oos.close();
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
/*     */   protected OutputStream decorateOutputStream(OutputStream os) throws IOException {
/* 187 */     return os;
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
/*     */   protected void doWriteRemoteInvocation(RemoteInvocation invocation, ObjectOutputStream oos) throws IOException {
/* 202 */     oos.writeObject(invocation);
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
/*     */   
/*     */   protected abstract RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration paramHttpInvokerClientConfiguration, ByteArrayOutputStream paramByteArrayOutputStream) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RemoteInvocationResult readRemoteInvocationResult(InputStream is, @Nullable String codebaseUrl) throws IOException, ClassNotFoundException {
/* 243 */     ObjectInputStream ois = createObjectInputStream(decorateInputStream(is), codebaseUrl);
/*     */     try {
/* 245 */       return doReadRemoteInvocationResult(ois);
/*     */     } finally {
/*     */       
/* 248 */       ois.close();
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
/*     */   protected InputStream decorateInputStream(InputStream is) throws IOException {
/* 261 */     return is;
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
/*     */   protected ObjectInputStream createObjectInputStream(InputStream is, @Nullable String codebaseUrl) throws IOException {
/* 275 */     return (ObjectInputStream)new CodebaseAwareObjectInputStream(is, getBeanClassLoader(), codebaseUrl);
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
/*     */   protected RemoteInvocationResult doReadRemoteInvocationResult(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 294 */     Object obj = ois.readObject();
/* 295 */     if (!(obj instanceof RemoteInvocationResult)) {
/* 296 */       throw new RemoteException("Deserialized object needs to be assignable to type [" + RemoteInvocationResult.class
/* 297 */           .getName() + "]: " + ClassUtils.getDescriptiveType(obj));
/*     */     }
/* 299 */     return (RemoteInvocationResult)obj;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/httpinvoker/AbstractHttpInvokerRequestExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */