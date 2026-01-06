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

    // Palette Neon
    private final Color DARK_BG = new Color(10, 10, 12);
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
        } catch (IOException e) { System.out.println("Database non trovato."); }
    }

    private void saveUserToFile(String user, String pass) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DB_FILE, true))) {
            bw.write(user + ":" + pass + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private JPanel createLoginScreen() {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(DARK_BG); p.setBorder(new EmptyBorder(50, 40, 50, 40));

        JLabel logo = new JLabel("NEXT LEVEL PARTY");
        logo.setFont(new Font("Monospaced", Font.BOLD, 30));
        logo.setForeground(NEON_CYAN); logo.setAlignmentX(0.5f);

        JTextField userField = new JTextField(); styleNeonField(userField, "USERNAME", NEON_CYAN);
        JPasswordField passField = new JPasswordField(); styleNeonField(passField, "PASSWORD", NEON_CYAN);

        JButton btnLogin = new JButton("ACCEDI"); styleNeonButton(btnLogin, NEON_PURPLE);

        JButton btnGoReg = new JButton("Registrati");
        btnGoReg.setForeground(Color.GRAY);
        btnGoReg.setContentAreaFilled(false);
        btnGoReg.setBorderPainted(false);
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

        // AGGIUNTI SPAZIATORI DI ALMENO 15-20 PX
        p.add(logo);
        p.add(Box.createRigidArea(new Dimension(0, 40)));
        p.add(userField);
        p.add(Box.createRigidArea(new Dimension(0, 20))); // Spazio tra campi
        p.add(passField);
        p.add(Box.createRigidArea(new Dimension(0, 30))); // Spazio prima del login
        p.add(btnLogin);
        p.add(Box.createRigidArea(new Dimension(0, 20))); // Spazio prima di registrati
        p.add(btnGoReg);
        return p;
    }

    private JPanel createRegisterScreen() {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(DARK_BG); p.setBorder(new EmptyBorder(50, 40, 50, 40));

        JLabel title = new JLabel("NUOVO ACCOUNT");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(NEON_PINK); title.setAlignmentX(0.5f);

        JTextField newUser = new JTextField(); styleNeonField(newUser, "UTENTE", NEON_PINK);
        JPasswordField newPass = new JPasswordField(); styleNeonField(newPass, "PASSWORD", NEON_PINK);

        JButton btnReg = new JButton("CONFERMA"); styleNeonButton(btnReg, NEON_PINK);

        JButton btnBack = new JButton("← Torna indietro");
        btnBack.setForeground(Color.GRAY);
        btnBack.setContentAreaFilled(false);
        btnBack.setAlignmentX(0.5f);
        btnBack.addActionListener(e -> cl.show(mainPanel, "LOGIN"));

        btnReg.addActionListener(e -> {
            if(!newUser.getText().isEmpty()) {
                userDb.put(newUser.getText(), new String(newPass.getPassword()));
                saveUserToFile(newUser.getText(), new String(newPass.getPassword()));
                JOptionPane.showMessageDialog(this, "Registrato!");
                cl.show(mainPanel, "LOGIN");
            }
        });

        // AGGIUNTI SPAZIATORI DI ALMENO 15-20 PX
        p.add(title);
        p.add(Box.createRigidArea(new Dimension(0, 40)));
        p.add(newUser);
        p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(newPass);
        p.add(Box.createRigidArea(new Dimension(0, 30)));
        p.add(btnReg);
        p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(btnBack);
        return p;
    }

    // Dashboard, Style e Utility...
    private JPanel createDashboard(String ruolo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(DARK_BG);
        boolean isStaff = ruolo.equals("STAFF");
        Color accent = isStaff ? NEON_CYAN : NEON_PURPLE;

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 20, 25));
        header.setPreferredSize(new Dimension(400, 60));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accent));

        JLabel l = new JLabel("  " + ruolo + " PANEL");
        l.setForeground(accent); l.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(l, BorderLayout.WEST);

        JButton btnLogout = new JButton("LOGOUT ");
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setContentAreaFilled(false);
        btnLogout.addActionListener(e -> cl.show(mainPanel, "LOGIN"));
        header.add(btnLogout, BorderLayout.EAST);

        JPanel grid = new JPanel(new GridLayout(3, 2, 15, 15));
        grid.setBackground(DARK_BG);
        grid.setBorder(new EmptyBorder(25, 25, 25, 25));

        if (isStaff) {
            grid.add(createMenuTile("SCANNER", "🔍", accent, e -> JOptionPane.showMessageDialog(this, "Scanner...")));
            grid.add(createMenuTile("LISTA NOMI", "📋", accent, e -> openRegistry()));
        } else {
            grid.add(createMenuTile("EVENTI", "📅", accent, e -> openEventsList()));
            grid.add(createMenuTile("MIO QR", "🎟️", accent, null));
        }

        p.add(header, BorderLayout.NORTH);
        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private void openEventsList() {
        JOptionPane.showMessageDialog(this, "Sezione Eventi in arrivo...");
    }

    private void openRegistry() {
        JFrame f = new JFrame("Registro Iscritti");
        f.setSize(350, 500); f.setLocationRelativeTo(null);
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String user : userDb.keySet()) model.addElement("👤 " + user);
        JList<String> list = new JList<>(model);
        list.setBackground(new Color(20,20,25)); list.setForeground(NEON_CYAN);
        f.add(new JScrollPane(list)); f.setVisible(true);
    }

    private JPanel createMenuTile(String title, String emoji, Color c, ActionListener action) {
        JPanel tile = new JPanel(new BorderLayout());
        tile.setBackground(new Color(25, 25, 30));
        tile.setBorder(BorderFactory.createLineBorder(c, 1));
        JLabel lEmoji = new JLabel(emoji, 0); lEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        JLabel lText = new JLabel(title, 0); lText.setForeground(Color.WHITE);
        tile.add(lEmoji, BorderLayout.CENTER); tile.add(lText, BorderLayout.SOUTH);
        tile.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { if (action != null) action.actionPerformed(null); }
        });
        return tile;
    }

    private void styleNeonField(JTextField f, String title, Color c) {
        f.setMaximumSize(new Dimension(320, 50)); f.setBackground(new Color(20, 20, 25)); f.setForeground(Color.WHITE);
        f.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(c, 1), title));
        ((TitledBorder)f.getBorder()).setTitleColor(c);
    }

    private void styleNeonButton(JButton b, Color c) {
        b.setBackground(c); b.setForeground(Color.WHITE); b.setMaximumSize(new Dimension(320, 50));
        b.setAlignmentX(0.5f); b.setFocusPainted(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NextLevelPartyApp().setVisible(true));
    }
}