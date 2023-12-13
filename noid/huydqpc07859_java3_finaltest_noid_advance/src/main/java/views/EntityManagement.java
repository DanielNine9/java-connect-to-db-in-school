/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import models.Entity;
import repositories.EntityRepository;
import services.SortService;
import utils.DatabaseUtil;
import utils.MyFile;
import utils.SaveImgUtil;

/**
 *
 * @author huyyd
 */
public class EntityManagement extends javax.swing.JFrame {

    private File selectedFile = null;
    private EntityRepository repository = new EntityRepository();
    private final String entity = "NHANVIEN";
    private String nameTable = "NhanVien";

    /**
     * Creates new form EntityManagement
     */
    public EntityManagement() {
        initComponents();
        lblTitle.setText("QUẢN LÝ " + (entity + "s").toUpperCase());
        this.setLocationRelativeTo(null);
//        Entity.generateCreateTableSQL(nameTable);
        repository = new EntityRepository(nameTable);

        setNameAll();

        grayBorderAll();

        updateTable();

        validateNumber(txtLuong1Gio);
        validateNumber(txtSoGioLamViec);

//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                dropTable();
//            }
//        });
//        List<Object> arr = new ArrayList<>(Arrays.asList(Entity.builder().name("control").build().getName(), "Ly", "Hoa"));
//        fillComboBox(cbo, arr);
    }

    private void dropTable() {
        try (Connection connection = DatabaseUtil.getConnection(); Statement statement = connection.createStatement()) {

            String sql = "DROP TABLE " + nameTable;

            statement.executeUpdate(sql);
            System.out.println("Table dropped successfully.");

        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle the exception according to your needs
        }
    }

    private void fillComboBox(JComboBox cbo, List<?> arr) {
        cbo.removeAllItems();
        for (Object string : arr) {
            cbo.addItem(string.toString());
        }
    }

    private void fillForm(Entity entity) {
        resetForm();
        Field[] fields = Entity.class.getDeclaredFields();

        for (Field field : fields) {
            System.out.println("field" + field.getName());

            try {
                if (field.getName().equalsIgnoreCase("viTri")) {
                    field.setAccessible(true);

                    System.out.println("vaoday");
                    if (field.get(entity) != null) {
                        cboViTri.setSelectedItem(field.get(entity));
                    }
                }
                field.setAccessible(true);
                Object value = field.get(entity);

                if (field.getName().equalsIgnoreCase("url")) {
                    System.out.println(entity.getUrl());
                    fillImage(entity.getUrl());
                }
                // Assuming there is a corresponding JTextField for each field
                JTextField textField = getTextFieldByName("txt" + capitalize(field.getName()));

                if (textField != null) {
                    textField.setText(value != null ? value.toString() : "");
                    continue;
                }

            } catch (IllegalAccessException e) {

                e.printStackTrace(); // Handle the exception based on your needs
            }
        }
        disableAll();
    }

    private String getSelectedRadio() {
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JRadioButton) {
                JRadioButton rdo = (JRadioButton) component;
                if (rdo.isSelected()) {
                    return rdo.getText();
                }
            }
        }
        return null;
    }

    private String getSelectedCheckbox() {
        Component[] components = getContentPane().getComponents();
        List<String> list = new ArrayList<>();
        for (Component component : components) {
            if (component instanceof JCheckBox) {
                JCheckBox chk = (JCheckBox) component;
                if (chk.isSelected()) {
                    list.add(chk.getText());
                }
            }
        }
        if (list.size() == 0) {
            return "";
        }
        return list.stream().collect(Collectors.joining(", "));
    }

    // Utility method to get a JTextField by name
    private JTextField getTextFieldByName(String fieldName) {
        try {
            Class<?> clazz = getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (JTextField) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace(); // Handle the exception based on your needs
            return null;
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private boolean validateForm() {
        String error = "";
        JTextField firstEmptyField = null;
        Component[] components = getContentPane().getComponents();

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                if (textField.getText().trim().isEmpty()) {
                    if (firstEmptyField == null) {
                        firstEmptyField = textField;
                    }
                    textField.setBorder(new LineBorder(Color.RED));
                    error += "The field " + textField.getName().substring(3).toLowerCase() + " is required.\n";
                } else {
                    textField.setBorder(new LineBorder(Color.GRAY));
                }
            }
        }

        if (!error.isEmpty()) {
            JOptionPane.showMessageDialog(this, error, "Validation Error", JOptionPane.ERROR_MESSAGE);
            if (firstEmptyField != null) {
                firstEmptyField.requestFocus();
            }
            return false;
        }

        return true;
    }

    private void resetForm() {
        Component[] components = getContentPane().getComponents();
        JTextField firstEmptyField = null;

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                if (firstEmptyField == null) {
                    firstEmptyField = textField;
                }
                textField.setText("");
                textField.setEnabled(true);
                textField.setBorder(new LineBorder(Color.GRAY));
            } else if (component instanceof JComboBox) {
                component.setEnabled(true);
                ((JComboBox) component).setSelectedIndex(0);
            }
        }

        if (firstEmptyField != null) {
            firstEmptyField.requestFocus();
        }

        this.lblImage.setIcon(null);
        this.lblImage.setText("Choose image");
        btnEdit.setText("Edit");

        txtMaNhanVien.requestFocus();
    }

    private void grayBorderAll() {
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;

                textField.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
//                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        textField.setBorder(new LineBorder(Color.GRAY));
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
//                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }
                });

            }
        }

    }

    private void validateNumber(JTextField jt) {
        jt.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void disableAll() {
        Component[] components = getContentPane().getComponents();
        JTextField firstEmptyField = null;

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                if (firstEmptyField == null) {
                    firstEmptyField = textField;
                }
                ((JTextField) component).setEnabled(false);
            } else if (component instanceof JComboBox) {
                ((JComboBox) component).setEnabled(false);
            }
        }

        if (firstEmptyField != null) {
            firstEmptyField.requestFocus();
        }
    }

    private void enableAll() {
        Component[] components = getContentPane().getComponents();
        JTextField firstEmptyField = null;

        for (Component component : components) {
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                if (firstEmptyField == null) {
                    firstEmptyField = textField;
                }
                ((JTextField) component).setEnabled(true);
            } else if (component instanceof JComboBox) {
                ((JComboBox) component).setEnabled(true);
            }
        }

        if (firstEmptyField != null) {
            firstEmptyField.requestFocus();
        }
    }

//    private static <T> void setDataToComboBox(JComboBox<T> comboBox, ArrayList<?> data) {
//        Object[] arrayData = data.toArray(new Object[0]);
//        DefaultComboBoxModel<Object> comboBoxModel = new DefaultComboBoxModel<>(arrayData);
//        comboBox.setModel((DefaultComboBoxModel<T>) comboBoxModel);
//        comboBox.addItem((T) data.get(0));
//    }
    private void fillImage(String url) {
        int w = lblImage.getWidth();
        int h = lblImage.getHeight();
        if (url != null) {
            try {
                var img = new ImageIcon(this.getClass().getResource("/img/" + url + ".png")).getImage();
                lblImage.setIcon(new ImageIcon(img.getScaledInstance(w, h, 0)));
            } catch (Exception ex) {
                lblImage.setIcon(null);
                lblImage.setText("No Image");
            }
        } else {
            lblImage.setIcon(null);
            lblImage.setText("No Image");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblTitle = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMaNhanVien = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEntity = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtTenNhanVien = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnSort = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnLoad = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtLuong1Gio = new javax.swing.JTextField();
        txtSoGioLamViec = new javax.swing.JTextField();
        cboViTri = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 51, 51));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("QUẢN LÝ ORDERS");

        jLabel2.setText("Mã nhân viên:");

        txtMaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNhanVienActionPerformed(evt);
            }
        });
        txtMaNhanVien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMaNhanVienKeyTyped(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImage.setText("Choose image");
        lblImage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImageMouseClicked(evt);
            }
        });
        jPanel1.add(lblImage, java.awt.BorderLayout.CENTER);

        tblEntity.setAutoCreateRowSorter(true);
        tblEntity.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Vị trí", "Lương 1 giờ", "Số giờ làm việc", "Thành tiền"
            }
        ));
        tblEntity.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEntityMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEntity);

        jLabel3.setText("Tên nhân viên:");

        txtTenNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenNhanVienActionPerformed(evt);
            }
        });
        txtTenNhanVien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTenNhanVienKeyPressed(evt);
            }
        });

        jButton1.setText("Export");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnSort.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/ranking.gif"))); // NOI18N
        btnSort.setText("Sort");
        btnSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortActionPerformed(evt);
            }
        });

        btnFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/find.png"))); // NOI18N
        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/edit.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/add.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/reset.png"))); // NOI18N
        btnLoad.setText("Load");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        jLabel4.setText("Lương 1 giờ");

        jLabel5.setText("Vị trí");

        jLabel6.setText("Giờ làm việc");

        txtLuong1Gio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLuong1GioActionPerformed(evt);
            }
        });
        txtLuong1Gio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLuong1GioKeyPressed(evt);
            }
        });

        txtSoGioLamViec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoGioLamViecActionPerformed(evt);
            }
        });
        txtSoGioLamViec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSoGioLamViecKeyPressed(evt);
            }
        });

        cboViTri.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quan Ly", "Pha Che", "Thu Ngan" }));

        jButton2.setText("Lọc");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(252, 252, 252)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(99, 99, 99)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnSort)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(121, 121, 121)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel3))
                                .addGap(42, 42, 42)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLuong1Gio, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSoGioLamViec, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cboViTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton2)))))
                        .addGap(12, 12, 12)))
                .addContainerGap(67, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(222, 222, 222))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboViTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2)))
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLuong1Gio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSoGioLamViec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(44, 44, 44))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSort, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if (btnEdit.getText().equalsIgnoreCase("save")) {
            showWarn("In the process updating, pls complete it");
            return;
        }

        if (choosing) {
            showWarn("You in the process view");
            return;
        }

        if (validateForm()) {
            if (repository.findById(this.txtMaNhanVien.getText()) != null) {
                this.showWarn("id is already, pls choose id another");
                return;
            }
            if (repository.createEntity(createEntity())) {
                updateTable();
                showSuccess("Add " + entity + " successfully");
                resetForm();
            } else {
                showErr("Something went wrong");
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private boolean choosing = false;

    private void tblEntityMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEntityMouseClicked
        // TODO add your handling code here:
        if (btnEdit.getText().equalsIgnoreCase("save")) {
            showWarn("In the process updating, pls complete it");
            return;
        }

        int row = tblEntity.getSelectedRow();

        if (row != -1) {
            choosing = true;
            String id = (String) tblEntity.getValueAt(row, 0);
            Entity e = repository.findById(id);
            fillForm(e);
        }

    }//GEN-LAST:event_tblEntityMouseClicked

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        // TODO add your handling code here:
        resetForm();
        choosing = false;

    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        if (!choosing) {
            showWarn("Pls choose the " + entity + " to edit");
            return;
        }
        if (btnEdit.getText().equalsIgnoreCase("edit")) {
            btnEdit.setText("Save");
            enableAll();
            txtMaNhanVien.setEnabled(false);
//            txtName.requestFocus();
        } else {
            if (validateForm()) {
                btnEdit.setText("Edit");
                Entity e = createEntity();
                e.setMaNhanVien(txtMaNhanVien.getText());
                if (e.getUrl() == null) {
                    e.setUrl(repository.findById(this.txtMaNhanVien.getText()).getUrl());
                }
                disableAll();
                if (repository.updateEntity(e)) {
                    showSuccess("Update " + entity + " successfully");
                    updateTable();
                    return;
                }
                showErr("Something went wrong in update process");
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:

        if (btnEdit.getText().equalsIgnoreCase("save")) {
            showWarn("In the process updating, pls complete it");
            return;
        }

        if (!choosing) {
            String id = showInput("Pls enter the id of " + entity + " you want to find");
            if (id != null) {
                if (id.trim() != "") {
                    try {
                        Entity e = repository.findById(id);
                        if (e != null) {
                            if (showConfirm("Do you want to delete the " + entity + " has id " + id)) {
                                if (repository.deleteEntity(id)) {
                                    showSuccess("Deleted the " + entity + " has id " + id);
                                    updateTable();
                                    resetForm();
                                    choosing = false;
                                    return;
                                }
                                showErr("Something went wrong in process delete");
                            }
                        } else {
                            showWarn("The id of " + entity + " you enter is not found");
                        }
                    } catch (Exception e) {
                        showWarn("The id of " + entity + " you enter is not found");
                    }
                } else {
                    showWarn("Id is not empty");
                }
            }

            return;
        } else {
            String id = txtMaNhanVien.getText();
            if (showConfirm("Do you want to delete the " + entity + " has id " + id)) {
                if (showConfirm("You in the process view this " + entity + ", Are you sure?")) {
                    if (repository.deleteEntity(id)) {
                        showSuccess("Deleted the " + entity + " has id " + id);
                        updateTable();
                        resetForm();
                        return;
                    } else {
                        showErr("Something went wrong in the process delete");
                    }
                }
            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        // TODO add your handling code here:

        if (btnEdit.getText().equalsIgnoreCase("save")) {
            showWarn("In the process updating, pls complete it");
            return;
        }

        String id = showInput("Pls enter the id of " + entity + " you want to find");
        if (id != null) {
            if (id.trim() != "") {
                try {
                    Entity e = repository.findById(id);
                    if (e != null) {
                        fillForm(e);
                    } else {
                        showWarn("The id of " + entity + " you enter is not found");
                    }
                } catch (Exception e) {
                    showWarn("The id of " + entity + " you enter is not found");
                }
            } else {
                showWarn("Id is not empty");
            }
        }
    }//GEN-LAST:event_btnFindActionPerformed

    private void lblImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageMouseClicked
        // TODO add your handling code here:
        if (this.txtTenNhanVien.isEnabled()) {
            this.showInputFile();
        }
    }//GEN-LAST:event_lblImageMouseClicked

    private void btnSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortActionPerformed
        // TODO add your handling code here:
        SortService.sort = !SortService.sort;
        this.updateTableSort(SortService.sortInteger(repository.getAllEntity()));
    }//GEN-LAST:event_btnSortActionPerformed

    private void txtMaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNhanVienActionPerformed

    private void txtTenNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenNhanVienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenNhanVienActionPerformed

    private void txtTenNhanVienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTenNhanVienKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenNhanVienKeyPressed

    private void txtMaNhanVienKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaNhanVienKeyTyped
        // TODO add your handling code here:
        if (txtMaNhanVien.getText().length() >= 4) {
            evt.consume();
        }
    }//GEN-LAST:event_txtMaNhanVienKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ArrayList<Entity> list = (ArrayList<Entity>) repository.getAllEntity();
        if (list.isEmpty()) {
            showWarn("The list is empty");

        } else {
            try {
                String path = this.showInputFileString();
                if (path == null) {
                    return;
                }
                MyFile.writeExcel(list, path);
                showSuccess("Save file successfully");
            } catch (Exception ex) {
                showErr("Something went wrong");
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtLuong1GioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLuong1GioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLuong1GioActionPerformed

    private void txtLuong1GioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLuong1GioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLuong1GioKeyPressed

    private void txtSoGioLamViecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoGioLamViecActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoGioLamViecActionPerformed

    private void txtSoGioLamViecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoGioLamViecKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoGioLamViecKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        List<Entity> list  = repository.fillter((String)cboViTri.getSelectedItem());
        System.out.println("list " + list.size());
        this.updateTableSort(list);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void setNameAll() {
        Field[] fields = this.getClass().getDeclaredFields();
        System.out.println("validate form");
        for (Field field : fields) {
            if (field.getName().contains("txt")) {
                try {
                    JTextField textField = (JTextField) field.get(this);
                    textField.setName(field.getName());
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(EntityManagement.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(EntityManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    private String showInput(String mess) {
        String input = JOptionPane.showInputDialog(this, mess, "Input", JOptionPane.INFORMATION_MESSAGE);
        return input;
    }

    private boolean showConfirm(String mess) {
        int choose = JOptionPane.showConfirmDialog(this, mess, "Confirm", JOptionPane.YES_NO_OPTION);
        return choose == 0;
    }

    private void showSuccess(String mess) {
        JOptionPane.showMessageDialog(this, mess, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErr(String mess) {
        JOptionPane.showMessageDialog(this, mess, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarn(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);

    }

    private Entity createEntity() {
        return Entity.builder()
                .maNhanVien(txtMaNhanVien.getText())
                .tenNhanVien(txtTenNhanVien.getText())
                .soGioLamViec(myParseInt(txtSoGioLamViec))
                .viTri((String) cboViTri.getSelectedItem())
                .luong1Gio(myParseInt(txtLuong1Gio))
                .thanhTien(myParseInt(txtSoGioLamViec) * myParseInt(txtLuong1Gio))
                .url(saveImageToDatabase())
                .build();
    }

    private int myParseInt(JTextField string) {
        return Integer.parseInt(string.getText());
    }

    private double myParseDouble(JTextField string) {
        return Integer.parseInt(string.getText());
    }

    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) tblEntity.getModel();
        model.setRowCount(0);

        List<Entity> list = repository.getAllEntity();

        if (!list.isEmpty()) {
            for (Entity entity : list) {
                Object[] rowData = getEntityRowData(entity);
                model.addRow(rowData);
            }
        }
    }

    private void updateTableSort(List<Entity> sortList) {
        DefaultTableModel model = (DefaultTableModel) tblEntity.getModel();
        model.setRowCount(0);

        List<Entity> list = sortList;

        if (!list.isEmpty()) {
            for (Entity entity : list) {
                Object[] rowData = getEntityRowData(entity);
                model.addRow(rowData);
            }
        }
    }

    private Object[] getEntityRowData(Entity entity) {
        List<Object> rowData = new ArrayList<>();

        // Using reflection to dynamically retrieve field values
        Field[] fields = Entity.class
                .getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
//                System.out.println(field.getName() + " " + field.get(entity));
                Object value = field.get(entity);
                rowData.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // Handle the exception based on your needs
            }
        }

        return rowData.toArray();
    }

    private String fileExtension(String fileName) {

        int dotIndex = fileName.lastIndexOf('.');
        String extension = "";
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            extension = fileName.substring(dotIndex + 1);

            System.out.println("Extension của tệp tin: " + extension);
        }
        return extension;
    }

    private File showInputFile() {
        JFileChooser fc = new JFileChooser();

        int returnValue = fc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String fileExtension = fileExtension(file.getName());
            if (!fileExtension.equals("png") && !fileExtension.equals("jpg")) {
                showErr("Please select image file");
                return null;
            }

            try {
                BufferedImage img = ImageIO.read(file);
                lblImage.setText("");
                int w = lblImage.getWidth();
                int h = lblImage.getHeight();
                lblImage.setIcon(new ImageIcon(img.getScaledInstance(w, h, 0)));
                return this.selectedFile = file;

            } catch (IOException ex) {
                Logger.getLogger(EntityManagement.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private String showInputFileString() {
        JFileChooser fc = new JFileChooser();

        int returnValue = fc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fc.getSelectedFile();
            return String.valueOf(selectedFile);
        }

        return null;
    }

    private String saveImageToDatabase() {
        BufferedImage img;
        if (selectedFile == null) {
            return null;
        }
        try {
            img = ImageIO.read(selectedFile);
            String fileNameRandom = SaveImgUtil.generateRandomFileName();
            boolean success = SaveImgUtil.saveImageToResources(img, fileNameRandom);
            if (success) {
                return fileNameRandom;

            }
        } catch (IOException ex) {
            Logger.getLogger(EntityManagement.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "avatar.png";
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EntityManagement.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EntityManagement.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EntityManagement.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EntityManagement.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EntityManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnSort;
    private javax.swing.JComboBox<String> cboViTri;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblEntity;
    private javax.swing.JTextField txtLuong1Gio;
    private javax.swing.JTextField txtMaNhanVien;
    private javax.swing.JTextField txtSoGioLamViec;
    private javax.swing.JTextField txtTenNhanVien;
    // End of variables declaration//GEN-END:variables
}
