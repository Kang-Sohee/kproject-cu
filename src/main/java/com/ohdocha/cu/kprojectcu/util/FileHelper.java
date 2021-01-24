package com.ohdocha.cu.kprojectcu.util;

import org.springframework.http.MediaType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileHelper {

    public static boolean removeFile(String location) {
        File file = new File(location);
        return file.exists() && file.delete();
    }

    public static void makeFolder(File folder) {
        if (folder.exists()) return;

        File parentFolder = folder.getParentFile();
        makeFolder(parentFolder);

        boolean folderMakeResult = folder.mkdir();
        if (!folderMakeResult) throw new IllegalStateException("폴더를 생성할 수 없습니다.");
    }

    public static String imageToBase64Encoding(String imagePath) {
        if (TextUtils.isEmpty(imagePath)) return null;

        File file = new File(imagePath);
        if (!file.exists()) return null;

        String fileExtName = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
        if (fileExtName.isEmpty() || !(fileExtName.contains("JPG") || fileExtName.contains("PNG"))) return null;

        try {
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getExtend(String contentType) {
        switch (contentType) {
            case MediaType.IMAGE_PNG_VALUE:
                return ".png";
            case MediaType.IMAGE_JPEG_VALUE:
                return ".jpg";
            default:
                return null;
        }
    }

    public static String getImageExtend(String contentType) {
        if (!(contentType.equals(MediaType.IMAGE_PNG_VALUE) || contentType.equals(MediaType.IMAGE_JPEG_VALUE)))
            return null;
        return getExtend(contentType);
    }

    public static void compressZipFile(String compressPath, String savePath, String fileName) throws IOException {
        File dir = new File(compressPath);
        String[] list = dir.list();

        if (!dir.exists() || list == null)
            throw new IOException("존재하지 않는 폴더 이거나 폴더 안에 파일이 존재하지 않습니다.");

        if (!dir.canRead() || !dir.canWrite())
            throw new IOException("파일을 읽거나 쓸 수 없습니다.");

        if (compressPath.charAt(compressPath.length() - 1) != '/') compressPath = compressPath + "/";

        FileOutputStream fos;
        BufferedOutputStream bos;
        ZipOutputStream zos;

        fos = new FileOutputStream(savePath + "/" + fileName);
        bos = new BufferedOutputStream(fos, 2048);
        zos = new ZipOutputStream(bos);

        for (String aList : list)
            addFileToZip(compressPath, "", new File(compressPath + aList), zos);

        zos.close();
        bos.close();
        fos.close();
    }

    private static void addFileToZip(String root, String parent, File file, ZipOutputStream zos) throws IOException {
        byte[] data = new byte[2048];
        int read;

        File rootFile = new File(root);

        if (file.isFile()) {
            ZipEntry entry = new ZipEntry(parent + file.getName());
            zos.putNextEntry(entry);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

            while ((read = bis.read(data, 0, 2048)) != -1)
                zos.write(data, 0, read);

            zos.flush();
            zos.closeEntry();
            bis.close();
        } else if (file.isDirectory()) {
            String parentString = file.getPath().replace(rootFile.getPath(), "");

            parentString = parentString.substring(0, parentString.length() - file.getName().length());

            ZipEntry entry = new ZipEntry(parentString + file.getName() + "/");
            zos.putNextEntry(entry);

            String[] list = file.list();
            if (list != null) {
                for (String aList : list)
                    addFileToZip(root, entry.getName(), new File(file.getPath() + "/" + aList), zos);
            }
        }
    }

    public static boolean extractZipFile(String extractPath, String zipFile) throws IOException {
        byte[] data = new byte[2048];

        ZipEntry entry;
        FileOutputStream out = null;

        if (!(extractPath.charAt(extractPath.length() - 1) == '/'))
            extractPath += "/";

        File destDir = new File(extractPath);

        boolean isDirMake = true;

        if (!destDir.exists())
            isDirMake = destDir.mkdirs();

        if (!isDirMake) throw new IOException("폴더 생성에 실패 했습니다.");

        try (FileInputStream fis = new FileInputStream(zipFile)) {
            try (ZipInputStream zis = new ZipInputStream(fis)) {
                while ((entry = zis.getNextEntry()) != null) {
                    int readBytes;
                    File entryFile;

                    // 디렉토리의 경우 폴더를 생성, 파일의 경우 파일 생성
                    if (entry.isDirectory()) {
                        File folder = new File(extractPath + entry.getName());
                        if (!folder.exists()) folder.mkdirs();
                        continue;
                    } else entryFile = new File(extractPath + entry.getName());

                    // 파일이 존재하지 않는 경우 임시 파일 생성
                    if (!entryFile.exists()) entryFile.createNewFile();

                    try (FileOutputStream fos = new FileOutputStream(entryFile)) {
                        while ((readBytes = zis.read(data, 0, 2048)) != -1)
                            fos.write(data, 0, readBytes);
                    } catch (IOException e) {
                        continue;
                    }

                    zis.closeEntry();
                }
            }
        }

        return true;
    }

}
