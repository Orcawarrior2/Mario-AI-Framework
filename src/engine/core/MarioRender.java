package engine.core;

import javax.swing.*;

import engine.helper.Assets;
import engine.helper.MarioActions;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


public class MarioRender extends JComponent implements FocusListener {
    private static final long serialVersionUID = 790878775993203817L;
    public static final int TICKS_PER_SECOND = 24;

    private float scale;
    private GraphicsConfiguration graphicsConfiguration;

    int frame;
    Thread animator;
    boolean focused;

    public MarioRender(float scale) {
        this.setFocusable(true);
        this.setEnabled(true);
        this.scale = scale;

        Dimension size = new Dimension((int) (256 * scale), (int) (240 * scale));

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        setFocusable(true);
    }

    public void init() {
        graphicsConfiguration = getGraphicsConfiguration();
        Assets.init(graphicsConfiguration);
    }

    public void renderWorld(MarioWorld world, Image image, Graphics g, Graphics og) {
        og.fillRect(0, 0, 256, 240);
        world.render(og);
        if(world.isPaused) drawStringDropShadow(og, "Game Paused", 6, 4, 7, 16);
        drawStringDropShadow(og, "Lives: " + world.lives, 0, 0, 7, 1);
        drawStringDropShadow(og, "Coins: " + world.coins, 11, 0, 7, 1);
        drawStringDropShadow(og, "Time: " + (world.currentTimer == -1 ? "Inf" : (int) Math.ceil(world.currentTimer / 1000f)),
                22, 0, 7, 1);
        if (MarioGame.verbose) {
            String pressedButtons = "";
            for (int i = 0; i < world.mario.actions.length; i++) {
                if (world.mario.actions[i]) {
                    pressedButtons += MarioActions.getAction(i).getString() + " ";
                }
            }
            drawStringDropShadow(og, "Buttons: " + pressedButtons, 0, 2, 1, 1);
        }
        if (scale > 1) {
            g.drawImage(image, 0, 0, (int) (256 * scale), (int) (240 * scale), null);
        } else {
            g.drawImage(image, 0, 0, null);
        }
    }

    public void drawStringDropShadow(Graphics g, String text, int x, int y, int c, int scale) {
        drawString(g, text, x * 8 + 5, y * 8 + 5, 0, scale);
        drawString(g, text, x * 8 + 4, y * 8 + 4, c, scale);
    }

    private void drawString(Graphics g, String text, int x, int y, int c, int scale) {
        char[] ch = text.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if(scale > 1) {
                g.drawImage(Assets.font[ch[i] - 32][c], x + (i * scale), y,
                        scale, scale, null);
            } else {
                g.drawImage(Assets.font[ch[i] - 32][c], x + i * 8, y, null);
            }
        }
    }

    public void focusGained(FocusEvent arg0) {
        focused = true;
    }

    public void focusLost(FocusEvent arg0) {
        focused = false;
    }
}