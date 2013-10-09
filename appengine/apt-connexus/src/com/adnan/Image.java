package com.adnan;

@SuppressWarnings("deprecation")
public class Image {
        double latitude;
        double longitude;
        byte[] physicalImage;

        public Image(double a, double b, byte[] c) {
                latitude = a;
                longitude = b;
                physicalImage = c;
        }

        public Image() {
        }
}
