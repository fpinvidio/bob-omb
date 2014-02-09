package helpers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;

import play.Play;
import play.mvc.Http.MultipartFormData.FilePart;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FileHandlerHelper {

	public static JsonNode generateResponse(List<File> files) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode onode = mapper.createObjectNode();
		ArrayNode anode = mapper.createArrayNode();
		for (File file : files) {
			ObjectNode obj = mapper.createObjectNode();
			if (files != null) {
				obj.put("name", file.getName());
				obj.put("size", file.getTotalSpace());
				obj.put("url", file.getPath());
				anode.add(obj);
			} else {
				obj.put("error", "Filetype not allowed");
				anode.add(obj);
			}
		}
		onode.put("files", anode);
		return onode;
	}

	public static File saveFile(FilePart picture) {
		String fileName = picture.getFilename();
		//String contentType = picture.getContentType();
		File file = picture.getFile();
		String path = getDirectoryPath(fileName);
		File destination = new File(path);
		try {
			FileUtils.forceMkdir(destination);
			destination = new File(path + fileName);
			FileUtils.moveFile(file, destination);
		} catch (FileExistsException e) {
			int counter = 1;
			while (true) {
				try {
					destination = getNewFileName(path, fileName, counter);
					FileUtils.moveFile(file, destination);
					break;
				} catch (FileExistsException f) {
					counter++;
				} catch (IOException f) {
					e.printStackTrace();
					destination = null;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			destination = null;
		}
		return destination;
	}

	public static File getNewFileName(String path, String fileName, int counter) {
		final int lastPeriodPos = fileName.lastIndexOf('.');
		if (lastPeriodPos <= 0) {
			return null;
		} else {
			String renamed_path = path + fileName.substring(0, lastPeriodPos)
					+ counter
					+ fileName.substring(lastPeriodPos, fileName.length());
			File renamed = new File(renamed_path);
			return renamed;
		}
	}

	public static String getDirectoryPath(String filename) {
		int hashcode = filename.hashCode();
		int mask = 255;
		int firstDir = hashcode & mask;
		int secondDir = (hashcode >> 8) & mask;
		StringBuilder path = new StringBuilder(Play.application().path()
				+ File.separator + "public/uploads/");
		path.append(String.format("%03d", firstDir));
		path.append(File.separator);
		path.append(String.format("%03d", secondDir));
		path.append(File.separator);
		// path.append(filename);

		return path.toString();
	}
}
