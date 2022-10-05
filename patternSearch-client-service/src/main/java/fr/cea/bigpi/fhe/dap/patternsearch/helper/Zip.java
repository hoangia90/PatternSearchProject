package fr.cea.bigpi.fhe.dap.patternsearch.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

@Service
public class Zip {

	public String zipMultipleFiles(String zipDirStr, String fileNameStr, String[] resultPathsStr) throws IOException {
		String zipPath = zipDirStr + "/" + fileNameStr + ".zip";
		FileOutputStream fos = new FileOutputStream(zipPath);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		for (String srcFile : resultPathsStr) {
			File fileToZip = new File(srcFile);
			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zipOut.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		}
		zipOut.close();
		fos.close();
		return zipPath;
	}

	public ArrayList<String> UnzipFile(String fileZipStr, String destDirStr) throws IOException {
		String fileZip = fileZipStr;
		File destDir = new File(destDirStr);
		byte[] buffer = new byte[1024];
		@SuppressWarnings("resource")
		ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
		ZipEntry zipEntry = zis.getNextEntry();
		ArrayList<String> extractedFiles = new ArrayList<String>();
		while (zipEntry != null) {
			File newFile = newFile(destDir, zipEntry);
			extractedFiles.add(newFile.getAbsolutePath());
//			System.out.println("halllooooo!: " + newFile.getAbsolutePath());
			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				// fix for Windows-created archives
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent);
				}
				// write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
		return extractedFiles;
	}

	@SuppressWarnings("unused")
	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());
		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();
		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}
		return destFile;
	}

//	public void UnzipFile(String fileZipStr, String destDirStr) throws IOException {
//	String fileZip = fileZipStr;
//	File destDir = new File(destDirStr);
//	byte[] buffer = new byte[1024];
//	@SuppressWarnings("resource")
//	ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
//	ZipEntry zipEntry = zis.getNextEntry();
//	while (zipEntry != null) {
//		File newFile = newFile(destDir, zipEntry);
//		if (zipEntry.isDirectory()) {
//			if (!newFile.isDirectory() && !newFile.mkdirs()) {
//				throw new IOException("Failed to create directory " + newFile);
//			}
//		} else {
//			// fix for Windows-created archives
//			File parent = newFile.getParentFile();
//			if (!parent.isDirectory() && !parent.mkdirs()) {
//				throw new IOException("Failed to create directory " + parent);
//			}
//
//			// write file content
//			FileOutputStream fos = new FileOutputStream(newFile);
//			int len;
//			while ((len = zis.read(buffer)) > 0) {
//				fos.write(buffer, 0, len);
//			}
//			fos.close();
//		}
//		zipEntry = zis.getNextEntry();
//	}
//	zis.closeEntry();
//	zis.close();
//}

}
