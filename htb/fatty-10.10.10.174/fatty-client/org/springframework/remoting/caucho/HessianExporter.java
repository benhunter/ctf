/*     */ package org.springframework.remoting.caucho;
/*     */ 
/*     */ import com.caucho.hessian.io.AbstractHessianInput;
/*     */ import com.caucho.hessian.io.AbstractHessianOutput;
/*     */ import com.caucho.hessian.io.Hessian2Input;
/*     */ import com.caucho.hessian.io.Hessian2Output;
/*     */ import com.caucho.hessian.io.HessianDebugInputStream;
/*     */ import com.caucho.hessian.io.HessianDebugOutputStream;
/*     */ import com.caucho.hessian.io.HessianInput;
/*     */ import com.caucho.hessian.io.HessianOutput;
/*     */ import com.caucho.hessian.io.HessianRemoteResolver;
/*     */ import com.caucho.hessian.io.SerializerFactory;
/*     */ import com.caucho.hessian.server.HessianSkeleton;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.remoting.support.RemoteExporter;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CommonsLogWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HessianExporter
/*     */   extends RemoteExporter
/*     */   implements InitializingBean
/*     */ {
/*     */   public static final String CONTENT_TYPE_HESSIAN = "application/x-hessian";
/*  65 */   private SerializerFactory serializerFactory = new SerializerFactory();
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private HessianRemoteResolver remoteResolver;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Log debugLogger;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private HessianSkeleton skeleton;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializerFactory(@Nullable SerializerFactory serializerFactory) {
/*  84 */     this.serializerFactory = (serializerFactory != null) ? serializerFactory : new SerializerFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSendCollectionType(boolean sendCollectionType) {
/*  92 */     this.serializerFactory.setSendCollectionType(sendCollectionType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowNonSerializable(boolean allowNonSerializable) {
/* 100 */     this.serializerFactory.setAllowNonSerializable(allowNonSerializable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoteResolver(HessianRemoteResolver remoteResolver) {
/* 108 */     this.remoteResolver = remoteResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 117 */     this.debugLogger = debug ? this.logger : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 123 */     prepare();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare() {
/* 130 */     checkService();
/* 131 */     checkServiceInterface();
/* 132 */     this.skeleton = new HessianSkeleton(getProxyForService(), getServiceInterface());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invoke(InputStream inputStream, OutputStream outputStream) throws Throwable {
/* 143 */     Assert.notNull(this.skeleton, "Hessian exporter has not been initialized");
/* 144 */     doInvoke(this.skeleton, inputStream, outputStream);
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
/*     */   protected void doInvoke(HessianSkeleton skeleton, InputStream inputStream, OutputStream outputStream) throws Throwable {
/* 157 */     ClassLoader originalClassLoader = overrideThreadContextClassLoader(); try {
/*     */       HessianDebugInputStream hessianDebugInputStream; BufferedInputStream bufferedInputStream; HessianDebugOutputStream hessianDebugOutputStream; HessianInput hessianInput; HessianOutput hessianOutput;
/* 159 */       InputStream isToUse = inputStream;
/* 160 */       OutputStream osToUse = outputStream;
/*     */       
/* 162 */       if (this.debugLogger != null && this.debugLogger.isDebugEnabled()) {
/* 163 */         try (PrintWriter debugWriter = new PrintWriter((Writer)new CommonsLogWriter(this.debugLogger))) {
/*     */           
/* 165 */           HessianDebugInputStream dis = new HessianDebugInputStream(inputStream, debugWriter);
/*     */           
/* 167 */           HessianDebugOutputStream dos = new HessianDebugOutputStream(outputStream, debugWriter);
/* 168 */           dis.startTop2();
/* 169 */           dos.startTop2();
/* 170 */           hessianDebugInputStream = dis;
/* 171 */           hessianDebugOutputStream = dos;
/*     */         } 
/*     */       }
/*     */       
/* 175 */       if (!hessianDebugInputStream.markSupported()) {
/* 176 */         bufferedInputStream = new BufferedInputStream((InputStream)hessianDebugInputStream);
/* 177 */         bufferedInputStream.mark(1);
/*     */       } 
/*     */       
/* 180 */       int code = bufferedInputStream.read();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 187 */       if (code == 72) {
/*     */         
/* 189 */         int major = bufferedInputStream.read();
/* 190 */         int minor = bufferedInputStream.read();
/* 191 */         if (major != 2) {
/* 192 */           throw new IOException("Version " + major + '.' + minor + " is not understood");
/*     */         }
/* 194 */         Hessian2Input hessian2Input = new Hessian2Input(bufferedInputStream);
/* 195 */         Hessian2Output hessian2Output = new Hessian2Output((OutputStream)hessianDebugOutputStream);
/* 196 */         hessian2Input.readCall();
/*     */       }
/* 198 */       else if (code == 67) {
/*     */         
/* 200 */         bufferedInputStream.reset();
/* 201 */         Hessian2Input hessian2Input = new Hessian2Input(bufferedInputStream);
/* 202 */         Hessian2Output hessian2Output = new Hessian2Output((OutputStream)hessianDebugOutputStream);
/* 203 */         hessian2Input.readCall();
/*     */       }
/* 205 */       else if (code == 99) {
/*     */         
/* 207 */         int major = bufferedInputStream.read();
/* 208 */         int minor = bufferedInputStream.read();
/* 209 */         hessianInput = new HessianInput(bufferedInputStream);
/* 210 */         if (major >= 2) {
/* 211 */           Hessian2Output hessian2Output = new Hessian2Output((OutputStream)hessianDebugOutputStream);
/*     */         } else {
/*     */           
/* 214 */           hessianOutput = new HessianOutput((OutputStream)hessianDebugOutputStream);
/*     */         } 
/*     */       } else {
/*     */         
/* 218 */         throw new IOException("Expected 'H'/'C' (Hessian 2.0) or 'c' (Hessian 1.0) in hessian input at " + code);
/*     */       } 
/*     */       
/* 221 */       hessianInput.setSerializerFactory(this.serializerFactory);
/* 222 */       hessianOutput.setSerializerFactory(this.serializerFactory);
/* 223 */       if (this.remoteResolver != null) {
/* 224 */         hessianInput.setRemoteResolver(this.remoteResolver);
/*     */       }
/*     */       
/*     */       try {
/* 228 */         skeleton.invoke((AbstractHessianInput)hessianInput, (AbstractHessianOutput)hessianOutput);
/*     */       } finally {
/*     */         
/*     */         try {
/* 232 */           hessianInput.close();
/* 233 */           bufferedInputStream.close();
/*     */         }
/* 235 */         catch (IOException iOException) {}
/*     */ 
/*     */         
/*     */         try {
/* 239 */           hessianOutput.close();
/* 240 */           hessianDebugOutputStream.close();
/*     */         }
/* 242 */         catch (IOException iOException) {}
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 248 */       resetThreadContextClassLoader(originalClassLoader);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/caucho/HessianExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */