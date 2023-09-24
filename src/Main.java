import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static ArrayList<Figure> figuresList = new ArrayList<>();
    private static Point currentPoint;
    private static Point pointForDrawing;
    private static Point previousPoint;
    private static File currentFile;
    private static JLabel leftLabel;
    private static JLabel rightLabel;
    private static JMenuItem open,save,saveAs;
    private static JRadioButtonMenuItem circle, square, pen;
    private static JPanel drawingPanel;
    private static boolean isF1Pressed = false;
    private static boolean isModified = false;
    private static Color penColor;
    private static JFrame frame;

    private static boolean isDbuttonPressed = false;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::GUIrun);
    }



    public static void GUIrun(){
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(500,500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Simple Draw");

        JPanel mainPanel = new JPanel(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();


        //FILE MENU
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);


        open = new JMenuItem("Open");
        open.setMnemonic(KeyEvent.VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.META_DOWN_MASK));
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == open){
                    openFile(frame);
                }
            }
        });

        save = new JMenuItem("Save");
        save.setMnemonic(KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.META_DOWN_MASK));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == save){
                    saveFile();
                }
            }
        });

        saveAs = new JMenuItem("Save As");
        saveAs.setMnemonic(KeyEvent.VK_A);
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,  KeyEvent.SHIFT_DOWN_MASK | KeyEvent.META_DOWN_MASK ));
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == saveAs){
                    saveFileAs(frame);
                }
            }
        });

        JMenuItem quit = new JMenuItem("Quit");
        quit.setMnemonic(KeyEvent.VK_Q);
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.META_DOWN_MASK));
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        menu.add(open);
        menu.add(save);
        menu.add(saveAs);
        menu.addSeparator();
        menu.add(quit);
        // END FILE MENU

        // DRAW MENU
        JMenu drawMenu = new JMenu("Draw");
        drawMenu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(drawMenu);

        JMenu figure = new JMenu("Figure");
        figure.setMnemonic(KeyEvent.VK_F);
        circle = new JRadioButtonMenuItem("Circle");
        square = new JRadioButtonMenuItem("Square");
        pen = new JRadioButtonMenuItem("Pen");


        circle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == circle){
                    leftLabel.setText("Circle");
                }
            }
        });

        square.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == square){
                    leftLabel.setText("Square");
                }
            }
        });
        pen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == pen){
                    leftLabel.setText("Pen");
                }
            }
        });
        circle.setMnemonic(KeyEvent.VK_C);
        square.setMnemonic(KeyEvent.VK_R);
        pen.setMnemonic(KeyEvent.VK_E);

        circle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK));
        square.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.META_DOWN_MASK));
        pen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.META_DOWN_MASK));
        ButtonGroup buttonFigureGroup = new ButtonGroup();
        buttonFigureGroup.add(circle);
        buttonFigureGroup.add(square);
        buttonFigureGroup.add(pen);
        figure.add(circle);
        figure.add(square);
        figure.add(pen);

        JMenuItem color = new JMenuItem("Color");
        color.setMnemonic(KeyEvent.VK_C);
        color.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.ALT_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK ));
        color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choosePenColor();
            }
        });

        JMenuItem clear = new JMenuItem("Clear");
        clear.setMnemonic(KeyEvent.VK_L);
        clear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.META_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK  ));
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == clear){
                    clearDrawingPanel();
                }
            }
        });

        drawMenu.add(figure);
        drawMenu.add(color);
        drawMenu.addSeparator();
        drawMenu.add(clear);

        // END OF DRAW MENU


        drawingPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (Figure figure : figuresList) {
                    g.setColor(figure.getColor());
                    if (figure.getShapeType().equals("Circle")) {
                        g.fillOval(figure.getX() - figure.getSize() / 2, figure.getY() - figure.getSize() / 2, figure.getSize(), figure.getSize());
                    } else if (figure.getShapeType().equals("Square")) {
                        g.fillRect(figure.getX() - figure.getSize() / 2, figure.getY() - figure.getSize() / 2, figure.getSize(), figure.getSize());
                    }

                }
            }
        };
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_D){
                    isDbuttonPressed = true;
                }
                if(e.getKeyCode() == KeyEvent.VK_F1){
                    isF1Pressed = true;

                    pointForDrawing = MouseInfo.getPointerInfo().getLocation();
                    Point drawPanel = drawingPanel.getLocationOnScreen();
                    int x = pointForDrawing.x - drawPanel.x;
                    int y = pointForDrawing.y - drawPanel.y;
                    if(buttonFigureGroup.isSelected(circle.getModel()) && isF1Pressed){
                        drawCircle(x,y);

                    }

                    else if(buttonFigureGroup.isSelected(square.getModel()) && isF1Pressed){
                        drawSquare(x,y);
                    }



                }
                if(isDbuttonPressed){
                    pointForDrawing = MouseInfo.getPointerInfo().getLocation();
                    Point drawPanel = drawingPanel.getLocationOnScreen();
                    int x = pointForDrawing.x - drawPanel.x;
                    int y = pointForDrawing.y - drawPanel.y;
                    Point deletionPoint = new Point(x, y);
                    selectFigure(deletionPoint);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_F1){
                    isF1Pressed = false;
                }

                if(e.getKeyCode() == KeyEvent.VK_D){
                    isDbuttonPressed = false;
                }
            }
        });
        drawingPanel.setFocusable(true);
        drawingPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                previousPoint = null;


            }
        });
        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(buttonFigureGroup.isSelected(pen.getModel())){
                     currentPoint = e.getPoint();
                    if (previousPoint != null) {
                        Graphics g = drawingPanel.getGraphics();
                        if(penColor != null){
                            g.setColor(penColor);
                        }else {
                            g.setColor(Color.black);
                        }
                        g.drawLine(previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y);

                    }
                    previousPoint = currentPoint;
                }
                }

        });




        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));
        toolBar.setFloatable(false);


        leftLabel = new JLabel("");
        leftLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        rightLabel = new JLabel("New");
        rightLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        rightLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        toolBar.add(leftLabel);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(rightLabel);

        mainPanel.add(menuBar, BorderLayout.NORTH);
        mainPanel.add(drawingPanel,BorderLayout.CENTER);
        mainPanel.add(toolBar, BorderLayout.SOUTH);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }


    public static void drawCircle(int x , int y){
        isModified = true;
        rightLabel.setText("Modified");
        Graphics g = drawingPanel.getGraphics();
        Color color = getRandomColor();
        g.setColor(color);

        g.fillOval(x-25,y-25,50,50);

        figuresList.add(new Figure(x,y,50,"Circle",color));
    }
    public static void drawSquare(int x, int y){
        isModified = true;
        rightLabel.setText("Modified");
        Graphics g = drawingPanel.getGraphics();
        Color color = getRandomColor();
        g.setColor(color);

        g.fillRect(x-25,y-25,50,50);

        figuresList.add(new Figure(x,y,50,"Square",color));

    }
    private static void choosePenColor(){
        penColor = JColorChooser.showDialog(frame,"Choose Pen Color", penColor);
    }

    public static Color getRandomColor(){
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }


    public static void clearDrawingPanel(){
        isModified = false;
        rightLabel.setText("Modified");
        Graphics g = drawingPanel.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, drawingPanel.getWidth(), drawingPanel.getHeight());
        figuresList.clear();
    }

    private static void openFile(Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(parentComponent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFile = file;
            try (Scanner scanner = new Scanner(file)) {
                figuresList.clear();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);
                        int size = Integer.parseInt(parts[2]);
                        String shapeType = parts[3];
                        Color color = new Color(Integer.parseInt(parts[4]));
                        figuresList.add(new Figure(x, y, size, shapeType, color));
                    }
                }
                drawingPanel.repaint();
                isModified = false;
                rightLabel.setText("Opened");
                frame.setTitle("Simple Draw: " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveFile() {
        if (currentFile != null) {
            try (PrintWriter writer = new PrintWriter(currentFile)) {
                for (Figure figure : figuresList) {
                    writer.println(figure.getX() + "," + figure.getY() + "," + figure.getSize() + "," +
                            figure.getShapeType() + "," + figure.getColor().getRGB());
                }
                isModified = false;
                rightLabel.setText("Saved");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveFileAs(frame);
        }
    }

    private static void saveFileAs(Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(parentComponent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                for (Figure figure : figuresList) {
                    writer.println(figure.getX() + "," + figure.getY() + "," + figure.getSize() + "," +
                            figure.getShapeType() + "," + figure.getColor().getRGB());
                }
                isModified = false;
                rightLabel.setText("Saved");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void quit(){
        if (isModified) {
            int result = JOptionPane.showConfirmDialog(frame, "Do you want to save the changes?", "Quit",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                saveFile();
            } else if (result == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        System.exit(0);
    }

    private static void selectFigure(Point clickPoint){
        ArrayList<Figure> delete = new ArrayList<>();
        for (Figure figure : figuresList) {
            if(figure.containsPoint(clickPoint)){
                delete.add(figure);

            }
        }
        if (!delete.isEmpty()) {
            figuresList.removeAll(delete);
            drawingPanel.repaint();

        }
    }

}