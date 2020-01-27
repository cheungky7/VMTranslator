package com.test.VMtranslator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

    private String m_fileName;
    private BufferedWriter m_writer;
    private int eqCmdCounter;
    private int gtCmdCounter;
    private int ltCmdCounter;


    public CodeWriter(String fileName) throws IOException {
        m_fileName=fileName;
        m_writer= new BufferedWriter(new FileWriter(m_fileName, false));
        eqCmdCounter=0;
        gtCmdCounter=0;
        ltCmdCounter=0;
    }

    public void close() throws IOException {
        m_writer.close();
    }

    public void writeAssemblyCode(Instruction instr) throws IOException {
        // write comment of the parsered
        m_writer.write("//"+instr.getCmd()+" "+instr.getArg1()+" "+instr.getArg2()+"\n");

        if(instr.getCommandType()==COMMAND_TYPE.C_ARITHMETIC){
            writeArithmetic(instr);
        }else if(instr.getCommandType()==COMMAND_TYPE.C_PUSH){
            WritePush(instr);
        } else if(instr.getCommandType()==COMMAND_TYPE.C_POP) {
            WritePop(instr);
        }

    }

    public void InitMemorySegement() throws IOException {
        m_writer.write("@"+Constant.SP_BASE_ADDR+"\n");
        m_writer.write("D=A\n");
        m_writer.write("@"+Constant.SP+"\n");
        m_writer.write("M=D\n");
    }

    public void writeArithmetic(Instruction instr) throws IOException {
        if(instr.getCmd().equals("add")){

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("D=M\n");
            m_writer.write("A=D\n");
            m_writer.write("D=M\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M-1\n");
            m_writer.write("M=D+M\n");

        } else if (instr.getCmd().equals("eq")){
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            //m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("D=M\n");//put the 2nd variable in D
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");   // M is now point to 1 st variable
            m_writer.write("D=M-D\n"); // substract 1st variable from 2nd variable
            m_writer.write("M=-1\n"); // if two variable is equal put -1 into 1 st variable
            m_writer.write("@EQ_LABEL"+this.eqCmdCounter+"\n");
            m_writer.write("D,JEQ\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=0\n");
            m_writer.write("(EQ_LABEL"+this.eqCmdCounter+")\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");
            this.eqCmdCounter++;
        } else if(instr.getCmd().equals("gt")) {

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            //m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("D=M\n");//put the 2nd variable in D
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");   // M is now point to 1 st variable
            m_writer.write("D=M-D\n"); // substract 1st variable from 2nd variable
            m_writer.write("M=-1\n"); // if two variable is equal put -1 into 1 st variable
            m_writer.write("@EQ_LABEL"+this.gtCmdCounter+"\n");
            m_writer.write("D,JGT\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=0\n");
            m_writer.write("(EQ_LABEL"+this.gtCmdCounter+")\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");
        }else if(instr.getCmd().equals("lt")) {

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            //m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("D=M\n");//put the 2nd variable in D
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");   // M is now point to 1 st variable
            m_writer.write("D=M-D\n"); // substract 1st variable from 2nd variable
            m_writer.write("M=-1\n"); // if two variable is equal put -1 into 1 st variable
            m_writer.write("@LT_LABEL"+this.ltCmdCounter+"\n");
            m_writer.write("D,JLT\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=0\n");
            m_writer.write("(LT_LABEL"+this.ltCmdCounter+")\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");
        }

    }

    public void WritePush(Instruction instr) throws IOException {
        if(instr.getArg1().equals("constant")) {
            m_writer.write("@" + instr.getArg2() + "\n");
            m_writer.write("D=A\n");
           // m_writer.write("@SP\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n");
            //m_writer.write("@SP\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");
        }
        
    }

    public void WriteProgramEnd() throws IOException {
        /*
        (END)
        @END
        0;JMP
         */
        m_writer.write("(END)\n");
        m_writer.write("@END\n");
        m_writer.write("0;JMP");

    }



    public void WritePop(Instruction instr){


    }

}
