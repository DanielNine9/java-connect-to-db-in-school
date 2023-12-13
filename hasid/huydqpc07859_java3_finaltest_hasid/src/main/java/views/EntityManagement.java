/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import models.Entity;
import repositories.EntityRepository;
import services.SortService;
import utils.MyFile;
import utils.SaveImgUtil;

/**
 *
 * @author huyyd
 */
public class EntityManagement extends javax.swing.JFrame {

    private File selectedFile = null;
    private EntityRepository repository = new EntityRepository();
    private final String entity = "order";

    /**
     * Creates new form EntityManagement
     */
    public EntityManagement() {
        initComponents();
        txtId.setName("txtId");
        lblTitle.setText("QUẢN LÝ " + (entity + "s").toUpperCase());
        updateTable();
        txtId.setEnabled(false);
        this.setLocationRelativeTo(null);

        txtName.setName("txtName");

        grayBorderAll();

//        List<Object> arr = new ArrayList<>(Arrays.asList(Entity.builder().name("control").build().getName(), "Ly", "Hoa"));
//        fillComboBox(cbo, arr);
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
            try {
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

//                if (field.getName().equalsIgnoreCase("subject")) {
//                    if (field.get(entity) != null) {
//                        cboMarks.setSelectedItem(field.get(entity).toString());
//                    }
//                }
            } catch (IllegalAccessException e) {

                e.printStackTrace(); // Handle the exception based on your needs
            }
        }
        disableAll();
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
                if (firstEmptyField == null && !component.getName().equalsIgnoreCase("txtId")) {
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

        txtId.setEnabled(false);
        txtId.setText("Id sẽ tự sinh");
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
        txtId = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEntity = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnLoad = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSort = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();

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

        jLabel2.setText("ID:");

        txtId.setEditable(false);
        txtId.setText("Id sẽ tự sinh");
        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
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
        jPanel1.add(lblImage, java.awt.BorderLayout.PAGE_START);

        tblEntity.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã đơn hàng", "Tên hàng", "Đơn vị tính", "Đơn giá", "Số lượng", "Thành tiền"
            }
        ));
        tblEntity.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEntityMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEntity);

        jLabel3.setText("Tên hàng:");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNameKeyPressed(evt);
            }
        });

        jButton1.setText("Export");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnLoad.setText("Load");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSort.setText("Sort");
        btnSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortActionPerformed(evt);
            }
        });

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEdit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSort, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41))
                            .addComponent(jButton1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 77, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(235, 235, 235)
                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSort, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addContainerGap(149, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if (btnEdit.getText().equalsIgnoreCase("save")) {
            showWarn("In the process updating, pls complete it");
            return;
        }

        if (!txtId.getText().equalsIgnoreCase("Id sẽ tự sinh")) {
            showWarn("You in the process view");
            return;
        }
        if (validateForm()) {
            if (repository.createEntity(createEntity())) {
                updateTable();
                showSuccess("Add " + entity + " successfully");
                resetForm();
            } else {
                showErr("Something went wrong");
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void tblEntityMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEntityMouseClicked
        // TODO add your handling code here:
        if (btnEdit.getText().equalsIgnoreCase("save")) {
            showWarn("In the process updating, pls complete it");
            return;
        }

        int row = tblEntity.getSelectedRow();

        if (row != -1) {
            int id = (int) tblEntity.getValueAt(row, 0);
            Entity e = repository.findById(id);
            fillForm(e);
        }

    }//GEN-LAST:event_tblEntityMouseClicked

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        // TODO add your handling code here:
        resetForm();
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        if (txtId.getText().equalsIgnoreCase("Id sẽ tự sinh")) {
            showWarn("Pls choose the " + entity + " to edit");
            return;
        }
        if (btnEdit.getText().equalsIgnoreCase("edit")) {
            btnEdit.setText("Save");
            enableAll();
            txtId.setEnabled(false);
//            txtName.requestFocus();
        } else {
            if (validateForm()) {
                btnEdit.setText("Edit");
                Entity e = createEntity();
                e.setId(Integer.parseInt(txtId.getText()));
                if (e.getUrl() == null) {
                    e.setUrl(repository.findById(Integer.parseInt(this.txtId.getText())).getUrl());
                }
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

        if (txtId.getText().equalsIgnoreCase("Id sẽ tự sinh")) {
            String id = showInput("Pls enter the id of " + entity + " you want to find");
            if (id != null) {
                if (id.trim() != "") {
                    try {
                        Entity e = repository.findById(Integer.parseInt(id));
                        if (e != null) {
                            if (showConfirm("Do you want to delete the " + entity + " has id " + id)) {
                                if (repository.deleteEntity(Integer.parseInt(id))) {
                                    showSuccess("Deleted the " + entity + " has id " + id);
                                    updateTable();
                                    resetForm();
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
            Integer id = Integer.parseInt(txtId.getText());
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
                    Entity e = repository.findById(Integer.parseInt(id));
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
        if (this.txtName.isEnabled()) {
            this.showInputFile();
        }
    }//GEN-LAST:event_lblImageMouseClicked

    private void btnSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortActionPerformed
        // TODO add your handling code here:
        SortService.sort = !SortService.sort;
        this.updateTableSort(SortService.sortIncrement(repository.getAllEntity()));
    }//GEN-LAST:event_btnSortActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameKeyPressed

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

    private String showInputFileString() {
        JFileChooser fc = new JFileChooser();

        int returnValue = fc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fc.getSelectedFile();
            return String.valueOf(selectedFile);
        }

        return null;
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
                .name(txtName.getText())
                .url(saveImageToDatabase())
                .build();
    }

    private int myParseInt(String string) {
        return Integer.parseInt(string);
    }

    private String getText(JTextField jt) {
        return jt.getText();
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblEntity;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
