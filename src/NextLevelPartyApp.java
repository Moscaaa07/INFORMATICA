import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class NextLevelPartyApp extends JFrame {
    private JPanel mainPanel;
    private CardLayout cl = new CardLayout();
    private HashMap<String, String> userDb = new HashMap<>();
    private final String DB_FILE = "users_database.txt";

    // Palette Neon Party
    private final Color DARK_BG = new Color(10, 10, 15);
    private final Color NEON_PURPLE = new Color(191, 0, 255);
    private final Color NEON_CYAN = new Color(0, 255, 255);
    private final Color NEON_PINK = new Color(255, 0, 150);

    public NextLevelPartyApp() {
        setTitle("Next Level Party");
        setSize(400, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadUsersFromFile();

        mainPanel = new JPanel(cl);
        mainPanel.add(createLoginScreen(), "LOGIN");
        mainPanel.add(createRegisterScreen(), "REGISTER");

        add(mainPanel);
        cl.show(mainPanel, "LOGIN");
    }

    private void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(DB_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) userDb.put(parts[0], parts[1]);
            }
        } catch (IOException e) { System.out.println("Nuovo database pronto."); }
    }

    private void saveUserToFile(String user, String pass) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DB_FILE, true))) {
            bw.write(user + ":" + pass + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private boolean isPasswordValid(String pass) {
        String pattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!\\._-])(?=\\S+$).{8,}$";
        return pass.matches(pattern);
    }

    // --- 1. SCHERMATA LOGIN ---
    private JPanel createLoginScreen() {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(DARK_BG); p.setBorder(new EmptyBorder(60, 40, 40, 40));

        JLabel logo = new JLabel("NEXT LEVEL PARTY");
        logo.setFont(new Font("Orbitron", Font.BOLD, 38)); // Font futuristic se disponibile, altrimenti Monospaced
        logo.setForeground(NEON_CYAN); logo.setAlignmentX(0.5f);

        JLabel subLogo = new JLabel("PARTY REGISTER");
        subLogo.setFont(new Font("Arial", Font.ITALIC, 14));
        subLogo.setForeground(NEON_PURPLE); subLogo.setAlignmentX(0.5f);

        JTextField userField = new JTextField(); styleNeonField(userField, "USERNAME", NEON_CYAN);
        JPasswordField passField = new JPasswordField(); styleNeonField(passField, "PASSWORD", NEON_CYAN);

        JButton btnLogin = new JButton("ACCEDI"); styleNeonButton(btnLogin, NEON_PURPLE);

        JButton btnGoReg = new JButton("Non hai un account? Registrati");
        btnGoReg.setForeground(Color.GRAY);
        btnGoReg.setContentAreaFilled(false); btnGoReg.setBorderPainted(false);
        btnGoReg.setAlignmentX(0.5f);

        btnLogin.addActionListener(e -> {
            String u = userField.getText(); String v = new String(passField.getPassword());
            if(u.equals("admin") && v.equals("admin")) {
                mainPanel.add(createDashboard("STAFF"), "DASH_STAFF"); cl.show(mainPanel, "DASH_STAFF");
            } else if(userDb.containsKey(u) && userDb.get(u).equals(v)) {
                mainPanel.add(createDashboard("GUEST"), "DASH_GUEST"); cl.show(mainPanel, "DASH_GUEST");
            } else { JOptionPane.showMessageDialog(this, "Dati errati!"); }
        });

        btnGoReg.addActionListener(e -> cl.show(mainPanel, "REGISTER"));

        p.add(logo); p.add(subLogo); p.add(Box.createRigidArea(new Dimension(0, 50)));
        p.add(userField); p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(passField); p.add(Box.createRigidArea(new Dimension(0, 35)));
        p.add(btnLogin); p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(btnGoReg);
        return p;
    }

    // --- 2. SCHERMATA REGISTRAZIONE ---
    private JPanel createRegisterScreen() {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(DARK_BG); p.setBorder(new EmptyBorder(50, 40, 40, 40));

        JLabel title = new JLabel("JOIN THE PARTY");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(NEON_PINK); title.setAlignmentX(0.5f);

        JTextField newUser = new JTextField(); styleNeonField(newUser, "UTENTE", NEON_PINK);
        JPasswordField newPass = new JPasswordField(); styleNeonField(newPass, "PASSWORD", NEON_PINK);

        JLabel info = new JLabel("<html><center>Min. 8 cifre, 1 Maiuscola, 1 Numero e 1 Speciale</center></html>");
        info.setForeground(Color.GRAY); info.setFont(new Font("Arial", Font.PLAIN, 10)); info.setAlignmentX(0.5f);

        JButton btnReg = new JButton("CONFERMA REGISTRAZIONE"); styleNeonButton(btnReg, NEON_PINK);

        JButton btnBack = new JButton("← Torna al Login");
        btnBack.setForeground(Color.GRAY); btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false); btnBack.setAlignmentX(0.5f);
        btnBack.addActionListener(e -> cl.show(mainPanel, "LOGIN"));

        btnReg.addActionListener(e -> {
            String pass = new String(newPass.getPassword());
            if(newUser.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Scegli un nome!");
            } else if (!isPasswordValid(pass)) {
                JOptionPane.showMessageDialog(this, "Password troppo debole!");
            } else {
                userDb.put(newUser.getText(), pass);
                saveUserToFile(newUser.getText(), pass);
                JOptionPane.showMessageDialog(this, "Registrazione completata!");
                cl.show(mainPanel, "LOGIN");
            }
        });

        p.add(title); p.add(Box.createRigidArea(new Dimension(0, 45)));
        p.add(newUser); p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(newPass); p.add(Box.createRigidArea(new Dimension(0, 5)));
        p.add(info); p.add(Box.createRigidArea(new Dimension(0, 30)));
        p.add(btnReg); p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(btnBack);
        return p;
    }

    // --- 3. DASHBOARD CON TILE (STILE PARTY) ---
    private JPanel createDashboard(String ruolo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(DARK_BG);
        boolean isStaff = ruolo.equals("STAFF");
        Color accent = isStaff ? NEON_CYAN : NEON_PURPLE;

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 20, 28));
        header.setPreferredSize(new Dimension(400, 70));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accent));

        JLabel l = new JLabel("  " + ruolo + " MODE");
        l.setForeground(accent); l.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(l, BorderLayout.WEST);

        JButton btnLogout = new JButton("ESCI ");
        btnLogout.setForeground(Color.WHITE); btnLogout.setContentAreaFilled(false);
        btnLogout.addActionListener(e -> cl.show(mainPanel, "LOGIN"));
        header.add(btnLogout, BorderLayout.EAST);

        // Griglia Tile
        JPanel grid = new JPanel(new GridLayout(3, 2, 15, 15));
        grid.setBackground(DARK_BG);
        grid.setBorder(new EmptyBorder(30, 25, 25, 25));

        if (isStaff) {
            grid.add(createTile("SCANNER", "📸", accent));
            grid.add(createTile("LISTA", "📝", accent));
            grid.add(createTile("LOGS", "🔍", accent));
            grid.add(createTile("POSTI", "📍", accent));
        } else {
            grid.add(createTile("EVENTI", "💃", accent));
            grid.add(createTile("IL MIO QR", "🆔", accent));
            grid.add(createTile("AMICI", "👥", accent));
            grid.add(createTile("PREMI", "🎁", accent));
        }

        p.add(header, BorderLayout.NORTH);
        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private JPanel createTile(String title, String icon, Color c) {
        JPanel t = new JPanel(new BorderLayout());
        t.setBackground(new Color(30, 30, 40));
        t.setBorder(BorderFactory.createLineBorder(c, 1));

        JLabel i = new JLabel(icon, 0); i.setFont(new Font("Arial", Font.PLAIN, 35));
        JLabel text = new JLabel(title, 0); text.setForeground(Color.WHITE);
        text.setFont(new Font("Arial", Font.BOLD, 11));

        t.add(i, BorderLayout.CENTER);
        t.add(text, BorderLayout.SOUTH);

        t.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { t.setBackground(new Color(45, 45, 60)); }
            public void mouseExited(MouseEvent e) { t.setBackground(new Color(30, 30, 40)); }
            public void mousePressed(MouseEvent e) { JOptionPane.showMessageDialog(null, "Apertura: " + title); }
        });
        return t;
    }

    private void styleNeonField(JTextField f, String title, Color c) {
        f.setMaximumSize(new Dimension(320, 50));
        f.setBackground(new Color(25, 25, 35));
        f.setForeground(Color.WHITE);
        f.setCaretColor(c);
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(c, 1), title);
        tb.setTitleColor(c); f.setBorder(tb);
    }

    private void styleNeonButton(JButton b, Color c) {
        b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setMaximumSize(new Dimension(320, 55));
        b.setAlignmentX(0.5f); b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NextLevelPartyApp().setVisible(true));
    }
}