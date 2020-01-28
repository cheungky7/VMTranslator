package com.test.VMtranslator;

public class VMtranslator {

    public static void main(String[] args){
        String sourceFileName = args[0];
        System.out.printf("The input file is %s\n",sourceFileName);



        try {
            int posOfExtension = sourceFileName.indexOf(".vm");
            String fileName=sourceFileName.substring(0, posOfExtension);
            String outFile=fileName+".asm";

            Parser parser = new Parser(sourceFileName);
            CodeWriter coder=new CodeWriter(outFile);
            int line=0;
            coder.InitMemorySegement();
           while(parser.hasMoreCommands()==true){
               parser.advance();
               Instruction parsedInstr=parser.getParsedInstr();
               /*
               System.out.println("line:"+line+",comand:"+parser.getCommandType()+",arg1:"
                       +parser.getArg1()+",arg2:"+parser.getArg2());*/
               if(parsedInstr !=null){
                   System.out.println("line:"+line+",comand:"+parsedInstr.getCommandType()+",arg1:"
                           +parsedInstr.getArg1()+",arg2:"+parsedInstr.getArg2());
                   coder.writeAssemblyCode(parsedInstr);
               }
               line++;
           }
           coder.WriteProgramEnd();
           parser.close();
           coder.close();
        }catch(Exception e){
           System.out.printf("Exception:%s\n",  e.getMessage());
           e.printStackTrace();
        }


    }
}
