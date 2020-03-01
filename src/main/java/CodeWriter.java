import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

    private String m_outfileName;
    private String m_fileName;
    private BufferedWriter m_writer;
    private int eqCmdCounter;
    private int gtCmdCounter;
    private int ltCmdCounter;
    private String m_FuncName;
    private static int m_labelnum=0;
    private FileWriter m_FileWriter;
    private static int m_lineInMEM=0;
    private String m_InFileName;


    private void writeASMLineWithComment(String line) throws IOException {
       String lineWithComment=line+" //"+m_lineInMEM+"\n";
        m_writer.write(lineWithComment);
        m_lineInMEM++;
    }


    public CodeWriter(String fileName,boolean isfolder) throws IOException {
        if(isfolder==true){
            m_outfileName =fileName+"/"+fileName + ".asm"; // should place inside source folder if source vm file is more than 1.
        }else {
            m_outfileName = fileName + ".asm";
        }
        m_fileName=fileName;
        //setFuncName(fileName);
        m_FuncName=null;
        m_FileWriter=new FileWriter(m_outfileName, false);
        m_writer= new BufferedWriter(m_FileWriter);
        eqCmdCounter=0;
        gtCmdCounter=0;
        ltCmdCounter=0;
        //m_labelnum=0;
    }

    public void setFuncName(String funcName){
        m_FuncName=funcName;
    }

    public String getFuncName(){
        return m_FuncName;
    }

    public void setInFileName(String inFileName){
        m_InFileName=inFileName;
    }

    public String getInFileName(){
        return this.m_InFileName;
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
        } else if(instr.getCommandType()==COMMAND_TYPE.C_FUNCTION){
            WriteFunction(instr);
        } else if (instr.getCommandType()==COMMAND_TYPE.C_RETURN){
            WriteReturn(instr);
        } else if (instr.getCommandType()==COMMAND_TYPE.C_CALL){
            WriteCall(instr);
        }

    }

    public void writeBoostStrapCode() throws IOException {
        // Initialize the stack pointer to 0x0100
        writeASMLineWithComment("@"+ Constant.SP_BASE_ADDR);
        writeASMLineWithComment("D=A");
        writeASMLineWithComment("@"+Constant.SP+"");
        writeASMLineWithComment("M=D");
        // Start executing (the translated code of) Sys.init
        WriteCall(new Instruction("call","Sys.init",0) );

    }

    public void InitMemorySegement() throws IOException {
        writeASMLineWithComment("@"+ Constant.SP_BASE_ADDR);
        writeASMLineWithComment("D=A");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("M=D");
    }

    public void WriteCall(Instruction instr) throws IOException {
       // m_writer.write("@"+Constant.SP+"\n");
        String ret_label=instr.getArg1()+"$ret."+m_labelnum;
        m_labelnum++;
        // put sp into R13
      //  m_writer.write("// put sp into R13\n");
      //  m_writer.write("@" + Constant.SP + "\n");
      //  m_writer.write("D=M\n");
      //  m_writer.write("@" + Constant.R13 + "\n");
      //  m_writer.write("M=D\n");

        // push return-address
        m_writer.write("// push return-address\n");
        writeASMLineWithComment("@" + ret_label );
        writeASMLineWithComment("D=A");
        // m_writer.write("@SP\n");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("A=M");
        writeASMLineWithComment("M=D");
        //m_writer.write("@SP");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("M=M+1");

        m_writer.write("//push local\n");
        //WritePush(new Instruction("push","local",0));
        writeASMLineWithComment("@" + Constant.LCL );
        writeASMLineWithComment("D=M");
        // m_writer.write("@SP\n");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("A=M");
        writeASMLineWithComment("M=D");
        //m_writer.write("@SP");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("M=M+1");


        m_writer.write("//push argument\n");
        //WritePush(new Instruction("push","argument",0));
        writeASMLineWithComment("@" + Constant.ARG);
        writeASMLineWithComment("D=M");
        // m_writer.write("@SP\n");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("A=M");
        writeASMLineWithComment("M=D");
        //m_writer.write("@SP");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("M=M+1");

        m_writer.write("//push this\n");
      //  WritePush(new Instruction("push","this",0));
        writeASMLineWithComment("@" + Constant.THIS );
        writeASMLineWithComment("D=M");
        // m_writer.write("@SP\n");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("A=M");
        writeASMLineWithComment("M=D");
        //m_writer.write("@SP");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("M=M+1");

        m_writer.write("//push that\n");
        //WritePush(new Instruction("push","that",0));
        writeASMLineWithComment("@" + Constant.THAT );
        writeASMLineWithComment("D=M");
        // m_writer.write("@SP\n");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("A=M");
        writeASMLineWithComment("M=D");
        //m_writer.write("@SP");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("M=M+1");

        // ARG = sp-n-5
        m_writer.write("// ARG = sp-n-5\n");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@"+instr.getArg2());
        writeASMLineWithComment("D=D-A");
        writeASMLineWithComment("@5");
        writeASMLineWithComment("D=D-A");
        writeASMLineWithComment("@"+Constant.ARG);
        writeASMLineWithComment("M=D");
        // LCL = SP
        m_writer.write("// LCL = SP\n");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@"+Constant.LCL);
        writeASMLineWithComment("M=D");

        // goto f
        m_writer.write("// goto f\n");
        writeASMLineWithComment("@"+instr.getArg1());
        writeASMLineWithComment("0;JMP");
        m_writer.write("("+ret_label+")\n");

    }

    public void writeArithmetic(Instruction instr) throws IOException {
        if(instr.getCmd().equals("add")){

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M");//put the 2nd variable in D
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");   // M is now point to 1 st variable
            writeASMLineWithComment("M=D+M"); // add 1st variable from 2nd variable
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1"); // increase the stack pointer

        } else if (instr.getCmd().equals("eq")){

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            //m_writer.write("@"+Constant.SP+"\n");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M");//put the 2nd variable in D
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");   // M is now point to 1 st variable
            writeASMLineWithComment("D=M-D"); // substract 1st variable from 2nd variable
            writeASMLineWithComment("M=-1"); // if two variable is equal put -1 into 1 st variable
            writeASMLineWithComment("@EQ_LABEL"+this.eqCmdCounter);
            writeASMLineWithComment("D,JEQ");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=0");
            m_writer.write("(EQ_LABEL"+this.eqCmdCounter+")\n");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");
            this.eqCmdCounter++;

        } else if(instr.getCmd().equals("gt")) {

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            //m_writer.write("@"+Constant.SP+"\n");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M");//put the 2nd variable in D
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");   // M is now point to 1 st variable
            writeASMLineWithComment("D=M-D"); // substract 1st variable from 2nd variable
            writeASMLineWithComment("M=-1"); // if two variable is equal put -1 into 1 st variable
            writeASMLineWithComment("@GT_LABEL"+this.gtCmdCounter);
            writeASMLineWithComment("D,JGT");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=0");
            m_writer.write("(GT_LABEL"+this.gtCmdCounter+")\n");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");
            gtCmdCounter++;

        }else if(instr.getCmd().equals("lt")) {

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M");//put the 2nd variable in D
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");   // M is now point to 1 st variable
            writeASMLineWithComment("D=M-D"); // substract 1st variable from 2nd variable
            writeASMLineWithComment("M=-1"); // if two variable is equal put -1 into 1 st variable
            writeASMLineWithComment("@LT_LABEL"+this.ltCmdCounter);
            writeASMLineWithComment("D,JLT");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=0");
            m_writer.write("(LT_LABEL"+this.ltCmdCounter+")\n");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");
            ltCmdCounter++;

        }else if(instr.getCmd().equals("sub")){

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M");//put the 2nd variable in D
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");   // M is now point to 1 st variable
            writeASMLineWithComment("M=M-D"); // substract 1st variable from 2nd variable
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");

        }else if(instr.getCmd().equals("or")){

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M");//put the 2nd variable in D
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");   // M is now point to 1 st variable
            writeASMLineWithComment("M=D|M"); // substract 1st variable from 2nd variable
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");

        }else if(instr.getCmd().equals("and")){

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M");//put the 2nd variable in D
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");   // M is now point to 1 st variable
            writeASMLineWithComment("M=D&M"); // substract 1st variable from 2nd variable
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");

        } else if(instr.getCmd().equals("not")){

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            writeASMLineWithComment("A=M");   // M is now point to 1 st variable
            writeASMLineWithComment("M=!M"); // substract 1st variable from 2nd variable
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");

        }else if(instr.getCmd().equals("neg")){

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            writeASMLineWithComment("A=M");   // M is now point to 1 st variable
            writeASMLineWithComment("M=-M"); // substract 1st variable from 2nd variable
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");

        }

    }

    public void WritePush(Instruction instr) throws IOException {
        if(instr.getArg1().equals("constant")) {
            writeASMLineWithComment("@" + instr.getArg2() );
            writeASMLineWithComment("D=A");
           // m_writer.write("@SP");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D");
            //m_writer.write("@SP\n");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");
        } else if(instr.getArg1().equals("static")) {
           // int index=instr.getArg2()+Constant.STATIC_BASE_ADDR;
            int index=instr.getArg2();
           // writeASMLineWithComment("@" + index );
            writeASMLineWithComment("@"+this.m_InFileName+"."+index);
            writeASMLineWithComment("D=M");
            // writeASMLineWithComment("@SP\n");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D");
            //writeASMLineWithComment("@SP\n");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");

        }else if(instr.getArg1().equals("local")){
            int index=instr.getArg2();
            writeASMLineWithComment("@"+Constant.LCL);
            writeASMLineWithComment("D=M"); // put the address stored in THAT into D
            writeASMLineWithComment("@"+index);
            writeASMLineWithComment("A=A+D");
            writeASMLineWithComment("D=M");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M+1");
        }else if(instr.getArg1().equals("argument")){
            int index=instr.getArg2();
            writeASMLineWithComment("@"+Constant.ARG);
            writeASMLineWithComment("D=M"); // put the address stored in THAT into D
            writeASMLineWithComment("@"+index);
            writeASMLineWithComment("A=A+D");
            writeASMLineWithComment("D=M");
            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D");
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M+1");
        }else if(instr.getArg1().equals("that")){
            int index=instr.getArg2();
            writeASMLineWithComment("@"+Constant.THAT+"");
            writeASMLineWithComment("D=M"); // put the address stored in THAT into D
            writeASMLineWithComment("@"+index+"");
            writeASMLineWithComment("A=A+D");
            writeASMLineWithComment("D=M");
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D");
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M+1");

        }else if(instr.getArg1().equals("this")){
            int index=instr.getArg2();
            writeASMLineWithComment("@"+Constant.THIS+"");
            writeASMLineWithComment("D=M"); // put the address stored in THAT into D
            writeASMLineWithComment("@"+index+"");
            writeASMLineWithComment("A=A+D");
            writeASMLineWithComment("D=M");
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D");
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M+1");

        }else if(instr.getArg1().equals("pointer")){

            if(instr.getArg2()==0){

                writeASMLineWithComment("@"+Constant.THIS+"");
                writeASMLineWithComment("D=M");
                writeASMLineWithComment("@"+Constant.SP+"");
                writeASMLineWithComment("A=M");
                writeASMLineWithComment("M=D");
                writeASMLineWithComment("@"+Constant.SP+"");
                writeASMLineWithComment("M=M+1");

            }else if(instr.getArg2()==1) {

                writeASMLineWithComment("@" + Constant.THAT + "");
                writeASMLineWithComment("D=M");
                writeASMLineWithComment("@" + Constant.SP + "");
                writeASMLineWithComment("A=M");
                writeASMLineWithComment("M=D");
                writeASMLineWithComment("@" + Constant.SP + "");
                writeASMLineWithComment("M=M+1");
            }
        }else if(instr.getArg1().equals("temp")){
            int index=instr.getArg2()+Constant.TEMP_BASE_ADDR;
            writeASMLineWithComment("@" + index + "");
            writeASMLineWithComment("D=M");
            // m_writer.write("@SP");
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D");
            //m_writer.write("@SP");
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M+1");

        }
        
    }

    public void WriteProgramEnd() throws IOException {
        /*
        (END)
        @END
        0;JMP
         */
        m_writer.write("(END)\n");
        writeASMLineWithComment("@END");
        writeASMLineWithComment("0;JMP");

    }



    public void WritePop(Instruction instr) throws IOException {
        if(instr.getArg1().equals("static")){
            /*
            int index=instr.getArg2()+Constant.STATIC_BASE_ADDR;
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M");//put the  variable from stack in D
            writeASMLineWithComment("@"+index+"");
            writeASMLineWithComment("M=D");//put the  variable from D to static location
             */
            //strAcode = new StringBuilder().append("@").append(fileName).append(nIndex).append("\nD=A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n").toString();
            int index=instr.getArg2();

            writeASMLineWithComment("@"+Constant.SP);
            writeASMLineWithComment("M=M-1"); // decrease the stack pointer first
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M");//put the  variable from stack in D
            writeASMLineWithComment("@"+this.m_InFileName+"."+index);
            /*
            Each variable is assigned a unique
            memory address, starting at 16*/
            //according to hack specification
            writeASMLineWithComment("M=D");//put the  variable from D to static location

        } else if(instr.getArg1().equals("local")){
            writeASMLineWithComment("@"+instr.getArg2()+"");
            writeASMLineWithComment("D=A");
            writeASMLineWithComment("@"+Constant.LCL+"");
            writeASMLineWithComment("D=D+M");
            //m_writer.write("@R13\n");
            writeASMLineWithComment("@"+Constant.R13+"");
            writeASMLineWithComment("M=D"); //put the calculated address into R13
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M"); //put variable from stack to D
        //    m_writer.write("@R13\n");
            writeASMLineWithComment("@"+Constant.R13+"");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D"); //put D into the calculated address



        }else if(instr.getArg1().equals("this")){

            writeASMLineWithComment("@"+instr.getArg2()+"");
            writeASMLineWithComment("D=A");
            writeASMLineWithComment("@"+Constant.THIS+"");
            writeASMLineWithComment("D=D+M");
           // m_writer.write("@R13\n");
            writeASMLineWithComment("@"+Constant.R13+"");
            writeASMLineWithComment("M=D"); //put the calculated address into R13
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M"); //put variable from stack to D
          //  m_writer.write("@R13\n");
            writeASMLineWithComment("@"+Constant.R13+"");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D"); //put D into the calculated address

        }else if(instr.getArg1().equals("that")){
            writeASMLineWithComment("@"+instr.getArg2()+"");
            writeASMLineWithComment("D=A");
            writeASMLineWithComment("@"+Constant.THAT+"");
            writeASMLineWithComment("D=D+M");
            //m_writer.write("@R13\n");
            writeASMLineWithComment("@"+Constant.R13+"");
            writeASMLineWithComment("M=D"); //put the calculated address into R13
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M"); //put variable from stack to D
          //  m_writer.write("@R13\n");
            writeASMLineWithComment("@"+Constant.R13+"");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D"); //put D into the calculated address

        }else if(instr.getArg1().equals("temp")){
            int index=instr.getArg2()+Constant.TEMP_BASE_ADDR;
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M-1"); //decrease stack pointer
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M"); // put the variable into D
            writeASMLineWithComment("@"+index+"");
            writeASMLineWithComment("M=D");



        }else if(instr.getArg1().equals("argument")) {
            writeASMLineWithComment("@"+instr.getArg2()+"");
            writeASMLineWithComment("D=A");
            writeASMLineWithComment("@"+Constant.ARG+"");
            writeASMLineWithComment("D=D+M");
            //m_writer.write("@R13\n");
            writeASMLineWithComment("@"+Constant.R13+"");
            writeASMLineWithComment("M=D"); //put the calculated address into R13
            writeASMLineWithComment("@"+Constant.SP+"");
            writeASMLineWithComment("M=M-1");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("D=M"); //put variable from stack to D
            // m_writer.write("@R13\n");
            writeASMLineWithComment("@"+Constant.R13+"");
            writeASMLineWithComment("A=M");
            writeASMLineWithComment("M=D"); //put D into the calculated address


        } else if(instr.getArg1().equals("pointer")){

            if(instr.getArg2()==0){

                writeASMLineWithComment("@"+Constant.THIS);
                writeASMLineWithComment("D=A");
               // m_writer.write("@R13\n");
                writeASMLineWithComment("@"+Constant.R13);
                writeASMLineWithComment("M=D"); //put this address into R13
                writeASMLineWithComment("@"+Constant.SP);
                writeASMLineWithComment("AM=M-1"); //decrease stack pointer
                writeASMLineWithComment("D=M"); //put variable from stack
               // m_writer.write("@R13\n");
                writeASMLineWithComment("@"+Constant.R13);
                writeASMLineWithComment("A=M");
                writeASMLineWithComment("M=D"); //put variable in D to This address


            }else if(instr.getArg2()==1){

                writeASMLineWithComment("@"+Constant.THAT);
                writeASMLineWithComment("D=A");
                //m_writer.write("@R13\n");
                writeASMLineWithComment("@"+Constant.R13);
                writeASMLineWithComment("M=D"); //put this address into R13
                writeASMLineWithComment("@"+Constant.SP);
                writeASMLineWithComment("AM=M-1"); //decrease stack pointer
                writeASMLineWithComment("D=M"); //put variable from stack
               // m_writer.write("@R13\n");
                writeASMLineWithComment("@"+Constant.R13);
                writeASMLineWithComment("A=M");
                writeASMLineWithComment("M=D"); //put variable in D to This address


            }


        }

    }

    public void WriteLabel(Instruction instr) throws IOException {
        m_writer.write("(" + this.getFuncName()+"$"+instr.getArg1() + ")\n");
    }

    public void WriteGoto(Instruction instr) throws IOException {
        writeASMLineWithComment("@" + this.getFuncName()+"$"+instr.getArg1() + "");
        writeASMLineWithComment("0;JMP");
    }

    public void WriteIfGoTo(Instruction instr) throws IOException {
        writeASMLineWithComment("@" + Constant.SP + "");
        writeASMLineWithComment("AM=M-1"); //Move stack up by 1
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@" + this.getFuncName()+"$"+instr.getArg1()  + "");
        writeASMLineWithComment("D;JNE");

    }

    public void WriteFunction(Instruction instr) throws IOException {
        int numLocals=instr.getArg2();
        this.setFuncName(instr.getArg1());
        m_writer.write("("+instr.getArg1()+")\n");
        for(int i=0;i<numLocals;i++){
            WritePush(new Instruction("push","constant",0));
        }

    }

    public void WriteReturn(Instruction instr) throws IOException {

        int TempAddrHoldFrame=Constant.R13;
        int TempAddrHoldRET=Constant.R14+1;
        //  FRAME = LCL // FRAME is a temporary variable
        writeASMLineWithComment("@"+Constant.LCL);
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@"+TempAddrHoldFrame);
        writeASMLineWithComment("M=D"); // put the LCL into Temp base addr frame=LCL
        // RET = *(FRAME-5) // Put the return-address in a temp. var.
        writeASMLineWithComment("@5");
        writeASMLineWithComment("D=D-A"); // Calculate address holding ret by LCL -5
        writeASMLineWithComment("A=D");
        writeASMLineWithComment("D=M"); //put value of ret into D
        writeASMLineWithComment("@"+TempAddrHoldRET);
        writeASMLineWithComment("M=D"); //put value of ret into Temp variable
       //  *ARG = pop() // Reposition the return value for the caller
        //below code equal to WritePop(new Instruction("pop","argument",0));
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("M=M-1");
        writeASMLineWithComment("A=M");
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@"+Constant.ARG);
        writeASMLineWithComment("A=M");
        writeASMLineWithComment("M=D");


        //  SP = ARG+1 // Restore SP of the caller
        writeASMLineWithComment("@"+Constant.ARG);
        writeASMLineWithComment("D=M+1");
        writeASMLineWithComment("@"+Constant.SP);
        writeASMLineWithComment("M=D");//restore  stack pointer and SP=ARG+1
        //THAT = *(FRAME-1) // Restore THAT of the caller
        writeASMLineWithComment("@"+TempAddrHoldFrame);
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@1");
        writeASMLineWithComment("D=D-A");
        writeASMLineWithComment("A=D");
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@"+Constant.THAT);
        writeASMLineWithComment("M=D");
        // THIS = *(FRAME-2) // Restore THIS of the caller
        writeASMLineWithComment("@"+TempAddrHoldFrame);
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@2");
        writeASMLineWithComment("D=D-A");
        writeASMLineWithComment("A=D");
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@"+Constant.THIS);
        writeASMLineWithComment("M=D");
        // ARG = *(FRAME-3) // Restore ARG of the caller
        writeASMLineWithComment("@"+TempAddrHoldFrame);
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@3");
        writeASMLineWithComment("D=D-A");
        writeASMLineWithComment("A=D");
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@"+Constant.ARG);
        writeASMLineWithComment("M=D");
        //LCL = *(FRAME-4) // Restore LCL of the caller
        writeASMLineWithComment("@"+TempAddrHoldFrame);
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@4");
        writeASMLineWithComment("D=D-A");
        writeASMLineWithComment("A=D");
        writeASMLineWithComment("D=M");
        writeASMLineWithComment("@"+Constant.LCL);
        writeASMLineWithComment("M=D");
       //  goto RET
        // Goto return-address
        writeASMLineWithComment("@"+TempAddrHoldRET);
        writeASMLineWithComment("A=M");
        writeASMLineWithComment("0,JMP");

    }



}
