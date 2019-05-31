package org.simbrain.network.gui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The visible label showing the time step as defined by the model network
 * class.
 */
public class TimeLabel extends JLabel {

    /**
     * Reference to parent NetworkPanel.
     */
    private NetworkPanel networkPanel;

    /**
     * Construct time label with reference to networkPanel.
     *
     * @param netPanel reference to networkPanel
     */
    public TimeLabel(final NetworkPanel netPanel) {
        super();
        networkPanel = netPanel;
        update();
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent event) {
                if (event.getClickCount() == 2) {
                    networkPanel.getNetwork().setTime(0);
                    update();
                }
            }
        });
    }

    /**
     * Update the text based on the network time.
     */
    public void update() {
        setText(networkPanel.getNetwork().getTimeLabel());
    }

}
