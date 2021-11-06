package wtf.moneymod.client.impl.utility.impl.shader.impl;

import org.lwjgl.opengl.GL20;
import wtf.moneymod.client.impl.utility.impl.shader.FramebufferShader;

public class GlowShader extends FramebufferShader {

    public GlowShader(String fragmentShader) {
        super(fragmentShader);
    }

    @Override public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("radius");
        this.setupUniform("devider");
        this.setupUniform("maxSample");
        this.setupUniform("rainbowStrength");
        this.setupUniform("rainbowSpeed");
        this.setupUniform("saturation");
    }

    @Override public void updateUniforms() {
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / this.mc.displayWidth * (this.radius * this.quality), 1.0f / this.mc.displayHeight * (this.radius * this.quality));
        GL20.glUniform3f(this.getUniform("color"), this.red, this.green, this.blue);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
        GL20.glUniform2f(this.getUniform("rainbowStrength"), x, y);
        GL20.glUniform1f(this.getUniform("rainbowSpeed"), speed);
        GL20.glUniform1f(this.getUniform("saturation"), saturation);
    }

    public static final FramebufferShader INSTANCE = new GlowShader("glow.frag");

}

