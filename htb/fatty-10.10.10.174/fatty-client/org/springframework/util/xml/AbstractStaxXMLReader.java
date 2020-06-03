/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractStaxXMLReader
/*     */   extends AbstractXMLReader
/*     */ {
/*     */   private static final String NAMESPACES_FEATURE_NAME = "http://xml.org/sax/features/namespaces";
/*     */   private static final String NAMESPACE_PREFIXES_FEATURE_NAME = "http://xml.org/sax/features/namespace-prefixes";
/*     */   private static final String IS_STANDALONE_FEATURE_NAME = "http://xml.org/sax/features/is-standalone";
/*     */   private boolean namespacesFeature = true;
/*     */   private boolean namespacePrefixesFeature = false;
/*     */   @Nullable
/*     */   private Boolean isStandalone;
/*  62 */   private final Map<String, String> namespaces = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  67 */     if ("http://xml.org/sax/features/namespaces".equals(name)) {
/*  68 */       return this.namespacesFeature;
/*     */     }
/*  70 */     if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
/*  71 */       return this.namespacePrefixesFeature;
/*     */     }
/*  73 */     if ("http://xml.org/sax/features/is-standalone".equals(name)) {
/*  74 */       if (this.isStandalone != null) {
/*  75 */         return this.isStandalone.booleanValue();
/*     */       }
/*     */       
/*  78 */       throw new SAXNotSupportedException("startDocument() callback not completed yet");
/*     */     } 
/*     */ 
/*     */     
/*  82 */     return super.getFeature(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  88 */     if ("http://xml.org/sax/features/namespaces".equals(name)) {
/*  89 */       this.namespacesFeature = value;
/*     */     }
/*  91 */     else if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
/*  92 */       this.namespacePrefixesFeature = value;
/*     */     } else {
/*     */       
/*  95 */       super.setFeature(name, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setStandalone(boolean standalone) {
/* 100 */     this.isStandalone = Boolean.valueOf(standalone);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasNamespacesFeature() {
/* 107 */     return this.namespacesFeature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasNamespacePrefixesFeature() {
/* 114 */     return this.namespacePrefixesFeature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String toQualifiedName(QName qName) {
/* 125 */     String prefix = qName.getPrefix();
/* 126 */     if (!StringUtils.hasLength(prefix)) {
/* 127 */       return qName.getLocalPart();
/*     */     }
/*     */     
/* 130 */     return prefix + ":" + qName.getLocalPart();
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
/*     */   public final void parse(InputSource ignored) throws SAXException {
/* 143 */     parse();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void parse(String ignored) throws SAXException {
/* 154 */     parse();
/*     */   }
/*     */   
/*     */   private void parse() throws SAXException {
/*     */     try {
/* 159 */       parseInternal();
/*     */     }
/* 161 */     catch (XMLStreamException ex) {
/* 162 */       Locator locator = null;
/* 163 */       if (ex.getLocation() != null) {
/* 164 */         locator = new StaxLocator(ex.getLocation());
/*     */       }
/* 166 */       SAXParseException saxException = new SAXParseException(ex.getMessage(), locator, ex);
/* 167 */       if (getErrorHandler() != null) {
/* 168 */         getErrorHandler().fatalError(saxException);
/*     */       } else {
/*     */         
/* 171 */         throw saxException;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void parseInternal() throws SAXException, XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startPrefixMapping(@Nullable String prefix, String namespace) throws SAXException {
/* 187 */     if (getContentHandler() != null && StringUtils.hasLength(namespace)) {
/* 188 */       if (prefix == null) {
/* 189 */         prefix = "";
/*     */       }
/* 191 */       if (!namespace.equals(this.namespaces.get(prefix))) {
/* 192 */         getContentHandler().startPrefixMapping(prefix, namespace);
/* 193 */         this.namespaces.put(prefix, namespace);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void endPrefixMapping(String prefix) throws SAXException {
/* 203 */     if (getContentHandler() != null && this.namespaces.containsKey(prefix)) {
/* 204 */       getContentHandler().endPrefixMapping(prefix);
/* 205 */       this.namespaces.remove(prefix);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StaxLocator
/*     */     implements Locator
/*     */   {
/*     */     private final Location location;
/*     */ 
/*     */ 
/*     */     
/*     */     public StaxLocator(Location location) {
/* 220 */       this.location = location;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPublicId() {
/* 225 */       return this.location.getPublicId();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSystemId() {
/* 230 */       return this.location.getSystemId();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLineNumber() {
/* 235 */       return this.location.getLineNumber();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnNumber() {
/* 240 */       return this.location.getColumnNumber();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/AbstractStaxXMLReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */