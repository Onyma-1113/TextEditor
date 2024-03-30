import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {
    JFrame window;
    JTextPane textArea;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JMenu file, edit, format, style,font,size;
    JMenuItem New, open, save, saveAs, exit;
    JMenuItem arial,aptos,calibri,size8, size10,size12,size14,size16;
    JMenuItem light,black,teal,undo,redo,search;
    ImageIcon magnifierIcon=new ImageIcon("icon/magnifier.png");
    JLabel label;
    UndoManager undoManager=new UndoManager();
    Functions functions = Functions.getInstance();

    public static void main(String[] args) {

        new GUI();
    }

    public GUI(){
        createWindow();
        creatTextArea();
        createMenubar();
        createMenuItems();
        createFormatMenu();
        createLabel();
        importImg();
        window.setVisible(true);
        //textArea.setLineWrap(true);
        //textArea.setWrapStyleWord(true);
    }

    public void importImg(){

        Image img2=magnifierIcon.getImage();
        magnifierIcon.setImage(img2.getScaledInstance(20,20,Image.SCALE_SMOOTH));


    }

    public void createWindow(){
        window=new JFrame("Notepad");
        window.setSize(800,600);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void creatTextArea(){
        textArea=new JTextPane();
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {

                undoManager.addEdit(e.getEdit());
            }
        });

            textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                label.setText(functions.countWords(GUI.this));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                label.setText(functions.countWords(GUI.this));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                label.setText(functions.countWords(GUI.this));
            }
        });
        scrollPane= new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        window.add(scrollPane);
    }

    public void createMenubar(){

        menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);
        file=new JMenu("File");
        edit = new JMenu("Edit");
        format= new JMenu("Format");
        style= new JMenu("Style");
        search=new JMenuItem(magnifierIcon);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(format);
        menuBar.add(style);
        menuBar.add(search);

    }

    public  void createMenuItems(){
        New= new JMenuItem("New");
        New.addActionListener(this);
        New.setActionCommand("New");

        open=new JMenuItem("Open");
        open.addActionListener(this);
        open.setActionCommand("Open");

        save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setActionCommand("Save");

        saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(this);
        saveAs.setActionCommand("Save As");

        exit =new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setActionCommand("Exit");

        light=new JMenuItem("Light");
        light.addActionListener(this);
        light.setActionCommand("Light");

        black=new JMenuItem("Black");
        black.addActionListener(this);
        black.setActionCommand("Black");

        teal= new JMenuItem("Teal");
        teal.addActionListener(this);
        teal.setActionCommand("Teal");

        undo= new JMenuItem("Undo");
        undo.addActionListener(this);
        undo.setActionCommand("Undo");

        redo= new JMenuItem("Redo");
        redo.addActionListener(this);
        redo.setActionCommand("Redo");

        search.addActionListener(this);
        search.setActionCommand("Search");

        file.add(New);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(exit);

        style.add(light);
        style.add(black);
        style.add(teal);

        edit.add(undo);
        edit.add(redo);
    }

    public void createFormatMenu(){

        font=new JMenu("Font");
        size = new JMenu("Size");

        arial =new JMenuItem("Arial");
        arial.addActionListener(this);
        arial.setActionCommand("Arial");

        aptos= new JMenuItem("Aptos");
        aptos.addActionListener(this);
        aptos.setActionCommand("Aptos");

        calibri=new JMenuItem("Calibri");
        calibri.addActionListener(this);
        calibri.setActionCommand("Calibri");

        size8= new JMenuItem("8");
        size8.addActionListener(this);
        size8.setActionCommand("Size 8");

        size10=new JMenuItem("10");
        size10.addActionListener(this);
        size10.setActionCommand("Size 10");

        size12=new JMenuItem("12");
        size12.addActionListener(this);
        size12.setActionCommand("Size 12");

        size14 = new JMenuItem("14");
        size14.addActionListener(this);
        size14.setActionCommand("Size 14");

        size16=new JMenuItem("16");
        size16.addActionListener(this);
        size16.setActionCommand("Size 16");

        format.add(font);
        format.add(size);
        font.add(arial);
        font.add(aptos);
        font.add(calibri);
        size.add(size8);
        size.add(size10);
        size.add(size12);
        size.add(size14);
        size.add(size16);
    }

    public void createLabel(){

        label=new JLabel(functions.countWords(this));
        window.add(label,BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command){
            case "New":functions.file_new(this);break;
            case "Open":functions.file_open(this);break;
            case "Save":functions.file_save(this);break;
            case "Save As":functions.file_saveAs(this);break;
            case "Exit":functions.file_exit();break;
            case "Arial":functions.setFont(this,"Arial");break;
            case "Calibri":functions.setFont(this,"Calibri");break;
            case "Aptos":functions.setFont(this,"Aptos");break;
            case "Size 8":functions.createFonts(this,8);break;
            case "Size 10":functions.createFonts(this,10);break;
            case "Size 12":functions.createFonts(this,12);break;
            case "Size 14":functions.createFonts(this,14);break;
            case "Size 16":functions.createFonts(this,16);break;
            case "Light":functions.setColor(this,"Light");break;
            case "Black":functions.setColor(this,"Black");break;
            case "Teal":functions.setColor(this,"Teal");break;
            case "Undo":{
                if(undoManager.canUndo())
                    undoManager.undo();
            }break;
            case "Redo":{
                if(undoManager.canRedo())
                    undoManager.redo();
            }break;

        }
    }

    
}
