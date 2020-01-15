package com.test.VMtranslator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

  //  private String m_vmFileName;
    private String m_Arg1;
    private int m_Arg2;
    private BufferedReader m_reade;
    private String m_current_instr;
    private boolean m_haveMoreCmd;

    public Parser(String filename) throws IOException {
        File file=new File(filename);
        m_reade = new BufferedReader(new FileReader(file));
        m_haveMoreCmd=true;
    }

    void advance() throws IOException{

        while(true) {
            String commandType=null;
           // String arg1=null;
           // String arg2=null;
            m_current_instr = m_reade.readLine();
            if (m_current_instr == null) {
                m_haveMoreCmd = false;
                break;
            }

            int firstPosOfComment=m_current_instr.indexOf("//");
            // turn the instruction into array of string tokens by white space
            String instrRemoveComment=m_current_instr.substring(0,firstPosOfComment);
            String[] tokens = instrRemoveComment.trim().split("\\s+");
            if(tokens.length==0) {
                continue; // if only comment and space skip this line
            }
            if(tokens.length>0) {
                commandType = tokens[0];
            }
            if(tokens.length>1) {
                m_Arg1 = tokens[1];
            }
            if(tokens.length>2) {
                m_Arg2 =Integer.parseInt( tokens[2]);
            }

            break;
        }


    }

    public void close() throws IOException{
        m_reade.close();
    }

}
