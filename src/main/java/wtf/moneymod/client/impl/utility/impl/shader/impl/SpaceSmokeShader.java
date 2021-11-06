package wtf.moneymod.client.impl.utility.impl.shader.impl;

import org.lwjgl.opengl.GL20;
import wtf.moneymod.client.impl.utility.impl.shader.FramebufferShader;

public class SpaceSmokeShader extends FramebufferShader {

    float time = 1.0f;

    public SpaceSmokeShader(String fragmentShader) {
        super(fragmentShader);
    }

    @Override public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("time");
        this.setupUniform("resolution");
    }

    @Override public void updateUniforms() {
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / this.mc.displayWidth * (this.radius * this.quality), 1.0f / this.mc.displayHeight * (this.radius * this.quality));
        GL20.glUniform4f(this.getUniform("color"), this.red, this.green, this.blue, this.alpha);
        GL20.glUniform1f(this.getUniform("time"), time);
        GL20.glUniform2f(this.getUniform("resolution"), mc.displayWidth, mc.displayHeight);
        time += 0.003;
    }

    public static final FramebufferShader INSTANCE = new SpaceSmokeShader("spacesmoke.frag");

}
