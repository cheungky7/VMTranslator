package com.test.VMtranslator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

    private String m_fileName;
    private BufferedWriter m_writer;



    public CodeWriter(String fileName) throws IOException {
        m_fileName=fileName;
        m_writer= new BufferedWriter(new FileWriter(m_fileName, false));
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

    public void writeArithmetic(Instruction instr) throws IOException {
        if(instr.getCmd().equals("add")){
            m_writer.write("@SP\n");
            m_writer.write("D=M\n"); // put the lower variable into D
            m_writer.write("@SP\n");
            m_writer.write("M=A-1\n"); //decrease stack pointer
            m_writer.write("@SP\n");
            m_writer.write("M=D+M\n");
        }

    }

    public void WritePush(Instruction instr) throws IOException {
        if(instr.getArg1().equals("constant")) {
            m_writer.write("@" + instr.getArg2() + "\n");
            m_writer.write("D=A\n");
            m_writer.write("@SP\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n");
            m_writer.write("@SP\n");
            m_writer.write("M=M+1\n");
        }
        
    }

    public void WritePop(Instruction instr){


    }

}
