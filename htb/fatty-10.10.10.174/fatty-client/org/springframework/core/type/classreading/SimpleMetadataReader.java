/*    */ package org.springframework.core.type.classreading;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.asm.ClassReader;
/*    */ import org.springframework.core.NestedIOException;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.type.AnnotationMetadata;
/*    */ import org.springframework.core.type.ClassMetadata;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class SimpleMetadataReader
/*    */   implements MetadataReader
/*    */ {
/*    */   private final Resource resource;
/*    */   private final ClassMetadata classMetadata;
/*    */   private final AnnotationMetadata annotationMetadata;
/*    */   
/*    */   SimpleMetadataReader(Resource resource, @Nullable ClassLoader classLoader) throws IOException {
/*    */     ClassReader classReader;
/* 51 */     InputStream is = new BufferedInputStream(resource.getInputStream());
/*    */     
/*    */     try {
/* 54 */       classReader = new ClassReader(is);
/*    */     }
/* 56 */     catch (IllegalArgumentException ex) {
/* 57 */       throw new NestedIOException("ASM ClassReader failed to parse class file - probably due to a new Java class file version that isn't supported yet: " + resource, ex);
/*    */     }
/*    */     finally {
/*    */       
/* 61 */       is.close();
/*    */     } 
/*    */     
/* 64 */     AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor(classLoader);
/* 65 */     classReader.accept(visitor, 2);
/*    */     
/* 67 */     this.annotationMetadata = visitor;
/*    */     
/* 69 */     this.classMetadata = visitor;
/* 70 */     this.resource = resource;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Resource getResource() {
/* 76 */     return this.resource;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassMetadata getClassMetadata() {
/* 81 */     return this.classMetadata;
/*    */   }
/*    */ 
/*    */   
/*    */   public AnnotationMetadata getAnnotationMetadata() {
/* 86 */     return this.annotationMetadata;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/SimpleMetadataReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */