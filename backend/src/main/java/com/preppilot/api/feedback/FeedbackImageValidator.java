package com.preppilot.api.feedback;

import com.preppilot.api.common.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class FeedbackImageValidator {

    public static final long MAX_BYTES = 8L * 1024 * 1024;

    public ValidatedImage validate(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        if (file.getSize() > MAX_BYTES) {
            throw new ApiException(HttpStatus.PAYLOAD_TOO_LARGE, "Images must be 8 MB or smaller.");
        }

        try {
            byte[] data = file.getBytes();
            String detectedType = detectType(data);
            if (detectedType == null) {
                throw new ApiException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        "Use a JPEG, PNG, GIF, WebP, BMP, TIFF, AVIF, HEIC, or HEIF image.");
            }
            String declaredType = file.getContentType();
            if (declaredType != null && !declaredType.isBlank() && !declaredType.toLowerCase(Locale.ROOT).startsWith("image/")) {
                throw new ApiException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "The attachment must be an image.");
            }
            return new ValidatedImage(safeName(file.getOriginalFilename()), detectedType, data);
        } catch (IOException exception) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "The image could not be read. Please choose another file.");
        }
    }

    private String detectType(byte[] bytes) {
        if (startsWith(bytes, 0xFF, 0xD8, 0xFF)) return "image/jpeg";
        if (startsWith(bytes, 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)) return "image/png";
        if (ascii(bytes, 0, "GIF87a") || ascii(bytes, 0, "GIF89a")) return "image/gif";
        if (ascii(bytes, 0, "RIFF") && ascii(bytes, 8, "WEBP")) return "image/webp";
        if (ascii(bytes, 0, "BM")) return "image/bmp";
        if (startsWith(bytes, 0x49, 0x49, 0x2A, 0x00) || startsWith(bytes, 0x4D, 0x4D, 0x00, 0x2A)) return "image/tiff";
        if (ascii(bytes, 4, "ftyp") && bytes.length >= 12) {
            String brand = new String(bytes, 8, 4, StandardCharsets.US_ASCII).toLowerCase(Locale.ROOT);
            if (brand.equals("avif") || brand.equals("avis")) return "image/avif";
            if (brand.equals("heic") || brand.equals("heix") || brand.equals("hevc") || brand.equals("hevx")) return "image/heic";
            if (brand.equals("mif1") || brand.equals("msf1")) return "image/heif";
        }
        return null;
    }

    private boolean startsWith(byte[] bytes, int... signature) {
        if (bytes.length < signature.length) return false;
        for (int i = 0; i < signature.length; i++) {
            if ((bytes[i] & 0xFF) != signature[i]) return false;
        }
        return true;
    }

    private boolean ascii(byte[] bytes, int offset, String value) {
        if (bytes.length < offset + value.length()) return false;
        for (int i = 0; i < value.length(); i++) {
            if (bytes[offset + i] != (byte) value.charAt(i)) return false;
        }
        return true;
    }

    private String safeName(String original) {
        if (original == null || original.isBlank()) return "feedback-image";
        String leaf = original.replace('\\', '/');
        leaf = leaf.substring(leaf.lastIndexOf('/') + 1);
        leaf = leaf.replaceAll("[\\p{Cntrl}]", "").trim();
        if (leaf.isBlank()) return "feedback-image";
        return leaf.length() > 255 ? leaf.substring(leaf.length() - 255) : leaf;
    }

    public record ValidatedImage(String name, String contentType, byte[] data) {
        public ValidatedImage {
            data = data.clone();
        }

        @Override
        public byte[] data() {
            return data.clone();
        }
    }
}
