import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatDraculaIJTheme;
import com.google.gson.*;
import javax.net.ssl.HttpsURLConnection;

public class GUI implements ActionListener {
    boolean autoOn = true;
    int size = 16;
    Font fontt;
    JFrame window;
    JTextPane textArea;
    JScrollPane scrollPane;
    JMenuBar menuBar;
    JMenu file, edit, format, style, font;
    JMenuItem New, open, save, saveAs, exit;
    JMenuItem arial, aptos, calibri, size8, size10, size12, size14, size16;
    JMenuItem Cobalt2, ArcOrange, MaterialLighter, undo, redo, search;
    ImageIcon magnifierIcon = new ImageIcon("icon/magnifier.png");
    ImageIcon autoCheckIcon = new ImageIcon("icon/grammarly-svgrepo-com.png");
    Image img2 = magnifierIcon.getImage();
    Image img3 = autoCheckIcon.getImage();
    String suggestions = "";
    JPanel panel;
    JLabel label;
    JLabel labelSuggestions = new JLabel(suggestions, SwingConstants.RIGHT);
    UndoManager undoManager = new UndoManager();
    Functions functions = Functions.getInstance();
    JButton button = new JButton(magnifierIcon);
    JButton switchButton = new JButton(magnifierIcon);
    static String host = "https://api.bing.microsoft.com/";
    static String path = "/v7.0/spellcheck";
    static String key = "5ecfa9ca9d04436bae1033b6608bad03";
    static String mkt = "en-US";
    static String mode = "proof";
    static String text = "Hollo, wrld!";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatCobalt2IJTheme());
            check();
        } catch (Exception e) {
            System.out.println(e);
        }

        new GUI();
    }

    // Method to call the spell check API
    public static String checkSpelling(String text) throws Exception {
        String params = "?mkt=" + mkt + "&mode=" + mode;
        URL url = new URL(host + path + params);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", key);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes("text=" + text);
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(response.toString());
        JsonArray flaggedTokens = json.getAsJsonObject().getAsJsonArray("flaggedTokens");

        if (flaggedTokens.size() > 0) {
            List<String> suggestionsList = new ArrayList<>();
            for (JsonElement flaggedToken : flaggedTokens) {
                JsonArray suggestions = flaggedToken.getAsJsonObject().getAsJsonArray("suggestions");
                for (JsonElement suggestion : suggestions) {
                    String suggestedWord = suggestion.getAsJsonObject().get("suggestion").getAsString();
                    suggestionsList.add(suggestedWord);
                }
            }

            return suggestionsList.toString();
        } else {
            return "";
        }
    }

    public static void check() throws Exception {
        String params = "?mkt=" + mkt + "&mode=" + mode;
        // add the rest of the code snippets here (except prettify() and main())...
        URL url = new URL(host + path + params);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", key);
        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes("text=" + text);
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(prettify(line));
        }
        in.close();
    }

    // This function prettifies the json response.
    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);

    }

    public GUI() {
        createWindow();
        creatTextArea();
        createMenubar();
        createMenuItems();
        createFormatMenu();
        createLabel();
        importImg();
        window.setVisible(true);
    }

    public void importImg() {
        magnifierIcon.setImage(img2.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        autoCheckIcon.setImage(img3.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        switchButton.setIcon(autoCheckIcon);
    }

    public void createWindow() {
        window = new JFrame("Notepad");
        window.setSize(800, 600);
        window.setLayout(new BorderLayout());
        window.addWindowListener(new WindowAdapter() {
    @Override
    public void windowClosing(WindowEvent e) {
        functions.file_save(GUI.this);
    }
});
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void creatTextArea() {
        textArea = new JTextPane();

// Create a StyledDocument and set it as the document for the JTextPane
        StyledDocument doc = new DefaultStyledDocument();
        textArea.setDocument(doc);

// Define a SimpleAttributeSet for the suggestion words
        SimpleAttributeSet suggestionAttributes = new SimpleAttributeSet();
        StyleConstants.setForeground(suggestionAttributes, Color.GRAY);
        textArea.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                     fontt = textArea.getFont();
                     size = fontt.getSize();
                    if (e.getWheelRotation() < 0) {
                        // Mouse wheel moved UP, increase font size
                        size++;
                    } else {
                        // Mouse wheel moved DOWN, decrease font size
                        size--;
                        if (size < 1) size = 1; // Ensure size never goes below 1
                    }
                    Font newFont = fontt.deriveFont((float) size);
                    textArea.setFont(newFont);
                }
            }
        });
        textArea.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        // Remove all highlights
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();
    }
});
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    if(autoOn){
                    suggestions = checkSpelling(textArea.getText());
                    labelSuggestions.setText(suggestions);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                label.setText(functions.countWords(GUI.this));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    if(autoOn){
                         suggestions = checkSpelling(textArea.getText());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                label.setText(functions.countWords(GUI.this));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    if(autoOn){
                     suggestions = checkSpelling(textArea.getText());}
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                label.setText(functions.countWords(GUI.this));
            }
        });
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        window.add(scrollPane);
    }

    public void createMenubar() {

        menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);
        file = new JMenu("File");
        edit = new JMenu("Edit");
        format = new JMenu("Format");
        style = new JMenu("Style");
        menuBar.add(button);
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(format);
        menuBar.add(style);
        menuBar.add(switchButton);
    }

    public void createMenuItems() {
        New = new JMenuItem("New");
        New.addActionListener(this);
        New.setActionCommand("New");

        open = new JMenuItem("Open");
        open.addActionListener(this);
        open.setActionCommand("Open");

        save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setActionCommand("Save");

        saveAs = new JMenuItem("Save As");
        saveAs.addActionListener(this);
        saveAs.setActionCommand("Save As");

        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setActionCommand("Exit");

        Cobalt2 = new JMenuItem("Cobalt2");
        Cobalt2.addActionListener(this);
        Cobalt2.setActionCommand("Cobalt2");

        ArcOrange = new JMenuItem("ArcOrange");
        ArcOrange.addActionListener(this);
        ArcOrange.setActionCommand("ArcOrange");

        MaterialLighter = new JMenuItem("MaterialLighter");
        MaterialLighter.addActionListener(this);
        MaterialLighter.setActionCommand("MaterialLighter");

        undo = new JMenuItem("Undo");
        undo.addActionListener(this);
        undo.setActionCommand("Undo");

        redo = new JMenuItem("Redo");
        redo.addActionListener(this);
        redo.setActionCommand("Redo");

        button.addActionListener(this);
        button.setActionCommand("Search");

        switchButton.addActionListener(this);
        switchButton.setActionCommand("AutoCheck");

        file.add(New);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(exit);

        style.add(Cobalt2);
        style.add(ArcOrange);
        style.add(MaterialLighter);

        edit.add(undo);
        edit.add(redo);
    }

    public void createFormatMenu() {

        font = new JMenu("Font");

        arial = new JMenuItem("Arial");
        arial.addActionListener(this);
        arial.setActionCommand("Arial");

        aptos = new JMenuItem("Aptos");
        aptos.addActionListener(this);
        aptos.setActionCommand("Aptos");

        calibri = new JMenuItem("Calibri");
        calibri.addActionListener(this);
        calibri.setActionCommand("Calibri");

        format.add(font);
        font.add(arial);
        font.add(aptos);
        font.add(calibri);
    }

    public void createLabel() {

        label = new JLabel(functions.countWords(this));
        panel = new JPanel(new GridLayout(2, 1));
        panel.add(label);
        panel.add(labelSuggestions);

        // Add the panel to the SOUTH area of the BorderLayout
        window.add(panel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                functions.file_new(this);
                break;
            case "Open":
                functions.file_open(this);
                break;
            case "Save":
                functions.file_save(this);
                break;
            case "Save As":
                functions.file_saveAs(this);
                break;
            case "Exit":
                functions.file_exit();
                break;
            case "Arial":
                functions.setFont(this, "Arial");
                break;
            case "Calibri":
                functions.setFont(this, "Calibri");
                break;
            case "Aptos":
                functions.setFont(this, "Aptos");
                break;
            case "Cobalt2":
                functions.setTheme(this, "Cobalt2");
                break;
            case "ArcOrange":
                functions.setTheme(this, "ArcOrange");
                break;
            case "MaterialLighter":
                functions.setTheme(this, "MaterialLighter");
                break;
            case "Undo": {
                if (undoManager.canUndo())
                    undoManager.undo();
            }
            break;
            case "Redo": {
                if (undoManager.canRedo())
                    undoManager.redo();
            }
            break;
            case "Search": functions.search(this);
            break;
            case "AutoCheck": functions.autoCheck(this);
            break;
        }


    }
}