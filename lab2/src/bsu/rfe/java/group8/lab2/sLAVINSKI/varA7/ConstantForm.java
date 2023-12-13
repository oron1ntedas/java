package bsu.rfe.java.group8.lab2.sLAVINSKI.varA7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import static java.lang.Math.*;

public class ConstantForm extends JFrame {
     private static final int WIDTH = 400;
     private static final int HEIGHT = 320;
     private JTextField textFieldX;
     private JTextField textFieldY;
     private JTextField textFieldZ;
     private JTextField textFieldResult;
     private ButtonGroup radioButtons = new ButtonGroup();
     private Box hboxFormulaType = Box.createHorizontalBox();
     private int formulaId = 1;
     private double result, sum=0;
     private Double f1_cal(Double x, Double y, Double z) {

          return Math.pow(Math.log(Math.pow(1+z,2)) + Math.cos(PI*Math.pow(y,3)), 1. / 4.)/ Math.pow((Math.cos(Math.exp(x))+ Math.sqrt(1/x)+Math.exp(Math.pow(x,2))),Math.sin(x)) ;
     }

     private Double f2_cal(Double x, Double y, Double z) {
          return Math.pow(Math.sin(Math.pow(z,y)),2)/Math.sqrt(1+Math.pow(x,3));
     }
     private void addRadioButton(String buttonName, final int formulaId) {
          JRadioButton button = new JRadioButton(buttonName);
          button.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ev) {
                    ConstantForm.this.formulaId = formulaId;
                    //imagePane.updateUI();
               }
          });
          radioButtons.add(button);
          hboxFormulaType.add(button);
     }
     public ConstantForm() {
          super("Вычисление формулы");
          setSize(WIDTH, HEIGHT);
          Toolkit kit = Toolkit.getDefaultToolkit();
          setLocation((kit.getScreenSize().width - WIDTH)/2,
                  (kit.getScreenSize().height - HEIGHT)/2);


          JButton m_plus = new JButton("M+");
          m_plus.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ev) {
                    sum+=result;
                    result=0;
                    textFieldResult.setText(String.valueOf(sum));
               }
          });

          JButton mc = new JButton("MC");
          mc.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ev) {
                    sum=0;
                    textFieldResult.setText("0");
               }
          });

          Box hboxbutton_M = Box.createHorizontalBox();
          hboxbutton_M.add(Box.createHorizontalGlue());
          hboxbutton_M.add(m_plus);
          hboxbutton_M.add(Box.createHorizontalStrut(10));
          hboxbutton_M.add(mc);
          hboxbutton_M.add(Box.createHorizontalGlue());
          hboxbutton_M.setBorder(
                  BorderFactory.createLineBorder(Color.GREEN));


          hboxFormulaType.add(Box.createHorizontalGlue());
          addRadioButton("Формула 1", 1);
          addRadioButton("Формула 2", 2);
          radioButtons.setSelected(
                  radioButtons.getElements().nextElement().getModel(), true);
          //hboxFormulaType.add(Box.createHorizontalGlue());
          hboxFormulaType.setBorder(
                  BorderFactory.createLineBorder(Color.YELLOW));
          JLabel labelForX = new JLabel("X:");
          textFieldX = new JTextField("0", 10);
          textFieldX.setMaximumSize(textFieldX.getPreferredSize());
          JLabel labelForY = new JLabel("Y:");
          textFieldY = new JTextField("0", 10);
          textFieldY.setMaximumSize(textFieldY.getPreferredSize());
          JLabel labelForZ = new JLabel("Z:");
          textFieldZ = new JTextField("0", 10);
          textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());
          Box hboxVariables = Box.createHorizontalBox();
          hboxVariables.setBorder(
                  BorderFactory.createLineBorder(Color.RED));
         // hboxVariables.add(Box.createHorizontalGlue());
          hboxVariables.add(labelForX);
          hboxVariables.add(Box.createHorizontalStrut(10));
          hboxVariables.add(textFieldX);
          hboxVariables.add(Box.createHorizontalGlue());
          hboxVariables.add(Box.createHorizontalStrut(10));
          hboxVariables.add(labelForY);
          hboxVariables.add(Box.createHorizontalStrut(10));
          hboxVariables.add(textFieldY);
          hboxVariables.add(Box.createHorizontalGlue());
          hboxVariables.add(Box.createHorizontalStrut(10));
          hboxVariables.add(labelForZ);
          hboxVariables.add(Box.createHorizontalStrut(10));
          hboxVariables.add(textFieldZ);


          hboxFormulaType.add(Box.createHorizontalGlue());
          hboxFormulaType.setBorder(
                  BorderFactory.createLineBorder(Color.BLACK));


          JLabel labelForResult = new JLabel("Результат:");
          textFieldResult = new JTextField("0", 10);
          textFieldResult.setMaximumSize(
                  textFieldResult.getPreferredSize());
          Box hboxResult = Box.createHorizontalBox();
          hboxResult.add(Box.createHorizontalGlue());
          hboxResult.add(labelForResult);
          hboxResult.add(Box.createHorizontalStrut(10));
          hboxResult.add(textFieldResult);
          hboxResult.add(Box.createHorizontalGlue());
          hboxResult.setBorder(BorderFactory.createLineBorder(Color.BLUE));

          JButton buttonCalc = new JButton("Вычислить");
          buttonCalc.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ev) {
                    try {
                         Double x = Double.parseDouble(textFieldX.getText());
                         Double y = Double.parseDouble(textFieldY.getText());
                         Double z = Double.parseDouble(textFieldZ.getText());
                         if (formulaId==1)
                              result = f1_cal(x, y, z);
                         else
                              result = f2_cal(x, y, z);
                         textFieldResult.setText(String.valueOf(result));
                    } catch (NumberFormatException ex) {
                         JOptionPane.showMessageDialog(ConstantForm.this,
                                 "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                                 JOptionPane.WARNING_MESSAGE);
                    }
               }
          });
          JButton buttonReset = new JButton("Очистить поля");
          buttonReset.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent ev) {
                    textFieldX.setText("0");
                    textFieldY.setText("0");
                    textFieldZ.setText("0");
                    textFieldResult.setText("0");
               }
          });



          Box hboxButtons = Box.createHorizontalBox();
          //hboxButtons.add(Box.createHorizontalGlue());
          hboxButtons.add(buttonCalc);
          hboxButtons.add(Box.createHorizontalGlue());
          hboxButtons.add(Box.createHorizontalStrut(30));
          hboxButtons.add(buttonReset);
          //hboxButtons.add(Box.createHorizontalGlue());
          hboxButtons.setBorder(
                  BorderFactory.createLineBorder(Color.GREEN));

          Box contentBox = Box.createVerticalBox();
          contentBox.add(Box.createVerticalGlue());
          //contentBox.add(hboxbutton_M);
          contentBox.add(hboxFormulaType);
          contentBox.add(hboxVariables);
          contentBox.add(hboxResult);
          contentBox.add(hboxButtons);
          contentBox.add(hboxbutton_M);
          contentBox.add(Box.createVerticalGlue());
          getContentPane().add(contentBox, BorderLayout.CENTER);
     }

}
