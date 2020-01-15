package com.test.VMtranslator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

enum  COMMAND_TYPE {
    C_ARITHMETIC,
    C_PUSH,
    C_POP,
    C_LABEL,
    C_GOTO,
    C_IF,
    C_FUNCTION,
    C_RETURN,
    C_CALL,
    C_INVALID
};

public class Parser {

  //  private String m_vmFileName;
    private String m_Arg1;
    private int m_Arg2;
    private COMMAND_TYPE m_CmdType;
    private BufferedReader m_reade;
    private String m_current_instr;
    private boolean m_haveMoreCmd;

    public Parser(String filename) throws IOException {
        File file=new File(filename);
        m_reade = new BufferedReader(new FileReader(file));
        m_haveMoreCmd=true;
    }

    private COMMAND_TYPE convertCMDStrToEun(String cmStr){
        if(cmStr.equals("push")){
            return COMMAND_TYPE.C_PUSH;
        }

        if(cmStr.equals("pop")){
            return COMMAND_TYPE.C_POP;
        }

        return COMMAND_TYPE.C_INVALID;

    }

    void advance() throws IOException{

        while(true) {
           // String commandType=null;
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
                m_CmdType = convertCMDStrToEun(tokens[0]);
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

    public String getArg1(){
        return this.m_Arg1;
    }

    public int getArg2(){
        return this.m_Arg2;
    }

    public void close() throws IOException{
        m_reade.close();
    }

}
