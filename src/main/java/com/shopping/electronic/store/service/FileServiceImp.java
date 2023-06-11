package com.shopping.electronic.store.service;

import com.shopping.electronic.store.exception.BadApiRequestException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
@Service
public class FileServiceImp implements FileService {
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileNameWithPath = path + originalFileName;
        String extension = null;
        if (originalFileName != null) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            boolean valid = switch (extension) {
                case ".png" -> true;
                case ".jpg" -> true;
                case ".jpeg" -> true;
                default -> false;
            };
            if (valid) {
                File folder = new File(path);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                Files.copy(file.getInputStream(), Paths.get(fileNameWithPath));
                return originalFileName;
            } else {
                throw new BadApiRequestException("File with extension: " + extension + " not allowed !");
            }
        } else {
            return "Invalid FileName !";
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + File.separator + name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
