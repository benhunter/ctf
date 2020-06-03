/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import org.springframework.core.ConfigurableObjectInputStream;
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
/*     */ public class CodebaseAwareObjectInputStream
/*     */   extends ConfigurableObjectInputStream
/*     */ {
/*     */   private final String codebaseUrl;
/*     */   
/*     */   public CodebaseAwareObjectInputStream(InputStream in, String codebaseUrl) throws IOException {
/*  67 */     this(in, (ClassLoader)null, codebaseUrl);
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
/*     */   public CodebaseAwareObjectInputStream(InputStream in, @Nullable ClassLoader classLoader, String codebaseUrl) throws IOException {
/*  82 */     super(in, classLoader);
/*  83 */     this.codebaseUrl = codebaseUrl;
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
/*     */   public CodebaseAwareObjectInputStream(InputStream in, @Nullable ClassLoader classLoader, boolean acceptProxyClasses) throws IOException {
/*  98 */     super(in, classLoader, acceptProxyClasses);
/*  99 */     this.codebaseUrl = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> resolveFallbackIfPossible(String className, ClassNotFoundException ex) throws IOException, ClassNotFoundException {
/* 109 */     if (this.codebaseUrl == null) {
/* 110 */       throw ex;
/*     */     }
/* 112 */     return RMIClassLoader.loadClass(this.codebaseUrl, className);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassLoader getFallbackClassLoader() throws IOException {
/* 117 */     return RMIClassLoader.getClassLoader(this.codebaseUrl);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/remoting/rmi/CodebaseAwareObjectInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */