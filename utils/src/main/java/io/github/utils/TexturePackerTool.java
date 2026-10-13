package io.github.utils;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
    public static void main(String[] args) {
        String inputDir = "assets_raw/objects";
        String outputDir = "assets/graphics";
        String packFilename = "objects";

        TexturePacker.process(inputDir, outputDir, packFilename);
    }
}
