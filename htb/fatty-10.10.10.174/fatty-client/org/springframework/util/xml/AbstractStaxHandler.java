/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractStaxHandler
/*     */   implements ContentHandler, LexicalHandler
/*     */ {
/*  45 */   private final List<Map<String, String>> namespaceMappings = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private boolean inCData;
/*     */ 
/*     */   
/*     */   public final void startDocument() throws SAXException {
/*  52 */     removeAllNamespaceMappings();
/*  53 */     newNamespaceMapping();
/*     */     try {
/*  55 */       startDocumentInternal();
/*     */     }
/*  57 */     catch (XMLStreamException ex) {
/*  58 */       throw new SAXException("Could not handle startDocument: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void endDocument() throws SAXException {
/*  64 */     removeAllNamespaceMappings();
/*     */     try {
/*  66 */       endDocumentInternal();
/*     */     }
/*  68 */     catch (XMLStreamException ex) {
/*  69 */       throw new SAXException("Could not handle endDocument: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startPrefixMapping(String prefix, String uri) {
/*  75 */     currentNamespaceMapping().put(prefix, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void endPrefixMapping(String prefix) {}
/*     */ 
/*     */   
/*     */   public final void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/*     */     try {
/*  85 */       startElementInternal(toQName(uri, qName), atts, currentNamespaceMapping());
/*  86 */       newNamespaceMapping();
/*     */     }
/*  88 */     catch (XMLStreamException ex) {
/*  89 */       throw new SAXException("Could not handle startElement: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void endElement(String uri, String localName, String qName) throws SAXException {
/*     */     try {
/*  96 */       endElementInternal(toQName(uri, qName), currentNamespaceMapping());
/*  97 */       removeNamespaceMapping();
/*     */     }
/*  99 */     catch (XMLStreamException ex) {
/* 100 */       throw new SAXException("Could not handle endElement: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void characters(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 107 */       String data = new String(ch, start, length);
/* 108 */       if (!this.inCData) {
/* 109 */         charactersInternal(data);
/*     */       } else {
/*     */         
/* 112 */         cDataInternal(data);
/*     */       }
/*     */     
/* 115 */     } catch (XMLStreamException ex) {
/* 116 */       throw new SAXException("Could not handle characters: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 123 */       ignorableWhitespaceInternal(new String(ch, start, length));
/*     */     }
/* 125 */     catch (XMLStreamException ex) {
/* 126 */       throw new SAXException("Could not handle ignorableWhitespace:" + ex
/* 127 */           .getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void processingInstruction(String target, String data) throws SAXException {
/*     */     try {
/* 134 */       processingInstructionInternal(target, data);
/*     */     }
/* 136 */     catch (XMLStreamException ex) {
/* 137 */       throw new SAXException("Could not handle processingInstruction: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void skippedEntity(String name) throws SAXException {
/*     */     try {
/* 144 */       skippedEntityInternal(name);
/*     */     }
/* 146 */     catch (XMLStreamException ex) {
/* 147 */       throw new SAXException("Could not handle skippedEntity: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startDTD(String name, @Nullable String publicId, String systemId) throws SAXException {
/*     */     try {
/* 154 */       StringBuilder builder = new StringBuilder("<!DOCTYPE ");
/* 155 */       builder.append(name);
/* 156 */       if (publicId != null) {
/* 157 */         builder.append(" PUBLIC \"");
/* 158 */         builder.append(publicId);
/* 159 */         builder.append("\" \"");
/*     */       } else {
/*     */         
/* 162 */         builder.append(" SYSTEM \"");
/*     */       } 
/* 164 */       builder.append(systemId);
/* 165 */       builder.append("\">");
/*     */       
/* 167 */       dtdInternal(builder.toString());
/*     */     }
/* 169 */     catch (XMLStreamException ex) {
/* 170 */       throw new SAXException("Could not handle startDTD: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void endDTD() throws SAXException {}
/*     */ 
/*     */   
/*     */   public final void startCDATA() throws SAXException {
/* 180 */     this.inCData = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void endCDATA() throws SAXException {
/* 185 */     this.inCData = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void comment(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 191 */       commentInternal(new String(ch, start, length));
/*     */     }
/* 193 */     catch (XMLStreamException ex) {
/* 194 */       throw new SAXException("Could not handle comment: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startEntity(String name) throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endEntity(String name) throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName toQName(String namespaceUri, String qualifiedName) {
/* 214 */     int idx = qualifiedName.indexOf(':');
/* 215 */     if (idx == -1) {
/* 216 */       return new QName(namespaceUri, qualifiedName);
/*     */     }
/*     */     
/* 219 */     String prefix = qualifiedName.substring(0, idx);
/* 220 */     String localPart = qualifiedName.substring(idx + 1);
/* 221 */     return new QName(namespaceUri, localPart, prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNamespaceDeclaration(QName qName) {
/* 226 */     String prefix = qName.getPrefix();
/* 227 */     String localPart = qName.getLocalPart();
/* 228 */     return (("xmlns".equals(localPart) && prefix.isEmpty()) || ("xmlns"
/* 229 */       .equals(prefix) && !localPart.isEmpty()));
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, String> currentNamespaceMapping() {
/* 234 */     return this.namespaceMappings.get(this.namespaceMappings.size() - 1);
/*     */   }
/*     */   
/*     */   private void newNamespaceMapping() {
/* 238 */     this.namespaceMappings.add(new HashMap<>());
/*     */   }
/*     */   
/*     */   private void removeNamespaceMapping() {
/* 242 */     this.namespaceMappings.remove(this.namespaceMappings.size() - 1);
/*     */   }
/*     */   
/*     */   private void removeAllNamespaceMappings() {
/* 246 */     this.namespaceMappings.clear();
/*     */   }
/*     */   
/*     */   protected abstract void startDocumentInternal() throws XMLStreamException;
/*     */   
/*     */   protected abstract void endDocumentInternal() throws XMLStreamException;
/*     */   
/*     */   protected abstract void startElementInternal(QName paramQName, Attributes paramAttributes, Map<String, String> paramMap) throws XMLStreamException;
/*     */   
/*     */   protected abstract void endElementInternal(QName paramQName, Map<String, String> paramMap) throws XMLStreamException;
/*     */   
/*     */   protected abstract void charactersInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void cDataInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void ignorableWhitespaceInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void processingInstructionInternal(String paramString1, String paramString2) throws XMLStreamException;
/*     */   
/*     */   protected abstract void skippedEntityInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void dtdInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void commentInternal(String paramString) throws XMLStreamException;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/AbstractStaxHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */