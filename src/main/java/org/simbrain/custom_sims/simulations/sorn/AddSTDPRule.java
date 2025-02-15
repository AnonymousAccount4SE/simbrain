package org.simbrain.custom_sims.simulations.sorn;

import org.simbrain.network.core.Synapse;
import org.simbrain.network.core.SynapseUpdateRule;
import org.simbrain.network.synapse_update_rules.STDPRule;
import org.simbrain.network.util.ScalarDataHolder;

public class AddSTDPRule extends STDPRule {

    private boolean srcSpk = false;

    private boolean tarSpk = false;

    @Override
    public void init(Synapse synapse) {
    }

    @Override
    public String getName() {
        return "STDP";
    }

    @Override
    public SynapseUpdateRule deepCopy() {
        STDPRule duplicateSynapse = new STDPRule();
        duplicateSynapse.setTau_minus(this.getTau_minus());
        duplicateSynapse.setTau_plus(this.getTau_plus());
        duplicateSynapse.setW_minus(this.getW_minus());
        duplicateSynapse.setW_plus(this.getW_plus());
        duplicateSynapse.setLearningRate(this.getLearningRate());
        return duplicateSynapse;
    }

    @Override
    public void apply(Synapse s, ScalarDataHolder data) {
        boolean ss = s.getSource().isSpike();
        boolean st = s.getTarget().isSpike();

        double str = s.getStrength();

        str += learningRate * ((srcSpk && st ? 1 : 0) - (tarSpk && ss ? 1 : 0));
        s.setStrength(s.clip(str));
        srcSpk = ss;
        tarSpk = st;

    }
}
