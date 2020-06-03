/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.parsing.Location;
/*    */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*    */ import org.springframework.core.type.MethodMetadata;
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
/*    */ abstract class ConfigurationMethod
/*    */ {
/*    */   protected final MethodMetadata metadata;
/*    */   protected final ConfigurationClass configurationClass;
/*    */   
/*    */   public ConfigurationMethod(MethodMetadata metadata, ConfigurationClass configurationClass) {
/* 37 */     this.metadata = metadata;
/* 38 */     this.configurationClass = configurationClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public MethodMetadata getMetadata() {
/* 43 */     return this.metadata;
/*    */   }
/*    */   
/*    */   public ConfigurationClass getConfigurationClass() {
/* 47 */     return this.configurationClass;
/*    */   }
/*    */   
/*    */   public Location getResourceLocation() {
/* 51 */     return new Location(this.configurationClass.getResource(), this.metadata);
/*    */   }
/*    */   
/*    */   String getFullyQualifiedMethodName() {
/* 55 */     return this.metadata.getDeclaringClassName() + "#" + this.metadata.getMethodName();
/*    */   }
/*    */   
/*    */   static String getShortMethodName(String fullyQualifiedMethodName) {
/* 59 */     return fullyQualifiedMethodName.substring(fullyQualifiedMethodName.indexOf('#') + 1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ProblemReporter problemReporter) {}
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return String.format("[%s:name=%s,declaringClass=%s]", new Object[] {
/* 69 */           getClass().getSimpleName(), getMetadata().getMethodName(), getMetadata().getDeclaringClassName()
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/context/annotation/ConfigurationMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */