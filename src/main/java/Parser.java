import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Parser {

  //  private String m_vmFileName;
    /*
    private String m_Arg1;
    private int m_Arg2;
    private COMMAND_TYPE m_CmdType;
     */
    private BufferedReader m_reade;
    private String m_current_instr;
    private boolean m_haveMoreCmd;
    private Instruction m_parsed_instr;

    public Parser(String filename) throws IOException {
        File file=new File(filename);
        m_reade = new BufferedReader(new FileReader(file));
        m_haveMoreCmd=true;
    }


/*
    private void clearInternalPara(){
        //m_Arg1=null;
        //m_Arg2=0;
        //m_CmdType=COMMAND_TYPE.C_INVALID;

    }
 */

    public void advance() throws IOException{

        while(true) {
           // String commandType=null;
           // String arg1=null;
           // String arg2=null;
            //COMMAND_TYPE CmdType=COMMAND_TYPE.C_NULL;
            String Cmd=null;
            String arg1=null;
            int arg2=0;
            m_parsed_instr=null;

            m_current_instr = m_reade.readLine();
           // clearInternalPara();
            if (m_current_instr == null) {
                m_haveMoreCmd = false;
                break;
            }

            int firstPosOfComment=m_current_instr.indexOf("//");
            // turn the instruction into array of string tokens by white space
            String instrRemoveComment=m_current_instr;
            if(firstPosOfComment!=-1) {
                instrRemoveComment = m_current_instr.substring(0, firstPosOfComment);
            }

            if(instrRemoveComment.length()==0) {
                continue; // if only comment and space skip this line
            }
            String[] tokens = instrRemoveComment.trim().split("\\s+");
            if(tokens.length>0) {
                Cmd = (tokens[0]);
            }
            if(tokens.length>1) {
                arg1 = tokens[1];
            }
            if(tokens.length>2) {
                arg2 =Integer.parseInt( tokens[2]);
            }

            m_parsed_instr=new Instruction(Cmd,arg1,arg2);

            break;
        }


    }

    public boolean hasMoreCommands(){
        return this.m_haveMoreCmd;
    }

    public Instruction getParsedInstr(){
        return this.m_parsed_instr;
    }

    public void close() throws IOException{
        m_reade.close();
    }

}
