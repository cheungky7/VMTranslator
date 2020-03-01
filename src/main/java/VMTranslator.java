import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class VMTranslator {

    public static boolean checkIfIsDirectory(String path){
        File file = new File(path);
        boolean isDir= file.isDirectory();
        return isDir;

    }

    public static void searchFilesInDir(String pattern, File folder,List<String> result) {

        for (File f : folder.listFiles()) {

            if (f.isDirectory()) {
                searchFilesInDir(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }

        }
    }

    public static String extractFilename(String path){
        //String fileName=null;
        File f = new File(path);
        String fileName=f.getName();
        int posOfExtension = fileName.indexOf(".vm");
        String fileNameWithoutExtension=fileName.substring(0, posOfExtension);
        return fileNameWithoutExtension;
    }


    public static void generateVMSingleFile(String sourceFileName){

        try {
            int posOfExtension = sourceFileName.indexOf(".vm");
            String fileName=sourceFileName.substring(0, posOfExtension);
          //  String outFile=fileName+".asm";

            Parser parser = new Parser(sourceFileName);
            CodeWriter coder=new CodeWriter(fileName,false);
            int line=0;
            //coder.InitMemorySegement();
            while(parser.hasMoreCommands()==true){
                parser.advance();
                Instruction parsedInstr=parser.getParsedInstr();

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

    public static void writeToASMFile(CodeWriter coder,String sourceFileName,Integer lineNo) throws IOException {

        Parser parser = new Parser(sourceFileName);
        coder.setInFileName(extractFilename(sourceFileName));
        while(parser.hasMoreCommands()==true){
            parser.advance();
            Instruction parsedInstr=parser.getParsedInstr();

            if(parsedInstr !=null){
                System.out.println("line:"+lineNo+",comand:"+parsedInstr.getCommandType()+",arg1:"
                        +parsedInstr.getArg1()+",arg2:"+parsedInstr.getArg2());
                coder.writeAssemblyCode(parsedInstr);
            }
            lineNo++;
        }
        parser.close();
    }

    public static void generateFromMultiFiles(String path){

        List<String> result = new ArrayList<>();
        File folder = new File(path);

        searchFilesInDir(".*\\.vm", folder,result);

/*
        int indexOfSysVM=0;

        for(int i=0; i<result.size();i++){
            if(result.get(i).contains("Sys.vm")==true){
                indexOfSysVM=i;
                break;
            }
        }

        Collections.swap(result, 0, indexOfSysVM);
        */

        try {
            Integer lineNo=0;
            CodeWriter coder = new CodeWriter(path,true);
            coder.writeBoostStrapCode();
            for (String s : result) {
                System.out.println("Read: " + s);
                //writeToASMFile(CodeWriter coder,String sourceFileName,Integer lineNo);
                writeToASMFile(coder,s,lineNo);
            }

            coder.close();
        }catch (Exception e){
            System.out.printf("Exception:%s\n",  e.getMessage());
            e.printStackTrace();
        }

        //parser.close();



    }

    public static void main(String[] args){
        String sourceFileName = args[0];
        System.out.printf("The input file is %s\n",sourceFileName);
        System.out.print("The input file is directory:"+checkIfIsDirectory(sourceFileName)+"\n");

       // checkIfIsDirectory

        if(checkIfIsDirectory(sourceFileName)==false) {
            generateVMSingleFile(sourceFileName);
        } else {
            generateFromMultiFiles(sourceFileName);
        }

    }
}
