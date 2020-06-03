/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.AnnotationVisitor;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.FieldVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.type.ClassMetadata;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ class ClassMetadataReadingVisitor
/*     */   extends ClassVisitor
/*     */   implements ClassMetadata
/*     */ {
/*  48 */   private String className = "";
/*     */   
/*     */   private boolean isInterface;
/*     */   
/*     */   private boolean isAnnotation;
/*     */   
/*     */   private boolean isAbstract;
/*     */   
/*     */   private boolean isFinal;
/*     */   
/*     */   @Nullable
/*     */   private String enclosingClassName;
/*     */   
/*     */   private boolean independentInnerClass;
/*     */   
/*     */   @Nullable
/*     */   private String superClassName;
/*     */   
/*  66 */   private String[] interfaces = new String[0];
/*     */   
/*  68 */   private Set<String> memberClassNames = new LinkedHashSet<>(4);
/*     */ 
/*     */   
/*     */   public ClassMetadataReadingVisitor() {
/*  72 */     super(458752);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, @Nullable String supername, String[] interfaces) {
/*  80 */     this.className = ClassUtils.convertResourcePathToClassName(name);
/*  81 */     this.isInterface = ((access & 0x200) != 0);
/*  82 */     this.isAnnotation = ((access & 0x2000) != 0);
/*  83 */     this.isAbstract = ((access & 0x400) != 0);
/*  84 */     this.isFinal = ((access & 0x10) != 0);
/*  85 */     if (supername != null && !this.isInterface) {
/*  86 */       this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
/*     */     }
/*  88 */     this.interfaces = new String[interfaces.length];
/*  89 */     for (int i = 0; i < interfaces.length; i++) {
/*  90 */       this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitOuterClass(String owner, String name, String desc) {
/*  96 */     this.enclosingClassName = ClassUtils.convertResourcePathToClassName(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInnerClass(String name, @Nullable String outerName, String innerName, int access) {
/* 101 */     if (outerName != null) {
/* 102 */       String fqName = ClassUtils.convertResourcePathToClassName(name);
/* 103 */       String fqOuterName = ClassUtils.convertResourcePathToClassName(outerName);
/* 104 */       if (this.className.equals(fqName)) {
/* 105 */         this.enclosingClassName = fqOuterName;
/* 106 */         this.independentInnerClass = ((access & 0x8) != 0);
/*     */       }
/* 108 */       else if (this.className.equals(fqOuterName)) {
/* 109 */         this.memberClassNames.add(fqName);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitSource(String source, String debug) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
/* 122 */     return new EmptyAnnotationVisitor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitAttribute(Attribute attr) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 133 */     return new EmptyFieldVisitor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 139 */     return new EmptyMethodVisitor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visitEnd() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 150 */     return this.className;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/* 155 */     return this.isInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotation() {
/* 160 */     return this.isAnnotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 165 */     return this.isAbstract;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConcrete() {
/* 170 */     return (!this.isInterface && !this.isAbstract);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 175 */     return this.isFinal;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIndependent() {
/* 180 */     return (this.enclosingClassName == null || this.independentInnerClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEnclosingClass() {
/* 185 */     return (this.enclosingClassName != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEnclosingClassName() {
/* 191 */     return this.enclosingClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSuperClass() {
/* 196 */     return (this.superClassName != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSuperClassName() {
/* 202 */     return this.superClassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getInterfaceNames() {
/* 207 */     return this.interfaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getMemberClassNames() {
/* 212 */     return StringUtils.toStringArray(this.memberClassNames);
/*     */   }
/*     */   
/*     */   private static class EmptyAnnotationVisitor
/*     */     extends AnnotationVisitor
/*     */   {
/*     */     public EmptyAnnotationVisitor() {
/* 219 */       super(458752);
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitAnnotation(String name, String desc) {
/* 224 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public AnnotationVisitor visitArray(String name) {
/* 229 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyMethodVisitor
/*     */     extends MethodVisitor
/*     */   {
/*     */     public EmptyMethodVisitor() {
/* 237 */       super(458752);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyFieldVisitor
/*     */     extends FieldVisitor
/*     */   {
/*     */     public EmptyFieldVisitor() {
/* 245 */       super(458752);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/type/classreading/ClassMetadataReadingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */