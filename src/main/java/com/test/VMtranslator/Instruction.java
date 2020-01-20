package com.test.VMtranslator;

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
    C_INVALID,
    C_NULL, //command is null
};


public class Instruction {
    private String m_Arg1;
    private int m_Arg2;
    private COMMAND_TYPE m_CmdType;

    public Instruction(COMMAND_TYPE CmdType,String Arg1, int Arg2){
        m_CmdType=CmdType;
        m_Arg1=Arg1;
        m_Arg2=Arg2;
    }

    public String getArg1(){
        return this.m_Arg1;
    }

    public int getArg2(){
        return this.m_Arg2;
    }

    public COMMAND_TYPE getCommandType(){
        return this.m_CmdType;
    }


}
