/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.Comment;
/*     */ import javax.xml.stream.events.DTD;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.EntityDeclaration;
/*     */ import javax.xml.stream.events.EntityReference;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.NotationDeclaration;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
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
/*     */ 
/*     */ class StaxEventXMLReader
/*     */   extends AbstractStaxXMLReader
/*     */ {
/*     */   private static final String DEFAULT_XML_VERSION = "1.0";
/*     */   private final XMLEventReader reader;
/*  66 */   private String xmlVersion = "1.0";
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
/*     */   StaxEventXMLReader(XMLEventReader reader) {
/*     */     try {
/*  81 */       XMLEvent event = reader.peek();
/*  82 */       if (event != null && !event.isStartDocument() && !event.isStartElement()) {
/*  83 */         throw new IllegalStateException("XMLEventReader not at start of document or element");
/*     */       }
/*     */     }
/*  86 */     catch (XMLStreamException ex) {
/*  87 */       throw new IllegalStateException("Could not read first element: " + ex.getMessage());
/*     */     } 
/*  89 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseInternal() throws SAXException, XMLStreamException {
/*  95 */     boolean documentStarted = false;
/*  96 */     boolean documentEnded = false;
/*  97 */     int elementDepth = 0;
/*  98 */     while (this.reader.hasNext() && elementDepth >= 0) {
/*  99 */       XMLEvent event = this.reader.nextEvent();
/* 100 */       if (!event.isStartDocument() && !event.isEndDocument() && !documentStarted) {
/* 101 */         handleStartDocument(event);
/* 102 */         documentStarted = true;
/*     */       } 
/* 104 */       switch (event.getEventType()) {
/*     */         case 7:
/* 106 */           handleStartDocument(event);
/* 107 */           documentStarted = true;
/*     */         
/*     */         case 1:
/* 110 */           elementDepth++;
/* 111 */           handleStartElement(event.asStartElement());
/*     */         
/*     */         case 2:
/* 114 */           elementDepth--;
/* 115 */           if (elementDepth >= 0) {
/* 116 */             handleEndElement(event.asEndElement());
/*     */           }
/*     */         
/*     */         case 3:
/* 120 */           handleProcessingInstruction((ProcessingInstruction)event);
/*     */         
/*     */         case 4:
/*     */         case 6:
/*     */         case 12:
/* 125 */           handleCharacters(event.asCharacters());
/*     */         
/*     */         case 8:
/* 128 */           handleEndDocument();
/* 129 */           documentEnded = true;
/*     */         
/*     */         case 14:
/* 132 */           handleNotationDeclaration((NotationDeclaration)event);
/*     */         
/*     */         case 15:
/* 135 */           handleEntityDeclaration((EntityDeclaration)event);
/*     */         
/*     */         case 5:
/* 138 */           handleComment((Comment)event);
/*     */         
/*     */         case 11:
/* 141 */           handleDtd((DTD)event);
/*     */         
/*     */         case 9:
/* 144 */           handleEntityReference((EntityReference)event);
/*     */       } 
/*     */     
/*     */     } 
/* 148 */     if (documentStarted && !documentEnded) {
/* 149 */       handleEndDocument();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleStartDocument(XMLEvent event) throws SAXException {
/* 155 */     if (event.isStartDocument()) {
/* 156 */       StartDocument startDocument = (StartDocument)event;
/* 157 */       String xmlVersion = startDocument.getVersion();
/* 158 */       if (StringUtils.hasLength(xmlVersion)) {
/* 159 */         this.xmlVersion = xmlVersion;
/*     */       }
/* 161 */       if (startDocument.encodingSet()) {
/* 162 */         this.encoding = startDocument.getCharacterEncodingScheme();
/*     */       }
/*     */     } 
/* 165 */     if (getContentHandler() != null) {
/* 166 */       final Location location = event.getLocation();
/* 167 */       getContentHandler().setDocumentLocator(new Locator2()
/*     */           {
/*     */             public int getColumnNumber() {
/* 170 */               return (location != null) ? location.getColumnNumber() : -1;
/*     */             }
/*     */             
/*     */             public int getLineNumber() {
/* 174 */               return (location != null) ? location.getLineNumber() : -1;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getPublicId() {
/* 179 */               return (location != null) ? location.getPublicId() : null;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getSystemId() {
/* 184 */               return (location != null) ? location.getSystemId() : null;
/*     */             }
/*     */             
/*     */             public String getXMLVersion() {
/* 188 */               return StaxEventXMLReader.this.xmlVersion;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getEncoding() {
/* 193 */               return StaxEventXMLReader.this.encoding;
/*     */             }
/*     */           });
/* 196 */       getContentHandler().startDocument();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleStartElement(StartElement startElement) throws SAXException {
/* 201 */     if (getContentHandler() != null) {
/* 202 */       QName qName = startElement.getName();
/* 203 */       if (hasNamespacesFeature()) {
/* 204 */         for (Iterator<Namespace> iterator = startElement.getNamespaces(); iterator.hasNext(); ) {
/* 205 */           Namespace namespace = iterator.next();
/* 206 */           startPrefixMapping(namespace.getPrefix(), namespace.getNamespaceURI());
/*     */         } 
/* 208 */         for (Iterator<Attribute> i = startElement.getAttributes(); i.hasNext(); ) {
/* 209 */           Attribute attribute = i.next();
/* 210 */           QName attributeName = attribute.getName();
/* 211 */           startPrefixMapping(attributeName.getPrefix(), attributeName.getNamespaceURI());
/*     */         } 
/*     */         
/* 214 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), 
/* 215 */             getAttributes(startElement));
/*     */       } else {
/*     */         
/* 218 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes(startElement));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleCharacters(Characters characters) throws SAXException {
/* 224 */     char[] data = characters.getData().toCharArray();
/* 225 */     if (getContentHandler() != null && characters.isIgnorableWhiteSpace()) {
/* 226 */       getContentHandler().ignorableWhitespace(data, 0, data.length);
/*     */       return;
/*     */     } 
/* 229 */     if (characters.isCData() && getLexicalHandler() != null) {
/* 230 */       getLexicalHandler().startCDATA();
/*     */     }
/* 232 */     if (getContentHandler() != null) {
/* 233 */       getContentHandler().characters(data, 0, data.length);
/*     */     }
/* 235 */     if (characters.isCData() && getLexicalHandler() != null) {
/* 236 */       getLexicalHandler().endCDATA();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndElement(EndElement endElement) throws SAXException {
/* 241 */     if (getContentHandler() != null) {
/* 242 */       QName qName = endElement.getName();
/* 243 */       if (hasNamespacesFeature()) {
/* 244 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 245 */         for (Iterator<Namespace> i = endElement.getNamespaces(); i.hasNext(); ) {
/* 246 */           Namespace namespace = i.next();
/* 247 */           endPrefixMapping(namespace.getPrefix());
/*     */         } 
/*     */       } else {
/*     */         
/* 251 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleEndDocument() throws SAXException {
/* 258 */     if (getContentHandler() != null) {
/* 259 */       getContentHandler().endDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleNotationDeclaration(NotationDeclaration declaration) throws SAXException {
/* 264 */     if (getDTDHandler() != null) {
/* 265 */       getDTDHandler().notationDecl(declaration.getName(), declaration.getPublicId(), declaration.getSystemId());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEntityDeclaration(EntityDeclaration entityDeclaration) throws SAXException {
/* 270 */     if (getDTDHandler() != null) {
/* 271 */       getDTDHandler().unparsedEntityDecl(entityDeclaration.getName(), entityDeclaration.getPublicId(), entityDeclaration
/* 272 */           .getSystemId(), entityDeclaration.getNotationName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleProcessingInstruction(ProcessingInstruction pi) throws SAXException {
/* 277 */     if (getContentHandler() != null) {
/* 278 */       getContentHandler().processingInstruction(pi.getTarget(), pi.getData());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleComment(Comment comment) throws SAXException {
/* 283 */     if (getLexicalHandler() != null) {
/* 284 */       char[] ch = comment.getText().toCharArray();
/* 285 */       getLexicalHandler().comment(ch, 0, ch.length);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleDtd(DTD dtd) throws SAXException {
/* 290 */     if (getLexicalHandler() != null) {
/* 291 */       Location location = dtd.getLocation();
/* 292 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/*     */     } 
/* 294 */     if (getLexicalHandler() != null) {
/* 295 */       getLexicalHandler().endDTD();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleEntityReference(EntityReference reference) throws SAXException {
/* 301 */     if (getLexicalHandler() != null) {
/* 302 */       getLexicalHandler().startEntity(reference.getName());
/*     */     }
/* 304 */     if (getLexicalHandler() != null) {
/* 305 */       getLexicalHandler().endEntity(reference.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Attributes getAttributes(StartElement event) {
/* 311 */     AttributesImpl attributes = new AttributesImpl();
/* 312 */     for (Iterator<Attribute> i = event.getAttributes(); i.hasNext(); ) {
/* 313 */       Attribute attribute = i.next();
/* 314 */       QName qName = attribute.getName();
/* 315 */       String namespace = qName.getNamespaceURI();
/* 316 */       if (namespace == null || !hasNamespacesFeature()) {
/* 317 */         namespace = "";
/*     */       }
/* 319 */       String type = attribute.getDTDType();
/* 320 */       if (type == null) {
/* 321 */         type = "CDATA";
/*     */       }
/* 323 */       attributes.addAttribute(namespace, qName.getLocalPart(), toQualifiedName(qName), type, attribute.getValue());
/*     */     } 
/* 325 */     if (hasNamespacePrefixesFeature()) {
/* 326 */       for (Iterator<Namespace> iterator = event.getNamespaces(); iterator.hasNext(); ) {
/* 327 */         String qName; Namespace namespace = iterator.next();
/* 328 */         String prefix = namespace.getPrefix();
/* 329 */         String namespaceUri = namespace.getNamespaceURI();
/*     */         
/* 331 */         if (StringUtils.hasLength(prefix)) {
/* 332 */           qName = "xmlns:" + prefix;
/*     */         } else {
/*     */           
/* 335 */           qName = "xmlns";
/*     */         } 
/* 337 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/*     */       } 
/*     */     }
/*     */     
/* 341 */     return attributes;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/StaxEventXMLReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */