package org.springframework.beans.factory.parsing;

public interface ProblemReporter {
  void fatal(Problem paramProblem);
  
  void error(Problem paramProblem);
  
  void warning(Problem paramProblem);
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/parsing/ProblemReporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */