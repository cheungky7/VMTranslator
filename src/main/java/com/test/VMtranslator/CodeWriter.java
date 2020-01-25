package com.test.VMtranslator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

    private String m_fileName;
    private BufferedWriter m_writer;

    /*
    @Test
    public void givenWritingStringToFile_whenUsingPrintWriter_thenCorrect()
            throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("Some String");
        printWriter.printf("Product name is %s and its price is %d $", "iPhone", 1000);
        printWriter.close();
    }
     */

    public CodeWriter(String fileName) throws IOException {
        m_fileName=fileName;
        m_writer= new BufferedWriter(new FileWriter(m_fileName, true));
    }

    public void close() throws IOException {
        m_writer.close();
    }

    public void writeAssemblyCode(Instruction instr) throws IOException {
        // write
        /*
          System.out.println("line:"+line+",comand:"+parsedInstr.getCommandType()+",arg1:"
                           +parsedInstr.getArg1()+",arg2:"+parsedInstr.getArg2());
         */
        m_writer.write("//"+instr.getCmd()+" "+instr.getArg1()+" "+instr.getArg2()+"\n");


        if(instr.getCommandType()==COMMAND_TYPE.C_ARITHMETIC){
            writeArithmetic(instr);
        }

        if(instr.getCommandType()==COMMAND_TYPE.C_PUSH){
            WritePushPop(instr);
        }

    }

    public void writeArithmetic(Instruction instr){

    }

    public void WritePushPop(Instruction instr){

        
    }

}
