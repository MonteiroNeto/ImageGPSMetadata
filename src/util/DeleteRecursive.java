package util;

import java.io.File;

public class DeleteRecursive {

	public static String deleteRecursive(File path) {
		String msg = "";

		try {
			if (path.isDirectory()) {
				for (File child : path.listFiles()) {
					deleteRecursive(child);
					System.out.println(" REMOVE RECURSIVE PATH ************************************************: ");

				}

				msg = "DONE";

			}

			path.delete();
		} catch (Exception e) {

			System.out.println(" \"ERROR REMOVE RECURSIVE PATH ************************************************: ");
			msg = "ERROR: " + e.getMessage();

		}
		return msg;

	}

}
