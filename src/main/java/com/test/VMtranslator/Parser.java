package com.test.VMtranslator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

  //  private String m_vmFileName;
    private String m_Arg1;
    private int m_Arg2;
    private BufferedReader m_reade;
    private String m_current_instr;
    private boolean m_haveMoreCmd;

    Parser(String filename) throws IOException {
        File file=new File(filename);
        m_reade = new BufferedReader(new FileReader(file));
        m_haveMoreCmd=true;
    }

    void advance() throws IOException{
        m_current_instr=m_reade.readLine();
        if(m_current_instr==null){
            m_haveMoreCmd=false;
        }
    }

    public void close() throws IOException{
        m_reade.close();
    }

}
