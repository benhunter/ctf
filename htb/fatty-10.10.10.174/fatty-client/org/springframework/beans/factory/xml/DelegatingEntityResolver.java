/*     */ package org.springframework.beans.factory.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DelegatingEntityResolver
/*     */   implements EntityResolver
/*     */ {
/*     */   public static final String DTD_SUFFIX = ".dtd";
/*     */   public static final String XSD_SUFFIX = ".xsd";
/*     */   private final EntityResolver dtdResolver;
/*     */   private final EntityResolver schemaResolver;
/*     */   
/*     */   public DelegatingEntityResolver(@Nullable ClassLoader classLoader) {
/*  62 */     this.dtdResolver = new BeansDtdResolver();
/*  63 */     this.schemaResolver = new PluggableSchemaResolver(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DelegatingEntityResolver(EntityResolver dtdResolver, EntityResolver schemaResolver) {
/*  73 */     Assert.notNull(dtdResolver, "'dtdResolver' is required");
/*  74 */     Assert.notNull(schemaResolver, "'schemaResolver' is required");
/*  75 */     this.dtdResolver = dtdResolver;
/*  76 */     this.schemaResolver = schemaResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId) throws SAXException, IOException {
/*  85 */     if (systemId != null) {
/*  86 */       if (systemId.endsWith(".dtd")) {
/*  87 */         return this.dtdResolver.resolveEntity(publicId, systemId);
/*     */       }
/*  89 */       if (systemId.endsWith(".xsd")) {
/*  90 */         return this.schemaResolver.resolveEntity(publicId, systemId);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     return "EntityResolver delegating .xsd to " + this.schemaResolver + " and " + ".dtd" + " to " + this.dtdResolver;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/xml/DelegatingEntityResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */