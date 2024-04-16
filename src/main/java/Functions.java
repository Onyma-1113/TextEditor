import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.Color;

public class Functions {
    private static Functions functions;
    String fileName,fileAddress,font="Arial";
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

    public void setFont(GUI gui,String selectedFont){
        font=selectedFont;

        switch (font){
            case "Arial":{
                gui.textArea.setFont(new Font("Arial", Font.PLAIN, gui.size));
            }break;
            case "Calibri":{
                gui.textArea.setFont(new Font("Calibri", Font.PLAIN, gui.size));
            }break;
            case "Aptos":{
                gui.textArea.setFont(new Font("Aptos", Font.PLAIN, gui.size));
            }break;
        }
    }

    public void setTheme(GUI gui,String theme){
        switch (theme){
            case "Cobalt2":
            {
                try {
                UIManager.setLookAndFeel(new FlatCobalt2IJTheme());
                SwingUtilities.updateComponentTreeUI(gui.window);
            } catch (Exception e) {
                e.printStackTrace();
            }
            }break;
            case "ArcOrange":{
                try {
                UIManager.setLookAndFeel(new FlatArcOrangeIJTheme());
                SwingUtilities.updateComponentTreeUI(gui.window);
            } catch (Exception e) {
                e.printStackTrace();
            }
            }break;
            case "MaterialLighter":{try {
                UIManager.setLookAndFeel(new FlatMaterialLighterIJTheme());
                SwingUtilities.updateComponentTreeUI(gui.window);
            } catch (Exception e) {
                e.printStackTrace();
            }
            }break;
        }
    }

    public String countWords(GUI gui){
        String texts=gui.textArea.getText().replaceAll("[\\s\\p{Punct}]", "");
        return String.valueOf(texts.length());
    }
    public void search(GUI gui) {

        String input = JOptionPane.showInputDialog("Enter the word to search");
        String text = gui.textArea.getText();

        // Find the index of the first occurrence of the search string
        int index = text.indexOf(input);

        if (index != -1) {
            // If the string was found, select it in the JTextPane
            gui.textArea.requestFocus();
            gui.textArea.select(index, index + input.length());
        } else {
            // If the string was not found, deselect any selected text
            gui.textArea.select(0, 0);
        }
    }

    public void autoCheck(GUI gui){
        if (gui.autoOn) {
            gui.autoOn = false;
            gui.switchButton.setIcon(null);
        } else {
            gui.autoOn = true;
            gui.switchButton.setIcon(gui.autoCheckIcon);
        }
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

