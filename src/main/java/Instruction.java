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
    private String m_Cmd;

    private COMMAND_TYPE convertCMDStrToEun(String cmStr){
        if(cmStr.equals("push")){
            return COMMAND_TYPE.C_PUSH;
        }

        if(cmStr.equals("pop")){
            return COMMAND_TYPE.C_POP;
        }

        if(cmStr.equals("add") || cmStr.equals("sub") || cmStr.equals("eq")|| cmStr.equals("or")
        || cmStr.equals("and") || cmStr.equals("neg")|| cmStr.equals("not") ||  cmStr.equals("lt")
        ||  cmStr.equals("gt")){
            return COMMAND_TYPE.C_ARITHMETIC;
        }

        return COMMAND_TYPE.C_INVALID;

    }

    public Instruction(String Cmd,String Arg1, int Arg2){
        m_CmdType=convertCMDStrToEun(Cmd);
        m_Cmd=Cmd;
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

    public String getCmd(){
        return this.m_Cmd;
    }


}
