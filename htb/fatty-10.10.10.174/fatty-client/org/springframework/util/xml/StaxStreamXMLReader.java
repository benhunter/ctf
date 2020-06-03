/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class StaxStreamXMLReader
/*     */   extends AbstractStaxXMLReader
/*     */ {
/*     */   private static final String DEFAULT_XML_VERSION = "1.0";
/*     */   private final XMLStreamReader reader;
/*  51 */   private String xmlVersion = "1.0";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StaxStreamXMLReader(XMLStreamReader reader) {
/*  65 */     int event = reader.getEventType();
/*  66 */     if (event != 7 && event != 1) {
/*  67 */       throw new IllegalStateException("XMLEventReader not at start of document or element");
/*     */     }
/*  69 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseInternal() throws SAXException, XMLStreamException {
/*  75 */     boolean documentStarted = false;
/*  76 */     boolean documentEnded = false;
/*  77 */     int elementDepth = 0;
/*  78 */     int eventType = this.reader.getEventType();
/*     */     while (true) {
/*  80 */       if (eventType != 7 && eventType != 8 && !documentStarted) {
/*     */         
/*  82 */         handleStartDocument();
/*  83 */         documentStarted = true;
/*     */       } 
/*  85 */       switch (eventType) {
/*     */         case 1:
/*  87 */           elementDepth++;
/*  88 */           handleStartElement();
/*     */           break;
/*     */         case 2:
/*  91 */           elementDepth--;
/*  92 */           if (elementDepth >= 0) {
/*  93 */             handleEndElement();
/*     */           }
/*     */           break;
/*     */         case 3:
/*  97 */           handleProcessingInstruction();
/*     */           break;
/*     */         case 4:
/*     */         case 6:
/*     */         case 12:
/* 102 */           handleCharacters();
/*     */           break;
/*     */         case 7:
/* 105 */           handleStartDocument();
/* 106 */           documentStarted = true;
/*     */           break;
/*     */         case 8:
/* 109 */           handleEndDocument();
/* 110 */           documentEnded = true;
/*     */           break;
/*     */         case 5:
/* 113 */           handleComment();
/*     */           break;
/*     */         case 11:
/* 116 */           handleDtd();
/*     */           break;
/*     */         case 9:
/* 119 */           handleEntityReference();
/*     */           break;
/*     */       } 
/* 122 */       if (this.reader.hasNext() && elementDepth >= 0) {
/* 123 */         eventType = this.reader.next();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 129 */     if (!documentEnded) {
/* 130 */       handleEndDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleStartDocument() throws SAXException {
/* 135 */     if (7 == this.reader.getEventType()) {
/* 136 */       String xmlVersion = this.reader.getVersion();
/* 137 */       if (StringUtils.hasLength(xmlVersion)) {
/* 138 */         this.xmlVersion = xmlVersion;
/*     */       }
/* 140 */       this.encoding = this.reader.getCharacterEncodingScheme();
/*     */     } 
/* 142 */     if (getContentHandler() != null) {
/* 143 */       final Location location = this.reader.getLocation();
/* 144 */       getContentHandler().setDocumentLocator(new Locator2()
/*     */           {
/*     */             public int getColumnNumber() {
/* 147 */               return (location != null) ? location.getColumnNumber() : -1;
/*     */             }
/*     */             
/*     */             public int getLineNumber() {
/* 151 */               return (location != null) ? location.getLineNumber() : -1;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getPublicId() {
/* 156 */               return (location != null) ? location.getPublicId() : null;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getSystemId() {
/* 161 */               return (location != null) ? location.getSystemId() : null;
/*     */             }
/*     */             
/*     */             public String getXMLVersion() {
/* 165 */               return StaxStreamXMLReader.this.xmlVersion;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getEncoding() {
/* 170 */               return StaxStreamXMLReader.this.encoding;
/*     */             }
/*     */           });
/* 173 */       getContentHandler().startDocument();
/* 174 */       if (this.reader.standaloneSet()) {
/* 175 */         setStandalone(this.reader.isStandalone());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleStartElement() throws SAXException {
/* 181 */     if (getContentHandler() != null) {
/* 182 */       QName qName = this.reader.getName();
/* 183 */       if (hasNamespacesFeature()) {
/* 184 */         int i; for (i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 185 */           startPrefixMapping(this.reader.getNamespacePrefix(i), this.reader.getNamespaceURI(i));
/*     */         }
/* 187 */         for (i = 0; i < this.reader.getAttributeCount(); i++) {
/* 188 */           String prefix = this.reader.getAttributePrefix(i);
/* 189 */           String namespace = this.reader.getAttributeNamespace(i);
/* 190 */           if (StringUtils.hasLength(namespace)) {
/* 191 */             startPrefixMapping(prefix, namespace);
/*     */           }
/*     */         } 
/* 194 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), 
/* 195 */             toQualifiedName(qName), getAttributes());
/*     */       } else {
/*     */         
/* 198 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleEndElement() throws SAXException {
/* 204 */     if (getContentHandler() != null) {
/* 205 */       QName qName = this.reader.getName();
/* 206 */       if (hasNamespacesFeature()) {
/* 207 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 208 */         for (int i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 209 */           String prefix = this.reader.getNamespacePrefix(i);
/* 210 */           if (prefix == null) {
/* 211 */             prefix = "";
/*     */           }
/* 213 */           endPrefixMapping(prefix);
/*     */         } 
/*     */       } else {
/*     */         
/* 217 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleCharacters() throws SAXException {
/* 223 */     if (12 == this.reader.getEventType() && getLexicalHandler() != null) {
/* 224 */       getLexicalHandler().startCDATA();
/*     */     }
/* 226 */     if (getContentHandler() != null) {
/* 227 */       getContentHandler().characters(this.reader.getTextCharacters(), this.reader
/* 228 */           .getTextStart(), this.reader.getTextLength());
/*     */     }
/* 230 */     if (12 == this.reader.getEventType() && getLexicalHandler() != null) {
/* 231 */       getLexicalHandler().endCDATA();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleComment() throws SAXException {
/* 236 */     if (getLexicalHandler() != null) {
/* 237 */       getLexicalHandler().comment(this.reader.getTextCharacters(), this.reader
/* 238 */           .getTextStart(), this.reader.getTextLength());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleDtd() throws SAXException {
/* 243 */     if (getLexicalHandler() != null) {
/* 244 */       Location location = this.reader.getLocation();
/* 245 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/*     */     } 
/* 247 */     if (getLexicalHandler() != null) {
/* 248 */       getLexicalHandler().endDTD();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEntityReference() throws SAXException {
/* 253 */     if (getLexicalHandler() != null) {
/* 254 */       getLexicalHandler().startEntity(this.reader.getLocalName());
/*     */     }
/* 256 */     if (getLexicalHandler() != null) {
/* 257 */       getLexicalHandler().endEntity(this.reader.getLocalName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndDocument() throws SAXException {
/* 262 */     if (getContentHandler() != null) {
/* 263 */       getContentHandler().endDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleProcessingInstruction() throws SAXException {
/* 268 */     if (getContentHandler() != null) {
/* 269 */       getContentHandler().processingInstruction(this.reader.getPITarget(), this.reader.getPIData());
/*     */     }
/*     */   }
/*     */   
/*     */   private Attributes getAttributes() {
/* 274 */     AttributesImpl attributes = new AttributesImpl(); int i;
/* 275 */     for (i = 0; i < this.reader.getAttributeCount(); i++) {
/* 276 */       String namespace = this.reader.getAttributeNamespace(i);
/* 277 */       if (namespace == null || !hasNamespacesFeature()) {
/* 278 */         namespace = "";
/*     */       }
/* 280 */       String type = this.reader.getAttributeType(i);
/* 281 */       if (type == null) {
/* 282 */         type = "CDATA";
/*     */       }
/* 284 */       attributes.addAttribute(namespace, this.reader.getAttributeLocalName(i), 
/* 285 */           toQualifiedName(this.reader.getAttributeName(i)), type, this.reader.getAttributeValue(i));
/*     */     } 
/* 287 */     if (hasNamespacePrefixesFeature()) {
/* 288 */       for (i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 289 */         String qName, prefix = this.reader.getNamespacePrefix(i);
/* 290 */         String namespaceUri = this.reader.getNamespaceURI(i);
/*     */         
/* 292 */         if (StringUtils.hasLength(prefix)) {
/* 293 */           qName = "xmlns:" + prefix;
/*     */         } else {
/*     */           
/* 296 */           qName = "xmlns";
/*     */         } 
/* 298 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/*     */       } 
/*     */     }
/*     */     
/* 302 */     return attributes;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/StaxStreamXMLReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */