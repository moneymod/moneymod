package wtf.moneymod.client.impl.utility.impl.shader.impl;

import org.lwjgl.opengl.GL20;
import wtf.moneymod.client.impl.utility.impl.shader.FramebufferShader;

public class ShadowShader extends FramebufferShader {

    public static final FramebufferShader INSTANCE = new ShadowShader("shadow.frag");

    public ShadowShader(String fragmentShader) {
        super(fragmentShader);
    }

    @Override public void setupUniforms() {
        setupUniform("radius");
        setupUniform("resolution");
        setupUniform("in_color");
        setupUniform("p1");
        setupUniform("p2");
        setupUniform("shadow_pos");
        setupUniform("shadow_blur");
        setupUniform("shadow_color");
    }

    @Override public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("radius"), 2);
        GL20.glUniform2f(this.getUniform("resolution"), 100, 20);
        GL20.glUniform4f(this.getUniform("in_color"), red, green, blue, alpha);
        GL20.glUniform2f(this.getUniform("p1"), 0, 0);
        GL20.glUniform2f(this.getUniform("p2"), 0, 0);
        GL20.glUniform2f(this.getUniform("shadow_pos"), 0, 0);
        GL20.glUniform1f(this.getUniform("shadow_blur"), 0);
        GL20.glUniform4f(this.getUniform("shadow_color"), 0,0,0,0);

    }

}
