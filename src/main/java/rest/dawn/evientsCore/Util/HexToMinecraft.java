package rest.dawn.evientsCore.Util;

import java.awt.*;

// Stolen from ChatGPT
public class HexToMinecraft {
    public static String convertHexToMiniMessageTag(String hexColor) {
        Color color = Color.decode(hexColor);
        int rgb = color.getRGB();
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        return getClosestMiniMessageTag(r, g, b);
    }

    private static String getClosestMiniMessageTag(int r, int g, int b) {
        // Define RGB values for the 16 Minecraft colors and their MiniMessage tags
        int[][] legacyColors = {
                {0, 0, 0}, // black
                {0, 0, 170}, // dark blue
                {0, 170, 0}, // dark green
                {0, 170, 170}, // dark aqua
                {170, 0, 0}, // dark red
                {170, 0, 170}, // dark purple
                {255, 170, 0}, // gold
                {170, 170, 170}, // gray
                {85, 85, 85}, // dark gray
                {85, 85, 255}, // blue
                {85, 255, 85}, // green
                {85, 255, 255}, // aqua
                {255, 85, 85}, // red
                {255, 85, 255}, // light purple
                {255, 255, 85}, // yellow
                {255, 255, 255}, // white
        };

        String[] miniMessageTags = {
                "black", "dark_blue", "dark_green", "dark_aqua",
                "dark_red", "dark_purple", "gold", "gray",
                "dark_gray", "blue", "green", "aqua",
                "red", "light_purple", "yellow", "white"
        };


        // Find the closest legacy color by comparing RGB values
        int closestIndex = 0;
        int closestDistance = Integer.MAX_VALUE;

        for (int i = 0; i < legacyColors.length; i++) {
            int[] legacyColor = legacyColors[i];
            int distance = calculateColorDistance(r, g, b, legacyColor[0], legacyColor[1], legacyColor[2]);

            if (distance < closestDistance) {
                closestDistance = distance;
                closestIndex = i;
            }
        }

        // Return the Minecraft MiniMessage tag
        return miniMessageTags[closestIndex];
    }

    private static int calculateColorDistance(int r1, int g1, int b1, int r2, int g2, int b2) {
        return (int) Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2));
    }
}
