package org.springframework.asm;

final class Context {
  Attribute[] attributePrototypes;
  
  int parsingOptions;
  
  char[] charBuffer;
  
  int currentMethodAccessFlags;
  
  String currentMethodName;
  
  String currentMethodDescriptor;
  
  Label[] currentMethodLabels;
  
  int currentTypeAnnotationTarget;
  
  TypePath currentTypeAnnotationTargetPath;
  
  Label[] currentLocalVariableAnnotationRangeStarts;
  
  Label[] currentLocalVariableAnnotationRangeEnds;
  
  int[] currentLocalVariableAnnotationRangeIndices;
  
  int currentFrameOffset;
  
  int currentFrameType;
  
  int currentFrameLocalCount;
  
  int currentFrameLocalCountDelta;
  
  Object[] currentFrameLocalTypes;
  
  int currentFrameStackCount;
  
  Object[] currentFrameStackTypes;
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/asm/Context.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */