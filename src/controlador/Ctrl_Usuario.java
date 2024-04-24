/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import DAO.Crud_Usuario;
import Vistas.Dashboard;
import Vistas.FrmRol;
import Vistas.FrmUsuario;
import conexion.Conexion;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import modelo.Usuario;

public class Ctrl_Usuario implements ActionListener {

    FrmUsuario vista;
    Crud_Usuario crud;

    public Ctrl_Usuario(FrmUsuario vista) {
        this.vista = vista;
        this.crud = new Crud_Usuario();
        Crud_Usuario.CargarTablaUsuarios();
        this.vista.btn_guardar.addActionListener(this);
        this.vista.btn_actualizar.addActionListener(this);
        this.vista.btn_eliminar.addActionListener(this);
        this.vista.btn_buscar.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_buscar) {
            //BUSCA EL CLIENTE SEGUN EL DNI
            if (FrmUsuario.txt_buscar_dniUsuario.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Escribe el DNI del usuario a buscar");

            } else {
                String usuarioBuscar = FrmUsuario.txt_buscar_dniUsuario.getText().trim();
                Connection cn = Conexion.conectar();
                String sql = "SELECT * FROM tb_usuario where DNI = '" + usuarioBuscar + "'";
                Statement st;
                try {
                    st = cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    if (rs.next()) {
                        
                        FrmUsuario.txt_gestionar_nombre.setText(rs.getString("nombre")); //"
                        FrmUsuario.txt_gestionar_apellido.setText(rs.getString("apellido")); //"
                        FrmUsuario.txt_gestionar_dni.setText(rs.getString("dni")); //"
                        FrmUsuario.txt_gestionar_usuario.setText(rs.getString("usuario")); //"
                        FrmUsuario.txt_gestionar_password.setText(rs.getString("password")); //"
                        FrmUsuario.txt_gestionar_telefono.setText(rs.getString("telefono")); //"
                    } else {
                        JOptionPane.showMessageDialog(null, "¡Dni de usuario incorrecta o no encontrada!");
                    }
                    FrmUsuario.txt_buscar_dniUsuario.setText("");
                    cn.close();
                } catch (SQLException a) { // como el "e" está ocupado se coloca "a
                    System.out.println("¡Error al buscar usuario!, " + a);
                }
            }
            
        }
        if (e.getSource() == vista.btn_guardar) {
            //VALIDADAR QUE LOS CAMPOS NO ESTRIAN NNUOS
            if (FrmUsuario.txt_nombre.getText().isEmpty() || FrmUsuario.txt_apellido.getText().isEmpty() || FrmUsuario.txt_dni.getText().isEmpty() || FrmUsuario.txt_usuario.getText().isEmpty()
                    || FrmUsuario.txt_password.getText().isEmpty() || FrmUsuario.txt_telefono.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Completa todos los campos");
            } else {
                //validamos si el usuaro ya esta registrado
                Usuario usuario = new Usuario();
                Crud_Usuario controlUsuario = new Crud_Usuario();
                if (!controlUsuario.existeUsuario(FrmUsuario.txt_dni.getText().trim())) {
                    //enviamos datos del usuario
                    usuario.setNombre(FrmUsuario.txt_nombre.getText().trim());
                    usuario.setApellido(FrmUsuario.txt_apellido.getText().trim());
                    usuario.setDni(FrmUsuario.txt_dni.getText().trim());
                    usuario.setUsuario(FrmUsuario.txt_usuario.getText().trim());
                    usuario.setPassword(FrmUsuario.txt_password.getText().trim());
                    usuario.setTelefono(FrmUsuario.txt_telefono.getText().trim());
                    usuario.setEstado(1);

                    if (controlUsuario.guardar(usuario)) {
                        JOptionPane.showMessageDialog(null, "¡Usuario Registrado!");
                        Crud_Usuario.CargarTablaUsuarios();
                    } else {
                        JOptionPane.showMessageDialog(null, "¡Error al registrar Usuario!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El Usuario ya esta registrado, ingrese otro.");
                }
            }
            Crud_Usuario.Limpiar();

        }

        if (e.getSource() == vista.btn_actualizar) {
            int idUsuario = Crud_Usuario.idUsuario; //obtine el id desde el crud y ya se soluciona el error
            Usuario usuario = new Usuario();
            Crud_Usuario controlUsuario = new Crud_Usuario();

            if (idUsuario == 0) {
                JOptionPane.showMessageDialog(null, "¡Seleccione en la tabla un Usuario!");
            } else {
                if (FrmUsuario.txt_gestionar_nombre.getText().isEmpty() || FrmUsuario.txt_gestionar_apellido.getText().isEmpty() || FrmUsuario.txt_gestionar_rol.getText().isEmpty() || FrmUsuario.txt_gestionar_dni.getText().isEmpty() || FrmUsuario.txt_gestionar_usuario.getText().isEmpty()
                        || FrmUsuario.txt_gestionar_password.getText().isEmpty() || FrmUsuario.txt_gestionar_telefono.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "¡Completa todos los campos!");

                } else {
                    usuario.setNombre(FrmUsuario.txt_gestionar_nombre.getText().trim());
                    usuario.setApellido(FrmUsuario.txt_gestionar_apellido.getText().trim());
                    usuario.setDni(FrmUsuario.txt_gestionar_dni.getText().trim());
                    usuario.setIdRol(Integer.parseInt(FrmUsuario.txt_gestionar_rol.getText().trim()));
                    usuario.setUsuario(FrmUsuario.txt_gestionar_usuario.getText().trim());
                    usuario.setPassword(FrmUsuario.txt_gestionar_password.getText().trim());
                    usuario.setTelefono(FrmUsuario.txt_gestionar_telefono.getText().trim());
                    usuario.setEstado(1);

                    if (controlUsuario.actualizar(usuario, idUsuario)) {
                        JOptionPane.showMessageDialog(null, "¡Actualizacion Exitosa!");
                        Crud_Usuario.Limpiar2();
                        Crud_Usuario.CargarTablaUsuarios();
                        idUsuario = 0;

                    } else {
                        JOptionPane.showMessageDialog(null, "¡Error al Actualizar usuario desde el ctrl!");

                    }
                }
            }

        }

        if (e.getSource() == vista.btn_eliminar) {
            Crud_Usuario controlUsuario = new Crud_Usuario();
            int idUsuario = Crud_Usuario.idUsuario;
            if (idUsuario == 0) {
                JOptionPane.showMessageDialog(null, "¡Seleccione un usuario!");
            } else {
                if (!controlUsuario.eliminar(idUsuario)) {
                    JOptionPane.showMessageDialog(null, "¡ususario Eliminado!");
                    Crud_Usuario.CargarTablaUsuarios();
                    Crud_Usuario.Limpiar2();
                } else {
                    JOptionPane.showMessageDialog(null, "¡Error al eliminar usuario!");
                    Crud_Usuario.Limpiar2();
                }
            }
        }
        
        //PARA GESTIONAR BOTONES PARA EDITAR roles
        if (e.getSource() == vista.btn_Roles) {
            FrmRol p11 = new FrmRol();
            Dashboard.ShowPanel(p11);
        }

    }

}
