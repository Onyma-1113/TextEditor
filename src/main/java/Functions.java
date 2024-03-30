import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.Color;

public class Functions {
    private static Functions functions;
    String fileName,fileAddress,font="Arial";
    Font arial,calibri,aptos;
    private Functions(){

    }

    public static Functions getInstance(){
        if(functions==null){
            functions=new Functions();
        }
        return functions;
    }

    public void file_new(GUI gui){
        gui.textArea.setText("");
        gui.window.setTitle("New");
        fileName=null;
        fileAddress=null;

    }

    public void file_open(GUI gui){
        FileDialog fileDialog= new FileDialog(gui.window,"Open",FileDialog.LOAD);
        fileDialog.setVisible(true);

        if(fileDialog.getFile()!=null){
            fileName=fileDialog.getFile();
            fileAddress=fileDialog.getDirectory();
            gui.window.setTitle(fileName);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileAddress+fileName));
            gui.textArea.setText("");
            String line=null;
            while((line = br.readLine())!=null){
                //gui.textArea.append(line+"\n");
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void file_save(GUI gui){
        if(fileName==null){
            file_saveAs(gui);
        }
        else {
            try{
                FileWriter fw =  new FileWriter(fileAddress+fileName);
                fw.write(gui.textArea.getText());
                gui.window.setTitle(fileName);
                fw.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void file_saveAs(GUI gui){
        FileDialog fd= new FileDialog(gui.window,"Save",FileDialog.SAVE);
        fd.setVisible(true);

        if(fd.getFile()!=null){
            fileName=fd.getFile();
            fileAddress=fd.getDirectory();
            gui.window.setTitle(fileName);
        }

        try{
            FileWriter fw =  new FileWriter(fileAddress+fileName);
            fw.write(gui.textArea.getText());
            fw.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void file_exit(){
    System.exit(0);
    }

    public void createFonts(GUI gui,int fontSize){
        arial=new Font("Arial",Font.PLAIN,fontSize);
        calibri=new Font("Calibri",Font.PLAIN,fontSize);
        aptos=new Font("Aptos",Font.PLAIN,fontSize);
        setFont(gui,font);
    }

    public void setFont(GUI gui,String selectedFont){
        font=selectedFont;

        switch (font){
            case "Arial":gui.textArea.setFont(arial);break;
            case "Calibri":gui.textArea.setFont(calibri);break;
            case "Aptos":gui.textArea.setFont(aptos);break;
        }
    }

    public void setColor(GUI gui,String color){
        switch (color){
            case "White":
            {gui.window.getContentPane().setBackground(Color.white);
            gui.textArea.setBackground(Color.white);
            gui.textArea.setForeground(Color.black);
            }break;
            case "Black":{
                gui.window.getContentPane().setBackground(Color.black);
                gui.textArea.setBackground(Color.black);
                gui.textArea.setForeground(Color.white);
            }break;
            case "Teal":{
                gui.window.getContentPane().setBackground(Color.cyan);
                gui.textArea.setBackground(Color.cyan);
                gui.textArea.setForeground(Color.white);
            }break;
        }
    }

    public String countWords(GUI gui){
        String texts=gui.textArea.getText().replaceAll("[\\s\\p{Punct}]", "");
        return String.valueOf(texts.length());
    }

//    public void underlineTexts(){
//        try{
//            int caretPosition = gui.textArea.getCaretPosition();
//            int start = gui.textArea.getLineStartOffset(gui.textArea.getLineOfOffset(caretPosition));
//            int end = gui.textArea.getLineEndOffset(gui.textArea.getLineOfOffset(caretPosition));
//
//            gui.textArea.setSelectionStart(start);
//            gui.textArea.setSelectionEnd(end);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
}

