package ner_service.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.io.FileUtils;

public class SampleText {

	public static String firstName() {
		return (fileList().isEmpty() ? null : fileList().get(0));
	}

	public static String nextName(String currentName) {
		ArrayList<String> list = fileList();
		int index = list.indexOf(currentName) + 1;
		return (index < list.size() ? list.get(index) : null);
	}

	public static String previousName(String currentName) {
		ArrayList<String> list = fileList();
		int index = list.indexOf(currentName) - 1;
		return (index >= 0 ? list.get(index) : null);
	}

	public static String textFor(String name) throws IOException {
		File file = new File("./text_samples/" + name);
		return FileUtils.readFileToString(file);
	}

	public static boolean hasSamples() {
		return !fileList().isEmpty();
	}

	private static ArrayList<String> fileList() {
		ArrayList<String> files = new ArrayList<String>();
		File dir = new File("./text_samples");
		File[] filesList = dir.listFiles();
		for (File file : filesList) {
			if (file.isFile()) {
				if (!file.getName().startsWith(".")) {
					files.add(file.getName());
				}
			}
		}
		Collections.sort(files);
		return files;
	}

}
