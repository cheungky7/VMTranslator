import java.io.File;
import java.util.ArrayList;
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


    public static void generateVMSingleFile(String sourceFileName){

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

    public static void generateFromMultiFiles(String path){

        List<String> result = new ArrayList<>();
        File folder = new File(path);

        searchFilesInDir(".*\\.vm", folder,result);

        for (String s : result) {
            System.out.println(s);
        }


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
