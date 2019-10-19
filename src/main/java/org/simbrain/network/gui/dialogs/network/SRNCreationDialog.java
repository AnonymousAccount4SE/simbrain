/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.network.gui.dialogs.network;

import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.neuron_update_rules.LinearRule;
import org.simbrain.network.neuron_update_rules.SigmoidalRule;
import org.simbrain.network.subnetworks.SimpleRecurrentNetwork;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.StandardDialog;
import org.simbrain.util.math.SquashingFunctionEnum;
import org.simbrain.util.widgets.ShowHelpAction;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Creates a GUI dialog to set the parameters for and then build a simple
 * recurrent network.
 *
 * @author Jeff Yoshimi
 */

@SuppressWarnings("serial")
public class SRNCreationDialog extends StandardDialog {

    /**
     * Underlying Network Panel
     */
    private final NetworkPanel panel;

    /**
     * Underlying labeled item panel for dialog
     */
    private LabelledItemPanel srnPanel = new LabelledItemPanel();

    /**
     * Text field for number of input nodes
     */
    private JTextField tfNumInputs = new JTextField();

    /**
     * Text field for number of hidden layer nodes
     */
    private JTextField tfNumHidden = new JTextField();

    /**
     * Text field for number of output nodes
     */
    private JTextField tfNumOutputs = new JTextField();

    /**
     * Maps string values to corresponding NeuronUpdateRules for the combo-boxes
     * governing desired Neuron type for a given layer
     */
    private HashMap<String, NeuronUpdateRule> boxMap = new HashMap<String, NeuronUpdateRule>();

    /**
     * Mapping of Strings to NeuronUpdateRules, currently only Logisitc, Tanh,
     * and Linear neurons are allowed.
     */ {
        boxMap.put("Linear", new LinearRule());
        SigmoidalRule sig0 = new SigmoidalRule();
        sig0.setSquashFunctionType(SquashingFunctionEnum.LOGISTIC);
        boxMap.put("Logistic", sig0);
        SigmoidalRule sig1 = new SigmoidalRule();
        sig1.setSquashFunctionType(SquashingFunctionEnum.TANH);
        boxMap.put("Tanh", sig1);
    }

    /**
     * String values for combo-boxes (same as key values for boxMap)
     */
    private String[] options = {"Linear", "Tanh", "Logistic"};

    /**
     * Combo box for selecting update rule for the hidden layer
     */
    private JComboBox hiddenNeuronTypes = new JComboBox(options);

    /**
     * Combo box for selecting the update rule for the output layer
     */
    private JComboBox outputNeuronTypes = new JComboBox(options);

    /**
     * Constructs a labeled item panel dialog for the creation of a simple
     * recurrent network.
     *
     * @param panel the network panel the SRN will be tied to
     */
    public SRNCreationDialog(final NetworkPanel panel) {
        this.panel = panel;

        // Grid bag constraints for manual positioning see #sectionSeparator
        GridBagConstraints gbc = new GridBagConstraints();

        setTitle("Build Simple Recurrent Network");

        // Set grid bag constraints
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        srnPanel.setMyNextItemRow(1);
        gbc.gridy = srnPanel.getMyNextItemRow();
        gbc.gridx = 0;

        // Add separator and title for fields below the separator
        sectionSeparator("Network Parameters", gbc, 1);

        // Add fields
        tfNumInputs.setColumns(5);
        srnPanel.addItem("Number of input nodes:", tfNumInputs);
        srnPanel.addItem("Hidden Neuron Type:", hiddenNeuronTypes, 2);
        srnPanel.addItem("Number of hidden nodes:", tfNumHidden);
        srnPanel.addItem("Output Neuron Type:", outputNeuronTypes, 2);
        srnPanel.addItem("Number of output nodes:", tfNumOutputs);

        // Fill fields with default values
        fillFieldValues();

        // Help button
        Action helpAction = new ShowHelpAction("Pages/Network/network/srn.html");
        addButton(new JButton(helpAction));

        setContentPane(srnPanel);
    }

    /**
     * Creates a new dialog section given a title and using a JSeparator.
     *
     * @param label name of the section
     * @param gbc   current GridBagConstraints, to align label and separators
     * @param cRow  current row relative to LabeledItemPanel
     */
    public void sectionSeparator(String label, GridBagConstraints gbc, int cRow) {
        // Section label
        srnPanel.add(new JLabel(label), gbc);

        // Place separator directly below label
        cRow++;
        srnPanel.setMyNextItemRow(cRow);
        gbc.gridy = srnPanel.getMyNextItemRow();

        // Add separators upping gridx each time to cover each column
        srnPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
        gbc.gridx = 1;
        srnPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
        gbc.gridx = 2;
        srnPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbc);

        // Ensures section content will be below section separator
        cRow++;
        srnPanel.setMyNextItemRow(cRow);
        // Reset column value
        gbc.gridx = 0;
    }

    /**
     * Fills the fields with default values.
     */
    public void fillFieldValues() {
        //TODO: These default values should be pulled from the model srn
        tfNumInputs.setText("" + 5);
        tfNumHidden.setText("" + 7);
        tfNumOutputs.setText("" + 5);
        hiddenNeuronTypes.setSelectedIndex(2);
        outputNeuronTypes.setSelectedIndex(2);
    }

    @Override
    public void closeDialogOk() {
        try {

            NeuronUpdateRule hidType = boxMap.get(hiddenNeuronTypes.getSelectedItem());
            NeuronUpdateRule outType = boxMap.get(outputNeuronTypes.getSelectedItem());
            SimpleRecurrentNetwork srn = new SimpleRecurrentNetwork(panel.getNetwork(), Integer.parseInt(tfNumInputs.getText()), Integer.parseInt(tfNumHidden.getText()), Integer.parseInt(tfNumOutputs.getText()), hidType, outType,
                    panel.getPlacementManager().getLocation());

            srn.getParentNetwork().addGroup(srn);
            dispose();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Inappropriate Field Values:" + "\nNetwork construction failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        panel.repaint();
    }

}
