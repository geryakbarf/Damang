/*  Copyright (C) 2020-2021 Daniel Dakhno

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package unikom.gery.damang.service.devices.qhybrid.requests.fossil_hr.image;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.ColorInt;

import java.io.IOException;

public class AssetImageFactory {
    public static AssetImage createAssetImage(byte[] fileData, int angle, int distance, int indexZ){
        return new AssetImage(fileData, angle, distance, indexZ);
    }

    // method created for creating empty files, which turned out not to work anyway
    private static AssetImage createAssetImage(byte[] fileData, String fileName, int angle, int distance, int indexZ){
        return new AssetImage(fileData, fileName, angle, distance, indexZ);
    }

    public static AssetImage createAssetImage(Bitmap fileData, boolean RLEencode, int angle, int distance, int indexZ) throws IOException {
        if(RLEencode == (distance == 0)) throw new RuntimeException("when RLEencoding distance must be 0, image must be at center of screen");
        if(RLEencode){
            int height = fileData.getHeight();
            int width = fileData.getWidth();

            // if(fileData.getConfig() != Bitmap.Config.ALPHA_8) throw new RuntimeException("Bitmap is not ALPHA_8");

            int[] pixels = new int[height * width];

            fileData.getPixels(pixels, 0, width, 0, 0, width, height);

            byte[] pixelBytes = new byte[width * height];

            for(int i = 0; i < pixels.length; i++){
                int monochrome = convertToMonochrome(pixels[i]);
                monochrome >>= 6;

                int alpha = Color.alpha(pixels[i]);
                monochrome |= (~((alpha & 0xFF) >> 4) & 0b00001100);

                pixelBytes[i] = (byte) monochrome;
            }
            return new AssetImage(ImageConverter.encodeToRLEImage(pixelBytes, height, width), angle, distance, indexZ);
        }else{
            // applies only to big background
            int width = 240;
            int height = 240;

            byte[] pixelBytes = new byte[width * height];

            float jumpX = fileData.getWidth() / (float) width;
            float jumpY = fileData.getHeight() / (float) height;
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    int pixel = fileData.getPixel((int)(x * jumpX), (int)(y * jumpY));

                    int monochrome = convertToMonochrome(pixel);

                    pixelBytes[pixelBytes.length - 1 - (y * width + x)] = (byte) monochrome;
                }
            }

            return new AssetImage(ImageConverter.encodeToRawImage(pixelBytes), angle, distance, indexZ);
        }
    }

    private static @ColorInt int convertToMonochrome(@ColorInt int color){
        int sum = Color.red(color) + Color.green(color) + Color.blue(color);

        sum /= 3;

        return sum;
    }
}
