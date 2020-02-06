import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

    private String m_fileName;
    private BufferedWriter m_writer;
    private int eqCmdCounter;
    private int gtCmdCounter;
    private int ltCmdCounter;
    private String m_FuncName;


    public CodeWriter(String fileName) throws IOException {
        m_fileName= fileName+".asm";
        setFuncName(fileName);
        m_writer= new BufferedWriter(new FileWriter(m_fileName, false));
        eqCmdCounter=0;
        gtCmdCounter=0;
        ltCmdCounter=0;
    }

    public void setFuncName(String funcName){
        m_FuncName=funcName;
    }

    public String getFuncName(){
        return m_FuncName;
    }

    public void close() throws IOException {
        m_writer.close();
    }

    public void writeAssemblyCode(Instruction instr) throws IOException {
        // write comment of the parsered
        m_writer.write("//"+instr.getCmd()+" "+instr.getArg1()+" "+instr.getArg2()+"\n");

        if(instr.getCommandType()== COMMAND_TYPE.C_ARITHMETIC){
            writeArithmetic(instr);
        }else if(instr.getCommandType()== COMMAND_TYPE.C_PUSH){
            WritePush(instr);
        } else if(instr.getCommandType()== COMMAND_TYPE.C_POP) {
            WritePop(instr);
        } else if(instr.getCommandType()==COMMAND_TYPE.C_LABEL) {
            WriteLabel(instr);
        } else if(instr.getCommandType()==COMMAND_TYPE.C_GOTO) {
            WriteGoto(instr);
        } else if(instr.getCommandType()==COMMAND_TYPE.C_IF) {
            WriteIfGoTo(instr);
        }

    }

    public void InitMemorySegement() throws IOException {
        m_writer.write("@"+ Constant.SP_BASE_ADDR+"\n");
        m_writer.write("D=A\n");
        m_writer.write("@"+Constant.SP+"\n");
        m_writer.write("M=D\n");
    }

    public void writeArithmetic(Instruction instr) throws IOException {
        if(instr.getCmd().equals("add")){

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            m_writer.write("A=M\n");
            m_writer.write("D=M\n");//put the 2nd variable in D
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");   // M is now point to 1 st variable
            m_writer.write("M=D+M\n"); // add 1st variable from 2nd variable
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n"); // increase the stack pointer

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
            m_writer.write("@GT_LABEL"+this.gtCmdCounter+"\n");
            m_writer.write("D,JGT\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=0\n");
            m_writer.write("(GT_LABEL"+this.gtCmdCounter+")\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");
            gtCmdCounter++;

        }else if(instr.getCmd().equals("lt")) {

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
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
            ltCmdCounter++;

        }else if(instr.getCmd().equals("sub")){

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            m_writer.write("A=M\n");
            m_writer.write("D=M\n");//put the 2nd variable in D
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");   // M is now point to 1 st variable
            m_writer.write("M=M-D\n"); // substract 1st variable from 2nd variable
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");

        }else if(instr.getCmd().equals("or")){

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            m_writer.write("A=M\n");
            m_writer.write("D=M\n");//put the 2nd variable in D
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");   // M is now point to 1 st variable
            m_writer.write("M=D|M\n"); // substract 1st variable from 2nd variable
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");

        }else if(instr.getCmd().equals("and")){

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            m_writer.write("A=M\n");
            m_writer.write("D=M\n");//put the 2nd variable in D
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");   // M is now point to 1 st variable
            m_writer.write("M=D&M\n"); // substract 1st variable from 2nd variable
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");

        } else if(instr.getCmd().equals("not")){

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            m_writer.write("A=M\n");   // M is now point to 1 st variable
            m_writer.write("M=!M\n"); // substract 1st variable from 2nd variable
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");

        }else if(instr.getCmd().equals("neg")){

            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            m_writer.write("A=M\n");   // M is now point to 1 st variable
            m_writer.write("M=-M\n"); // substract 1st variable from 2nd variable
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
        } else if(instr.getArg1().equals("static")) {
            int index=instr.getArg2()+Constant.STATIC_BASE_ADDR;
            m_writer.write("@" + index + "\n");
            m_writer.write("D=M\n");
            // m_writer.write("@SP\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n");
            //m_writer.write("@SP\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");

        }else if(instr.getArg1().equals("local")){
            int index=instr.getArg2();
            m_writer.write("@"+Constant.LCL+"\n");
            m_writer.write("D=M\n"); // put the address stored in THAT into D
            m_writer.write("@"+index+"\n");
            m_writer.write("A=A+D\n");
            m_writer.write("D=M\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");
        }else if(instr.getArg1().equals("argument")){
            int index=instr.getArg2();
            m_writer.write("@"+Constant.ARG+"\n");
            m_writer.write("D=M\n"); // put the address stored in THAT into D
            m_writer.write("@"+index+"\n");
            m_writer.write("A=A+D\n");
            m_writer.write("D=M\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");
        }else if(instr.getArg1().equals("that")){
            int index=instr.getArg2();
            m_writer.write("@"+Constant.THAT+"\n");
            m_writer.write("D=M\n"); // put the address stored in THAT into D
            m_writer.write("@"+index+"\n");
            m_writer.write("A=A+D\n");
            m_writer.write("D=M\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");

        }else if(instr.getArg1().equals("this")){
            int index=instr.getArg2();
            m_writer.write("@"+Constant.THIS+"\n");
            m_writer.write("D=M\n"); // put the address stored in THAT into D
            m_writer.write("@"+index+"\n");
            m_writer.write("A=A+D\n");
            m_writer.write("D=M\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n");
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M+1\n");

        }else if(instr.getArg1().equals("pointer")){

            if(instr.getArg2()==0){

                m_writer.write("@"+Constant.THIS+"\n");
                m_writer.write("D=M\n");
                m_writer.write("@"+Constant.SP+"\n");
                m_writer.write("A=M\n");
                m_writer.write("M=D\n");
                m_writer.write("@"+Constant.SP+"\n");
                m_writer.write("M=M+1\n");

            }else if(instr.getArg2()==1) {

                m_writer.write("@" + Constant.THAT + "\n");
                m_writer.write("D=M\n");
                m_writer.write("@" + Constant.SP + "\n");
                m_writer.write("A=M\n");
                m_writer.write("M=D\n");
                m_writer.write("@" + Constant.SP + "\n");
                m_writer.write("M=M+1\n");
            }
        }else if(instr.getArg1().equals("temp")){
            int index=instr.getArg2()+Constant.TEMP_BASE_ADDR;
            m_writer.write("@" + index + "\n");
            m_writer.write("D=M\n");
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



    public void WritePop(Instruction instr) throws IOException {
        if(instr.getArg1().equals("static")){
            int index=instr.getArg2()+Constant.STATIC_BASE_ADDR;
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); // decrease the stack pointer first
            m_writer.write("A=M\n");
            m_writer.write("D=M\n");//put the  variable from stack in D
            m_writer.write("@"+index+"\n");
            m_writer.write("M=D\n");//put the  variable from D to static location
          //  m_writer.write("@"+Constant.SP+"\n");
          //  m_writer.write("M=M-1\n");

        } else if(instr.getArg1().equals("local")){
            m_writer.write("@"+instr.getArg2()+"\n");
            m_writer.write("D=A\n");
            m_writer.write("@"+Constant.LCL+"\n");
            m_writer.write("D=D+M\n");
            //m_writer.write("@R13\n");
            m_writer.write("@"+Constant.R13+"\n");
            m_writer.write("M=D\n"); //put the calculated address into R13
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");
            m_writer.write("D=M\n"); //put variable from stack to D
        //    m_writer.write("@R13\n");
            m_writer.write("@"+Constant.R13+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n"); //put D into the calculated address



        }else if(instr.getArg1().equals("this")){

            m_writer.write("@"+instr.getArg2()+"\n");
            m_writer.write("D=A\n");
            m_writer.write("@"+Constant.THIS+"\n");
            m_writer.write("D=D+M\n");
           // m_writer.write("@R13\n");
            m_writer.write("@"+Constant.R13+"\n");
            m_writer.write("M=D\n"); //put the calculated address into R13
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");
            m_writer.write("D=M\n"); //put variable from stack to D
          //  m_writer.write("@R13\n");
            m_writer.write("@"+Constant.R13+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n"); //put D into the calculated address

        }else if(instr.getArg1().equals("that")){
            m_writer.write("@"+instr.getArg2()+"\n");
            m_writer.write("D=A\n");
            m_writer.write("@"+Constant.THAT+"\n");
            m_writer.write("D=D+M\n");
            //m_writer.write("@R13\n");
            m_writer.write("@"+Constant.R13+"\n");
            m_writer.write("M=D\n"); //put the calculated address into R13
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");
            m_writer.write("D=M\n"); //put variable from stack to D
          //  m_writer.write("@R13\n");
            m_writer.write("@"+Constant.R13+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n"); //put D into the calculated address

        }else if(instr.getArg1().equals("temp")){
            int index=instr.getArg2()+Constant.TEMP_BASE_ADDR;
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n"); //decrease stack pointer
            m_writer.write("A=M\n");
            m_writer.write("D=M\n"); // put the variable into D
            m_writer.write("@"+index+"\n");
            m_writer.write("M=D\n");



        }else if(instr.getArg1().equals("argument")) {
            m_writer.write("@"+instr.getArg2()+"\n");
            m_writer.write("D=A\n");
            m_writer.write("@"+Constant.ARG+"\n");
            m_writer.write("D=D+M\n");
            //m_writer.write("@R13\n");
            m_writer.write("@"+Constant.R13+"\n");
            m_writer.write("M=D\n"); //put the calculated address into R13
            m_writer.write("@"+Constant.SP+"\n");
            m_writer.write("M=M-1\n");
            m_writer.write("A=M\n");
            m_writer.write("D=M\n"); //put variable from stack to D
            // m_writer.write("@R13\n");
            m_writer.write("@"+Constant.R13+"\n");
            m_writer.write("A=M\n");
            m_writer.write("M=D\n"); //put D into the calculated address


        } else if(instr.getArg1().equals("pointer")){

            if(instr.getArg2()==0){

                m_writer.write("@"+Constant.THIS+"\n");
                m_writer.write("D=A\n");
               // m_writer.write("@R13\n");
                m_writer.write("@"+Constant.R13+"\n");
                m_writer.write("M=D\n"); //put this address into R13
                m_writer.write("@"+Constant.SP+"\n");
                m_writer.write("AM=M-1\n"); //decrease stack pointer
                m_writer.write("D=M\n"); //put variable from stack
               // m_writer.write("@R13\n");
                m_writer.write("@"+Constant.R13+"\n");
                m_writer.write("A=M\n");
                m_writer.write("M=D\n"); //put variable in D to This address


            }else if(instr.getArg2()==1){

                m_writer.write("@"+Constant.THAT+"\n");
                m_writer.write("D=A\n");
                //m_writer.write("@R13\n");
                m_writer.write("@"+Constant.R13+"\n");
                m_writer.write("M=D\n"); //put this address into R13
                m_writer.write("@"+Constant.SP+"\n");
                m_writer.write("AM=M-1\n"); //decrease stack pointer
                m_writer.write("D=M\n"); //put variable from stack
               // m_writer.write("@R13\n");
                m_writer.write("@"+Constant.R13+"\n");
                m_writer.write("A=M\n");
                m_writer.write("M=D\n"); //put variable in D to This address


            }


        }

    }

    public void WriteLabel(Instruction instr) throws IOException {
        m_writer.write("(" + this.getFuncName()+"$"+instr.getArg1() + ")\n");
    }

    public void WriteGoto(Instruction instr) throws IOException {
        m_writer.write("@" + instr.getArg1() + "\n");
        m_writer.write("0;JMP\n");
    }

    public void WriteIfGoTo(Instruction instr) throws IOException {
        m_writer.write("@" + Constant.SP + "\n");
        m_writer.write("AM=M-1\n"); //Move stack up by 1
        m_writer.write("D=M\n");
        m_writer.write("@" + this.getFuncName()+"$"+instr.getArg1()  + "\n");
        m_writer.write("D;JNE\n");

    }



}
