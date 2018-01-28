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
package org.simbrain.world.odorworld.sensors;

import org.simbrain.world.odorworld.entities.OdorWorldEntity;

/**
 * Very simple bump sensor. Holding off on more sophisticated "touch" sensors in
 * case an existing library can provide it.
 * <p>
 * TODO: Implement once collisions are implemented. At that point can rename to
 * collision sensor? Can also give the sensor a location and make it visible.
 */
public class BumpSensor extends Sensor {

    /**
     * Whether it was bumped.
     */
    private boolean wasBumped = false;

    /**
     * Value to produce when bumped.
     */
    private double bumpValue = 0;

    /**
     * Construct bump sensor.
     *
     * @param parent  parent entity
     * @param bumpVal value
     */
    public BumpSensor(OdorWorldEntity parent, double bumpVal) {
        super(parent, "" + bumpVal);
        this.bumpValue = bumpVal;
    }

    /**
     * {@inheritDoc}
     */
    public void update() {
        // if (wasBumped()) {
        // return new Double(bumpValue);
        // } else
        // return new Double(0);
    }

    /**
     * @return the wasBumped
     */
    public boolean wasBumped() {
        return wasBumped;
    }

    /**
     * @param wasBumped the wasBumped to set
     */
    public void setBumped(boolean wasBumped) {
        this.wasBumped = wasBumped;
    }

    @Override
    public String getTypeDescription() {
        return "Bump";
    }

}
