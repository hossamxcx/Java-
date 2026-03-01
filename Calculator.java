import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class Calculator implements ActionListener {
    JFrame frame;
    JTextField textField;
    JTextArea historyArea;
    JButton[] numberButtons = new JButton[10];
    ArrayList<String> history = new ArrayList<>();
    
    JButton solveBtn, clrBtn, delBtn, equBtn, decBtn, shiftBtn;
    JButton addBtn, subBtn, mulBtn, divBtn, powBtn;
    JButton piBtn, eBtn, factBtn; 
    JButton sinBtn, cosBtn, sinhBtn, coshBtn;

    JPanel panel;
    double num1 = 0, num2 = 0, result = 0;
    char operator = ' ';
    boolean isShifted = false;

    class RoundedButton extends JButton {
        public RoundedButton(String label) {
            super(label);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isPressed()) g2.setColor(getBackground().darker());
            else if (getModel().isRollover()) g2.setColor(getBackground().brighter());
            else g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public Calculator() {
        frame = new JFrame("Elite Scientific Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 850); 
        frame.getContentPane().setBackground(new Color(15, 15, 20)); 
        frame.setLayout(null);

        textField = new JTextField();
        textField.setBounds(30, 30, 420, 100);
        textField.setEditable(false);
        textField.setFont(new Font("SansSerif", Font.BOLD, 42)); 
        textField.setBackground(new Color(25, 25, 35));
        textField.setForeground(new Color(0, 255, 180)); 
        textField.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        textField.setHorizontalAlignment(JTextField.RIGHT);

        historyArea = new JTextArea("HISTORY\n---");
        historyArea.setBounds(470, 30, 230, 750);
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(20, 20, 25));
        historyArea.setForeground(new Color(130, 130, 150));
        historyArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        historyArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initializeButtons();

        panel = new JPanel();
        panel.setBounds(30, 150, 420, 630);
        panel.setLayout(new GridLayout(8, 4, 12, 12)); 
        panel.setBackground(new Color(15, 15, 20));

        panel.add(shiftBtn); panel.add(solveBtn); panel.add(clrBtn); panel.add(delBtn);
        panel.add(eBtn);     panel.add(piBtn);   panel.add(powBtn); panel.add(divBtn);
        panel.add(numberButtons[7]); panel.add(numberButtons[8]); panel.add(numberButtons[9]); panel.add(mulBtn);
        panel.add(numberButtons[4]); panel.add(numberButtons[5]); panel.add(numberButtons[6]); panel.add(subBtn);
        panel.add(numberButtons[1]); panel.add(numberButtons[2]); panel.add(numberButtons[3]); panel.add(addBtn);
        panel.add(factBtn); panel.add(numberButtons[0]); panel.add(decBtn); panel.add(equBtn);
        panel.add(sinBtn); panel.add(cosBtn); panel.add(sinhBtn); panel.add(coshBtn);
        panel.add(new JLabel("")); panel.add(new JLabel("")); panel.add(new JLabel("")); panel.add(new JLabel(""));

        frame.add(panel); frame.add(textField); frame.add(historyArea);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void initializeButtons() {
        shiftBtn = new RoundedButton("SHIFT");
        solveBtn = new RoundedButton("SOLVE"); piBtn = new RoundedButton("π"); eBtn = new RoundedButton("e"); 
        factBtn = new RoundedButton("n!");
        sinBtn = new RoundedButton("sin"); cosBtn = new RoundedButton("cos");
        sinhBtn = new RoundedButton("sinh"); coshBtn = new RoundedButton("cosh");
        powBtn = new RoundedButton("^"); addBtn = new RoundedButton("+"); subBtn = new RoundedButton("-");
        mulBtn = new RoundedButton("x"); divBtn = new RoundedButton("/");
        decBtn = new RoundedButton("."); equBtn = new RoundedButton("=");
        delBtn = new RoundedButton("DEL"); clrBtn = new RoundedButton("AC");

        styleButton(shiftBtn, new Color(75, 85, 99), 18);
        styleButton(solveBtn, new Color(139, 92, 246), 18); 
        styleButton(clrBtn, new Color(244, 63, 94), 18);   
        styleButton(delBtn, new Color(225, 29, 72), 18);
        styleButton(equBtn, new Color(16, 185, 129), 26);  
        
        Color opCol = new Color(245, 158, 11); 
        JButton[] ops = {addBtn, subBtn, mulBtn, divBtn, powBtn};
        for(JButton b : ops) styleButton(b, opCol, 24);

        Color sciCol = new Color(14, 165, 233); 
        styleButton(sinBtn, sciCol, 20); styleButton(cosBtn, sciCol, 20);
        styleButton(sinhBtn, new Color(79, 70, 229), 20); 
        styleButton(coshBtn, new Color(79, 70, 229), 20);

        Color numColor = new Color(51, 65, 85); 
        for(int i=0; i<10; i++) {
            numberButtons[i] = new RoundedButton(String.valueOf(i));
            styleButton(numberButtons[i], numColor, 26); 
        }
        styleButton(decBtn, numColor, 26);

        Color constantColor = new Color(212, 175, 55);
        styleButton(eBtn, constantColor, 24);    
        styleButton(piBtn, constantColor, 24);   
        
        styleButton(factBtn, new Color(219, 39, 119), 22); 
    }

    private void styleButton(JButton btn, Color color, int fontSize) {
        btn.addActionListener(this);
        btn.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
    }

    private boolean isErrorState(String text) {
        return text.contains("NaN") || text.contains("Error") || text.contains("No Sol") || text.contains("Infinity");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String txt = textField.getText();
        Object src = e.getSource();
        
        try {
            if(src == solveBtn) { openSolveMenu(); return; }
            if(src == shiftBtn) { toggleShift(); return; }

            for(int i=0; i<10; i++) {
                if(src == numberButtons[i]) {
                    if (isErrorState(txt)) textField.setText(String.valueOf(i));
                    else textField.setText(txt + i);
                    return;
                }
            }
            
            if(src == clrBtn) { 
                textField.setText(""); 
                num1 = num2 = result = 0; 
                operator = ' ';
                return; 
            }

            if(src == delBtn && !txt.isEmpty()) {
                if(isErrorState(txt)) textField.setText("");
                else textField.setText(txt.substring(0, txt.length()-1));
                return;
            }
            
            if(src == decBtn) {
                if (isErrorState(txt)) textField.setText("0.");
                else if (!txt.contains(".")) textField.setText(txt + ".");
                return;
            }
            
            if(src == eBtn) { updateDisplay(String.valueOf(Math.E)); return; }
            if(src == piBtn) { updateDisplay(String.valueOf(Math.PI)); return; }

            if(src == subBtn) {
                if(txt.isEmpty() || isErrorState(txt)) textField.setText("-");
                else { num1 = Double.parseDouble(txt); operator = '-'; textField.setText(""); }
                return;
            }

            if(src == addBtn || src == mulBtn || src == divBtn || src == powBtn) {
                if(!txt.isEmpty() && !isErrorState(txt)) {
                    num1 = Double.parseDouble(txt);
                    operator = (src == mulBtn) ? '*' : (src == powBtn) ? '^' : e.getActionCommand().charAt(0);
                    textField.setText("");
                }
            }

            if(src == equBtn) {
                if(!txt.isEmpty() && !isErrorState(txt)) {
                    num2 = Double.parseDouble(txt);
                    switch(operator) {
                        case '+': result = num1 + num2; break;
                        case '-': result = num1 - num2; break;
                        case '*': result = num1 * num2; break;
                        case '/': result = (num2 == 0) ? Double.NaN : num1 / num2; break;
                        case '^': 
                            // Custom requirement: power to 0 is not allowed
                            result = (num2 == 0) ? Double.NaN : Math.pow(num1, num2); 
                            break;
                    }
                    updateDisplay(Double.isNaN(result) ? "Error" : String.valueOf(result));
                    operator = ' '; 
                }
            }

            if(src == sinBtn) calculateSci(isShifted ? "asin" : "sin");
            if(src == cosBtn) calculateSci(isShifted ? "acos" : "cos");
            if(src == sinhBtn) calculateSci(isShifted ? "asinh" : "sinh");
            if(src == coshBtn) calculateSci(isShifted ? "acosh" : "cosh");
            
            if(src == factBtn && !txt.isEmpty() && !isErrorState(txt)) {
                double val = Double.parseDouble(txt);
                if (val < 0 || val != Math.floor(val)) updateDisplay("Error");
                else updateDisplay(String.valueOf(factorial((int)val)));
            }

        } catch(Exception ex) { textField.setText("Error"); }
    }

    private void toggleShift() {
        isShifted = !isShifted;
        shiftBtn.setBackground(isShifted ? new Color(5, 255, 180) : new Color(75, 85, 99));
        shiftBtn.setForeground(isShifted ? Color.BLACK : Color.WHITE);
        sinBtn.setText(isShifted ? "asin" : "sin"); cosBtn.setText(isShifted ? "acos" : "cos");
        sinhBtn.setText(isShifted ? "asinh" : "sinh"); coshBtn.setText(isShifted ? "acosh" : "cosh");
    }

    private void calculateSci(String f) {
        String txt = textField.getText();
        if(txt.isEmpty() || isErrorState(txt)) return;
        double v = Double.parseDouble(txt);
        double res = 0;
        switch(f) {
            case "sin": res = Math.sin(Math.toRadians(v)); break;
            case "cos": res = Math.cos(Math.toRadians(v)); break;
            case "asin": res = Math.toDegrees(Math.asin(v)); break;
            case "acos": res = Math.toDegrees(Math.acos(v)); break;
            case "sinh": res = Math.sinh(v); break;
            case "cosh": res = Math.cosh(v); break;
            case "asinh": res = Math.log(v + Math.sqrt(v*v + 1)); break;
            case "acosh": res = (v >= 1) ? Math.log(v + Math.sqrt(v*v - 1)) : Double.NaN; break;
        }
        updateDisplay(Double.isNaN(res) ? "Error" : String.valueOf(res));
        if(isShifted) toggleShift();
    }

    private void openSolveMenu() {
        String[] options = {"2x2 System", "3x3 System", "CANCEL"};
        int choice = JOptionPane.showOptionDialog(frame, "Select Equation Type:", "Solver", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) solve2x2(); else if (choice == 1) solve3x3();
    }

    private double dI(String s) { 
        String res = JOptionPane.showInputDialog(s); 
        if (res == null) throw new RuntimeException("Quit"); 
        return Double.parseDouble(res); 
    }

    private void solve2x2() {
        try {
            double a1=dI("a1"), b1=dI("b1"), c1=dI("c1"), a2=dI("a2"), b2=dI("b2"), c2=dI("c2");
            double det = a1*b2 - a2*b1;
            updateDisplay(det == 0 ? "No Sol" : "x="+(c1*b2-c2*b1)/det+" y="+(a1*c2-a2*c1)/det);
        } catch(RuntimeException e) { } catch(Exception e) { textField.setText("Error"); }
    }

    private void solve3x3() {
        try {
            double a1=dI("a1"), b1=dI("b1"), c1=dI("c1"), d1=dI("d1"), a2=dI("a2"), b2=dI("b2"), c2=dI("c2"), d2=dI("d2"), a3=dI("a3"), b3=dI("b3"), c3=dI("c3"), d3=dI("d3");
            double det = a1*(b2*c3-b3*c2) - b1*(a2*c3-a3*c2) + c1*(a2*b3-a3*b2);
            if(det == 0) { updateDisplay("No Sol"); return; }
            double dx = d1*(b2*c3-b3*c2) - b1*(d2*c3-d3*c2) + c1*(d2*b3-d3*b2);
            double dy = a1*(d2*c3-d3*c2) - d1*(a2*c3-a3*c2) + c1*(a2*d3-a3*d2);
            double dz = a1*(b2*d3-b3*d2) - b1*(a2*d3-a3*d2) + d1*(a2*b3-a3*b2);
            updateDisplay("x="+(dx/det)+" y="+(dy/det)+" z="+(dz/det));
        } catch(RuntimeException e) { } catch(Exception e) { textField.setText("Error"); }
    }

    private long factorial(int n) {
        long f = 1; for (int i = 1; i <= n; i++) f *= i; return f;
    }

    private void updateDisplay(String val) {
        textField.setText(val);
        history.add(0, val);
        historyArea.setText("HISTORY\n---\n" + String.join("\n", history));
    }

    public static void main(String[] args) { new Calculator(); }
}